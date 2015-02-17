package hr.cleancode.receiver;

import hr.cleancode.receiver.cf.CFHttpHandler;
import hr.cleancode.repository.MessageRepository;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by zac on 12/02/15.
 */
@Component
public class Receiver {
	private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	@Qualifier("messagesProcessingQueueTemplate")
	private RabbitTemplate messagesProcessingQueueTemplate;

	public void run() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 200)
					.childOption(ChannelOption.ALLOCATOR,
							PooledByteBufAllocator.DEFAULT)
					.childHandler(
							new ChannelInitializer<SocketChannel>() {
								@Override
								public void initChannel(
										SocketChannel ch) throws Exception {
									logger.info("Initialize new channel");
									ChannelPipeline p = ch.pipeline();
									p.addLast(new HttpRequestDecoder());
									p.addLast(new HttpResponseEncoder());
									p.addLast(new CFHttpHandler(
											objectMapper,
											messageRepository,
											messagesProcessingQueueTemplate));
								}
							});
			ChannelFuture future = bootstrap.bind(9090).sync();
			logger.info("receiving messages...");
			future.channel().closeFuture().sync();

		}
		finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();

			bossGroup.terminationFuture().sync();
			workerGroup.terminationFuture().sync();
		}
	}

	public static void main(String[] args)
			throws InterruptedException {
		ApplicationContext ctx = new AnnotationConfigApplicationContext("hr.cleancode.receiver");
		ctx.getBean(Receiver.class).run();
	}
}
