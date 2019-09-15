package com.lin.ch10;

import com.lin.ch10.decoder.IntegerToStringDecoder;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 将整数转换成字符串的解码器（使用 MessageToMessageDecoder）测试
 * @author lkmc2
 * @date 2019/9/15 15:56
 */
public class IntegerToStringDecoderTest {

    @Test
    public void testDecode() {
        // 创建一个 EmbeddedChannel，并添加一个 IntegerToStringDecoder
        EmbeddedChannel channel = new EmbeddedChannel(new IntegerToStringDecoder());

        // 将数据写入 EmbeddedChannel
        assertTrue(channel.writeInbound(99));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的消息
        String read = channel.readInbound();
        assertEquals("99", read);
    }

}