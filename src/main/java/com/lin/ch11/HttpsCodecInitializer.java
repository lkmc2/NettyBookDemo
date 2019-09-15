package com.lin.ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 使用 https 的通道初始化器
 * @author lkmc2
 * @date 2019/9/15 17:53
 */
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {

    /** SSL 上下文 **/
    private final SslContext context;

    /** 是否是客户端 **/
    private final boolean isClient;

    public HttpsCodecInitializer(SslContext context, boolean isClient) {
        this.context = context;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = channel.pipeline();

        // 创建 SSL 引擎
        SSLEngine engine = context.newEngine(channel.alloc());

        // 将 SslHandler 添加到 ChannelPipeline 中以使用 https
        pipeline.addLast("ssl", new SslHandler(engine));

        if (isClient) {
            // 是客户端，添加客户端编解码器
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            // 是服务器，添加服务器编解码器
            pipeline.addLast("codec", new HttpServerCodec());
        }
    }

}
