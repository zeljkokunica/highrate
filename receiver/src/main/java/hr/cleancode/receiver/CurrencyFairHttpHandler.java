package hr.cleancode.receiver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Created by zac on 12/02/15.
 */
public class CurrencyFairHttpHandler extends SimpleChannelInboundHandler<Object> {
	private static final byte[] CONTENT = "Hello".getBytes();

	@Override
	public void channelReadComplete(
			ChannelHandlerContext ctx) {

		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx,
			Object msg) {
		if (msg instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) msg;
			String reqUrl = req.getUri();
			System.out.println(reqUrl);
			System.out.println(req.toString());
			// do something further with request here ...

			// this is the response part
			if (HttpHeaders.is100ContinueExpected(req)) {
				ctx.write(new DefaultFullHttpResponse(
						HttpVersion.HTTP_1_1,
						HttpResponseStatus.CONTINUE));
			}

			boolean keepAlive = HttpHeaders.isKeepAlive(req);
			FullHttpResponse response =
					new DefaultFullHttpResponse(
							HttpVersion.HTTP_1_1,
							HttpResponseStatus.OK,
							Unpooled.wrappedBuffer(CONTENT));

			response.headers().set(
					HttpHeaders.Names.CONTENT_TYPE, "text/plain");
			response.headers().set(
					HttpHeaders.Names.CONTENT_LENGTH,
					response.content().readableBytes());

			if (!keepAlive) {
				ctx.write(response)
						.addListener(ChannelFutureListener.CLOSE);
			}
			else {
				response.headers().set(
						HttpHeaders.Names.CONNECTION,
						HttpHeaders.Values.KEEP_ALIVE);

				ctx.write(response);
			}
		}
	}

	@Override
	public void exceptionCaught(
			ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
}
