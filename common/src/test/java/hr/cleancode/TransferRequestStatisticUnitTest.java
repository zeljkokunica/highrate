package hr.cleancode;

import hr.cleancode.domain.TransferRequest;
import hr.cleancode.domain.TransferRequestStatistics;
import org.junit.Assert;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by zac on 18/02/15.
 */
public class TransferRequestStatisticUnitTest {
	@Test
	public void testAvg() {
		TransferRequestStatistics s = new TransferRequestStatistics();
		s = s
				.addRequest(
						new TransferRequest("123", "EUR", "USD", 1000.0, 1000.0, 1.0, DateTime.now(), "HR", DateTime
								.now()))
				.addRequest(
						new TransferRequest("123", "EUR", "USD", 1000.0, 900.0, 0.9, DateTime.now(), "HR", DateTime
								.now()))
				.addRequest(
						new TransferRequest("123", "EUR", "USD", 1000.0, 800.0, 0.8, DateTime.now(), "HR", DateTime
								.now()))
				.addRequest(
						new TransferRequest("123", "EUR", "USD", 1000.0, 900.0, 0.9, DateTime.now(), "HR", DateTime
								.now()));
		Assert.assertEquals(Double.valueOf(4000.0), s.getVolumeSell());
		Assert.assertEquals(Double.valueOf(3600.0), s.getVolumeBuy());
		Assert.assertEquals(Double.valueOf(0.9), s.getAvgRate());
		Assert.assertEquals(4, s.getTopRequests().size());
	}
}
