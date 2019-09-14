package com.lin;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 用于回复消息的 Netty 客户端
 * @author lkmc2
 * @date 2019/9/14 11:33
 */
public class EchoClient {

    /** 服务器主机地址 **/
    private final String host;

    /** 服务器监听的端口号 **/
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            System.err.println(String.format("Usage: %s <host> <port>", EchoClient.class.getSimpleName()));
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        // 启动 Netty 客户端
        new EchoClient(host, port).start();
    }

    /**
     * 启动 Netty 客户端
     */
    public void start() throws InterruptedException {
        // 基于 NIO 的事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            // 客户端启动器
            Bootstrap server = new Bootstrap();

            // 设置客户端使用的组
            server.group(group)
                    // 设置使用基于NIO 的 Socket 通道
                    .channel(NioSocketChannel.class)
                    // 设置客户端将连接的服务器主机地址和客户端监听的端口号
                    .localAddress(new InetSocketAddress(host, port))
                    // 添加任务的 Handler 事件处理器
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            // 为通道添加用于服务器交互的 Channel 事件处理器（相当于入站拦截器）
                            channel.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            // 将客户端绑定到指定的端口（同步模式，阻塞等待直到绑定完成）
            ChannelFuture future = server.bind().sync();

            // 监听通道关闭事件（同步模式，阻塞当前线程直到它完成）
            future.channel().closeFuture().sync();
        } finally {
            // 优雅地关闭组，释放所有资源
            group.shutdownGracefully();
        }

    }

}
