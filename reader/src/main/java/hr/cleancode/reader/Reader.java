package hr.cleancode.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by zac on 14/02/15.
 */
@Component
public class Reader {
	private static final Logger logger = LoggerFactory.getLogger(Reader.class);
	@Autowired
	private TransferRequestReader transferRequestReader;

	public void run() {
		while (true) {
			transferRequestReader.processChunk();
			try {
				Thread.sleep(1);
			}
			catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = new AnnotationConfigApplicationContext("hr.cleancode.reader", "hr.cleancode.queue");
		ctx.getBean(Reader.class).run();
	}
}
