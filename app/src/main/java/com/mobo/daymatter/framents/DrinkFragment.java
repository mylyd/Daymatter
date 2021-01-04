package com.mobo.daymatter.framents;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.mobo.daymatter.R;
import com.mobo.daymatter.activities.BaseActivity;
import com.mobo.daymatter.activities.OkDrinkActivity;
import com.mobo.daymatter.adapter.DrinkRecordingAdapter;
import com.mobo.daymatter.beans.DrinkBean;
import com.mobo.daymatter.dialog.DrinkEditGoalDialog;
import com.mobo.daymatter.dialog.DrinkOptionMenuDialog;
import com.mobo.daymatter.helper.AdInfoUtil;
import com.mobo.daymatter.helper.NativeAdDelegate;
import com.mobo.daymatter.interfaces.GrayKey;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.manager.NotificationManager;
import com.mobo.daymatter.manager.SoftKeyBoardListenerManager;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;
import com.mobo.daymatter.utils.PunchClockUtils;
import com.mobo.daymatter.views.YWaveLoadView;

import java.util.List;

import ad.mobo.base.view.NativeControllerLayout;

import static java.lang.Integer.parseInt;


/**
 * 喝水记录主页面
 */
public class DrinkFragment extends ViewPagerFragment implements View.OnClickListener {
    private NativeControllerLayout mAdView;
    private NativeAdDelegate mNativePull;
    private YWaveLoadView mWaveLoad;
    private AppBarLayout appBarLayout;
    private ImageView optionItem;
    private TextView mTvTitle;
    private TextView recordsTitle;
    private DrinkOptionMenuDialog optionMenuDialog;
    private TextView mContainerSchedule;
    private EditText mCapacity;
    private RecyclerView recyclerView;
    private DrinkRecordingAdapter adapter;
    private FrameLayout mActionLayout;
    private DrinkEditGoalDialog dialogNone;
    private TextView mNextTime;
    private TextView drinkEmpty;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_drink;
    }

    @Override
    public void onFragmentVisible() {
        if (mNativePull == null || mNativePull.isFailed()) {
            initAd();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mWaveLoad = findViewById(R.id.wave_load);
        appBarLayout = findViewById(R.id.appBarLayout);
        optionItem = findViewById(R.id.option_item);
        mTvTitle = findViewById(R.id.DrinkTitle);
        recordsTitle = findViewById(R.id.records_title);
        mActionLayout = findViewById(R.id.layout_action);
        drinkEmpty = findViewById(R.id.drink_isEmpty);

        mContainerSchedule = findViewById(R.id.drink_container_schedule);
        mNextTime = findViewById(R.id.drink_container_time);
        mCapacity = findViewById(R.id.tv_capacity);
        findViewById(R.id.iv_cup).setOnClickListener(this);
        findViewById(R.id.capacity_subtract).setOnClickListener(this);
        findViewById(R.id.capacity_add).setOnClickListener(this);
        optionItem.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler);
        optionMenuDialog = new DrinkOptionMenuDialog(this);
        adapter = new DrinkRecordingAdapter(getContext());
        dialogNone = new DrinkEditGoalDialog(getContext());
    }

    private void initData() {
        mWaveLoad.setProgress((float) DrinkManager.getDrinkProgress(), (float) DrinkManager.getDrinkTotal());
        mContainerSchedule.setText(String.format(getString(R.string.drink_cup_capacity), String.valueOf(DrinkManager.getDrinkProgress()),
                String.valueOf(DrinkManager.getDrinkTotal()), DrinkManager.getDrinkUnit()));
        mCapacity.setText(String.valueOf(DrinkManager.getDrinkCapacity()));
        mNextTime.setText(PunchClockUtils.getHHMM(DrinkManager.getDrinkNextWaterTime()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.refresh(DrinkManager.getDrinkRecordingBean());
        setEmptyVisible(isListBeanEmpty());
    }

    private void setEmptyVisible(boolean visible) {
        if (recyclerView != null && drinkEmpty != null) {
            recyclerView.setVisibility(visible ? View.VISIBLE : View.GONE);
            drinkEmpty.setVisibility(visible ? View.GONE : View.VISIBLE);
            recordsTitle.setText(String.format(getString(R.string.drink_drinking_records),
                    PunchClockUtils.getYYMMDDDot(System.currentTimeMillis())));
        }
    }

    /**
     * 判断历史喝水记录时候存在数据
     *
     * @return
     */
    private boolean isListBeanEmpty() {
        List<DrinkBean> mBeanDrink = DrinkManager.getDrinkRecordingBean();
        if (mBeanDrink == null || mBeanDrink.isEmpty()) {
            return false;
        } else {
            int item = 0;
            for (DrinkBean bean : mBeanDrink) {
                if (adapter.isToDay(bean.getTime())) {
                    item++;
                }
            }
            if (item == 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     *
     */
    private void setListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, final int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                    mTvTitle.setTextColor(Color.BLACK);
                    optionItem.setImageResource(R.drawable.ic_options_black);
                    mActionLayout.setBackgroundColor(Color.WHITE);
                } else {
                    mTvTitle.setTextColor(Color.WHITE);
                    optionItem.setImageResource(R.drawable.ic_options_white);
                    mActionLayout.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        adapter.setOnItemClickListener(new DrinkRecordingAdapter.OnItemClickListener() {
            @Override
            public void onItemReviseClick(List<DrinkBean> beans, DrinkBean bean, int position) {
                //编辑
                if (dialogNone != null && !dialogNone.isShowing()) {
                    dialogNone.show(bean.getValue(), position, bean.getTime());
                    dialogNone.setUnit(bean.getUnit().equals(DrinkManager.UNIT_ML));
                }
            }

            @Override
            public void onItemDeleteClick(List<DrinkBean> beans, DrinkBean bean, int position) {
                //删除
                int remove = beans.size() + 1;
                for (int i = 0; i < beans.size(); i++) {
                    if (beans.get(i).getTime() == bean.getTime()) {
                        remove = i;
                    }
                }
                if (beans.size() >= remove) {
                    beans.remove(remove);
                }

                int mun = 0;
                for (DrinkBean be : beans) {
                    if (adapter.isToDay(be.getTime())) {
                        mun = mun + be.getValue();
                    }
                }
                DrinkManager.setDrinkProgress(mun);
                DrinkManager.setDrinkRecordingBean(beans);
                adapter.refresh(DrinkManager.getDrinkRecordingBean());
                setEmptyVisible(isListBeanEmpty());
                showProgress();
                mContainerSchedule.setText(String.format(getString(R.string.drink_cup_capacity), String.valueOf(DrinkManager.getDrinkProgress()),
                        String.valueOf(DrinkManager.getDrinkTotal()), DrinkManager.getDrinkUnit()));
            }
        });

        dialogNone.setOnItemClickListener(new DrinkEditGoalDialog.OnItemClickListener() {
            @Override
            public void onItemConfirmClick(int value, int position, long time) {
                List<DrinkBean> drinkBeans = DrinkManager.getDrinkRecordingBean();
                if (drinkBeans != null && !drinkBeans.isEmpty()) {
                    int mun = 0;
                    for (DrinkBean bean : drinkBeans) {
                        if (bean.getTime() == time) {
                            bean.setValue(value);
                        }
                        if (adapter.isToDay(bean.getTime())) {
                            if (DrinkManager.getDrinkUnit().equals(bean.getUnit())) {
                                mun = mun + bean.getValue();
                            } else {
                                if (bean.getUnit().equals(DrinkManager.UNIT_ML)) {
                                    mun = mun + DrinkManager.MlSetFloz(bean.getValue());
                                } else {
                                    mun = mun + DrinkManager.FlozSetMl(bean.getValue());
                                }
                            }
                        }
                    }

                    DrinkManager.setDrinkProgress(mun);
                    DrinkManager.setDrinkRecordingBean(drinkBeans);
                    adapter.refresh(DrinkManager.getDrinkRecordingBean());
                    setEmptyVisible(isListBeanEmpty());
                    showProgress();
                    mContainerSchedule.setText(String.format(getString(R.string.drink_cup_capacity), String.valueOf(DrinkManager.getDrinkProgress()),
                            String.valueOf(DrinkManager.getDrinkTotal()), DrinkManager.getDrinkUnit()));
                    if (mun != DrinkManager.getDrinkProgress()) {
                        NotificationManager.get().showNotification(getContext(), ((BaseActivity)getActivity()).manager);
                    }
                }
            }

            @Override
            public void onItemCancelClick(int value, int position) {
                //showProgress();
            }
        });

        //软键盘显示与收起监听，
        //问题：在软键盘弹出时会关掉容器动画导致容器显示进度动画为0，所以监听键盘关闭时重新刷新动画
        SoftKeyBoardListenerManager.setListener(getActivity(), new SoftKeyBoardListenerManager.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Log.e("keyBoardShow", "keyBoardShow: keyBoardShow");
            }

            @Override
            public void keyBoardHide(int height) {
                if (mCapacity != null) {
                    if (TextUtils.isEmpty(mCapacity.getText().toString())) {
                        mCapacity.setText(String.valueOf(DrinkManager.getDrinkCapacity()));
                        return;
                    }
                    int capacity = parseInt(mCapacity.getText().toString());
                    if (capacity >= 0) {
                        DrinkManager.setDrinkCapacity(capacity);
                    } else {
                        mCapacity.setText(DrinkManager.getDrinkCapacity());
                    }
                }
                showProgress();
            }
        });
    }

    private void initAd() {
        if (mAdView == null) {
            mNativePull = new NativeAdDelegate();
            mAdView = findViewById(R.id.ad);
            AdInfoUtil.initNativeView(mAdView, GrayKey.EVENT_AD_CLICK, false);
            mNativePull.initPull(3, mAdView);
        }
        mNativePull.load(this);
    }

    private void showProgress() {
        if (mWaveLoad != null) {
            mWaveLoad.stopLoad();
            mWaveLoad.setProgress((float) ((DrinkManager.getDrinkTotal() > DrinkManager.getDrinkProgress()) ? DrinkManager.getDrinkProgress() : DrinkManager.getDrinkTotal()),
                    (float) DrinkManager.getDrinkTotal());
            mWaveLoad.startLoad();
            DrinkManager.ToAddRecordTotalBean();
            DrinkManager.ToAddRecordProgressBean();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_item:
                if (optionMenuDialog != null && !optionMenuDialog.isShowing()) {
                    optionMenuDialog.show();
                }
                break;
            case R.id.iv_cup:
                FirebaseTracker.get().track(MyTracker.DRINKWATER_CLICK);
                if (DrinkManager.getDrinkProgress() >= DrinkManager.getDrinkTotal()) {
                    Toast.makeText(mActivity, R.string.current_drinking_wate, Toast.LENGTH_SHORT).show();
                }
                if (mCapacity != null) {
                    if (TextUtils.isEmpty(mCapacity.getText().toString())) {
                        return;
                    }
                    DrinkManager.setDrinkCapacity(parseInt(mCapacity.getText().toString()));
                }
                DrinkManager.ToAddRecordNumBean();
                DrinkManager.ToDrinkProgress(getContext(), DrinkManager.getDrinkCapacity());
                DrinkManager.setDrinkNextWaterTime(DrinkManager.getNextTime(DrinkManager.getDrinkNextWaterTime(), DrinkManager.getNextIntervalTime(getContext())));
                mContainerSchedule.setText(String.format(getString(R.string.drink_cup_capacity), String.valueOf(DrinkManager.getDrinkProgress()),
                        String.valueOf(DrinkManager.getDrinkTotal()), DrinkManager.getDrinkUnit()));
                adapter.refresh(DrinkManager.getDrinkRecordingBean());
                setEmptyVisible(isListBeanEmpty());
                mNextTime.setText(PunchClockUtils.getHHMM(DrinkManager.getDrinkNextWaterTime()));
                showProgress();
                //更新通知
                NotificationManager.get().showNotification(getContext(),((BaseActivity)getActivity()).manager);
                OkDrinkActivity.startNew(this, 0);
                break;
            case R.id.capacity_subtract:
                if (mCapacity != null) {
                    if (TextUtils.isEmpty(mCapacity.getText().toString())) {
                        return;
                    }
                    DrinkManager.setDrinkCapacity(parseInt(mCapacity.getText().toString()));
                }
                DrinkManager.ToCapacitySubtract(getContext());
                mCapacity.setText(String.valueOf(DrinkManager.getDrinkCapacity()));
                break;
            case R.id.capacity_add:
                if (mCapacity != null) {
                    if (TextUtils.isEmpty(mCapacity.getText().toString())) {
                        return;
                    }
                    DrinkManager.setDrinkCapacity(parseInt(mCapacity.getText().toString()));
                }
                DrinkManager.ToCapacityAdd(getContext());
                mCapacity.setText(String.valueOf(DrinkManager.getDrinkCapacity()));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showTextContext();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        RefreshDrinkTime(false);
        showTextContext();
        if (!isVisibleToUser) {
            if (mWaveLoad != null) {
                mWaveLoad.stopLoad();
            }
        }
    }

    private void showTextContext() {
        if (mContainerSchedule != null) {
            mContainerSchedule.setText(String.format(getString(R.string.drink_cup_capacity), String.valueOf(DrinkManager.getDrinkProgress()),
                    String.valueOf(DrinkManager.getDrinkTotal()), DrinkManager.getDrinkUnit()));
        }
        if (mCapacity != null) {
            mCapacity.setText(String.valueOf(DrinkManager.getDrinkCapacity()));
        }
        if (adapter != null) {
            adapter.refresh(DrinkManager.getDrinkRecordingBean());
            setEmptyVisible(isListBeanEmpty());
        }
        if (mNextTime != null) {
            mNextTime.setText(PunchClockUtils.getHHMM(DrinkManager.getDrinkNextWaterTime()));
        }
        showProgress();
    }

    private void RefreshDrinkTime(boolean isShow){
        if (PunchClockUtils.getIsNoToday()) {
            DrinkManager.setDrinkProgress(0);
            showRecord();
            //刷新日期更改后饮水时间
            //更新饮水时间差
            DrinkManager.setDrinkNextWaterTime(PunchClockUtils.getTimeInMillis(DrinkManager.getSetTimeHourWakeup(), DrinkManager.getSetTimeMinuteWakeup()));
            //更新起床时间
            DrinkManager.setDrinkWakeUpTime(PunchClockUtils.getTimeInMillis(DrinkManager.getSetTimeHourWakeup(),DrinkManager.getSetTimeMinuteWakeup()));
            //更新睡觉时间
            DrinkManager.setDrinkSleepingTime(PunchClockUtils.getTimeInMillis(DrinkManager.getSetTimeHourSleeping(),DrinkManager.getSetTimeMinuteSleeping()));

            if (isShow){
                showTextContext();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RefreshDrinkTime(true);
    }

    private void showRecord() {
        if (adapter != null) {
            List<DrinkBean> beanList = DrinkManager.getDrinkRecordingBean();
            if (beanList != null && !beanList.isEmpty()) {
                int num = 0;
                for (DrinkBean bean : beanList) {
                    if (adapter.isToDay(bean.getTime())) {
                        num += bean.getValue();
                    }
                }
                DrinkManager.setDrinkProgress(num);
            }
        }
    }

}
