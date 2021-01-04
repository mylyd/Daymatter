package com.mobo.daymatter.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mobo.daymatter.framents.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29.
 */
public class BaseFragmentAdapter<T extends BaseFragment> extends FragmentPagerAdapter {
    private final ArrayList<T> mFragments = new ArrayList<>();

    public BaseFragmentAdapter(FragmentManager fm, List<T> fragments) {
        super(fm);
        mFragments.addAll(fragments);
    }


    public void notifyDataSetChanged(List<T> fragments) {
        mFragments.clear();
        mFragments.addAll(fragments);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
