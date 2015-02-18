package hr.cleancode.reader;

import hr.cleancode.repository.MessageRepository;
import hr.cleancode.repository.MessageRepositoryCassandra;

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

}
