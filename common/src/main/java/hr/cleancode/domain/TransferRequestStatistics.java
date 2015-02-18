package hr.cleancode.domain;

import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zac on 15/02/15.
 */
public class TransferRequestStatistics implements Serializable {
	private Long count = 0L;
	private Double volumeSell = 0d;
	private Double volumeBuy = 0d;
	private Double sumRate = 0d;
	private Double avgRate = 0d;
	private List<TransferRequest> topRequests;
	private DateTime creationTimestamp = DateTime.now();

	private TransferRequestStatistics(Long count, Double volumeSell, Double volumeBuy, Double sumOfRate, List<TransferRequest> topRequests) {
		this.count = count;
		this.volumeSell = volumeSell;
		this.volumeBuy = volumeBuy;
		this.sumRate = sumOfRate;
		this.avgRate = sumOfRate / count.doubleValue();
		this.topRequests = topRequests;
	}

	public TransferRequestStatistics() {
		topRequests = new LinkedList<>();
	}

	public TransferRequestStatistics addRequest(TransferRequest request) {
		List<TransferRequest> newTopRequests = new LinkedList<>(topRequests);
		newTopRequests.add(request);
		Collections.sort(newTopRequests);

		TransferRequestStatistics result = new TransferRequestStatistics(
				count + 1,
				volumeSell + request.getAmountSell(),
				volumeBuy + request.getAmountBuy(),
				sumRate + request.getRate(),
				new ArrayList<>(newTopRequests.subList(0, Math.min(10, newTopRequests.size()))));
		return result;
	}

	public Long getCount() {
		return count;
	}

	public Double getVolumeSell() {
		return volumeSell;
	}

	public Double getVolumeBuy() {
		return volumeBuy;
	}

	public Double getAvgRate() {
		return avgRate;
	}

	public List<TransferRequest> getTopRequests() {
		return ImmutableList.copyOf(topRequests);
	}

	public DateTime getCreationTimestamp() {
		return creationTimestamp;
	}

	@Override
	public String toString() {
		return "TransferRequestStatistics{" +
				"count=" + count +
				", volumeSell=" + volumeSell +
				", volumeBuy=" + volumeBuy +
				", avgRate=" + avgRate +
				", topRequests=" + topRequests +
				", creationTimestamp=" + creationTimestamp +
				'}';
	}
}
