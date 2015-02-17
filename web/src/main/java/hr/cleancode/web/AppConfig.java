package hr.cleancode.web;

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

}
