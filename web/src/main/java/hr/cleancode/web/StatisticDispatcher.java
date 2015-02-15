package hr.cleancode.web;

import hr.cleancode.HighRateConstants;
import hr.cleancode.domain.TransferRequestStatistics;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import java.io.IOException;
import java.util.Random;

/**
 * Created by zac on 15/02/15.
 */
public class StatisticDispatcher {
	private final Random random = new Random();
	public void startProcessing() throws InterruptedException {
		String queueName = HighRateConstants.QUEUE_NAME_STATS + random.nextInt();
		ConnectionFactory factoryRequests = HighRateConstants
				.getFanoutExchangeConnectionFactory(queueName);
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factoryRequests);
		Object listener = new Object() {
			public void handleMessage(Object object) throws IOException {
				if (object instanceof TransferRequestStatistics) {
					System.out.println(object);
				}
			}
		};
		MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
		container.setMessageListener(adapter);
		container.setQueueNames(queueName);
		container.start();
	}

	public static void main(String[] args) throws InterruptedException {
		new StatisticDispatcher().startProcessing();
	}

}
