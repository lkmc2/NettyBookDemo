package com.lin.ch09;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 出站 EmbeddedChannel 测试
 * @author lkmc2
 * @date 2019/9/15 13:34
 */
public class AbsIntegerEncoderTest {

    @Test
    public void testEncoded() {
        // 创建（获取）堆中的缓存字节
        ByteBuf buf = Unpooled.buffer();

        // 存储 10 个字节的负数数据
        for (int i = 0; i < 10; i++) {
            buf.writeInt(i * -1);
        }

        // 创建一个 EmbeddedChannel ，并安装一个要测试的 AbsIntegerEncoder
        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());

        // 写入数据
        assertTrue(channel.writeOutbound(buf));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取锁产生的消息，并断言它们包含了对应的绝对值
        for (int i = 0; i < 10; i++) {
            assertEquals(i, (int) channel.readOutbound());
        }

        // 此时已读取完字节信息，返回 null
        assertNull(channel.readOutbound());
    }

}