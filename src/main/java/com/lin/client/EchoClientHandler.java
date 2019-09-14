package com.lin.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 用于服务器交互的 Channel 事件处理器（客户端使用）
 *
 *  注解 @Sharable 表示一个 ChannelHandler 可以被多个 Channel 安全地共享
 *  Inbound 表示入站
 * @author lkmc2
 * @date 2019/9/14 11:20
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 当从服务器接收到一条消息时，将调用此方法
     * @param ctx 通道事件处理器上下文
     * @param in 服务器发过来的消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println(String.format("客户端接收到：【%s】", in.toString(CharsetUtil.UTF_8)));
    }

    /**
     * 在到服务器的连接已经建立之后，将调用此方法
     * @param ctx 通道事件处理器上下文
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当被通知 Channel 是活跃的时候，发送一条消息到服务器
        ctx.writeAndFlush(Unpooled.copiedBuffer("我被激活了", CharsetUtil.UTF_8));
    }

    /**
     * 当处理过程中，有异常抛出会调用此方法
     * @param ctx 通道事件处理器上下文
     * @param cause 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印异常栈跟踪信息
        cause.printStackTrace();
        // 当前该 Channel
        ctx.close();
    }

}
