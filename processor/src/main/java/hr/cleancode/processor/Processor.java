package hr.cleancode.processor;

import hr.cleancode.repository.MessageRepository;
import hr.cleancode.repository.MessageRepositoryCassandra;

/**
 * Created by zac on 14/02/15.
 */
public class Processor {
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
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		new Processor().processLoop();
	}
}
