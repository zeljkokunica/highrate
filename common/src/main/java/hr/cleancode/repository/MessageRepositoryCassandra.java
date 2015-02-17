package hr.cleancode.repository;

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.gt;

import hr.cleancode.domain.TransferRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.utils.UUIDs;

/**
 * Created by zac on 14/02/15.
 */
public class MessageRepositoryCassandra implements MessageRepository {
	private static final Logger logger = LoggerFactory.getLogger(MessageRepositoryCassandra.class);
	private DateTime defaultBeginDate = DateTime.now().minusMonths(1);
	private Cluster cluster;
	private Session session;
	private PreparedStatement transferRequestInsertStatement;
	private PreparedStatement transferRequestFilterByDateStatement;
	private boolean asyncPersistence = false;
	private String keySpace;
	private String cassandraHost;
	private static Random random = new Random();
	private ArrayBlockingQueue<ResultSetFuture> queue = new ArrayBlockingQueue<>(10000);
	private final AtomicLong insertCount = new AtomicLong(0);

	public MessageRepositoryCassandra(String cassandraHost, String keySpace, boolean asyncPersistence) {
		this.cassandraHost = cassandraHost;
		this.keySpace = keySpace;
		this.asyncPersistence = asyncPersistence;

		SocketOptions socketOptions = new SocketOptions();
		socketOptions.setKeepAlive(true);

		cluster = Cluster.builder().withSocketOptions(socketOptions).addContactPoint(cassandraHost).build();

		Metadata metadata = cluster.getMetadata();
		logger.info("Connected to cluster: {}", metadata.getClusterName());
		for (Host host : metadata.getAllHosts()) {
			logger.info("Datacenter: {}, Host: {}, Rack: {}", host.getDatacenter(), host.getAddress(), host.getRack());
		}
		this.session = cluster.connect(keySpace);
		prepareStatements();
	}

	private void prepareStatements() {
		transferRequestInsertStatement = session.prepare("INSERT INTO transfer_requests " +
				"(timestamp, user_id, currency_from, currency_to, rate, amount_buy, amount_sell, country, time_placed)"
				+
				"VALUES (?,?,?,?,?,?,?,?,?)");
		transferRequestInsertStatement.setConsistencyLevel(ConsistencyLevel.ONE);

		Select select = QueryBuilder
				.select("timestamp", "user_id", "currency_from", "currency_to", "rate", "amount_buy", "amount_sell",
						"country", "time_placed").
				from("transfer_requests").
				where(eq("country", bindMarker()))
				.and(gt("timestamp", bindMarker()))
				.limit(10000);
		transferRequestFilterByDateStatement = session.prepare(select);
	}

	@Override
	public void saveTransferRequest(TransferRequest transferRequest) {
		BoundStatement bs = new BoundStatement(transferRequestInsertStatement);
		bs.bind(
				getTimeUUID(transferRequest.getTimeReceived().getMillis()),
				transferRequest.getUserId(),
				transferRequest.getCurrencyFrom(),
				transferRequest.getCurrencyTo(),
				transferRequest.getRate().floatValue(),
				transferRequest.getAmountBuy().floatValue(),
				transferRequest.getAmountSell().floatValue(),
				transferRequest.getOriginatingCountry(),
				transferRequest.getTimePlaced().toDate()
				);
		Long currentCount = insertCount.addAndGet(1);
		if (currentCount % 100 == 0) {
			logger.info("inserted " + currentCount + " messages in this run...");
		}
		if (this.asyncPersistence) {
			queue.add(session.executeAsync(bs));
			if (queue.size() % 1000 == 0) {
				ResultSetFuture elem;
				do {
					elem = queue.poll();
					if (elem != null) {
						elem.getUninterruptibly();
					}
				} while (elem != null);

			}
		}
		else {
			session.execute(bs);
		}
	}

	@Override
	public ContinuousListResult<TransferRequest> listTransferRequests(UUID lastProcessed) {
		List<TransferRequest> result = new ArrayList<>();
		UUID lastUUID = lastProcessed == null ? getTimeUUID(new DateTime().minusDays(1000).getMillis()) : lastProcessed;
		BoundStatement bs = new BoundStatement(transferRequestFilterByDateStatement);
		bs.bind("FR", lastUUID);
		ResultSet results = session.execute(bs);
		for (Row row : results) {
			lastUUID = row.getUUID("timestamp");
			DateTime timeReceived = new DateTime(UUIDs.unixTimestamp(lastUUID));
			String userId = row.getString("user_id");
			String currencyFrom = row.getString("currency_from");
			String currencyTo = row.getString("currency_to");
			Double rate = new Double(row.getFloat("rate"));
			Double amountBuy = new Double(row.getFloat("amount_buy"));
			Double amountSell = new Double(row.getFloat("amount_sell"));
			DateTime timePlaced = new DateTime(row.getDate("time_placed"));
			String country = row.getString("country");
			TransferRequest item = new TransferRequest(userId, currencyFrom, currencyTo, amountSell, amountBuy, rate,
					timePlaced, country, timeReceived);
			result.add(item);
		}
		return new ContinuousListResult<>(result, lastUUID);
	}

	private UUID fromUUID(DateTime from) {
		return UUIDs.endOf((from != null ? from : defaultBeginDate).getMillis());
	}

	private static UUID getTimeUUID(Long timestamp) {
		return new UUID(UUIDs.startOf(timestamp).getMostSignificantBits(), random.nextLong());
	}
}
