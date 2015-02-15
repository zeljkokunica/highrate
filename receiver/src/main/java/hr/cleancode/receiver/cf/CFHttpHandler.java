package hr.cleancode.receiver.cf;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.TransferRequest;
import hr.cleancode.receiver.JsonRequestHttpHandler;
import hr.cleancode.repository.MessageRepository;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Created by zac on 12/02/15.
 */
public class CFHttpHandler extends JsonRequestHttpHandler {
	private static final Logger logger = LoggerFactory.getLogger(CFHttpHandler.class);
	private final ObjectMapper mapper;
	private MessageRepository messageRepository;
	private RabbitTemplate template;
	private AtomicInteger cnt = new AtomicInteger(0);
	public CFHttpHandler(ObjectMapper mapper, MessageRepository messageRepository, RabbitTemplate template) {
		this.mapper = mapper;
		this.messageRepository = messageRepository;
		this.template = template;
	}

	@Override
	public boolean handleRequestInput(String content) {
		try {
			TransferRequest request = mapper.readValue(content.getBytes(), TransferRequest.class);
			request.validate();
			messageRepository.saveTransferRequest(request);
			template.convertAndSend(HighRateConstants.EXCHANGE, HighRateConstants.ROUTING_KEY_TRANSFER_REQUEST, request);
		}
		catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
}
