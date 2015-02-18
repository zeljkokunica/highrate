package hr.cleancode.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zac on 18/02/15.
 */
public class CompleteStatisticsWrapper implements Serializable {
	private int historySize = 60;
	private List<TransferRequestStatistics> seconds = new LinkedList<>();
	private List<TransferRequestStatistics> minutes = new LinkedList<>();
	private Map<String, TransferRequestStatistics> perCountry = new HashMap<>();
	private Map<String, TransferRequestStatistics> perCurrency = new HashMap<>();
	private TransferRequestStatistics lifetime = new TransferRequestStatistics();

	public void updateSecond(TransferRequestStatistics statistics, boolean addNew) {
		if (addNew || seconds.isEmpty()) {
			seconds.add(0, statistics);
			while (seconds.size() > historySize) {
				seconds.remove(seconds.size() - 1);
			}
		}
		else {
			seconds.remove(0);
			seconds.add(0, statistics);
		}
	}
	public void updateMinute(TransferRequestStatistics statistics, boolean addNew) {
		if (addNew || minutes.isEmpty()) {
			minutes.add(0, statistics);
			while (minutes.size() > historySize) {
				minutes.remove(minutes.size() - 1);
			}
		}
		else {
			minutes.remove(0);
			minutes.add(0, statistics);
		}
	}

	public void updateLifetime(TransferRequestStatistics statistics) {
		lifetime = statistics;
	}

	public void updateLifetimePerCountry(Map<String, TransferRequestStatistics> statistics) {
		perCountry = statistics;
	}
	public void updateLifetimePerCurrency(Map<String, TransferRequestStatistics> statistics) {
		perCurrency = statistics;
	}

	public int getHistorySize() {
		return historySize;
	}

	public void setHistorySize(int historySize) {
		this.historySize = historySize;
	}

	public List<TransferRequestStatistics> getSeconds() {
		return seconds;
	}

	public void setSeconds(List<TransferRequestStatistics> seconds) {
		this.seconds = seconds;
	}

	public List<TransferRequestStatistics> getMinutes() {
		return minutes;
	}

	public void setMinutes(List<TransferRequestStatistics> minutes) {
		this.minutes = minutes;
	}

	public Map<String, TransferRequestStatistics> getPerCountry() {
		return perCountry;
	}

	public void setPerCountry(Map<String, TransferRequestStatistics> perCountry) {
		this.perCountry = perCountry;
	}

	public Map<String, TransferRequestStatistics> getPerCurrency() {
		return perCurrency;
	}

	public void setPerCurrency(Map<String, TransferRequestStatistics> perCurrency) {
		this.perCurrency = perCurrency;
	}

	public TransferRequestStatistics getLifetime() {
		return lifetime;
	}

	public void setLifetime(TransferRequestStatistics lifetime) {
		this.lifetime = lifetime;
	}
}
