package cn.imtianx.bluetoothchatdemo.bean;

/**
 * Created by imtianx on 2016-8-17.
 */
public class EventMsg {
    private int msgType;

    public EventMsg() {
    }

    public EventMsg(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
