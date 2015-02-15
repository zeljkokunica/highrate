package hr.cleancode.processor;

import hr.cleancode.domain.TransferRequest;
import hr.cleancode.repository.ContinuousListResult;
import hr.cleancode.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zac on 14/02/15.
 */
public class TransferRequestReader {
	private static final Logger logger = LoggerFactory.getLogger(TransferRequestReader.class);
	private List<TransferRequestProcessor> processors = new ArrayList<>();
	private MessageRepository messageRepository;
	private UUID lastReadRequest = null;

	public TransferRequestReader(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	public void registerProcessor(TransferRequestProcessor processor) {
		this.processors.add(processor);
	}

	public void processChunk() {
		ContinuousListResult<TransferRequest> requestsToProcess = messageRepository.listTransferRequests(lastReadRequest);
		lastReadRequest = requestsToProcess.getLastUUID();
		for (TransferRequest request: requestsToProcess.getResult()) {
			processTransferRequest(request);
		}
	}

	private void processTransferRequest(TransferRequest request) {
		for (TransferRequestProcessor processor: this.processors) {
			try {
				processor.processTransferRequest(request);
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}


}
