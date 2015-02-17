package hr.cleancode.reader;

import hr.cleancode.HighRateConstants;
import hr.cleancode.converters.DateTimeModule;
import hr.cleancode.repository.MessageRepository;
import hr.cleancode.repository.MessageRepositoryCassandra;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zac on 17/02/15.
 */
@Configuration
public class ReaderConfig {
	@Bean
	public MessageRepository messageRepository() {
		return new MessageRepositoryCassandra("localhost", "highrate", false);
	}

	@Bean(name="messagesProcessingQueueTemplate")
	public RabbitTemplate messagesProcessingQueueTemplate() {
		RabbitTemplate templateReceiver = new RabbitTemplate(
				HighRateConstants.getDirectExchangeConnectionFactory(
						HighRateConstants.QUEUE_NAME_REQUESTS,
						HighRateConstants.ROUTING_KEY_TRANSFER_REQUEST));
		return templateReceiver;
	}
}
