package com.mobo.daymatter.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.mobo.daymatter.R;
import com.mobo.daymatter.helper.AdInfoUtil;
import com.mobo.daymatter.helper.InterstitialAdDelegate;
import com.mobo.daymatter.helper.NativeAdDelegate;
import com.mobo.daymatter.interfaces.GrayKey;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;

import ad.mobo.base.request.AdPull;
import ad.mobo.base.view.NativeControllerLayout;
import ad.mobo.common.request.AdRequestHelper;

/**
 * @author : ydli
 * @description 完成喝水功能页面
 * @time :
 */
public class OkDrinkActivity extends BaseActivity {
    private static final String TAG = "OkDrinkActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_drink);
        //initAd();
    }

    public void OnBack(View view) {
        onBackPressed();
    }


    public static void startNew(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), OkDrinkActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    private void initAd() {
        mPull = new AdPull().asInterstitial().info(AdRequestHelper.getPullInfos(1004 + "", 1,
                AdRequestHelper.getAdInfos(admob, facebook, "", "", "")))
                .handler(new AdPull.InterstitialListener() {
                    @Override
                    public void onFailed() {
                        Log.d(TAG, "onFailed: ");
                    }

                    @Override
                    public void onSucess() {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onClosed() {
                        finish();
                    }

                    @Override
                    public void onClick() {

                    }
                });
        mPull.load(OkDrinkActivity.this);
    }

    @Override
    public void onBackPressed() {
        if (mPull == null) {
            finish();
        }
        if (mPull.isSucess()) {
            mPull.show(OkDrinkActivity.this);
            FirebaseTracker.get().track(MyTracker.DRINKWATER_AD_SHOW);
        } else {
            finish();
        }
    }
}
