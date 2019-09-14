package com.lin.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 用于回复客户端信息的 Channel 事件处理器，回复的内容与客户端发过来的内容一样（服务器使用）
 *
 * 注解 @Sharable 表示一个 ChannelHandler 可以被多个 Channel 安全地共享
 * Inbound 表示入站
 * @author lkmc2
 * @date 2019/9/14 10:41
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对于每个传入的消息，都会调用此方法
     * @param ctx 通道事件处理器上下文
     * @param msg 传入的消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 将传入的消息打印到控制台
        ByteBuf in = (ByteBuf) msg;
        System.out.println(String.format("服务器接收到消息： 【%s】", in.toString(CharsetUtil.UTF_8)));

        // 将接收到的消息写给发送者，而不冲刷出站消息
        ctx.write(in);
    }

    /**
     * 批量读取消息时，最后一次调用 channelRead() 方法后，会调用此方法
     * @param ctx 通道事件处理器上下文
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将空消息冲刷到远程节点，并且关闭该 Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 当读取操作期间，有异常抛出会调用此方法
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
