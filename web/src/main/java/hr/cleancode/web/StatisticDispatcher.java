package hr.cleancode.web;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.TransferRequestStatistics;

import java.io.IOException;
import java.util.Random;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Created by zac on 15/02/15.
 */
public class StatisticDispatcher {
	private final Random random = new Random();
	private SimpMessagingTemplate template;

	public StatisticDispatcher(SimpMessagingTemplate template) {
		this.template = template;
		try {
			startProcessing();
		}
		catch (Exception e) {
		}
	}

	public void startProcessing() throws InterruptedException {
		String queueName = HighRateConstants.QUEUE_NAME_STATS + random.nextInt();
		ConnectionFactory factoryRequests = HighRateConstants
				.getFanoutExchangeConnectionFactory(queueName);
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factoryRequests);
		Object listener = new Object() {
			public void handleMessage(Object object) throws IOException {
				if (object instanceof TransferRequestStatistics) {
					System.out.println("Send stats " + object.toString());
					template.convertAndSend("/topic/stats", object);
				}
			}
		};
		MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
		container.setMessageListener(adapter);
		container.setQueueNames(queueName);
		container.start();
	}
}
