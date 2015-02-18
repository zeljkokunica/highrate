package hr.cleancode;

import hr.cleancode.domain.PropertyValueRequired;
import hr.cleancode.domain.TransferRequest;
import org.junit.Assert;
import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by zac on 18/02/15.
 */
public class TransferRequestUnitTest {

	@Test
	public void testNoUser() {
		TransferRequest req = new TransferRequest(
				null,
				"EUR",
				"GBP",
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	@Test
	public void testEmptyUser() {
		TransferRequest req = new TransferRequest(
				"",
				"EUR",
				"GBP",
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	@Test
	public void testNoCurrencyFrom() {
		TransferRequest req = new TransferRequest(
				"1234",
				null,
				"GBP",
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	@Test
	public void testEmptyCurrencyFrom() {
		TransferRequest req = new TransferRequest(
				"1234",
				"",
				"GBP",
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	@Test
	public void testNoCurrencyTo() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				null,
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	@Test
	public void testEmptyCurrencyTo() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"",
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	public void testZeroVolumeFrom() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"EUR",
				Double.valueOf(0),
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	public void testNegativeVolumeFrom() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"EUR",
				Double.valueOf(-1),
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	public void testNoVolumeFrom() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"EUR",
				null,
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	public void testZeroVolumeTo() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"EUR",
				Double.valueOf(1000),
				Double.valueOf(0),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	public void testNegativeVolumeTo() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"EUR",
				Double.valueOf(1000),
				Double.valueOf(-1),
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	public void testNoVolumeTo() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"EUR",
				Double.valueOf(1000),
				null,
				Double.valueOf(0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	public void testZeroRate() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"EUR",
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(0),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	public void testNegativeRate() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"EUR",
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(-0.787),
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	public void testNoRate() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"EUR",
				Double.valueOf(1000),
				Double.valueOf(787),
				null,
				DateTime.now(),
				"FR",
				DateTime.now());
		expectFail(req);
	}

	public void testNoTimePlaced() {
		TransferRequest req = new TransferRequest(
				"1234",
				"GBP",
				"EUR",
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(0.787),
				null,
				"FR",
				DateTime.now());
		expectFail(req);
	}

	@Test
	public void testNoCountry() {
		TransferRequest req = new TransferRequest(
				null,
				"EUR",
				"GBP",
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				null,
				DateTime.now());
		expectFail(req);
	}

	@Test
	public void testEmptyCountry() {
		TransferRequest req = new TransferRequest(
				"",
				"EUR",
				"GBP",
				Double.valueOf(1000),
				Double.valueOf(787),
				Double.valueOf(0.787),
				DateTime.now(),
				"",
				DateTime.now());
		expectFail(req);
	}

	private void expectFail(TransferRequest req) {
		try {
			req.validate();
			Assert.fail("Should faild");
		}
		catch (PropertyValueRequired e) {
			// ok
		}
	}
}
