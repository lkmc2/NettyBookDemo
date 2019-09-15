package com.lin.ch09;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 固定长度的栈帧解码器（用于入站 EmbeddedChannel 测试）
 * @author lkmc2
 * @date 2019/9/15 12:15
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    /** 栈帧长度 **/
    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) throws IllegalAccessException {
        if (frameLength <= 0) {
            throw new IllegalAccessException("frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outList) throws Exception {
        // 当可读字节长度大于等于栈帧长度时
        while (in.readableBytes() >= frameLength) {
            // 读取一个栈帧长度的字节信息
            ByteBuf buf = in.readBytes(frameLength);
            // 放入字节信息到输出列表中
            outList.add(buf);
        }
    }

}
