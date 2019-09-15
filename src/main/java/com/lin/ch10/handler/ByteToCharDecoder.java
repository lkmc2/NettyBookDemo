package com.lin.ch10.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 将字节转换成 char 类型的解码器
 * @author lkmc2
 * @date 2019/9/15 16:59
 */
public class ByteToCharDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outList) throws Exception {
        // 当输入的字节的可读长度大于等于2
        while (in.readableBytes() >= 2) {
            // 读取一个 char 变量，添加到输出列表中
            outList.add(in.readChar());
        }
    }

}
