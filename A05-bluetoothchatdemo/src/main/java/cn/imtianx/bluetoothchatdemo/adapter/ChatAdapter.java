package cn.imtianx.bluetoothchatdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.imtianx.bluetoothchatdemo.R;
import cn.imtianx.bluetoothchatdemo.activity.MainActivity;
import cn.imtianx.bluetoothchatdemo.bean.MsgBean;

/**
 * 会话适配器
 * <p/>
 * Created by imtianx on 2016-8-17.
 */
public class ChatAdapter extends BaseAdapter {

    private List<MsgBean> mDatas;
    private Context mContext;

    public ChatAdapter(List<MsgBean> datas, Context context) {
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

    /**
     * item 类型
     *
     * @param position
     * @return 1 server；2 client
     */
    @Override
    public int getItemViewType(int position) {
        MsgBean msgBean = mDatas.get(position);
        if (msgBean.getType() == MainActivity.Type.SERVER)
            return 0;
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            if (getItemViewType(i) == 0) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_from_msg, viewGroup, false);
                holder = new ViewHolder(view, 0);
            } else if (getItemViewType(i) == 1) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_to_msg, viewGroup, false);
                holder = new ViewHolder(view, 1);
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //设置数据
        MsgBean msgBean = mDatas.get(i);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.mtvDate.setText("" + sdf.format(msgBean.getDate()));
        holder.mtvMsg.setText(msgBean.getMsg());
        notifyDataSetChanged();

        return view;
    }

    private class ViewHolder {
        TextView mtvDate;
        TextView mtvMsg;

        public ViewHolder(View view, int type) {
            if (type == 0) {
                mtvDate = (TextView) view.findViewById(R.id.tv_from_msg_date);
                mtvMsg = (TextView) view.findViewById(R.id.tv_from_msg_info);
            } else if (type == 1) {
                mtvDate = (TextView) view.findViewById(R.id.tv_to_msg_date);
                mtvMsg = (TextView) view.findViewById(R.id.tv_to_msg_info);
            }
        }
    }
}
