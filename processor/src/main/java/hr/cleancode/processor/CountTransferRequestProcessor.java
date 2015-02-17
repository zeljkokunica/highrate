package hr.cleancode.processor;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.TransferRequest;
import hr.cleancode.domain.TransferRequestStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zac on 14/02/15.
 */
public class CountTransferRequestProcessor implements TransferRequestProcessor {
	private static final Logger logger = LoggerFactory.getLogger(CountTransferRequestProcessor.class);
	private static final Object MUTEX = new Object();
	private TransferRequestStatistics statistics = new TransferRequestStatistics();
	private TransferRequestStatistics lastSecondStatistics = new TransferRequestStatistics();
	private ConcurrentHashMap<String, TransferRequestStatistics> statisticsPerCountry = new ConcurrentHashMap<>();
	private RabbitTemplate templateStatistics;
	private Timer timer = new Timer();

	class SecondsTimer extends TimerTask {
		public void run() {
			try {
				synchronized (MUTEX) {
					logger.info("process {} messages in last seconf...", lastSecondStatistics.getCount());
					templateStatistics.convertAndSend(HighRateConstants.EXCHANGE_FANOUT,
							HighRateConstants.ROUTING_KEY_STATISTICS, lastSecondStatistics);
					lastSecondStatistics = new TransferRequestStatistics();
				}
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public CountTransferRequestProcessor() {
		templateStatistics = new RabbitTemplate(HighRateConstants.getFanoutExchangeConnectionFactory(HighRateConstants.QUEUE_NAME_STATS));
		timer.schedule(new SecondsTimer(), 0, 1000);
	}

	@Override
	public void processTransferRequest(TransferRequest transferRequest) {
		synchronized (MUTEX) {
			statistics = statistics.addRequest(transferRequest);
			lastSecondStatistics = lastSecondStatistics.addRequest(transferRequest);
			TransferRequestStatistics countryStatistics = statisticsPerCountry.get(transferRequest
					.getOriginatingCountry());
			if (countryStatistics == null) {
				countryStatistics = new TransferRequestStatistics();
			}
			countryStatistics.addRequest(transferRequest);
			statisticsPerCountry.put(transferRequest.getOriginatingCountry(), countryStatistics);
		}
	}
}
