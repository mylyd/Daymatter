package com.mobo.daymatter.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobo.daymatter.utils.StatusBarCompat;

import ad.mobo.base.request.AdPull;
import ad.mobo.common.request.AdRequestHelper;

/**
 * 所有界面的基类，做一些公用操作，兼容处理等
 */
public class BaseActivity extends AppCompatActivity {
    public NotificationManager manager;
    public AdPull mPull;
    String admob = "ca-app-pub-3707640778474213/2089881332";//正式id
    //String admob = "ca-app-pub-3940256099942544/1033173712";//测试id
    String facebook = "548840219402418_575010520118721";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusTextColor(this, true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        initAdInterstitial();
    }


    protected void adapterNavigationBar(View view) {
        if (StatusBarCompat.isNavigationBarShow(this)) {
            int height = StatusBarCompat.getBottomNavigatorHeight(this);
            if (height > 0) {
                view.setPadding(view.getLeft(), view.getTop(), view.getRight(), view.getBottom() + height);
            }
        }
    }

    public void initAdInterstitial() {
        mPull = new AdPull().asInterstitial().info(AdRequestHelper.getPullInfos(1004 + "", 1,
                AdRequestHelper.getAdInfos(admob, facebook, "", "", "")))
                .handler(new AdPull.InterstitialListener() {
                    @Override
                    public void onFailed() {
                    }

                    @Override
                    public void onSucess() {
                    }

                    @Override
                    public void onClosed() {
                        mPull.load(BaseActivity.this);
                        finish();
                    }

                    @Override
                    public void onClick() {

                    }
                });
        mPull.load(BaseActivity.this);
    }
}
