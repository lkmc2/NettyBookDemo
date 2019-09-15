package com.lin.ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 在服务器支持 WebSocket 的通道初始化器
 * @author lkmc2
 * @date 2019/9/15 18:10
 */
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        // 获取管道，添加通道事件处理器（相当于拦截器）
        channel.pipeline().addLast(
                // http 服务器编解码器
                new HttpServerCodec(),
                // http 对象聚合器（可聚合分散的请求数据）
                new HttpObjectAggregator(65535),
                // 设置要处理请求端点
                new WebSocketServerProtocolHandler("/websocket"),
                // 自定义文本栈帧事件处理器
                new TextFrameHandler(),
                // 自定义二进制栈帧事件处理器
                new BinaryFrameHandler(),
                // 自定义可以关联上一个栈帧的二进制或文本栈帧事件处理器
                new ContinuatoinFrameHandler()
        );
    }

    static final class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame socketFrame) throws Exception {
            // 处理文本栈帧
        }
    }

    static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, BinaryWebSocketFrame socketFrame) throws Exception {
            // 处理二进制栈帧
        }
    }

    static final class ContinuatoinFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ContinuationWebSocketFrame socketFrame) throws Exception {
            // 处理可以关联上一个栈帧的二进制或文本栈帧
        }
    }

}
