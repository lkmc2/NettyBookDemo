package com.lin.ch10.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 将 char 类型转换成字节的编码器
 * @author lkmc2
 * @date 2019/9/15 17:01
 */
public class CharToByteEncoder extends MessageToByteEncoder<Character> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out) throws Exception {
        // 将 char 类型的值写入字节变量中
        out.writeChar(msg);
    }

}
