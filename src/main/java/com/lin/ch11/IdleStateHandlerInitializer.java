package com.lin.ch11;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 可以发送心跳的通道初始化器
 * @author lkmc2
 * @date 2019/9/15 18:54
 */
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = channel.pipeline();

        // 添加空闲连接状态处理器
        pipeline.addLast( new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
        // 添加自定义心跳事件处理器
        pipeline.addLast(new HeartbeatHandler());
    }

    /** 添加自定义心跳事件处理器 **/
    static final class HeartbeatHandler extends ChannelInboundHandlerAdapter {
        /** 一个表示心跳的不主动释放的字节 **/
        private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.ISO_8859_1));

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                // 是空闲状态事件
                // 刷新心跳信息到客户端，并添加监听事件，如果发送失败则关闭通道
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate())
                        .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }


}
