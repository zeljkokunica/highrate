package hr.cleancode.domain;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

import java.beans.Transient;
import java.io.Serializable;

/**
 * Created by zac on 12/02/15.
 */
public class TransferRequest implements Comparable<TransferRequest>, Serializable {
	private String userId;
	private String currencyFrom;
	private String currencyTo;
	private Double amountSell;
	private Double amountBuy;
	private Double rate;
	private DateTime timePlaced;
	private String originatingCountry;
	private DateTime timeReceived = DateTime.now();

	private TransferRequest() {
	}

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

	private void checkRequired(String property, String value) {
		if (StringUtils.isEmpty(value)) {
			throw new PropertyValueRequired(property);
		}
	}

	private void checkRequired(String property, Double value) {
		if (value == null || value.doubleValue() < 0.01) {
			throw new PropertyValueRequired(property);
		}
	}

	public void validate() {
		checkRequired("userId", userId);
		checkRequired("currencyFrom", currencyFrom);
		checkRequired("currencyTo", currencyTo);
		checkRequired("originatingCountry", originatingCountry);
		checkRequired("amountBuy", amountBuy);
		checkRequired("amountSell", amountSell);
		checkRequired("rate", rate);
		if (timePlaced == null) {
			throw new PropertyValueRequired("timePlaced");
		}
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
