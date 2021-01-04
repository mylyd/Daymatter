package com.mobo.daymatter.framents;

public abstract class ViewPagerFragment extends BaseFragment {
    protected boolean mCurVisible = true;

    public void initVisible(boolean curVisible) {
        this.mCurVisible = curVisible;
    }

    public abstract void onFragmentVisible();
}
