package hr.cleancode.processor;

import hr.cleancode.HighRateConstants;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zac on 17/02/15.
 */
@Configuration
public class ProcessorConfig {

	@Bean
	SimpleMessageListenerContainer transferRequestsListenerContainer(
			ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter)
	{
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(HighRateConstants.QUEUE_NAME_REQUESTS);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Processor processor) {
		return new MessageListenerAdapter(processor, "receiveMessage");
	}

	@Bean
	CountTransferRequestProcessor transferRequestProcessor(RabbitTemplate template) {
		return new CountTransferRequestProcessor(template);
	}

}