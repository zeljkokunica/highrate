package hr.cleancode.sender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import hr.cleancode.HighRateConstants;
import hr.cleancode.converters.DateTimeModule;
import hr.cleancode.domain.ReferenceExchange;
import hr.cleancode.domain.TransferRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by zac on 14/02/15.
 */
public class MessageSenderThread implements Runnable {
	private int id;
	private Long messagesToSend = 1000L;
	private String uri = "http://localhost:9090/";
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd-MMM-YY HH:mm:ss");
	private ObjectMapper mapper = new ObjectMapper();
	private Random random = new Random();
	private List<ReferenceExchange> referenceExchangeList;
	public MessageSenderThread(int id, Long messagesToSend, String uri) {
		this.id = id;
		this.messagesToSend = messagesToSend;
		this.uri = uri;
		mapper.registerModule(new DateTimeModule());
		referenceExchangeList = new ImmutableList.Builder<ReferenceExchange>()
				.addAll(HighRateConstants.CURRENCY_EXCHANGES.values()).build();

	}

	@Override
	public void run() {
		int success = 0;
		DateTime start = DateTime.now();

		CloseableHttpClient client = prepareHttpClient();
		for (int i = 0; i < messagesToSend; i++) {
			if (sendMessage(client)) {
				success++;
			}
			if (i % 100 == 0) {
				System.out.println("Sender " + id + " sent " + i + " messages.");
			}
			try {
				Thread.sleep(1);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		DateTime end = DateTime.now();
		System.out.println(id + " finished in " + String.valueOf(end.getMillis() - start.getMillis())
				+ "ms with successfull messages: " + success);
	}

	private CloseableHttpClient prepareHttpClient() {
		ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
				return 90 * 1000;
			}
		};

		return HttpClients.custom()
				.setKeepAliveStrategy(myStrategy)
				.build();
	}
	private ReferenceExchange getRandomCurrencyExchange() {
		return referenceExchangeList.get(random.nextInt(referenceExchangeList.size()));
//		return HighRateConstants.CURRENCY_EXCHANGES.get("EUR-USD");
	}
	private boolean sendMessage(HttpClient c) {
		try {
			HttpPost p = new HttpPost(this.uri);
			ReferenceExchange ref = getRandomCurrencyExchange();
			String country = HighRateConstants.COUNTRIES.get(random.nextInt(HighRateConstants.COUNTRIES.size()));
			Double rate = ref.getReferenceExchangeRate() + (random.nextDouble() - 0.5) * 0.1 * ref.getReferenceExchangeRate();
			Double volumeSell = random.nextDouble() * 1000;
			Double volumeBuy = volumeSell * rate;
			TransferRequest request = new TransferRequest(
					"12345",
					ref.getFromCurrency(),
					ref.getToCurrency(),
					volumeSell,
					volumeBuy,
					rate,
					DateTime.now(),
					country,
					DateTime.now());

			p.setEntity(new StringEntity(mapper.writeValueAsString(request),
					ContentType.create("application/json")));
			HttpResponse r = c.execute(p);
			BufferedReader rd = new BufferedReader(new InputStreamReader(r.getEntity().getContent()));
			String line = "";
			while (rd.readLine() != null) {
			}
			if (r.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return true;
			}
			return false;
		}
		catch (IOException e) {
			System.out.println(e);
			return false;
		}

	}

}
