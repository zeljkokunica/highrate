package hr.cleancode.web;

import hr.cleancode.HighRateConstants;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Created by zac on 17/02/15.
 */
@Configuration
public class AppConfig {
	@Autowired
	private SimpMessagingTemplate template;

	@Bean
	public StatisticDispatcher statisticDispatcher() throws InterruptedException {
		return new StatisticDispatcher();
	}

	@Bean
	SimpleMessageListenerContainer statsListenerContainer(
			ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter)
	{
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(HighRateConstants.QUEUE_NAME_STATS);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(StatisticDispatcher dispatcher) {
		return new MessageListenerAdapter(dispatcher, "receiveStatistic");
	}

}
