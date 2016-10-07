package cn.imtianx.a06_nettydemo;

import android.os.Handler;
import android.os.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by imtianx on 2016-9-11.
 */
public class MyHandler extends SimpleChannelInboundHandler<String> {

    private final Handler mHandler;

    public MyHandler(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Message message = mHandler.obtainMessage(0x01);
        message.obj = "[SYSTEM] - " + s;
        message.sendToTarget();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Message message = mHandler.obtainMessage(0x01);
        message.obj = "[SYSTEM] - CLIENT ACTIVE ";
        message.sendToTarget();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Message message = mHandler.obtainMessage(0x01);
        message.obj = "[SYSTEM] - CLIENT INACTIVE";
        message.sendToTarget();
        super.channelInactive(ctx);
    }
}
