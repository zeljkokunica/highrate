package hr.cleancode.processor;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.TransferRequest;
import hr.cleancode.domain.TransferRequestStatistics;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zac on 14/02/15.
 */
public class CountTransferRequestProcessor implements TransferRequestProcessor {
	private static final Object MUTEX = new Object();
	private TransferRequestStatistics statistics = new TransferRequestStatistics();
	private ConcurrentHashMap<String, TransferRequestStatistics> statisticsPerCountry = new ConcurrentHashMap<>();
	private RabbitTemplate templateStatistics;

	public CountTransferRequestProcessor() {
		templateStatistics = new RabbitTemplate(
				HighRateConstants.getConnectionFactory(HighRateConstants.ROUTING_KEY_STATISTICS));
	}

	@Override
	public void processTransferRequest(TransferRequest transferRequest) {
		synchronized (MUTEX) {
			statistics = statistics.addRequest(transferRequest);
			TransferRequestStatistics countryStatistics = statisticsPerCountry.get(transferRequest.getOriginatingCountry());
			if (countryStatistics == null) {
				countryStatistics = new TransferRequestStatistics();
			}
			countryStatistics.addRequest(transferRequest);
			statisticsPerCountry.put(transferRequest.getOriginatingCountry(), countryStatistics);
			if (statistics.getCount() % 100 == 0) {
				System.out.println(statistics);
			}
			templateStatistics.convertAndSend(HighRateConstants.EXCHANGE, HighRateConstants.ROUTING_KEY_STATISTICS, statistics);
		}
	}
}
