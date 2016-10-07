package cn.imtianx.bluetoothchatdemo.bean;

/**
 * 蓝牙信息
 * <p/>
 * Created by imtianx on 2016-8-17.
 */
public class BtInfo {

    private String name;
    private String address;
    private boolean state;

    public BtInfo(String name, String address, boolean state) {
        this.name = name;
        this.address = address;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
