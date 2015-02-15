package hr.cleancode.processor;

import hr.cleancode.domain.TransferRequest;
import hr.cleancode.domain.TransferRequestStatistics;
import org.joda.time.DateTime;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zac on 14/02/15.
 */
public class CountTransferRequestProcessor implements TransferRequestProcessor {
	private TransferRequestStatistics statistics = new TransferRequestStatistics();
	private ConcurrentHashMap<String, TransferRequestStatistics> statisticsPerCountry = new ConcurrentHashMap<>();

	@Override
	public void processTransferRequest(TransferRequest transferRequest) {
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
	}
}
