package hr.cleancode.processor;

import hr.cleancode.domain.TransferRequest;

/**
 * Created by zac on 14/02/15.
 */
public interface TransferRequestProcessor {
	void processTransferRequest(TransferRequest transferRequest);
}
