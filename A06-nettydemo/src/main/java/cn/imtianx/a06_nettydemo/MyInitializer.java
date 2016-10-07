package cn.imtianx.a06_nettydemo;

import android.os.Handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * Created by imtianx on 2016-9-11.
 */
public class MyInitializer extends ChannelInitializer<SocketChannel> {

    private final Handler mHandler;

    public MyInitializer(Handler handler) {
        mHandler = handler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        socketChannel.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE,
                ClassResolvers.weakCachingResolver(null)));
        socketChannel.pipeline().addLast(new ObjectEncoder());

        pipeline.addLast(new MyHandler(mHandler));
    }
}
