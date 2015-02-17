package hr.cleancode.test;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by zac on 14/02/15.
 */
public class MessageSender {

	public static void main(String[] args) throws InterruptedException {
		DateTime start = DateTime.now();
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			executorService.execute(new Thread(new MessageSenderThread(i, 100000L, "http://localhost:9090/")));
		}
		executorService.shutdown();
		while(!executorService.awaitTermination(1L, TimeUnit.SECONDS));
		System.out.println("Test sending took " + String.valueOf(DateTime.now().getMillis() - start.getMillis()) + "ms");
	}
}
