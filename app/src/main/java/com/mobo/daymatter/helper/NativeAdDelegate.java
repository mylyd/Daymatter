package com.mobo.daymatter.helper;

import android.app.Activity;
import android.view.View;

import com.mobo.daymatter.framents.BaseFragment;
import com.mobo.daymatter.utils.MoboLogger;

import java.util.ArrayList;

import ad.mobo.base.bean.NativeResponseInfo;
import ad.mobo.base.interfaces.IMediaSizeAdapter;
import ad.mobo.base.request.AdPull;
import ad.mobo.base.view.AbsNativeDisplayView;

public class NativeAdDelegate {
    private static final String TAG = "NativeAdDelegate";
    private AdPull mPull;

    public void initPull(final int relatedId, final AbsNativeDisplayView mNativeView, final IMediaSizeAdapter adapter) {
        mPull = new AdPull().info(AdInfoUtil.getAdsIdInfo(relatedId)).handler(new AdPull.NativeListener() {
            @Override
            public void onFailed() {
                MoboLogger.debug(TAG, "request failed related " + relatedId);
            }

            @Override
            public void onSucess(ArrayList<NativeResponseInfo> arrayList) {
                MoboLogger.debug(TAG, "request success related " + relatedId);
                mNativeView.setNativeResoponseInfo(arrayList.get(0), adapter);
                if (mNativeView.getVisibility() != View.VISIBLE) {
                    mNativeView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void initPull(int relatedId, AbsNativeDisplayView mNativeView) {
        initPull(relatedId, mNativeView, null);
    }

    public void load(Activity activity) {
        mPull.load(activity);
    }

    public void load(BaseFragment fragment) {
        if (fragment != null && fragment.getActivity() != null) {
            mPull.load(fragment.getActivity());
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
