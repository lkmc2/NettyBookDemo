package com.lin.ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 具有 http 支持的通道初始化器
 * @author lkmc2
 * @date 2019/9/15 17:32
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {

    /** 是否是客户端 **/
    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = channel.pipeline();

        if (client) {
            // 是客户端
            // HttpResponseDecoder 用于将字节解码为 HttpResponse 、 HttpContent 和 LastHttpContent 消息
            pipeline.addLast("decoder", new HttpResponseDecoder());
            // HttpRequestEncoder 用于将 HttpRequest 、 HttpContent 和 LastHttpContent 消息编码为字节
            pipeline.addLast("encoder", new HttpRequestEncoder());
        } else {
            // 是服务端
            // HttpRequestDecoder 用于将字节解码为 HttpRequest 、 HttpContent 和 LastHttpContent 消息
            pipeline.addLast("decoder", new HttpRequestDecoder());
            // HttpResponseEncoder 用于将 HttpResponse 、 HttpContent 和 LastHttpContent 消息编码为字节
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }
    }

}
