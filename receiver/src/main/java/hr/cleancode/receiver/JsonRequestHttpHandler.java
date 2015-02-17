package hr.cleancode.receiver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zac on 13/02/15.
 */
public abstract class JsonRequestHttpHandler extends SimpleChannelInboundHandler {
	private static final Logger logger = LoggerFactory.getLogger(JsonRequestHttpHandler.class);
	private static final String RESPONSE_MESSAGE = "";
	/**
	 * @param content
	 * @return true if request handled ok
	 */
	public abstract boolean handleRequestInput(String content);

	private HttpRequest request;
	private final StringBuilder requestBuffer = new StringBuilder();

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof HttpRequest) {
			this.request = (HttpRequest) msg;
			requestBuffer.setLength(0);
		}

		if (msg instanceof HttpContent) {
			HttpContent httpContent = (HttpContent) msg;
			ByteBuf content = httpContent.content();
			if (content.isReadable()) {
				requestBuffer.append(content.toString(CharsetUtil.UTF_8));
			}
			if (msg instanceof LastHttpContent) {
				LastHttpContent trailer = (LastHttpContent) msg;
				if (!writeResponse(trailer, ctx)) {
					// If keep-alive is off, close the connection once the content is fully written.
					ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
				}
			}
		}
	}

	private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx) {
		boolean keepAlive = HttpHeaders.isKeepAlive(request);
		HttpResponseStatus responseStatus = null;
		if (currentObj.getDecoderResult().isSuccess()) {
			responseStatus = HttpResponseStatus.OK;
			try {
				if (!handleRequestInput(requestBuffer.toString())) {
					responseStatus = HttpResponseStatus.BAD_REQUEST;
				}
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
				responseStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR;
			}
		}
		else {
			responseStatus = HttpResponseStatus.BAD_REQUEST;
		}

		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1,
				responseStatus,
				Unpooled.copiedBuffer(RESPONSE_MESSAGE, CharsetUtil.UTF_8));
		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json; charset=UTF-8");

		if (keepAlive) {
			response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
			response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
		ctx.write(response);
		return keepAlive;
	}

	private static void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
		ctx.write(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error(cause.getMessage(), cause);
		ctx.close();
	}
}
