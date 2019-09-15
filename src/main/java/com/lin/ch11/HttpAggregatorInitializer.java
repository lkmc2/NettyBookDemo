package com.lin.ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 自动聚合 http 消息的通道初始化器
 * @author lkmc2
 * @date 2019/9/15 17:40
 */
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    /** 是否是客户端 **/
    private final boolean isClient;

    public HttpAggregatorInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = channel.pipeline();

        if (isClient) {
            // 是客户端，添加客户端的编解码器
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            // 是服务端，添加服务端的编解码器
            pipeline.addLast("codec", new HttpServerCodec());
        }


        // 由于 HTTP 的请求和响应可能由许多部分组成，因此你需要聚合它们以形成完整的消息。
        // 为了消除这项繁琐的任务，Netty 提供了一个聚合器，
        // 它可以将多个消息部分合并为 FullHttpRequest 或者 FullHttpResponse 消息。
        // 通过这样的方式，你将总是看到完整的消息内容。

        // 添加最大的消息为 512 kb 的 http 对象聚合器到管道中
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
    }

}
