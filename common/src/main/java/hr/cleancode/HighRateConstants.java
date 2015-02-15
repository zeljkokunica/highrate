package hr.cleancode;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * Created by zac on 15/02/15.
 */
public class HighRateConstants {
	public static final String EXCHANGE = "highRate";
	public static final String QUEUE_NAME = "transferRequests";
	public static final String ROUTING_KEY_TRANSFER_REQUEST = "highrate.transferRequest";
	public static final String ROUTING_KEY_STATISTICS = "highrate.transferStatistics";

	public static ConnectionFactory getConnectionFactory(String binding) {
		ConnectionFactory cf = new CachingConnectionFactory();
		RabbitAdmin admin = new RabbitAdmin(cf);
		Queue queue = new Queue(QUEUE_NAME);
		admin.declareQueue(queue);
		TopicExchange exchange = new TopicExchange(EXCHANGE);
		admin.declareExchange(exchange);
		admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(binding));
		return cf;
	}
}
