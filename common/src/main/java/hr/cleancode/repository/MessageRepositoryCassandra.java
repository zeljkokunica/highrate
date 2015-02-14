package hr.cleancode.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.utils.UUIDs;
import hr.cleancode.domain.TransferRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by zac on 14/02/15.
 */
public class MessageRepositoryCassandra implements MessageRepository {
	private static final Logger logger = LoggerFactory.getLogger(MessageRepositoryCassandra.class);

	private Cluster cluster;
	private Session session;
	private PreparedStatement transferRequestInsertStatement;
	private boolean asyncPersistence = false;
	private String keySpace;
	private String cassandraHost;
	private Random random = new Random();
	private ArrayBlockingQueue<ResultSetFuture> queue = new ArrayBlockingQueue<>(10000);

	public MessageRepositoryCassandra(String cassandraHost, String keyspace, boolean asyncPersistence) {
		this.cassandraHost = cassandraHost;
		this.keySpace = keyspace;
		this.asyncPersistence = asyncPersistence;

		SocketOptions socketOptions = new SocketOptions();
		socketOptions.setKeepAlive(true);

		cluster = Cluster.builder().withSocketOptions(socketOptions).addContactPoint(cassandraHost).build();

		Metadata metadata = cluster.getMetadata();
		logger.info("Connected to cluster: {}", metadata.getClusterName());
		for (Host host : metadata.getAllHosts()) {
			logger.info("Datacenter: {}, Host: {}, Rack: {}", host.getDatacenter(), host.getAddress(), host.getRack());
		}
		this.session = cluster.connect(keyspace);
		prepareStatements();
	}

	private void prepareStatements() {
		transferRequestInsertStatement = session.prepare("INSERT INTO transfer_requests " +
				"(timestamp, user_id, currency_from, currency_to, rate, amount_buy, amount_sell, country)" +
				"VALUES (?,?,?,?,?,?,?,?)");
		transferRequestInsertStatement.setConsistencyLevel(ConsistencyLevel.ONE);
	}

	@Override
	public void saveTransferRequest(TransferRequest transferRequest) {
		BoundStatement bs = new BoundStatement(transferRequestInsertStatement);
		bs.bind(
				getTimeUUID(transferRequest.getTimePlaced().getMillis()),
				transferRequest.getUserId(),
				transferRequest.getCurrencyFrom(),
				transferRequest.getCurrencyTo(),
				transferRequest.getRate().floatValue(),
				transferRequest.getAmountBuy().floatValue(),
				transferRequest.getAmountSell().floatValue(),
				transferRequest.getOriginatingCountry()
		);
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
		} else {
			session.execute(bs);
		}
	}

	@Override
	public Long countTransferRequests() {
		return null;
	}

	private UUID getTimeUUID(Long timestamp) {
		return new UUID(UUIDs.startOf(timestamp).getMostSignificantBits(), random.nextLong());
	}
}
