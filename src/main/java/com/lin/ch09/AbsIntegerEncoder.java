package com.lin.ch09;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 整数绝对值编码器（用于出站 EmbeddedChannel 测试）
 *
 * 拓展 MessageToMessageEncoder 以将一个消息编码转换成另一种格式
 * @author lkmc2
 * @date 2019/9/15 13:28
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outList) throws Exception {
        // 当输入字节的可读长度大于 4 时
        while (in.readableBytes() >= 4) {
            // 从输入字节中读取一个整数值，并求绝对值
            int value = Math.abs(in.readInt());
            // 将整数值添加到输出列表中
            outList.add(value);
        }
    }

}
