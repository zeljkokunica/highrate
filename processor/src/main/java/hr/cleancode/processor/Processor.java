package hr.cleancode.processor;

import hr.cleancode.repository.MessageRepository;
import hr.cleancode.repository.MessageRepositoryCassandra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zac on 14/02/15.
 */
public class Processor {
	private static final Logger logger = LoggerFactory.getLogger(Processor.class);
	private TransferRequestReader transferRequestReader;

	public Processor() {
		final MessageRepository messageRepository = new MessageRepositoryCassandra("localhost", "highrate", false);
		transferRequestReader = new TransferRequestReader(messageRepository);
		transferRequestReader.registerProcessor(new CountTransferRequestProcessor());
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
	public static void main(String[] args) {
		new Processor().processLoop();
	}
}
