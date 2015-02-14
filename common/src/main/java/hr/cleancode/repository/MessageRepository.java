package hr.cleancode.repository;

import hr.cleancode.domain.TransferRequest;

/**
 * Created by zac on 14/02/15.
 */
public interface MessageRepository {
	public void saveTransferRequest(TransferRequest transferRequest);
	public Long countTransferRequests();
}
