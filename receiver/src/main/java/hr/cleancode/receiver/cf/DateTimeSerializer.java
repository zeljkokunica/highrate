package hr.cleancode.receiver.cf;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

/**
 * Created by zac on 13/02/15.
 */
public class DateTimeSerializer extends JsonSerializer<DateTime> {
	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-YY HH:mm:ss");
	public DateTimeSerializer() {
	}

	@Override
	public void serialize(DateTime dateTime,
						  JsonGenerator jsonGenerator,
						  SerializerProvider provider) throws IOException, JsonGenerationException {
		String dateTimeAsString = formatter.print(dateTime);
		jsonGenerator.writeString(dateTimeAsString);
	}
}
