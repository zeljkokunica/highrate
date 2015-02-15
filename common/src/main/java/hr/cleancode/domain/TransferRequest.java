package hr.cleancode.domain;

import org.joda.time.DateTime;

/**
 * Created by zac on 12/02/15.
 */
public class TransferRequest implements Comparable<TransferRequest> {
	private String userId;
	private String currencyFrom;
	private String currencyTo;
	private Double amountSell;
	private Double amountBuy;
	private Double rate;
	private DateTime timePlaced;
	private String originatingCountry;
	private DateTime timeReceived = DateTime.now();

	public TransferRequest(String userId, String currencyFrom, String currencyTo, Double amountSell, Double amountBuy,
			Double rate, DateTime timePlaced, String originatingCountry, DateTime timeReceived) {
		this.userId = userId;
		this.currencyFrom = currencyFrom;
		this.currencyTo = currencyTo;
		this.amountSell = amountSell;
		this.amountBuy = amountBuy;
		this.rate = rate;
		this.timePlaced = timePlaced;
		this.originatingCountry = originatingCountry;
		this.timeReceived = timeReceived;
	}

	public String getUserId() {
		return userId;
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public String getCurrencyTo() {
		return currencyTo;
	}

	public Double getAmountSell() {
		return amountSell;
	}

	public Double getAmountBuy() {
		return amountBuy;
	}

	public Double getRate() {
		return rate;
	}

	public DateTime getTimePlaced() {
		return timePlaced;
	}

	public String getOriginatingCountry() {
		return originatingCountry;
	}

	public DateTime getTimeReceived() {
		return timeReceived;
	}

	@Override
	public String toString() {
		return "TransferRequest{" +
				"userId='" + userId + '\'' +
				", currencyFrom='" + currencyFrom + '\'' +
				", currencyTo='" + currencyTo + '\'' +
				", amountSell=" + amountSell +
				", amountBuy=" + amountBuy +
				", rate=" + rate +
				", timePlaced=" + timePlaced +
				", originatingCountry='" + originatingCountry + '\'' +
				", timeReceived=" + timeReceived +
				'}';
	}

	@Override
	public int compareTo(TransferRequest o) {
		return rate.compareTo(o.getRate());
	}
}
