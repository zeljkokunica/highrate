package hr.cleancode.queue;

import hr.cleancode.HighRateConstants;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zac on 18/02/15.
 */
@Configuration
public class QueuesConfig {
	@Bean
	ConnectionFactory connectionFactory() {
		return new CachingConnectionFactory();
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}

	@Bean
	Queue queueRequestsToProcess() {
		return new Queue(HighRateConstants.QUEUE_NAME_REQUESTS, true);
	}

	@Bean
	DirectExchange exchangeRequestsToProcess() {
		return new DirectExchange(HighRateConstants.EXCHANGE);
	}

	@Bean
	Binding bindingRequestsToProcess(
			@Qualifier(value = "queueRequestsToProcess") Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(HighRateConstants.QUEUE_NAME_REQUESTS);
	}

	@Bean
	Queue queueStatistics() {
		return new Queue(HighRateConstants.QUEUE_NAME_STATS, true);
	}

	@Bean
	FanoutExchange exchangeStatistics() {
		return new FanoutExchange(HighRateConstants.EXCHANGE_FANOUT);
	}

	@Bean
	Binding bindingStatistics(@Qualifier(value = "queueStatistics") Queue queue, FanoutExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange);
	}
}
