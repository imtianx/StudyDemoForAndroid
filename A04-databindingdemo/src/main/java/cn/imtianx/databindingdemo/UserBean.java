package cn.imtianx.databindingdemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

/**
 * Created by imtianx on 2016-9-17.
 */
public class UserBean extends BaseObservable {

    private String headPic;
    private String name;
    private String sex;
    private int age;



    public ObservableBoolean isAdult = new ObservableBoolean();

    public UserBean() {
    }

    public UserBean(String name, String sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.isAdult.set(false);
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(cn.imtianx.databindingdemo.BR.name);
    }

    @Bindable
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
        notifyPropertyChanged(cn.imtianx.databindingdemo.BR.sex);
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(cn.imtianx.databindingdemo.BR.age);
    }

    public boolean getIsAdult() {
        return isAdult.get();
    }

    public void setIsAdult(boolean isAdult) {
        this.isAdult.set(isAdult);
    }
}
