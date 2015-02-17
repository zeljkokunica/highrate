package hr.cleancode.web.controller;

import hr.cleancode.web.StatisticDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Created by zac on 17/02/15.
 */
@Controller
public class StatisticController {
	@Autowired
	private SimpMessagingTemplate template;

	private StatisticDispatcher statisticDispatcher;


	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		if (statisticDispatcher == null) {
			statisticDispatcher = new StatisticDispatcher(template);
		}

		return new Greeting("Jeee");
	}
}
