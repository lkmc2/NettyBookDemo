package com.lin.ch09;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 入站 EmbeddedChannel 测试
 * @author lkmc2
 * @date 2019/9/15 13:09
 */
public class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded() throws IllegalAccessException {
        // 创建（获取）堆中的缓存字节
        ByteBuf buf = Unpooled.buffer();

        // 存储 9 字节的数据
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        // 拷贝字节数据（浅拷贝，修改该变量可能的原来的字节数据产生影响）
        ByteBuf input = buf.duplicate();

        // 创建一个 EmbeddedChannel，并添加一个 FixedLengthFrameDecoder，其将以 3 字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        // 将数据写入 EmbeddedChannel
        assertTrue(channel.writeInbound(input.retain()));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的消息，并且验证是否有 3 帧（切片），其中每帧（切片）都为 3 字节
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        // 上面已经读取完 9 个字节，所以这里没有字节可以读取
        assertNull(channel.readInbound());
        buf.release();
    }

    @Test
    public void testFramesDecoded2() throws IllegalAccessException {
        // 创建（获取）堆中的缓存字节
        ByteBuf buf = Unpooled.buffer();

        // 存储 9 字节的数据
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        // 拷贝字节数据（浅拷贝，修改该变量可能的原来的字节数据产生影响）
        ByteBuf input = buf.duplicate();

        // 创建一个 EmbeddedChannel，并添加一个 FixedLengthFrameDecoder，其将以 3 字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        // 将数据写入 EmbeddedChannel
        assertFalse(channel.writeInbound(input.readBytes(2))); // 返回 false ，因为没有一个完整的可供读取的帧
        assertTrue(channel.writeInbound(input.readBytes(7)));

        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的消息，并且验证是否有 3 帧（切片），其中每帧（切片）都为 3 字节
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        // 上面已经读取完 9 个字节，所以这里没有字节可以读取
        assertNull(channel.readInbound());
        buf.release();
    }

}