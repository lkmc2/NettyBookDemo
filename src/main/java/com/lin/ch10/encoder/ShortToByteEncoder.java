package com.lin.ch10.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 将短整型编码成字节的编码器（使用 MessageToByteEncoder）
 * @author lkmc2
 * @date 2019/9/15 16:13
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
        // 向输入的字节中写入一个短整型值
        out.writeShort(msg);
    }

}
