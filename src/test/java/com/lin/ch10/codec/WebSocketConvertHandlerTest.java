package com.lin.ch10.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * WebSocket 转换处理器，是编码器，也是解码器（使用 MessageToMessageCodec）测试
 * @author lkmc2
 * @date 2019/9/15 16:48
 */
public class WebSocketConvertHandlerTest {

    @Test
    public void encode() {
        // 创建一个 EmbeddedChannel，并添加一个 WebSocketConvertHandler
        EmbeddedChannel channel = new EmbeddedChannel(new WebSocketConvertHandler());

        // 创建（获取）堆中的缓存字节
        ByteBuf buf = Unpooled.buffer();
        // 写入一个整数
        buf.writeInt(99);

        // 创建自定义栈帧
        WebSocketConvertHandler.MyWebSocketFrame webSocketFrame =
                new WebSocketConvertHandler.MyWebSocketFrame(WebSocketConvertHandler.MyWebSocketFrame.FrameType.CLOSE, buf);

        // 将出栈数据写入 EmbeddedChannel
        assertTrue(channel.writeOutbound(webSocketFrame));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的出站消息
        CloseWebSocketFrame read = channel.readOutbound();
        assertEquals(buf, read.content());
    }

    @Test
    public void decode() {
        // 创建一个 EmbeddedChannel，并添加一个 WebSocketConvertHandler
        EmbeddedChannel channel = new EmbeddedChannel(new WebSocketConvertHandler());

        // 创建（获取）堆中的缓存字节
        ByteBuf buf = Unpooled.buffer();
        // 写入一个整数
        buf.writeInt(99);

        // 创建栈帧
        PongWebSocketFrame webSocketFrame = new PongWebSocketFrame(buf);

        // 将入栈数据写入 EmbeddedChannel
        assertTrue(channel.writeInbound(webSocketFrame));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的入站消息
        WebSocketConvertHandler.MyWebSocketFrame read = channel.readInbound();
        assertEquals(buf, read.getData());
    }

}