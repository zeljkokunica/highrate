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
}
