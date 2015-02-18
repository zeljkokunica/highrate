package hr.cleancode.receiver;

import hr.cleancode.converters.DateTimeModule;
import hr.cleancode.repository.MessageRepository;
import hr.cleancode.repository.MessageRepositoryCassandra;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zac on 17/02/15.
 */
@Configuration
public class ReceiverConfig {
	@Bean
	public MessageRepository messageRepository() {
		return new MessageRepositoryCassandra("localhost", "highrate", false);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new DateTimeModule());
		return mapper;
	}
}
