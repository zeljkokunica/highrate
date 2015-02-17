package hr.cleancode.sender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by zac on 14/02/15.
 */
@Component
public class Sender {
	public void run() {
		DateTime start = DateTime.now();
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			executorService.execute(new Thread(new MessageSenderThread(i, 100000L, "http://78.46.95.77:9090/")));
		}
		executorService.shutdown();
		try {
			while(!executorService.awaitTermination(1L, TimeUnit.SECONDS));
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Test sending took " + String.valueOf(DateTime.now().getMillis() - start.getMillis()) + "ms");
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext("hr.cleancode.sender");
		ctx.getBean(Sender.class).run();
	}
}
