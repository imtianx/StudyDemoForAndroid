package cn.imtianx.bluetoothchatdemo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.imtianx.bluetoothchatdemo.R;
import cn.imtianx.bluetoothchatdemo.bean.BtInfo;

/**
 * 蓝牙列表适配器
 * <p/>
 * Created by imtianx on 2016-8-15.
 */
public class BtAdapter extends BaseAdapter {

    private List<BtInfo> mDatas;
    private Context mContext;

    public BtAdapter(List<BtInfo> datas, Context context) {
        mDatas = datas;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_bt_info, viewGroup, false);
            holder = new ViewHolder(view);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        BtInfo btInfo = mDatas.get(i);
        if (!TextUtils.isEmpty(btInfo.getName()))
            holder.mtvName.setText(btInfo.getName());
        else
            holder.mtvName.setText("无名称！");

        if (!TextUtils.isEmpty(btInfo.getAddress()))
            holder.mtvAddress.setText(btInfo.getAddress());
        else
            holder.mtvAddress.setText("无地址！");

        if (btInfo.isState())
            holder.mtvState.setText("已配对");
        else
            holder.mtvState.setText("未配对");


        return view;
    }


    private class ViewHolder {
        TextView mtvName;
        TextView mtvAddress;
        TextView mtvState;

        public ViewHolder(View view) {
            mtvName = (TextView) view.findViewById(R.id.tv_search_bt_name);
            mtvAddress = (TextView) view.findViewById(R.id.tv_search_bt_address);
            mtvState = (TextView) view.findViewById(R.id.tv_search_bt_state);
        }
    }
}
