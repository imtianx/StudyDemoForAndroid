package cn.imtianx.bluetoothchatdemo.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.imtianx.bluetoothchatdemo.Fragment.BtListFragment;
import cn.imtianx.bluetoothchatdemo.Fragment.ChatListFragment;
import cn.imtianx.bluetoothchatdemo.R;
import cn.imtianx.bluetoothchatdemo.adapter.FragmentAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    public static ViewPager mViewPager;


    private List<Fragment> mFragments;
    private List<String> mTitles;
    private FragmentAdapter mFragmentAdapter;

    private BtListFragment mBtListFragment;
    private ChatListFragment mChatListFragment;


    public static Type mType = Type.NONE;//类型
    public static boolean isOpen = false;
    public static String FRIEND_MAC_ADDRESS = "";

    public enum Type {
        NONE, SERVER, CILENT
    }


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

        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.container);


        //标题
        mTitles = new ArrayList<>();
        mTitles.add("蓝牙列表");
        mTitles.add("会话列表");

        //添加页面
        mFragments = new ArrayList<>();
        mBtListFragment = new BtListFragment();
        mChatListFragment = new ChatListFragment();
        mFragments.add(mBtListFragment);
        mFragments.add(mChatListFragment);

        //初始化适配器
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);

        //设置适配器
        mViewPager.setAdapter(mFragmentAdapter);
        //加载ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
