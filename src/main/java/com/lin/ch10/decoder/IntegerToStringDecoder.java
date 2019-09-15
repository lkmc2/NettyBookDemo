package com.lin.ch10.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 将整数转换成字符串的解码器（使用 MessageToMessageDecoder）
 *
 * 拓展了 MessageToMessageDecoder ，以将一种 POJO 转换成另一种 POJO
 * @author lkmc2
 * @date 2019/9/15 15:54
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

    @Override
    protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> outList) throws Exception {
        // 将传入的整数转换成字符串后，添加到输出列表中
        outList.add(String.valueOf(msg));
    }

}
