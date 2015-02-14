package hr.cleancode.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	public MessageSenderThread(int id, Long messagesToSend, String uri) {
		this.id = id;
		this.messagesToSend = messagesToSend;
		this.uri = uri;
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

	private boolean sendMessage(HttpClient c) {
		try {
			HttpPost p = new HttpPost(this.uri);
			p.setEntity(new StringEntity(
					"{\"userId\": \"134256\", \"currencyFrom\": \"EUR\", \"currencyTo\": \"GBP\", \"amountSell\": 1000, \"amountBuy\": 747.10, \"rate\": 0.7471, \"timePlaced\" : \"" + dateTimeFormatter.print(DateTime.now()) + "\", \"originatingCountry\" : \"FR\"}",
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
