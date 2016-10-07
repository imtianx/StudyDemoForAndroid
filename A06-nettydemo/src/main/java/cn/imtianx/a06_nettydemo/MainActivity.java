package cn.imtianx.a06_nettydemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetSocketAddress;

import cn.imtianx.nettymessage.ChatMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MainActivity extends AppCompatActivity {


    private TextView mTvMsg;
    private Button mBtnSend;
    private EditText mEtInputMsg;

    private String host = "192.168.1.101";
    int port = 8080;

    NioEventLoopGroup mGroup;
    Channel mChannel;
    ChannelFuture mChannelFuture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {

        //建立连接
        connect(mHandler);

        mTvMsg = (TextView) findViewById(R.id.tv_msg);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mEtInputMsg = (EditText) findViewById(R.id.et_input_msg);


        //发送消息 监听
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = String.valueOf(mEtInputMsg.getText()) + "\r\n";
                if (msg.length() != 0) {
                    mHandler.obtainMessage(0x03).sendToTarget();
                }
            }
        });
    }


    /**
     * 连接服务器
     *
     * @param handler
     */
    private void connect(final Handler handler) {
        new Thread() {
            @Override
            public void run() {
                try {

                    mGroup = new NioEventLoopGroup();
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(mGroup);

                    bootstrap.channel(NioSocketChannel.class);
                    bootstrap.handler(new MyInitializer(handler));
                    bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                    bootstrap.option(ChannelOption.TCP_NODELAY, true);

                    mChannelFuture = bootstrap.connect(new InetSocketAddress(host, port));

                    mChannel = mChannelFuture.sync().channel();
                    mChannelFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            handler.obtainMessage(0x00).sendToTarget();
                        }
                    });

                    mChannel.closeFuture().sync();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            String m = msg.obj + "";

            switch (msg.what) {

                case 0x00://在线
                    String s = new String("已连接---");
                    ChatMessage chatMessage = new ChatMessage();
                    byte[] b = s.getBytes();
                    chatMessage.setBytes(b);
                    chatMessage.setSumCountPackage(b.length);
                    chatMessage.setCountPackage(1);
                    chatMessage.setSend_time(System.currentTimeMillis());

                    mChannel.writeAndFlush(chatMessage);
                    break;
                case 0x01://接收
                    mTvMsg.setText(mTvMsg.getText() + m + "\r\n");
                    break;
                case 0x02://发送完成
                    mEtInputMsg.setText("");
                    break;
                case 0x03://发送
                    String ms = mEtInputMsg.getText().toString().trim();
                    if (TextUtils.isEmpty(ms)) {
                        Toast.makeText(MainActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ChatMessage cmsg = new ChatMessage();
                    cmsg.setSend_time(System.currentTimeMillis());

                    byte[] bb = ms.getBytes();
                    cmsg.setBytes(bb);
                    cmsg.setSumCountPackage(bb.length);
                    cmsg.setCountPackage(1);
                    cmsg.setSend_time(System.currentTimeMillis());

                    mChannel.writeAndFlush(cmsg).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            mHandler.obtainMessage(0x02).sendToTarget();
                        }
                    });
                    break;

                default:
                    Toast.makeText(MainActivity.this, "未知消息" + m, Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };
}
