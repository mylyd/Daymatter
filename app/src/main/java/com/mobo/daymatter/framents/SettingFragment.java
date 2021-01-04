package com.mobo.daymatter.framents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mobo.daymatter.R;
import com.mobo.daymatter.activities.DrinkingWaterParametersActivity;
import com.mobo.daymatter.dialog.FeedBackDialog;
import com.mobo.daymatter.dialog.RatingDialog;
import com.mobo.daymatter.helper.AdInfoUtil;
import com.mobo.daymatter.helper.NativeAdDelegate;
import com.mobo.daymatter.interfaces.GrayKey;
import com.mobo.daymatter.manager.NotificationManager;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;
import com.mobo.daymatter.utils.BaseUtils;
import com.mobo.daymatter.utils.GradeUtils;
import com.mobo.daymatter.utils.PunchClockUtils;

import ad.mobo.base.view.NativeControllerLayout;

/**
 * 主页设置界面
 */
public class SettingFragment extends ViewPagerFragment implements View.OnClickListener {
    private TextView mVersionTextView;
    private NativeControllerLayout mAdView;
    private NativeAdDelegate mNativePull;
    private RatingDialog mRatingDialog;
    private FeedBackDialog mFeedDialog;
    private Switch mSwitch;
    private TextView tvTotal;
    private TextView tvUnit;
    private TextView tvGender;
    private TextView tvBodyWeight;
    private TextView tvWakeUpTime;
    private TextView tvSleepingTime;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        mSwitch = findViewById(R.id.notification_switch);
        findViewById(R.id.privacy_area).setOnClickListener(this);
        findViewById(R.id.five_star).setOnClickListener(this);
        findViewById(R.id.feedback).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        mVersionTextView = findViewById(R.id.product_info);

        findViewById(R.id.drink_total).setOnClickListener(this);
        findViewById(R.id.drink_unit).setOnClickListener(this);
        findViewById(R.id.drink_gender).setOnClickListener(this);
        findViewById(R.id.drink_body_weight).setOnClickListener(this);
        findViewById(R.id.drink_wake_up_time).setOnClickListener(this);
        findViewById(R.id.drink_sleeping_time).setOnClickListener(this);

