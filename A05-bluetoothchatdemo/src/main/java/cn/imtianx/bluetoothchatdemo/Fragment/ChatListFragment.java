package cn.imtianx.bluetoothchatdemo.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.imtianx.bluetoothchatdemo.R;
import cn.imtianx.bluetoothchatdemo.activity.MainActivity;
import cn.imtianx.bluetoothchatdemo.adapter.ChatAdapter;
import cn.imtianx.bluetoothchatdemo.bean.EventMsg;
import cn.imtianx.bluetoothchatdemo.bean.MsgBean;

/**
 * 会话列表
 * <p/>
 * Created by imtianx on 2016-8-17.
 */
public class ChatListFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ChatListFragment";

    private View mRootView;

    private ListView mListView;
    private Button mBtnStopServer;
    private EditText mEtInputMsg;
    private Button mBtnSendMsg;

    private ChatAdapter mAdapter;
    private List<MsgBean> mDatas;


    // 蓝牙服务端socket
    private BluetoothServerSocket mServerSocket;
    // 蓝牙客户端socket
    private BluetoothSocket mSocket;
    // 设备
    private BluetoothDevice mDevice;
    private BluetoothAdapter mBluetoothAdapter;

    // 蓝牙客户端和服务器线程类
    private ServerThread mServerThread;
    private ClientThread mClientThread;
    private ReadThread mReadThread;

    private static final int STATUS_CONNECT = 0;
    private static final int STATUS_CONNECT_SUCCESS = 1;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String info = (String) msg.obj;


            if (msg.what == STATUS_CONNECT) {
                Toast.makeText(getActivity(), "" + info, Toast.LENGTH_SHORT).show();
            }

            if (msg.what == 1) {
                mDatas.add(new MsgBean(info, MainActivity.Type.SERVER, new Date()));
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size() - 1);
            } else {
                mDatas.add(new MsgBean(info, MainActivity.Type.CILENT, new Date()));
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size() - 1);
            }


        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_chat_list, null);
        EventBus.getDefault().register(ChatListFragment.this);
        initView();

        return mRootView;
    }

    private void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.list_chat);

        mBtnStopServer = (Button) mRootView.findViewById(R.id.btn_stop_server);
        mBtnStopServer.setOnClickListener(this);

        mEtInputMsg = (EditText) mRootView.findViewById(R.id.et_input_msg);
        mEtInputMsg.clearFocus();

        mBtnSendMsg = (Button) mRootView.findViewById(R.id.btn_send_msg);
        mBtnSendMsg.setOnClickListener(this);

        mDatas = new ArrayList<>();
        mAdapter = new ChatAdapter(mDatas, getActivity());
        mListView.setAdapter(mAdapter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_stop_server://断开
                if (MainActivity.mType == MainActivity.Type.CILENT) {
                    shutdownClient();
                } else if (MainActivity.mType == MainActivity.Type.SERVER) {
                    shutdownServer();
                }
                MainActivity.isOpen = false;
                MainActivity.mType = MainActivity.Type.NONE;
                Toast.makeText(getActivity(), "已断开连接！", Toast.LENGTH_SHORT).show();
                MainActivity.mViewPager.setCurrentItem(0);
                break;

            case R.id.btn_send_msg://发送消息
                String msg = mEtInputMsg.getText().toString();
                if (TextUtils.isEmpty(msg))
                    Toast.makeText(getActivity(), "发送内容不能为空哦！", Toast.LENGTH_SHORT).show();
                else {
                    //发送消息
                    sendMessageHandle(msg);
                    mEtInputMsg.setText("");
                    mEtInputMsg.clearFocus();
                    InputMethodManager manager = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(mEtInputMsg.getWindowToken(), 0);
                }
                break;
        }

    }

    // 发送数据
    private void sendMessageHandle(String msg) {
        if (mSocket == null) {
            Toast.makeText(getActivity(), "没有连接", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            OutputStream os = mSocket.getOutputStream();
            os.write(msg.getBytes());

            MsgBean bean = new MsgBean(msg, MainActivity.Type.CILENT, new Date());

            Log.e(TAG, "sendMessageHandle: " + bean.toString());
            mDatas.add(bean);
            mAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // 客户端线程
    private class ClientThread extends Thread {
        public void run() {
            try {
                mSocket = mDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                Message msg = new Message();
                msg.obj = "请稍候，正在连接服务器:" + MainActivity.FRIEND_MAC_ADDRESS;
                msg.what = STATUS_CONNECT;
                mHandler.sendMessage(msg);

                mSocket.connect();

                msg = new Message();
                msg.obj = "已经连接上服务端！可以发送信息。";
                msg.what = STATUS_CONNECT_SUCCESS;
                mHandler.sendMessage(msg);
                // 启动接受数据
                mReadThread = new ReadThread();
                mReadThread.start();
            } catch (IOException e) {
                Message msg = new Message();
                msg.obj = "连接服务端异常！断开连接重新试一试。";
                msg.what = STATUS_CONNECT_SUCCESS;
                mHandler.sendMessage(msg);
            }
        }
    }

    // 服务器端线程
    private class ServerThread extends Thread {
        public void run() {
            try {
                // 创建一个蓝牙服务器 参数分别：服务器名称、UUID
                mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("btserver",
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                Message msg = new Message();
                msg.obj = "请稍候，正在等待客户端的连接...";
                msg.what = STATUS_CONNECT;
                mHandler.sendMessage(msg);

				/* 接受客户端的连接请求 */
                mSocket = mServerSocket.accept();

                msg = new Message();
                msg.obj = "客户端已经连接上！可以发送信息。";
                msg.what = STATUS_CONNECT;
                mHandler.sendMessage(msg);
                // 启动接受数据
                mReadThread = new ReadThread();
                mReadThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /* 停止服务器 */
    private void shutdownServer() {
        new Thread() {
            public void run() {
                if (mServerThread != null) {
                    mServerThread.interrupt();
                    mServerThread = null;
                }
                if (mReadThread != null) {
                    mReadThread.interrupt();
                    mReadThread = null;
                }
                try {
                    if (mSocket != null) {
                        mSocket.close();
                        mSocket = null;
                    }
                    if (mServerSocket != null) {
                        mServerSocket.close();
                        mServerSocket = null;
                    }
                } catch (IOException e) {
                    Log.e("server", "mserverSocket.close()", e);
                }
            }

            ;
        }.start();
    }

    /* ͣ停止客户端连接 */
    private void shutdownClient() {
        new Thread() {
            public void run() {
                if (mClientThread != null) {
                    mClientThread.interrupt();
                    mClientThread = null;
                }
                if (mReadThread != null) {
                    mReadThread.interrupt();
                    mReadThread = null;
                }
                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mSocket = null;
                }
            }

            ;
        }.start();
    }

    // 读取数据
    private class ReadThread extends Thread {
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream is = null;
            try {
                is = mSocket.getInputStream();
                while (true) {
                    if ((bytes = is.read(buffer)) > 0) {
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            buf_data[i] = buffer[i];
                        }
                        String s = new String(buf_data);
                        Message msg = new Message();
                        msg.obj = s;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }

    }

    @Subscribe
    public void onEventMainThread(EventMsg msg) {

        if (msg.getMsgType() == 1) {
            if (MainActivity.isOpen) {
                Toast.makeText(getActivity(), "连接已经打开，可以通信。若需连接其他账号，请先断开", Toast.LENGTH_SHORT).show();
                return;
            }

            if (MainActivity.mType == MainActivity.Type.CILENT) {
                String address = MainActivity.FRIEND_MAC_ADDRESS;//蓝牙地址
                if (!TextUtils.isEmpty(address)) {
                    mDevice = mBluetoothAdapter.getRemoteDevice(address);
                    mClientThread = new ClientThread();
                    mClientThread.start();
                    MainActivity.isOpen = true;
                } else {
                    Toast.makeText(getActivity(), "address is null !", Toast.LENGTH_SHORT).show();
                }
            } else if (MainActivity.mType == MainActivity.Type.SERVER) {
                mServerThread = new ServerThread();
                mServerThread.start();
                MainActivity.isOpen = true;
            }

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (MainActivity.mType == MainActivity.Type.CILENT) {
            shutdownClient();
        } else if (MainActivity.mType == MainActivity.Type.SERVER) {
            shutdownServer();
        }
        MainActivity.isOpen = false;
        MainActivity.mType = MainActivity.Type.NONE;
    }
}
