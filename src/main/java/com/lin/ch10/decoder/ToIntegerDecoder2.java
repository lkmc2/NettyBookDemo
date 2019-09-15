package com.lin.ch10.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 将字节解码成 int 类型的解码器（使用 ReplayingDecoder，不需要 if (in.readableBytes() >= 4) 判断，自动处理）
 *
 * 拓展 ReplayingDecoder<Void> 以将字节解码为消息
 * @author lkmc2
 * @date 2019/9/15 15:47
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outList) throws Exception {
        // 从输入的中字节读取一个整数
        outList.add(in.readInt());
    }

}
