package hr.cleancode.domain;

/**
 * Created by zac on 15/02/15.
 */
public class ReferenceExchange {
	private String fromCurrency;
	private String toCurrency;
	private Double referenceExchangeRate;

	public ReferenceExchange(String fromCurrency, String toCurrency, Double referenceExchangeRate) {
		this.fromCurrency = fromCurrency;
		this.toCurrency = toCurrency;
		this.referenceExchangeRate = referenceExchangeRate;
	}


	public String getFromCurrency() {
		return fromCurrency;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public Double getReferenceExchangeRate() {
		return referenceExchangeRate;
	}
}
