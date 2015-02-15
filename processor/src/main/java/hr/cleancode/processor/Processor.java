package hr.cleancode.processor;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.TransferRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import java.io.IOException;

/**
 * Created by zac on 14/02/15.
 */
public class Processor  {
	private static final Logger logger = LoggerFactory.getLogger(Processor.class);
	private CountTransferRequestProcessor processor = new CountTransferRequestProcessor();

	public void startProcessing() throws InterruptedException {
		ConnectionFactory cf = HighRateConstants.getConnectionFactory();
		// set up the listener and container
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cf);
		Object listener = new Object() {
			public void handleMessage(Object object) throws IOException {
				if (object instanceof TransferRequest) {
					TransferRequest transferRequest = (TransferRequest) object;
					processor.processTransferRequest(transferRequest);
				}
			}
		};
		MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
		container.setMessageListener(adapter);
		container.setQueueNames(HighRateConstants.QUEUE_NAME);
		container.start();
	}

	public static void main(String[] args) throws InterruptedException {
		new Processor().startProcessing();
	}

}
