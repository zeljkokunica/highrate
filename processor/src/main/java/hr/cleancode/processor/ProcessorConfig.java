package hr.cleancode.processor;

import hr.cleancode.HighRateConstants;
import hr.cleancode.repository.MessageRepository;
import hr.cleancode.repository.MessageRepositoryCassandra;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zac on 17/02/15.
 */
@Configuration
public class ProcessorConfig {

	@Bean(name = "messagesProcessingQueueTemplate")
	public RabbitTemplate messagesProcessingQueueTemplate() {
		RabbitTemplate templateReceiver = new RabbitTemplate(
				HighRateConstants.getDirectExchangeConnectionFactory(
						HighRateConstants.QUEUE_NAME_REQUESTS,
						HighRateConstants.ROUTING_KEY_TRANSFER_REQUEST));
		return templateReceiver;
	}

	@Bean(name = "transferRequestsQueueFactory")
	public ConnectionFactory transferRequestsQueueFactory() {
		ConnectionFactory factoryRequests = HighRateConstants.getDirectExchangeConnectionFactory(
				HighRateConstants.QUEUE_NAME_REQUESTS,
				HighRateConstants.ROUTING_KEY_TRANSFER_REQUEST);

		return factoryRequests;
	}

	@Bean
	public TransferRequestProcessor transferRequestProcessor() {
		return new CountTransferRequestProcessor();
	}
}