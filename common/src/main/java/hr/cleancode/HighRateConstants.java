package hr.cleancode;

import hr.cleancode.domain.ReferenceExchange;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Created by zac on 15/02/15.
 */
public class HighRateConstants {
	public static final String EXCHANGE = "highRateExcahnge";
	public static final String EXCHANGE_FANOUT = "highRateExcahngeFanout";

	public static final String QUEUE_NAME_REQUESTS = "transferRequests";
	public static final String QUEUE_NAME_STATS = "statistics";
	public static final String ROUTING_KEY_TRANSFER_REQUEST = "highrate.transferRequest";
	public static final String ROUTING_KEY_STATISTICS = "highrate.transferStatistics";

	public static final ImmutableMap<String, ReferenceExchange> CURRENCY_EXCHANGES =
			new ImmutableMap.Builder<String, ReferenceExchange>()
					.put("EUR-GBP", new ReferenceExchange("EUR", "GBP", 0.765))
					.put("GBP-EUR", new ReferenceExchange("GBP", "EUR", 1.35))
					.put("EUR-USD", new ReferenceExchange("EUR", "USD", 1.14))
					.put("USD-EUR", new ReferenceExchange("USD", "EUR", 0.88))
					.put("EUR-HRK", new ReferenceExchange("HRK", "EUR", 0.13))
					.put("HRK-EUR", new ReferenceExchange("EUR", "HRK", 7.69))
					.build();
	public static final ImmutableList<String> COUNTRIES =
			new ImmutableList.Builder<String>()
					.add("POR")
					.add("SP")
					.add("FR")
					.add("UK")
					.add("POL")
					.add("LUX")
					.add("SLO")
					.add("NL")
					.add("US")
					.add("HR")
					.add("AUS")
					.add("DE")
					.add("HUN")
					.add("BE")
					.add("SWI")
					.build();
}
