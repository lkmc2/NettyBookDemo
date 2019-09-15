package com.lin.ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * 自动压缩 http 消息的通道初始化器
 * @author lkmc2
 * @date 2019/9/15 17:47
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {

    /** 是否是客户端 **/
    private final boolean isClient;

    public HttpCompressionInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = channel.pipeline();

        if (isClient) {
            // 是客户端
            // 添加客户端编解码器
            pipeline.addLast("codec", new HttpClientCodec());
            // 添加 HttpContentDecompressor 以处理来自服务器的压缩内容
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        } else {
            // 是服务器
            // 添加服务器编解码器
            pipeline.addLast("codec", new HttpServerCodec());
            // 添加 HttpContentCompressor 来压缩内容
            pipeline.addLast("compressor", new HttpContentCompressor());
        }
    }

}
