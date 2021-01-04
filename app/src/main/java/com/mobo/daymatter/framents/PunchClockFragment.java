package com.mobo.daymatter.framents;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.mobo.daymatter.R;
import com.mobo.daymatter.activities.PunchEditActivity;
import com.mobo.daymatter.activities.PunchArchiveActivity;
import com.mobo.daymatter.activities.PunchHistoryActivity;
import com.mobo.daymatter.activities.PunchTimingActivity;
import com.mobo.daymatter.adapter.PunchClockAdapter;
import com.mobo.daymatter.beans.PunchClockBean;
import com.mobo.daymatter.beans.PunchClockRecorder;
import com.mobo.daymatter.beans.PunchClockTargetMode;
import com.mobo.daymatter.dialog.PunchDialog;
import com.mobo.daymatter.helper.AdInfoUtil;
import com.mobo.daymatter.helper.NativeAdDelegate;
import com.mobo.daymatter.interfaces.GrayKey;
import com.mobo.daymatter.manager.GraySwitch;
import com.mobo.daymatter.manager.PunchClockManager;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;
import com.mobo.daymatter.utils.BaseUtils;

import ad.mobo.base.view.NativeControllerLayout;
import ad.mobo.base.view.NativeNoControllView;

/**
 * 主界面打卡展示
 */
public class PunchClockFragment extends ViewPagerFragment implements View.OnClickListener {
    private RecyclerView mRecycleView;
    private NativeControllerLayout mAdView;
    private NativeAdDelegate mNativePull;
    private ImageView mGameTitle;
    private PunchClockAdapter mAdapter;
    private PunchDialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_punch_clock;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PunchClockManager.get().init(getActivity());
        mGameTitle = findViewById(R.id.game);

        if (GraySwitch.getInstance().isSwitchOn(GrayKey.EVENT_GAME, true)) {
            mGameTitle.setVisibility(View.VISIBLE);
            mGameTitle.setOnClickListener(this);
        }
        mRecycleView = findViewById(R.id.recycler);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PunchClockAdapter(getActivity(), new PunchClockAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(PunchClockBean bean) {
                //打卡记录
                if (bean == null) {
                    return;
                }
                startActivity(new Intent(getContext(), PunchHistoryActivity.class));
            }

            @Override
            public void onPunchClick(PunchClockBean bean) {
                //打卡按钮
                if (bean == null) {
                    return;
                }
                FirebaseTracker.get().track(MyTracker.TASK_CLICK_CHECKIN);
                //创建任务选择不计时,直接打卡
                if (bean.getTimeMode() == PunchClockBean.TIME_NONE) {
                    showPunchDialog(bean);
                } else {
                    PunchTimingActivity.start(getContext(), bean);
                }
            }

            @Override
            public void onHistoryClick(PunchClockBean bean) {
                //编辑按钮
                if (bean == null) {
                    return;
                }
                PunchEditActivity.startNew(PunchClockFragment.this, bean, 0, true);
            }
        });
        mRecycleView.setAdapter(mAdapter);

        findViewById(R.id.add_area).setOnClickListener(this);
        findViewById(R.id.archive_area).setOnClickListener(this);
        if (mCurVisible) {
            if (mGameTitle.getVisibility() == View.VISIBLE) {
                FirebaseTracker.get().track(MyTracker.CHECKIN_GAME_SHOW);
            }
            initAd();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*预置2条打卡数据*/
        if (CollectionUtils.isEmpty(PunchClockManager.get().getDetailData())) {
            String[] mColor = getResources().getStringArray(R.array.punch_color);
            if (mColor == null){
                return;
            }
            //第一条 学英语
            PunchClockBean data1 = new PunchClockBean();
            data1.setName(getResources().getString(R.string.preset_punch_data_1_title));
            data1.setTime(25);
            data1.setTimeMode(PunchClockBean.TIME_NONE);
            data1.setColorPos(Color.parseColor(mColor[3]));
            data1.setBitmapPos(5);
            data1.setRepeatMode(PunchClockBean.REPEAT_ONE_DAY);
            PunchClockTargetMode punchClockTargetMode1 = new PunchClockTargetMode();
            punchClockTargetMode1.setMode(PunchClockTargetMode.MODE_COUNT);
            punchClockTargetMode1.setCount(1);
            data1.setTargetMode(punchClockTargetMode1);
            PunchClockManager.get().addDetailData(data1,false);
            //第二条 锻炼腹肌
            PunchClockBean data2 = new PunchClockBean();
            data2.setName(getResources().getString(R.string.preset_punch_data_2_title));
            data2.setTime(25);
            data2.setTimeMode(PunchClockBean.TIME_NONE);
            data2.setColorPos(Color.parseColor(mColor[7]));
            data2.setBitmapPos(4);
            data2.setRepeatMode(PunchClockBean.REPEAT_ONE_DAY);
            PunchClockTargetMode punchClockTargetMode2 = new PunchClockTargetMode();
            punchClockTargetMode2.setMode(PunchClockTargetMode.MODE_COUNT);
            punchClockTargetMode2.setCount(1);
            data2.setTargetMode(punchClockTargetMode2);
            PunchClockManager.get().addDetailData(data2,true);
        }
        mAdapter.refresh(PunchClockManager.get().getDetailData());
    }

    private void showPunchDialog(final PunchClockBean bean) {
        if (mDialog == null) {
            mDialog = new PunchDialog(getContext());
        }
        mDialog.setOnButtonListener(new PunchDialog.OnButtonListener() {
            @Override
            public void onSubmitOnListener() {
                FirebaseTracker.get().track(MyTracker.CHECKIN_CLICK_OK);
                PunchClockRecorder recorder = new PunchClockRecorder();
                recorder.setTime(System.currentTimeMillis());
                recorder.setUsedTime(bean.getTime());
                PunchClockManager.get().addRecorder(bean, recorder, true);
                mAdapter.refresh(PunchClockManager.get().getDetailData());
            }
        });
        mDialog.show();
    }

    private void initAd() {
        if (mAdView == null) {
            mNativePull = new NativeAdDelegate();
            mAdView = findViewById(R.id.ad);
            if (mAdView != null) {
                AdInfoUtil.initNativeView(mAdView, GrayKey.PUNCH_AD_CLICK, false);
                mNativePull.initPull(1, mAdView);
            }
        }
        if (mAdView != null) {
            mNativePull.load(this);
        }
    }

    @Override
    public void onFragmentVisible() {
        if (mGameTitle.getVisibility() == View.VISIBLE) {
            FirebaseTracker.get().track(MyTracker.CHECKIN_GAME_SHOW);
        }
        if (mNativePull == null || mNativePull.isFailed()) {
            initAd();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.game:
                FirebaseTracker.get().track(MyTracker.CHECKIN_GAME_CLICK);
                BaseUtils.goToGame(getContext());
                break;
            case R.id.add_area:
                // TODO
                PunchEditActivity.startNew(this, null, 0, false);
                FirebaseTracker.get().track(MyTracker.CHECK_IN_CLICK_ADD);
                break;
            case R.id.archive_area:
                startActivity(new Intent(getContext(), PunchArchiveActivity.class));
                break;
            default:
                break;
        }
    }
}
