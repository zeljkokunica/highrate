package hr.cleancode.web;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.CompleteStatisticsWrapper;
import hr.cleancode.domain.TransferRequestStatistics;

import java.io.IOException;
import java.util.Random;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Created by zac on 15/02/15.
 */
public class StatisticDispatcher {
	private final Random random = new Random();

	@Autowired
	private SimpMessagingTemplate template;

	public StatisticDispatcher() throws InterruptedException {
		startProcessing();
	}

	public void startProcessing() throws InterruptedException {
		String queueName = HighRateConstants.QUEUE_NAME_STATS + random.nextInt();
		ConnectionFactory factoryRequests = HighRateConstants
				.getFanoutExchangeConnectionFactory(queueName);
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factoryRequests);
		Object listener = new Object() {
			public void handleMessage(Object object) throws IOException {
				if (object instanceof CompleteStatisticsWrapper) {
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
