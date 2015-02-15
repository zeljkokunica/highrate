package hr.cleancode.receiver.cf;

import hr.cleancode.domain.TransferRequest;
import hr.cleancode.receiver.JsonRequestHttpHandler;
import hr.cleancode.repository.MessageRepository;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zac on 12/02/15.
 */
public class CFHttpHandler extends JsonRequestHttpHandler {
	private static final Logger logger = LoggerFactory.getLogger(CFHttpHandler.class);
	private final ObjectMapper mapper;
	private MessageRepository messageRepository;

	public CFHttpHandler(ObjectMapper mapper, MessageRepository messageRepository) {
		this.mapper = mapper;
		this.messageRepository = messageRepository;
	}

	@Override
	public boolean handleRequestInput(String content) {
		try {
			TransferRequest request = mapper.readValue(content.getBytes(), TransferRequest.class);
			messageRepository.saveTransferRequest(request);
		}
		catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
}
