package com.lin.ch10.handler;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * 可以将字节和 char 进行相互转换的编解码器（使用 CombinedChannelDuplexHandler）
 *
 * 继承 CombinedChannelDuplexHandler 类之后，可同时使用 ByteToCharDecoder 解码器和 CharToByteEncoder 编码器的特性
 * @author lkmc2
 * @date 2019/9/15 17:03
 */
public class CombineByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {

    public CombineByteCharCodec() {
        // 将委托实例传递给父类
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }

}
