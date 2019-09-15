package com.lin.ch10.codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * WebSocket 转换处理器，是编码器，也是解码器（使用 MessageToMessageCodec）
 * 
 * 拓展 MessageToMessageCodec，以将 POJO 转换成另一种 POJO
 * @author lkmc2
 * @date 2019/9/15 16:31
 */
public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> outList) throws Exception {
        // 复制一份原来的字节数据，并保留原来的字节数据
        ByteBuf payload = msg.getData().duplicate().retain();

        // 根据类型决定将编码的类型
        switch (msg.getType()) {
            case BINARY:
                outList.add(new BinaryWebSocketFrame(payload));
                break;
            case TEXT:
                outList.add(new TextWebSocketFrame(payload));
                break;
            case CLOSE:
                outList.add(new CloseWebSocketFrame(true, 0, payload));
                break;
            case CONTINUATION:
                outList.add(new ContinuationWebSocketFrame(payload));
                break;
            case PONG:
                outList.add(new PongWebSocketFrame(payload));
                break;
            case PING:
                outList.add(new PingWebSocketFrame(payload));
                break;
            default :
                throw new IllegalStateException("Unsupported WebSocket msg " + msg);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> outList) throws Exception {
        // 复制一份原来的字节数据，并保留原来的字节数据
        ByteBuf payload = msg.content().duplicate().retain();

        // 根据 WebSocket 的栈帧类型决定将解码的类型
        if (msg instanceof BinaryWebSocketFrame) {
            outList.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.BINARY, payload));
        } else if (msg instanceof CloseWebSocketFrame) {
            outList.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CLOSE, payload));
        }else if (msg instanceof PingWebSocketFrame) {
            outList.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PING, payload));
        }else if (msg instanceof PongWebSocketFrame) {
            outList.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PONG, payload));
        }else if (msg instanceof TextWebSocketFrame) {
            outList.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.TEXT, payload));
        }else if (msg instanceof ContinuationWebSocketFrame) {
            outList.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CONTINUATION, payload));
        } else {
            throw new IllegalStateException("Unsupported WebSocket msg " + msg);
        }
    }

    /** 自定义 WebSocket 栈帧 **/
    static final class MyWebSocketFrame {
        /** 被包装的有效负载的 WebSocketFrame 类型 **/
        enum FrameType {
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }

        /** WebSocketFrame 类型 **/
        private final FrameType type;

        /** 字节数据**/
        private final ByteBuf data;

        public MyWebSocketFrame(FrameType type, ByteBuf data) {
            this.type = type;
            this.data = data;
        }

        public FrameType getType() {
            return type;
        }

        public ByteBuf getData() {
            return data;
        }

        @Override
        public String toString() {
            return "MyWebSocketFrame{" +
                    "type=" + type +
                    ", data=" + data +
                    '}';
        }
    }

}
