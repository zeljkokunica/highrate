package hr.cleancode.web;

import hr.cleancode.domain.CompleteStatisticsWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Created by zac on 15/02/15.
 */
public class StatisticDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(StatisticDispatcher.class);
	@Autowired
	private SimpMessagingTemplate template;

	public void receiveStatistic(Object object) {
		if (object instanceof CompleteStatisticsWrapper) {
			template.convertAndSend("/topic/stats", object);
		}
		else {
			logger.error("Got wrong message!");
		}
	}

}
