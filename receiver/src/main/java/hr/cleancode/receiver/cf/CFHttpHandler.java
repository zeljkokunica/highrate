package hr.cleancode.receiver.cf;

import hr.cleancode.domain.TransferRequest;
import hr.cleancode.receiver.JsonRequestHttpHandler;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by zac on 12/02/15.
 */
public class CFHttpHandler extends JsonRequestHttpHandler {
	private final ObjectMapper mapper;
	public CFHttpHandler(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public boolean handleRequestInput(String content) {
		try {
			TransferRequest request = mapper.readValue(content.getBytes(), TransferRequest.class);
			System.out.println(request.toString());

		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
