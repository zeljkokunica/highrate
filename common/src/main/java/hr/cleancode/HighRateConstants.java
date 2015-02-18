package hr.cleancode;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import hr.cleancode.domain.ReferenceExchange;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

/**
 * Created by zac on 15/02/15.
 */
public class HighRateConstants {
	public static final String EXCHANGE = "highRateExcahnge";
	public static final String EXCHANGE_TOPIC = "highRateExcahngeTopic";
	public static final String EXCHANGE_FANOUT = "highRateExcahngeFanout";

	public static final String QUEUE_NAME_REQUESTS = "transferRequests";
	public static final String QUEUE_NAME_STATS = "statistics";
	public static final String ROUTING_KEY_TRANSFER_REQUEST = "highrate.transferRequest";
	public static final String ROUTING_KEY_STATISTICS = "highrate.transferStatistics";

	public static final ImmutableMap<String, ReferenceExchange> CURRENCY_EXCHANGES =
			new ImmutableMap.Builder<String, ReferenceExchange>()
				.put("EUR-USD", new ReferenceExchange("EUR", "USD", 0.765))
				.put("USD-EUR", new ReferenceExchange("USD", "EUR", 1.0 / 0.765))
				.put("EUR-HRK", new ReferenceExchange("HRK", "EUR", 7.69))
				.put("HRK-EUR", new ReferenceExchange("EUR", "HRK", 1.0 / 7.69))
				.build();
	public static final ImmutableList<String> COUNTRIES =
			new ImmutableList.Builder<String>()
					.add("FR")
					.add("UK")
					.add("US")
					.add("HR")
					.add("AUS")
					.add("DE")
					.add("HUN")
					.add("BE")
					.add("SWI")
					.build();

	public static ConnectionFactory getDirectExchangeConnectionFactory(String queueName, String binding) {
		ConnectionFactory cf = new CachingConnectionFactory();
		RabbitAdmin admin = new RabbitAdmin(cf);
		Queue queue = new Queue(queueName);
		admin.declareQueue(queue);
		DirectExchange exchange = new DirectExchange(EXCHANGE);
		admin.declareExchange(exchange);
		admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(binding));
		return cf;
	}

	public static ConnectionFactory getTopicExchangeConnectionFactory(String queueName, String binding) {
		ConnectionFactory cf = new CachingConnectionFactory();
		RabbitAdmin admin = new RabbitAdmin(cf);
		Queue queue = new Queue(queueName);
		admin.declareQueue(queue);
		TopicExchange exchange = new TopicExchange(EXCHANGE_TOPIC);
		admin.declareExchange(exchange);
		admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(binding));
		return cf;
	}

	public static ConnectionFactory getFanoutExchangeConnectionFactory(String queueName) {
		ConnectionFactory cf = new CachingConnectionFactory();
		RabbitAdmin admin = new RabbitAdmin(cf);
		Queue queue = new Queue(queueName);
		admin.declareQueue(queue);
		FanoutExchange exchange = new FanoutExchange(EXCHANGE_FANOUT);
		admin.declareExchange(exchange);
		admin.declareBinding(BindingBuilder.bind(queue).to(exchange));
		return cf;
	}
}
