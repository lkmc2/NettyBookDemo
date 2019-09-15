package com.lin.ch09;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 出站 EmbeddedChannel 的异常测试
 * @author lkmc2
 * @date 2019/9/15 13:47
 */
public class FrameChunkDecoderTest {

    @Test
    public void testFramesDecoded() {
        // 创建（获取）堆中的缓存字节
        ByteBuf buf = Unpooled.buffer();

        // 存储 9 字节的数据
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        // 拷贝字节数据（浅拷贝，修改该变量可能的原来的字节数据产生影响）
        ByteBuf input = buf.duplicate();

        // 创建一个 EmbeddedChannel，并添加一个 FrameChunkDecoder，其将以 3 字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

        // 将 2 字节数据写入 EmbeddedChannel，并断言它们将会产生一个新的栈帧
        assertTrue(channel.writeInbound(input.readBytes(2)));

        try {
            // 写入一个 4 字节大小的帧，并捕获预期的 TooLongFrameException
            channel.writeInbound(input.readBytes(4));
            Assert.fail();
        } catch (TooLongFrameException e) {
            // e.printStackTrace();
        }

        // 写入生成的 2 字节，并断言将会产生一个有效帧
        assertTrue(channel.writeInbound(input.readBytes(3)));
        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的消息
        // 第一次写入了 2 字节的数据
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(2), read);
        read.release();

        // 第三次写入了 3 字节的数据
        read = channel.readInbound();
        assertEquals(buf.skipBytes(4).readSlice(3), read);
        read.release();

        // 释放缓存字节的资源
        buf.release();
    }

}