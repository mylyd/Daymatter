package com.mobo.daymatter.framents;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.activities.ReminderEditActivity;
import com.mobo.daymatter.activities.ReminderShareActivity;
import com.mobo.daymatter.adapter.ReminderAdapter;
import com.mobo.daymatter.beans.ReminderBean;
import com.mobo.daymatter.helper.AdInfoUtil;
import com.mobo.daymatter.helper.NativeAdDelegate;
import com.mobo.daymatter.interfaces.GrayKey;
import com.mobo.daymatter.manager.GraySwitch;
import com.mobo.daymatter.manager.ReminderManager;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;
import com.mobo.daymatter.utils.BaseUtils;

import ad.mobo.base.view.NativeControllerLayout;
import ad.mobo.base.view.NativeNoControllView;

/**
 * 主页纪念日界面
 */
public class ReminderFragment extends ViewPagerFragment implements View.OnClickListener {
    private RecyclerView mRecycleView;
    //private Button mAddBtn;
    private NativeControllerLayout mAdView;
    private NativeAdDelegate mNativePull;
    //private TextView mMainTitle, mSubTitle;
    private ImageView mGameTitle;
    private ReminderAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reminder;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*mMainTitle = findViewById(R.id.mainTitle);
        mSubTitle = findViewById(R.id.subTitle);*/
        mGameTitle = findViewById(R.id.game);

        if (GraySwitch.getInstance().isSwitchOn(GrayKey.EVENT_GAME, true)) {
            mGameTitle.setVisibility(View.VISIBLE);
            mGameTitle.setOnClickListener(this);
        }
        mRecycleView = findViewById(R.id.recycler);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        ReminderManager.get().init(getContext());
        mAdapter = new ReminderAdapter(getContext(), null,new ReminderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ReminderBean bean) {
                FirebaseTracker.get().track(MyTracker.EVENT_CLICK_EVENT);
                ReminderShareActivity.start(getContext(), bean);
            }
        });
        mRecycleView.setAdapter(mAdapter);

        findViewById(R.id.add_area).setOnClickListener(this);
        if (mCurVisible) {
            FirebaseTracker.get().track(MyTracker.EVENT_SHOW);
            if (mGameTitle.getVisibility() == View.VISIBLE) {
                FirebaseTracker.get().track(MyTracker.EVENT_GAME_SHOW);
            }
            initAd();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.refresh(ReminderManager.get().getReminderList());
    }

    private void initAd() {
        if (mAdView == null) {
            mNativePull = new NativeAdDelegate();
            mAdView = findViewById(R.id.ad);
            AdInfoUtil.initNativeView(mAdView, GrayKey.EVENT_AD_CLICK, false);
            mNativePull.initPull(0, mAdView);
        }
        mNativePull.load(this);
    }

    @Override
    public void onFragmentVisible() {
        FirebaseTracker.get().track(MyTracker.EVENT_SHOW);
        if (mGameTitle.getVisibility() == View.VISIBLE) {
            FirebaseTracker.get().track(MyTracker.EVENT_GAME_SHOW);
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
                FirebaseTracker.get().track(MyTracker.EVENT_GAME_CLICK);
                BaseUtils.goToGame(getContext());
                break;
            case R.id.add_area:
                FirebaseTracker.get().track(MyTracker.EVENT_CLICK_ADD);
                ReminderEditActivity.startNew(getContext());
                break;
            default:
                break;
        }
    }
}
