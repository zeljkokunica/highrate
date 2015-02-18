package hr.cleancode.processor;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.CompleteStatisticsWrapper;
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
	private CompleteStatisticsWrapper completeStatisticsWrapper = new CompleteStatisticsWrapper();
	private TransferRequestStatistics lifetimeStatistics = new TransferRequestStatistics();
	private TransferRequestStatistics lastSecondStatistics = new TransferRequestStatistics();
	private TransferRequestStatistics lastMinuteStatistics = new TransferRequestStatistics();
	private ConcurrentHashMap<String, TransferRequestStatistics> statisticsPerCountry = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, TransferRequestStatistics> statisticsPerCurrency = new ConcurrentHashMap<>();
	private RabbitTemplate templateStatistics;
	private Timer timer = new Timer();

	class SecondsTimer extends TimerTask {
		private static final long TICKS_PER_SECOND = 1;
		private static final long TICKS_PER_MINUTE = TICKS_PER_SECOND * 60;
		private long tickCount = 0;
		public void run() {
			try {
				synchronized (MUTEX) {
					tickCount++;
					boolean newSecond = tickCount % TICKS_PER_SECOND == 0;
					boolean newMinute = tickCount % TICKS_PER_MINUTE == 0;
					completeStatisticsWrapper.updateSecond(lastSecondStatistics, newSecond);
					completeStatisticsWrapper.updateMinute(lastMinuteStatistics, newMinute);
					completeStatisticsWrapper.updateLifetime(lifetimeStatistics);
					completeStatisticsWrapper.updateLifetimePerCountry(statisticsPerCountry);
					completeStatisticsWrapper.updateLifetimePerCurrency(statisticsPerCurrency);
					if (newSecond) {
						lastSecondStatistics = new TransferRequestStatistics();
					}
					if (newMinute) {
						lastMinuteStatistics = new TransferRequestStatistics();
					}
					templateStatistics.convertAndSend(HighRateConstants.EXCHANGE_FANOUT,
							HighRateConstants.ROUTING_KEY_STATISTICS, completeStatisticsWrapper);
					tickCount = tickCount % TICKS_PER_MINUTE;

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
			lifetimeStatistics = lifetimeStatistics.addRequest(transferRequest);
			lastSecondStatistics = lastSecondStatistics.addRequest(transferRequest);
			lastMinuteStatistics = lastMinuteStatistics.addRequest(transferRequest);
			TransferRequestStatistics countryStatistics = statisticsPerCountry.get(transferRequest
					.getOriginatingCountry());
			if (countryStatistics == null) {
				countryStatistics = new TransferRequestStatistics();
			}
			countryStatistics = countryStatistics.addRequest(transferRequest);
			statisticsPerCountry.put(transferRequest.getOriginatingCountry(), countryStatistics);

			String currencyKey = transferRequest.getCurrencyFrom() + "-" + transferRequest.getCurrencyTo();
			TransferRequestStatistics currencyStatistics = statisticsPerCurrency.get(currencyKey);
			if (currencyStatistics == null) {
				currencyStatistics = new TransferRequestStatistics();
			}
			currencyStatistics = currencyStatistics.addRequest(transferRequest);
			statisticsPerCurrency.put(currencyKey, currencyStatistics);
		}
	}
}
