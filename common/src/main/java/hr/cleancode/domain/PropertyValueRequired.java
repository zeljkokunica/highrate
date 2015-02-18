package hr.cleancode.domain;

/**
 * Created by zac on 18/02/15.
 */
public class PropertyValueRequired extends RuntimeException{

	public PropertyValueRequired(String property) {
		super("Property " + property + " is required");
	}
}
