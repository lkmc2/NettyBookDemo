package com.lin.ch10.encoder;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 将整形编码成字符串的编码器（使用 MessageToMessageEncoder）测试
 * @author lkmc2
 * @date 2019/9/15 16:24
 */
public class IntegerToStringEncoderTest {

    @Test
    public void testDecode() {
        // 创建一个 EmbeddedChannel，并添加一个 IntegerToStringEncoder
        EmbeddedChannel channel = new EmbeddedChannel(new IntegerToStringEncoder());

        // 将出栈数据写入 EmbeddedChannel
        assertTrue(channel.writeOutbound(22));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的出站消息
        String read = channel.readOutbound();
        assertEquals("22", read);
    }

}