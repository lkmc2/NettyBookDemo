package com.lin.ch11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 具有 SSL 支持的通道初始化器
 * @author lkmc2
 * @date 2019/9/15 17:21
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    /** SSL 上下文 **/
    private final SslContext context;

    /** 是否开始安全传输层协议 **/
    private final boolean startTls;

    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        // 对于每个 SslHandler 实例，都使用 Channel 的 ByteBufAllocator 从 SslContext 获取一个新的 SSLEngine
        SSLEngine engine = context.newEngine(channel.alloc());
        // 将 SslHandler 作为第一个 ChannelHandler 添加到 ChannelPipeline 中
        channel.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }

}
