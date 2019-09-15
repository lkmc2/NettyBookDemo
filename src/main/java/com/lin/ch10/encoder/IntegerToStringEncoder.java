package com.lin.ch10.encoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 将整形编码成字符串的编码器（使用 MessageToMessageEncoder）
 *
 * 拓展 MessageToMessageEncoder<Integer> ，以将 POJO 转换成另一种 POJO
 * @author lkmc2
 * @date 2019/9/15 16:22
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, List<Object> outList) throws Exception {
        // 将传入的整形值转换成字符串后，添加到输出列表中
        outList.add(String.valueOf(msg));
    }

}
