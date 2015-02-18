package hr.cleancode.reader;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.TransferRequest;
import hr.cleancode.repository.ContinuousListResult;
import hr.cleancode.repository.MessageRepository;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zac on 14/02/15.
 */
@Component
public class TransferRequestReader {
	private static final Logger logger = LoggerFactory.getLogger(TransferRequestReader.class);
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	private UUID lastReadRequest = null;

	public TransferRequestReader() {
	}


	public void processChunk() {
		ContinuousListResult<TransferRequest> requestsToProcess = messageRepository.listTransferRequests(lastReadRequest);
		lastReadRequest = requestsToProcess.getLastUUID();
		for (TransferRequest request: requestsToProcess.getResult()) {
			processTransferRequest(request);
		}
	}

	private void processTransferRequest(TransferRequest request) {
		rabbitTemplate.convertAndSend(HighRateConstants.EXCHANGE, HighRateConstants.ROUTING_KEY_TRANSFER_REQUEST, request);
	}

}
