package hr.cleancode.repository;

import hr.cleancode.domain.TransferRequest;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zac on 14/02/15.
 */
public class MessageRepositoryInMemory implements MessageRepository {
	private AtomicLong counter = new AtomicLong();
	@Override
	public void saveTransferRequest(TransferRequest transferRequest) {
		Long currentCount = counter.getAndAdd(1);
		System.out.println(currentCount + ": " + transferRequest);
	}

	@Override
	public Long countTransferRequests() {
		return counter.get();
	}
}
