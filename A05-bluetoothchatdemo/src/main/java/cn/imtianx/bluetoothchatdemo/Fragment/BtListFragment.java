package cn.imtianx.bluetoothchatdemo.Fragment;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.imtianx.bluetoothchatdemo.R;
import cn.imtianx.bluetoothchatdemo.activity.MainActivity;
import cn.imtianx.bluetoothchatdemo.adapter.BtAdapter;
import cn.imtianx.bluetoothchatdemo.bean.BtInfo;
import cn.imtianx.bluetoothchatdemo.bean.EventMsg;

/**
 * 蓝牙列表
 * <p/>
 * Created by imtianx on 2016-8-17.
 */
public class BtListFragment extends Fragment implements View.OnClickListener {


    private View mRootView;

    private ListView mListView;
    private Button mBtnStartServer;
    private Button mBtnStartSearch;

    private BtAdapter mAdapter;
    private List<BtInfo> mDatas;

    private BluetoothAdapter mBluetoothAdapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_bt_list, null);

        initView();
        registerBroadcast();

        init();

        return mRootView;
    }

    private void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.list_bt);
        mBtnStartServer = (Button) mRootView.findViewById(R.id.btn_start_server);
        mBtnStartServer.setOnClickListener(this);
        mBtnStartSearch = (Button) mRootView.findViewById(R.id.btn_start_search);
        mBtnStartSearch.setOnClickListener(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mDatas = new ArrayList<>();
        mAdapter = new BtAdapter(mDatas, getActivity());
        mListView.setAdapter(mAdapter);

        //列表item设置监听，
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BtInfo info = mDatas.get(i);
                //好友mac地址
                MainActivity.FRIEND_MAC_ADDRESS = info.getAddress();
                //显示提示对话框
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("连接");
                dialog.setMessage("名称：" + info.getName() + "\n" + "地址：" + info.getAddress());
                dialog.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mBluetoothAdapter.cancelDiscovery();
                        mBtnStartSearch.setText("重新搜索");

                        //连接后，跳转到会话页面
                        MainActivity.mType = MainActivity.Type.CILENT;
                        //viewPager 显示第二页
                        MainActivity.mViewPager.setCurrentItem(1);
                        //通知 ChatListFragment 刷新信息
                        EventBus.getDefault().post(new EventMsg(1));

                        dialogInterface.dismiss();

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.FRIEND_MAC_ADDRESS = "";
                        dialogInterface.dismiss();

                    }
                });
                dialog.show();
            }
        });
    }


    private void init() {
        //根据适配器得到所有的设备信息
        Set<BluetoothDevice> deviceSet = mBluetoothAdapter.getBondedDevices();
        if (deviceSet.size() > 0) {
            for (BluetoothDevice device : deviceSet) {
                mDatas.add(new BtInfo(device.getName(), device.getAddress(), true));
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size() - 1);
            }
        } else {
            if (mDatas.size() == 0)
                Toast.makeText(getActivity(), "没有设备信息", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), "未搜索到蓝牙信息", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        //设备被发现广播
        IntentFilter discoveryFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, discoveryFilter);

        // 设备发现完成
        IntentFilter foundFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, foundFilter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_server://开启服务器
                MainActivity.mType = MainActivity.Type.SERVER;
                MainActivity.mViewPager.setCurrentItem(1);
                EventBus.getDefault().post(new EventMsg(1));

                break;
            case R.id.btn_start_search://开始查找
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                    mBtnStartSearch.setText("重新搜索");
                } else {
                    mDatas.clear();
                    mAdapter.notifyDataSetChanged();
                    init();
                }
                mBluetoothAdapter.startDiscovery();
                mBtnStartSearch.setText("ֹͣ停止搜索");
                break;
        }

    }




    /**
     * 发现设备广播
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 获得设备信息
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 如果绑定的状态不一样
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mDatas.add(new BtInfo(device.getName(), device.getAddress(), false));
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(mDatas.size() - 1);
                }
                // 如果搜索完成了
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mListView.getCount() == 0) {
                    Toast.makeText(context, "没有发现设备！", Toast.LENGTH_SHORT).show();
                }
                mBtnStartSearch.setText("重新搜索");
            }

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 3);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        getActivity().unregisterReceiver(mReceiver);
    }
}
