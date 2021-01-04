package com.mobo.daymatter.framents;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * 所有界面的基类，做一些公用操作，兼容处理等
 */
public abstract class BaseFragment extends Fragment {
    protected volatile AppCompatActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

    /**
     * 获取布局id
     * @return
     */
    protected abstract int getLayoutId();

    protected <T extends View> T findViewById(@IdRes int id) {
        try {
            return getView().findViewById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
