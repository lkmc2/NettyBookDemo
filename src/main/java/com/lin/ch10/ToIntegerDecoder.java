package com.lin.ch10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 将字节解码成 int 类型的解码器
 *
 * 拓展 ByteToMessageDecoder 类，以将字节解码为特定的格式
 * @author lkmc2
 * @date 2019/9/15 15:35
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outList) throws Exception {
        // 当输入字节的可读字节数长度大于等于 4 时
        if (in.readableBytes() >= 4) {
            // 读取一个整数，并添加到输出列表中
            outList.add(in.readInt());
        }
    }

}
