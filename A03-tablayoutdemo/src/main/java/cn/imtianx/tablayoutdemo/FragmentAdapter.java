package cn.imtianx.tablayoutdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by imtianx on 2016-5-27.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<String> mTitles; //标题
    private List<Fragment> mFragments;//viewpager 显示的页面

    public FragmentAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments) {
        super(fm);
        mTitles = titles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    /**
     * tab 标题
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
