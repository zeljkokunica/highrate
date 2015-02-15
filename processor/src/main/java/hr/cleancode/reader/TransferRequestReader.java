package hr.cleancode.reader;

import hr.cleancode.domain.TransferRequest;
import hr.cleancode.processor.TransferRequestProcessor;
import hr.cleancode.repository.ContinuousListResult;
import hr.cleancode.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

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
	private RabbitTemplate rabbitTemplate;
	private UUID lastReadRequest = null;

	public TransferRequestReader(MessageRepository messageRepository, RabbitTemplate rabbitTemplate) {
		this.messageRepository = messageRepository;
		this.rabbitTemplate = rabbitTemplate;
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
		rabbitTemplate.convertAndSend("highRate", "highrate.transfer", request);
	}

}
