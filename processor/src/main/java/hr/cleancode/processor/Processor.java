package hr.cleancode.processor;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.TransferRequest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by zac on 14/02/15.
 */
@Component
public class Processor {
	private static final Logger logger = LoggerFactory.getLogger(Processor.class);
	@Autowired
	private TransferRequestProcessor processor;

	@Autowired
	private ConnectionFactory transferRequestsQueueFactory;
	
	public void run() throws InterruptedException {
		SimpleMessageListenerContainer transferRequestsListenerContainer = new SimpleMessageListenerContainer(
				transferRequestsQueueFactory);
		Object listener = new Object() {
			public void handleMessage(Object object) throws IOException {
				if (object instanceof TransferRequest) {
					TransferRequest transferRequest = (TransferRequest) object;
					processor.processTransferRequest(transferRequest);
				}
				else {
					logger.error("got wrong type of message: " + object.getClass().getName());
				}
			}
		};
		MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
		transferRequestsListenerContainer.setMessageListener(adapter);
		transferRequestsListenerContainer.setQueueNames(HighRateConstants.QUEUE_NAME_REQUESTS);
		transferRequestsListenerContainer.start();
		logger.info("waiting for messages to process...");
	}

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = new AnnotationConfigApplicationContext("hr.cleancode.processor");
		ctx.getBean(Processor.class).run();
	}

}
