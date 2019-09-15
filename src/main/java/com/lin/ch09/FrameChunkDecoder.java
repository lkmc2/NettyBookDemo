package com.lin.ch09;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 栈帧分块解码器（出站 EmbeddedChannel 的异常测试）
 *
 * 拓展 ByteToMessageDecoder 以将入栈字节解码为消息
 * @author lkmc2
 * @date 2019/9/15 13:42
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {

    /** 最大栈帧大小 **/
    private final int maxFrameSize;

    public FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outList) throws Exception {
        // 获取可读的字节长度
        int readableBytes = in.readableBytes();

        // 可读的字节长度大于最大的栈帧大小
        if (readableBytes > maxFrameSize) {
            // 抛弃所有字节
            in.clear();
            throw new TooLongFrameException();
        }

        // 读取所有字节信息
        ByteBuf buf = in.readBytes(readableBytes);
        // 将字节信息添加到输出列表中
        outList.add(buf);
    }

}
