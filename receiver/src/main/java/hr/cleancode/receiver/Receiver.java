package hr.cleancode.receiver;

import hr.cleancode.HighRateConstants;
import hr.cleancode.receiver.cf.CFHttpHandler;
import hr.cleancode.receiver.cf.DateTimeModule;
import hr.cleancode.repository.MessageRepository;
import hr.cleancode.repository.MessageRepositoryCassandra;

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

import java.util.concurrent.atomic.AtomicLong;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Created by zac on 12/02/15.
 */
public class Receiver {
	public static void main(String[] args)
			throws InterruptedException {

		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new DateTimeModule());
		final MessageRepository messageRepository = new MessageRepositoryCassandra("localhost", "highrate", false);
		final RabbitTemplate template = new RabbitTemplate(HighRateConstants.getConnectionFactory());
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		final AtomicLong connectionCount = new AtomicLong(0);
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
									Long connections = connectionCount.addAndGet(1);
									if (connections % 100 == 0) {
										System.out.println("Connections " + connections);
									}
									ChannelPipeline p = ch.pipeline();
									p.addLast(new HttpRequestDecoder());
									p.addLast(new HttpResponseEncoder());
									p.addLast(new CFHttpHandler(mapper, messageRepository, template));
								}
							});

			ChannelFuture future = bootstrap.bind(9090).sync();

			future.channel().closeFuture().sync();

		}
		finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();

			bossGroup.terminationFuture().sync();
			workerGroup.terminationFuture().sync();
		}

	}
}
