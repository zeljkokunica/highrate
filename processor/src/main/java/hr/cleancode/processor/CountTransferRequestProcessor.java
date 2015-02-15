package hr.cleancode.processor;

import hr.cleancode.domain.TransferRequest;
import org.joda.time.DateTime;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zac on 14/02/15.
 */
public class CountTransferRequestProcessor implements TransferRequestProcessor {
	private AtomicLong processedCount = new AtomicLong(0);
	@Override
	public void processTransferRequest(TransferRequest transferRequest) {
		Long count = processedCount.addAndGet(1);
		if (count % 100 == 0) {
			System.out.println("Processed: " + count);
		}
	}
}
