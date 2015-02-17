package hr.cleancode.converters;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.module.SimpleModule;
import org.joda.time.DateTime;

/**
 * Created by zac on 13/02/15.
 */
public class DateTimeModule extends SimpleModule {

	public DateTimeModule() {
		super("dateTimeSerializer", Version.unknownVersion());
		addSerializer(DateTime.class, new DateTimeSerializer());
		addDeserializer(DateTime.class, new DateTimeDeserializer());
	}
}
