package cn.imtianx.bluetoothchatdemo.bean;

import java.util.Date;

import cn.imtianx.bluetoothchatdemo.activity.MainActivity;

/**
 * 消息实体
 * <p/>
 * Created by imtianx on 2016-8-17.
 */
public class MsgBean {

    private String name;
    private String msg;
    private MainActivity.Type type;
    private Date date;


    public MsgBean() {
    }

    public MsgBean(String msg, MainActivity.Type type, Date date) {
        this.msg = msg;
        this.type = type;
        this.date = date;
    }

    public MsgBean(String name, String msg, MainActivity.Type type, Date date) {
        this.name = name;
        this.msg = msg;
        this.type = type;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MainActivity.Type getType() {
        return type;
    }

    public void setType(MainActivity.Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MsgBean{" +
                "name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", date=" + date +
                '}';
    }
}
