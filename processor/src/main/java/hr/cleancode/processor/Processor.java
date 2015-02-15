package hr.cleancode.processor;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.TransferRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zac on 14/02/15.
 */
public class Processor {
	private static final Logger logger = LoggerFactory.getLogger(Processor.class);
	private CountTransferRequestProcessor processor = new CountTransferRequestProcessor();

	public void startProcessing() throws InterruptedException {
		final AtomicInteger cnt = new AtomicInteger(0);
		ConnectionFactory factoryRequests = HighRateConstants
				.getDirectExchangeConnectionFactory(HighRateConstants.QUEUE_NAME_REQUESTS, HighRateConstants.ROUTING_KEY_TRANSFER_REQUEST);
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factoryRequests);
		Object listener = new Object() {
			public void handleMessage(Object object) throws IOException {
				System.out.println(cnt.addAndGet(1));
				if (object instanceof TransferRequest) {
					TransferRequest transferRequest = (TransferRequest) object;
					processor.processTransferRequest(transferRequest);
				}
				else {
					System.out.println("Greska");
				}
			}
		};
		MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
		container.setMessageListener(adapter);
		container.setQueueNames(HighRateConstants.QUEUE_NAME_REQUESTS);
		container.start();
	}

	public static void main(String[] args) throws InterruptedException {
		new Processor().startProcessing();
	}

}
