package com.lin.ch10;

import com.lin.ch10.decoder.ToIntegerDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 将字节解码成 int 类型的解码器测试
 * @author lkmc2
 * @date 2019/9/15 15:39
 */
public class ToIntegerDecoderTest {

    @Test
    public void testDecode() {
        // 创建（获取）堆中的缓存字节
        ByteBuf buf = Unpooled.buffer();

        // 存储 5 个整数
        for (int i = 0; i < 5; i++) {
            buf.writeInt(i);
        }

        // 拷贝字节数据（浅拷贝，修改该变量可能的原来的字节数据产生影响）
        ByteBuf input = buf.duplicate();

        // 创建一个 EmbeddedChannel，并添加一个 ToIntegerDecoder
        EmbeddedChannel channel = new EmbeddedChannel(new ToIntegerDecoder());


        // 将数据写入 EmbeddedChannel
        assertTrue(channel.writeInbound(input.retain()));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的消息
        for (int i = 0; i < 5; i++) {
            int read = channel.readInbound();
            assertEquals(buf.readInt(), read);
        }
    }

}