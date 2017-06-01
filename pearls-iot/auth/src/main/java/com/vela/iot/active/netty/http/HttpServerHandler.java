package com.vela.iot.active.netty.http;

import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.AsciiString;
import io.netty.util.ReferenceCountUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vela.iot.auth.gw.active.ActiveResource;
import com.vela.iot.common.Param;
import com.vela.iot.common.Request;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HttpServerHandler.class);
	private static final byte[] CONTENT = "{\"pt\":\"fZud4fM6SUuvvvBoFyGNYw\",\"at\":\"fI8LLGb7QaOZw6wgYInDrQ\"}"
			.getBytes();

	private static final HttpDataFactory factory = new DefaultHttpDataFactory(
			DefaultHttpDataFactory.MAXSIZE);

	private static final AsciiString CONTENT_TYPE = new AsciiString(
			"Content-Type");
	private static final AsciiString CONTENT_LENGTH = new AsciiString(
			"Content-Length");
	private static final AsciiString CONNECTION = new AsciiString("Connection");
	private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");
	private static final String prefix = "/auth";

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) msg;
			HttpMethod method = req.method();
			Map<Param, Object> params = new EnumMap<Param, Object>(Param.class);
			try {
				if (method.equals(HttpMethod.GET)) {
					params = parseGetUri(req);
				} else if (method.equals(HttpMethod.POST)) {
					// POST请求，由于你需要从消息体中获取数据，因此有必要把msg转换成FullHttpRequest
					FullHttpRequest fullRequest = (FullHttpRequest) msg;
					params = parsePostContent(fullRequest);
				}
				//TODO 解析ip作为参数保存
				params.put(Param.ip, "192.168.1.34");
				
			} catch (Exception e) {
				LOGGER.error("channelRead error", e);
				writeResponse(ctx, req,
						HttpResponseStatus.INTERNAL_SERVER_ERROR);
			} finally {
				ReferenceCountUtil.release(msg);
			}
			if (req.uri().startsWith(prefix)) {
				Request request = buildReq(req,params);
				ActiveResource ar = new ActiveResource();
				ar.action(request);
			} else {
				// TODO 否则丢弃
			}
			writeResponse(ctx, req, OK);
		}
	}

	private Map<Param, Object> parseGetUri(HttpRequest req) {
		String uri = req.uri();
		Map<Param, Object> paramMap = new HashMap<>();
		QueryStringDecoder queryDecoder = new QueryStringDecoder(uri,
				Charset.forName("UTF-8"));
		Map<String, List<String>> uriAttributes = queryDecoder.parameters();
		// 此处仅打印请求参数（你可以根据业务需求自定义处理）
		for (Map.Entry<String, List<String>> attr : uriAttributes.entrySet()) {
			for (String attrVal : attr.getValue()) {
				paramMap.put(Param.valueOf(attr.getKey()), attrVal);
			}
		}
		return paramMap;
	}

	/**
	 * 简单处理常用几种 Content-Type 的 POST 内容（可自行扩展）
	 * 
	 * @param headers
	 * @param content
	 * @throws Exception
	 */
	private Map<Param, Object> parsePostContent(FullHttpRequest fullRequest)
			throws Exception {
		String contentType = getContentType(fullRequest);
		Map<Param, Object> map = new HashMap<>();
		if (contentType.equals("application/json")) { // 可以使用HttpJsonDecoder
			String jsonStr = fullRequest.content().toString(
					Charset.forName("UTF-8"));
			// TODO 这里有个bug，value值没有进行trim
			ObjectMapper mapper = new ObjectMapper(); // 转换器
			map = mapper.readValue(jsonStr, Map.class); // json转换成map
		} else if (contentType.equals("application/x-www-form-urlencoded")
				|| contentType.equals("multipart/form-data")) {
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
					factory, fullRequest, Charset.forName("UTF-8"));
			List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
			for (InterfaceHttpData data : datas) {
				if (data.getHttpDataType() == HttpDataType.Attribute) {
					Attribute attribute = (Attribute) data;
					map.put(Param.valueOf(attribute.getName()), attribute.getValue());
				} else if (data.getHttpDataType() == HttpDataType.FileUpload) {// 用于文件上传
					writeHttpData(data);
				}
			}

		} else {
			// do nothing...
		}
		return map;
	}

	private String getContentType(FullHttpRequest fullRequest) {
		String typeStr = fullRequest.headers().get("Content-Type").toString();
		String[] list = typeStr.split(";");
		return list[0];
	}

	private void writeHttpData(InterfaceHttpData data) throws IOException {
		// 后续会加上块传输（HttpChunk），目前仅简单处理
		FileUpload fileUpload = (FileUpload) data;
		String fileName = fileUpload.getFilename();
		if (fileUpload.isCompleted()) {
			// 保存到磁盘
			StringBuffer fileNameBuf = new StringBuffer();
			fileNameBuf.append(DiskFileUpload.baseDirectory).append(fileName);
			fileUpload.renameTo(new File(fileNameBuf.toString()));
		}
	}

	private void writeResponse(ChannelHandlerContext ctx, HttpRequest req,
			HttpResponseStatus status) {
		if (HttpUtil.is100ContinueExpected(req)) {
			ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
		}
		boolean keepAlive = HttpUtil.isKeepAlive(req);
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				status, Unpooled.wrappedBuffer(CONTENT));
		if (req.method() == HttpMethod.GET || req.method() == HttpMethod.POST) {
			response.headers().set(CONTENT_TYPE, "text/plain");
			response.headers().setInt(CONTENT_LENGTH,
					response.content().readableBytes());
		}
		if (!keepAlive) {
			ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		} else {
			response.headers().set(CONNECTION, KEEP_ALIVE);
			ctx.write(response);
		}
	}

	private Request buildReq(HttpRequest httpRequest,Map<Param, Object> params) {
		Request request = new Request();
		request.setUri(httpRequest.uri());
		request.setMethod(httpRequest.method().name());
		request.setVersion(httpRequest.protocolVersion().protocolName());
		Map<String, String> headers = new HashMap<>();
		httpRequest.headers().iteratorAsString()
				.forEachRemaining(e -> headers.put(e.getKey(), e.getValue()));
		request.setHeaders(headers);
		request.setParams(params);
		return request;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
