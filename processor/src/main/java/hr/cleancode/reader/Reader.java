package hr.cleancode.reader;

import hr.cleancode.HighRateConstants;
import hr.cleancode.repository.MessageRepository;
import hr.cleancode.repository.MessageRepositoryCassandra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Created by zac on 14/02/15.
 */
public class Reader {
	private static final Logger logger = LoggerFactory.getLogger(Reader.class);
	private TransferRequestReader transferRequestReader;

	public Reader() {
		final MessageRepository messageRepository = new MessageRepositoryCassandra("localhost", "highrate", false);
		transferRequestReader = new TransferRequestReader(messageRepository, new RabbitTemplate(
				HighRateConstants.getDirectExchangeConnectionFactory(HighRateConstants.QUEUE_NAME_REQUESTS, HighRateConstants.ROUTING_KEY_TRANSFER_REQUEST)));
	}

	public void processLoop() {
		while (true) {
			transferRequestReader.processChunk();
			try {
				Thread.sleep(1);
			}
			catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new Reader().processLoop();
	}
}
