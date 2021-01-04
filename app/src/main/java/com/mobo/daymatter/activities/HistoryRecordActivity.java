package com.mobo.daymatter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.drinkrecord.RecordNumBean;
import com.mobo.daymatter.beans.drinkrecord.RecordProgressBean;
import com.mobo.daymatter.beans.drinkrecord.RecordTotalBean;
import com.mobo.daymatter.helper.AdInfoUtil;
import com.mobo.daymatter.helper.NativeAdDelegate;
import com.mobo.daymatter.interfaces.GrayKey;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.utils.MoboLogger;
import com.mobo.daymatter.utils.PunchClockUtils;
import com.mobo.daymatter.utils.ShareUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ad.mobo.base.view.NativeControllerLayout;

/**
 * @author : ydli
 * @description 喝水历史记录页面
 * @time :
 */
public class HistoryRecordActivity extends BaseActivity implements View.OnClickListener {
    private NativeControllerLayout mAdView;
    private NativeAdDelegate mNativePull;
    private TextView tvWeeklyAverage;
    private TextView tvMonthlyAverage;
    private TextView tvAverageCompletion;
    private TextView tvDrinkingFrequency;
    private ScrollView layoutShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_record);

        initView();
        initAd();
        initData();
    }

    private void initAd() {
        if (mAdView == null) {
            mNativePull = new NativeAdDelegate();
            mAdView = findViewById(R.id.ad);
            AdInfoUtil.initNativeView(mAdView, GrayKey.EVENT_AD_CLICK, false);
            mNativePull.initPull(4, mAdView);
        }
        mNativePull.load(this);
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.weekly_average).setOnClickListener(this);
        findViewById(R.id.monthly_average).setOnClickListener(this);
        findViewById(R.id.average_completion).setOnClickListener(this);
        findViewById(R.id.drinking_frequency).setOnClickListener(this);
        findViewById(R.id.record_share).setOnClickListener(this);
        layoutShare = findViewById(R.id.layout_share);
        tvWeeklyAverage = findViewById(R.id.tv_weekly_average);
        tvMonthlyAverage = findViewById(R.id.tv_monthly_average);
        tvAverageCompletion = findViewById(R.id.tv_average_completion);
        tvDrinkingFrequency = findViewById(R.id.tv_drinking_frequency);
    }

    private void initData() {
        List<RecordProgressBean> progressBeans = DrinkManager.getDrinkRecordProgressBean();
        List<RecordTotalBean> recordTotalBeans = DrinkManager.getDrinkRecordTotalBean();
        List<RecordNumBean> recordNumBeans = DrinkManager.getDrinkRecordNumBean();
        if (progressBeans == null || progressBeans.isEmpty() || recordTotalBeans.isEmpty() || recordTotalBeans == null) {
            return;
        }
        if (progressBeans.size() < 7) {
            tvWeeklyAverage.setText(String.format(getString(R.string.history_record_value), "-- ",
                    DrinkManager.getDrinkUnit()));
            tvMonthlyAverage.setText(String.format(getString(R.string.history_record_value), "-- ",
                    DrinkManager.getDrinkUnit()));
            tvAverageCompletion.setText("-- %");
            tvDrinkingFrequency.setText(String.format(getString(R.string.history_record_value), "-- ",
                    "Time"));
        } else if (progressBeans.size() > 7 && progressBeans.size() < 30) {
            Collections.reverse(progressBeans);
            int water = 0;
            for (int i = 0; i < 7; i++) {
                water = water + progressBeans.get(i).getRecordProgress();
            }
            tvWeeklyAverage.setText(String.format(getString(R.string.history_record_value),
                    String.valueOf(water / 7),
                    DrinkManager.getDrinkUnit()));
            tvMonthlyAverage.setText(String.format(getString(R.string.history_record_value), "-- ",
                    DrinkManager.getDrinkUnit()));
            tvAverageCompletion.setText("-- %");
            tvDrinkingFrequency.setText(String.format(getString(R.string.history_record_value), "-- ",
                    "Time"));
        } else if (progressBeans.size() >= 30 && recordTotalBeans.size() >= 30) {
            Collections.reverse(progressBeans);
            Collections.reverse(recordTotalBeans);
            int water7 = 0;
            for (int i = 0; i < 7; i++) {
                water7 = water7 + progressBeans.get(i).getRecordProgress();
            }
            tvWeeklyAverage.setText(String.format(getString(R.string.history_record_value),
                    String.valueOf(water7 / 7),
                    DrinkManager.getDrinkUnit()));
            int water30 = 0;
            int waterTotal30 = 0;
            for (int i = 0; i < 30; i++) {
                water30 = water30 + progressBeans.get(i).getRecordProgress();
                waterTotal30 = waterTotal30 + recordTotalBeans.get(i).getRecordWater();
            }
            tvMonthlyAverage.setText(String.format(getString(R.string.history_record_value),
                    String.valueOf(water30 / 30),
                    DrinkManager.getDrinkUnit()));


            NumberFormat nt = NumberFormat.getPercentInstance();
            //设置百分数精确度2即保留两位小数
            nt.setMinimumFractionDigits(0);
            tvAverageCompletion.setText(nt.format(((float) water30 / (float) waterTotal30) * 100) + "%");

            if (recordNumBeans.size() == 0) {
                tvDrinkingFrequency.setText(String.format(getString(R.string.history_record_value), "-- ",
                        "Time"));
                return;
            }
            int num = 0;
            for (RecordNumBean bean : recordNumBeans) {
                if (bean.getRecordTime().indexOf(PunchClockUtils.getYYMM(System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000))) != -1) {
                    num++;
                }
            }
            tvDrinkingFrequency.setText(String.format(getString(R.string.history_record_value), String.valueOf(num / 30),
                    "Time"));
        }

    }

    /**
     * @param fragment
     * @param requestCode
     */
    public static void startNew(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), HistoryRecordActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity
     * @param requestCode
     */
    public static void startNew(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, HistoryRecordActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getScrollViewBitmap(ScrollView scrollView) {
        int height = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            height += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), height,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.weekly_average:

                break;
            case R.id.monthly_average:

                break;
            case R.id.average_completion:

                break;
            case R.id.drinking_frequency:

                break;
            case R.id.record_share:
                //截图前隐藏广告
                mAdView.setVisibility(View.GONE);
                ShareUtils.shareImage(this, getScrollViewBitmap(layoutShare));
                mAdView.setVisibility(View.VISIBLE);
                break;
        }
    }
}