        tvTotal = findViewById(R.id.tv_drink_total);
        tvUnit = findViewById(R.id.tv_drink_unit);
        tvGender = findViewById(R.id.tv_drink_gender);
        tvBodyWeight = findViewById(R.id.tv_body_weight);
        tvWakeUpTime = findViewById(R.id.tv_drink_wake_up_time);
        tvSleepingTime = findViewById(R.id.tv_drink_sleeping_time);
    }

    private void initData() {
        mSwitch.setChecked(NotificationManager.get().isNotificationOn());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseTracker.get().track(isChecked ? MyTracker.SETTING_CLICK_PUSHREMIND_ON : MyTracker.SETTING_CLICK_PUSHREMIND_OFF);
                NotificationManager.get().saveNotificationStatus(isChecked);
            }
        });

        mVersionTextView.setText("Daily Reminder V " + BaseUtils.getVersionName(getContext()));
        if (mCurVisible) {
            initAd();
        }
    }

    private void initAd() {
        if (mAdView == null) {
            mNativePull = new NativeAdDelegate();
            mAdView = findViewById(R.id.ad);
            AdInfoUtil.initNativeView(mAdView, GrayKey.SETTING_AD_CLICK, false);
            mNativePull.initPull(2, mAdView);
        }
        mNativePull.load(this);
    }

    @Override
    public void onFragmentVisible() {
        if (mNativePull == null || mNativePull.isFailed()) {
            initAd();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.privacy_area:
                FirebaseTracker.get().track(MyTracker.SETTING_CLICK_ABOUTUS);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/daily-reminder"));
                startActivity(intent);
                break;
            case R.id.five_star:
                FirebaseTracker.get().track(MyTracker.SETTING_CLICK_RATEUS);
                showRatingDialog();
                break;
            case R.id.feedback:
                FirebaseTracker.get().track(MyTracker.SETTING_CLICK_FEEDBACK);
                if (mFeedDialog == null) {
                    mFeedDialog = new FeedBackDialog(getActivity());
                }
                mFeedDialog.show();
                break;
            case R.id.share:
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.invite_content));
                startActivity(shareIntent);
                break;
            case R.id.drink_total:
                //设置总饮水量
                DrinkingWaterParametersActivity.startNew(this, 0,
                        DrinkingWaterParametersActivity.PARAMETERS_WATER_GOALS);
                break;
            case R.id.drink_unit:
                //设置单位
                DrinkingWaterParametersActivity.startNew(this, 0,
                        DrinkingWaterParametersActivity.PARAMETERS_UNIT);
                break;
            case R.id.drink_gender:
                //设置性别
                DrinkingWaterParametersActivity.startNew(this, 0,
                        DrinkingWaterParametersActivity.PARAMETERS_GENDER);
                break;
            case R.id.drink_body_weight:
                //设置体重
                DrinkingWaterParametersActivity.startNew(this, 0,
                        DrinkingWaterParametersActivity.PARAMETERS_BODY_WEIGHT);
                break;
            case R.id.drink_wake_up_time:
                //设置起床时间
                DrinkingWaterParametersActivity.startNew(this, 0,
                        DrinkingWaterParametersActivity.PARAMETERS_WAKE_UP_TIME);
                break;
            case R.id.drink_sleeping_time:
                //设置睡觉时间
                DrinkingWaterParametersActivity.startNew(this, 0,
                        DrinkingWaterParametersActivity.PARAMETERS_SLEEPING_TIME);
                break;
        }
    }

    private void showRatingDialog() {
        if (mRatingDialog == null) {
            mRatingDialog = new RatingDialog(getActivity(), new RatingDialog.OnRateListener() {
                @Override
                public void onHeightLevel() {
                    GradeUtils.gotoGooglePlay(getContext());
                }

                @Override
                public void onLowLevel() {
                    if (mFeedDialog == null) {
                        mFeedDialog = new FeedBackDialog(getActivity());
                    }
                    mFeedDialog.show();
                }
            });
        }
        mRatingDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showTextContent();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            showTextContent();
        }
    }

    private void showTextContent() {
        if (tvTotal != null) {
            tvTotal.setText(String.format("%s %s", DrinkManager.getDrinkTotal(),
                    DrinkManager.getUnitType() ? DrinkManager.UNIT_ML : DrinkManager.UNIT_FLOZ));
        }
        if (tvUnit != null) {
            tvUnit.setText(DrinkManager.getUnitType() ? "kg.ml" : "lbs.floz");
        }
        if (tvGender != null) {
            String gender;
            if (DrinkManager.getDrinkGender().equals(DrinkManager.GENDER_MALE)){
                gender = getString(R.string.drink_value_male);
            }else if (DrinkManager.getDrinkGender().equals(DrinkManager.GENDER_FEMALE)){
                gender = getString(R.string.drink_value_female);
            }else {
                gender = getString(R.string.drink_value_confidential);
            }
            tvGender.setText(gender);
        }
        if (tvBodyWeight != null) {
            tvBodyWeight.setText(String.format("%s%s", DrinkManager.getDrinkBodyWeight(),
                    DrinkManager.getUnitType() ? DrinkManager.UNIT_KG : DrinkManager.UNIT_LBS));
        }
        if (tvWakeUpTime != null) {
            tvWakeUpTime.setText(PunchClockUtils.getHHMM(DrinkManager.getDrinkWakeUpTime()));
        }
        if (tvSleepingTime != null) {
            tvSleepingTime.setText(PunchClockUtils.getHHMM(DrinkManager.getDrinkSleepingTime()));
        }
    }
}
