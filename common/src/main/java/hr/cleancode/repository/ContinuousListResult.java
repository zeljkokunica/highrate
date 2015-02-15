package hr.cleancode.repository;

import java.util.List;
import java.util.UUID;

/**
 * Created by zac on 15/02/15.
 */
public class ContinuousListResult<T> {
	private List<T> result;
	private UUID lastUUID;

	public ContinuousListResult(List<T> result, UUID lastUUID) {
		this.result = result;
		this.lastUUID = lastUUID;
	}

	public List<T> getResult() {
		return result;
	}

	public UUID getLastUUID() {
		return lastUUID;
	}
}
