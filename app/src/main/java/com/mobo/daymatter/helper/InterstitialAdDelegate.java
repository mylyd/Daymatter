package com.mobo.daymatter.helper;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.mobo.daymatter.framents.BaseFragment;
import com.mobo.daymatter.utils.MoboLogger;

import java.util.ArrayList;

import ad.mobo.base.bean.NativeResponseInfo;
import ad.mobo.base.interfaces.IMediaSizeAdapter;
import ad.mobo.base.request.AdPull;
import ad.mobo.base.view.AbsNativeDisplayView;

public class InterstitialAdDelegate {
    private static final String TAG = "InterstitialAdDelegate";
    private AdPull mPull;

    public void initPulls(final int relatedId, final boolean isShow, final Activity activity) {
        mPull = new AdPull().info(AdInfoUtil.getAdsIdInfo(relatedId)).handler(new AdPull.InterstitialListener() {
            @Override
            public void onFailed() {

            }

            @Override
            public void onSucess() {
                if (isShow && mPull != null && !mPull.isLoading()) {
                    mPull.show(activity);
                }
            }

            @Override
            public void onClosed() {
                activity.finish();
            }

            @Override
            public void onClick() {

            }
        });
    }

    public void initPull(int relatedId, boolean isShow, Activity activity) {
        for (int ads : AdInfoUtil.getNoTypeAdsId()) {
            if (ads != relatedId) {
                return;
            }
        }
        initPulls(relatedId, isShow, activity);
    }

    public void showAds(Activity activity) {
        if (mPull != null && !mPull.isLoading() && mPull.isSucess()) {
            mPull.show(activity);
        }
    }

    public void load(BaseFragment fragment) {
        if (fragment != null && fragment.getActivity() != null &&
                mPull != null && !mPull.isLoading() && mPull.isSucess()) {
            mPull.show(fragment.getActivity());
        }
    }

    public boolean isLoading() {
        return mPull.isLoading();
    }

    public boolean isSuccess() {
        return mPull.isSucess();
    }

    public boolean isFailed() {
        return !isLoading() && !isSuccess();
    }
}
