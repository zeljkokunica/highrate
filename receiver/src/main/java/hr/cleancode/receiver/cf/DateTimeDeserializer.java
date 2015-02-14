package hr.cleancode.receiver.cf;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdScalarDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by zac on 13/02/15.
 */
public class DateTimeDeserializer extends StdScalarDeserializer<DateTime> {
	private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MMM-YY HH:mm:ss");

	public DateTimeDeserializer() {
		super(DateTime.class);
	}

	@Override
	public DateTime deserialize(JsonParser jsonParser,
			DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		JsonToken currentToken = jsonParser.getCurrentToken();
		if (currentToken == JsonToken.VALUE_STRING) {
			String dateTimeAsString = jsonParser.getText().trim();
			return formatter.parseDateTime(dateTimeAsString);
		}
		throw deserializationContext.mappingException(getValueClass());
	}
}
