package hr.cleancode.domain;

import org.joda.time.DateTime;

/**
 * Created by zac on 12/02/15.
 */
public class TransferRequest {
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
			Double rate, DateTime timePlaced, String originatingCountry) {
		this.userId = userId;
		this.currencyFrom = currencyFrom;
		this.currencyTo = currencyTo;
		this.amountSell = amountSell;
		this.amountBuy = amountBuy;
		this.rate = rate;
		this.timePlaced = timePlaced;
		this.originatingCountry = originatingCountry;
	}

	public TransferRequest() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public void setCurrencyFrom(String currencyFrom) {
		this.currencyFrom = currencyFrom;
	}

	public String getCurrencyTo() {
		return currencyTo;
	}

	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}

	public Double getAmountSell() {
		return amountSell;
	}

	public void setAmountSell(Double amountSell) {
		this.amountSell = amountSell;
	}

	public Double getAmountBuy() {
		return amountBuy;
	}

	public void setAmountBuy(Double amountBuy) {
		this.amountBuy = amountBuy;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public DateTime getTimePlaced() {
		return timePlaced;
	}

	public void setTimePlaced(DateTime timePlaced) {
		this.timePlaced = timePlaced;
	}

	public String getOriginatingCountry() {
		return originatingCountry;
	}

	public void setOriginatingCountry(String originatingCountry) {
		this.originatingCountry = originatingCountry;
	}

	public DateTime getTimeReceived() {
		return timeReceived;
	}

	public void setTimeReceived(DateTime timeReceived) {
		this.timeReceived = timeReceived;
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
				'}';
	}
}
