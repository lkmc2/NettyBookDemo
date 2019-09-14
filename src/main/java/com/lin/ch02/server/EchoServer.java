package com.lin.ch02.server;
import	java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 用于回复消息的 Netty 服务器
 * @author lkmc2
 * @date 2019/9/14 11:00
 */
public class EchoServer {

    /** 服务器监听的端口号 **/
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.err.println(String.format("Usage: %s <port>", EchoServer.class.getSimpleName()));
            return;
        }

        int port = Integer.parseInt(args [0]);
        // 启动 Netty 服务器
        new EchoServer(port).start();
    }

    /**
     * 启动 Netty 服务器
     */
    public void start() throws InterruptedException {
        // 用于回复客户端信息的 Channel 事件处理器
        final EchoServerHandler serverHandler = new EchoServerHandler();

        // 基于 NIO 的事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            // 服务启动器
            ServerBootstrap server = new ServerBootstrap();

            // 设置服务器使用的组
            server.group(group)
                    // 设置使用基于NIO 服务的 Socket 通道
                    .channel(NioServerSocketChannel.class)
                    // 设置服务器监听的端口号
                    .localAddress(new InetSocketAddress(port))
                    // 添加子任务的 Handler 事件处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            // 为通道添加用于回复客户端信息的 Channel 事件处理器（相当于入站拦截器）
                            channel.pipeline().addLast(serverHandler);
                        }
                    });

            // 将服务器绑定到指定的端口（同步模式，阻塞等待直到绑定完成）
            ChannelFuture future = server.bind().sync();

            System.out.println("Netty 服务器启动成功：http://127.0.0.1/9999");

            // 监听通道关闭事件（同步模式，阻塞当前线程直到它完成）
            future.channel().closeFuture().sync();

            System.out.println("Netty 服务器关闭");
        } finally {
            // 优雅地关闭组，释放所有资源
            group.shutdownGracefully();
        }

    }

}
