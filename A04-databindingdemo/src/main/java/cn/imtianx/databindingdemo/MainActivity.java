package cn.imtianx.databindingdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import cn.imtianx.databindingdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mBinding;
    UserBean mUserBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        mUserBean = new UserBean("imtianx", "男", 22);
        mUserBean.setHeadPic("http://imtianx.cn/img/head.jpg");
//        mUserBean.setSex(null);
        mBinding.setUser(mUserBean);
        mBinding.setPresenter(new Presenter());
        mBinding.viewStub.getViewStub().inflate();

    }

    /**
     * 绑定事件:
     * 1.方法引用：必须使用android 已有的监听的方法名及其参数，如下onTextChanged，onClick
     * 2.监听器引用：可以方便的丛xml中向java代码中传递数据，可使用lambda表达式,如onClickListenerBinding
     */
    public class Presenter {

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mUserBean.setName(s.toString());
            mUserBean.setIsAdult(!(mUserBean.isAdult.get()));
//            mBinding.setUser(mUserBean);
        }

        public void onClick(View view) {
            Toast.makeText(MainActivity.this, "点击了名字", Toast.LENGTH_SHORT).show();
        }

        public void onClickListenerBinding(UserBean bean) {
            Toast.makeText(MainActivity.this, bean.getSex(), Toast.LENGTH_SHORT).show();
        }

    }
}
