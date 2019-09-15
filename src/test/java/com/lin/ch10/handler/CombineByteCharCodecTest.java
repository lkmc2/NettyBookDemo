package com.lin.ch10.handler;

import com.lin.ch10.codec.WebSocketConvertHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 可以将字节和 char 进行相互转换的编解码器（使用 CombinedChannelDuplexHandler）测试
 * @author lkmc2
 * @date 2019/9/15 17:06
 */
public class CombineByteCharCodecTest {

    @Test
    public void encode() {
        // 创建一个 EmbeddedChannel，并添加一个 CombineByteCharCodec
        EmbeddedChannel channel = new EmbeddedChannel(new CombineByteCharCodec());

        // 创建（获取）堆中的缓存字节
        ByteBuf buf = Unpooled.buffer();
        // 写入一个 char 值
        buf.writeChar(43);

        // 将出栈数据写入 EmbeddedChannel
        assertTrue(channel.writeOutbound((char) 43));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的出站消息
        ByteBuf read = channel.readOutbound();
        assertEquals(buf, read);
    }

    @Test
    public void decode() {
        // 创建一个 EmbeddedChannel，并添加一个 CombineByteCharCodec
        EmbeddedChannel channel = new EmbeddedChannel(new CombineByteCharCodec());

        // 创建（获取）堆中的缓存字节
        ByteBuf buf = Unpooled.buffer();
        // 写入一个 char 值
        buf.writeChar(43);

        // 浅拷贝一份字节数据
        ByteBuf input = buf.duplicate();

        // 将入栈数据写入 EmbeddedChannel
        assertTrue(channel.writeInbound(input.retain()));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的入站消息
        char read = channel.readInbound();
        assertEquals(buf.readChar(), read);
    }

}