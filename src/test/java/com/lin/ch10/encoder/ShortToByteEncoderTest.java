package com.lin.ch10.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 将短整型编码成字节的编码器（使用 MessageToByteEncoder）测试
 * @author lkmc2
 * @date 2019/9/15 16:14
 */
public class ShortToByteEncoderTest {

    @Test
    public void testDecode() {
        // 创建一个 EmbeddedChannel，并添加一个 ShortToByteEncoder
        EmbeddedChannel channel = new EmbeddedChannel(new ShortToByteEncoder());

        short shortValue = 33;

        // 将出栈数据写入 EmbeddedChannel
        assertTrue(channel.writeOutbound(shortValue));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 创建（获取）堆中的缓存字节
        ByteBuf buf = Unpooled.buffer();

        // 存储 1 个短整型数
        buf.writeShort(shortValue);

        // 读取所生成的出站消息
        ByteBuf read = channel.readOutbound();
        assertEquals(buf.readSlice(2), read);
    }

}