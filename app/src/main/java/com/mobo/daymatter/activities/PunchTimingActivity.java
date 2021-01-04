package com.mobo.daymatter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.PunchClockBean;
import com.mobo.daymatter.beans.PunchClockRecorder;
import com.mobo.daymatter.dialog.PunchTaskDialog;
import com.mobo.daymatter.manager.PunchClockManager;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;

/**
 * 打卡计时界面
 */
public class PunchTimingActivity extends BaseActivity implements View.OnClickListener {
    private static final String PUNCH_CLOCK_BEAN = "punch_clock_bean";
    private static final String FORMAT = "%02d:%02d";
    private PunchClockBean mBean;
    private TextView mTitleView, mTimeView, mStartView;
    private Handler mHandler;
    private int startTime = 0;
    private boolean isStared = false; // 任务当前是否运行
    private boolean isStartTask = false; // 标记任务是否结束，false 表示结束，这个结束，可以是未开始，也可以是手动执行完成，也可以是计时完成
    private PunchTaskDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_timing);
        mBean = getIntent().getParcelableExtra(PUNCH_CLOCK_BEAN);
        mTitleView = findViewById(R.id.title);
        mTitleView.setText(mBean.getName());
        mTimeView = findViewById(R.id.time);
        if (mBean.getTimeMode() == PunchClockBean.TIME_DOWN) {
            mTimeView.setText(String.format(FORMAT, mBean.getTime(), 0));
            startTime = mBean.getTime() * 60;
        } else {
            mTimeView.setText(String.format(FORMAT, 0, 0));
        }

        mStartView = findViewById(R.id.start);
        mStartView.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.complete_tint).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (!isStartTask) {
            super.onBackPressed();
            return;
        }
        showCompleteDialog(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.start:
                if (getString(R.string.matter_punch_time_complete).equals(mStartView.getText())) {
                    showCompleteDialog(true);
                } else {
                    isStartTask = true;
                    isStared = !isStared;
                    if (isStared) {
                        mStartView.setText(R.string.matter_punch_time_pause);
                        startTiming();
                    } else {
                        mStartView.setText(R.string.matter_punch_time_start);
                    }
                }
                break;
            case R.id.complete_tint:
//                if (!isStartTask) {
                showCompleteDialog(true);
//                } else {
//                    isStartTask = false;
//                    showCompleteDialog(!isStared);
//                }
                break;
        }
    }

    private void showCompleteDialog(boolean b) {
        if (mDialog == null) {
            mDialog = new PunchTaskDialog(this);
        }
        if (b) {
            mDialog.setTintContent(R.string.matter_punch_task_complete, R.string.matter_punch_dialog_no_target, R.string.matter_confirm);
            mDialog.setOnButtonListener(new PunchTaskDialog.OnButtonListener() {
                @Override
                public void onSubmitOnListener() {
                    addPunchClockRecorder();
                    FirebaseTracker.get().track(MyTracker.CHECKIN_CLICK_OK);
                    finish();
                }
            });
        } else {
            mDialog.setTintContent(R.string.matter_punch_task_interrupt, R.string.matter_punch_task_interrupt_content, R.string.matter_abort);
            mDialog.setOnButtonListener(new PunchTaskDialog.OnButtonListener() {
                @Override
                public void onSubmitOnListener() {
                    finish();
                }
            });
        }
        mDialog.show();
    }

    private void addPunchClockRecorder() {
        PunchClockRecorder recorder = new PunchClockRecorder();
        recorder.setTime(System.currentTimeMillis());
        recorder.setUsedTime(mBean.getTime());
        PunchClockManager.get().addRecorder(mBean, recorder, true);
    }

    private void startTiming() {
        if (mHandler == null) {
            mHandler = new Handler(getMainLooper());
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isStared) {
                    return;
                }
                if (mBean.getTimeMode() == PunchClockBean.TIME_DOWN) {
                    startTime--;
                    updateTime();
                    if (startTime <= 0) {
                        isStartTask = false;
                        mStartView.setText(R.string.matter_punch_time_complete);
                        Toast.makeText(PunchTimingActivity.this, R.string.matter_punch_time_complete_and_punch, Toast.LENGTH_SHORT).show();
                    } else {
                        mHandler.postDelayed(this, 1000);
                    }
                } else {
                    startTime++;
                    updateTime();
                    if (startTime >= mBean.getTime() * 60) {
                        isStartTask = false;
                        mStartView.setText(R.string.matter_punch_time_complete);
                        Toast.makeText(PunchTimingActivity.this, R.string.matter_punch_time_complete_and_punch, Toast.LENGTH_SHORT).show();
                    } else {
                        mHandler.postDelayed(this, 1000);
                    }
                }
            }
        }, 1000);
    }

    private void updateTime() {
        int minute = startTime / 60;
        int second = startTime % 60;
        mTimeView.setText(String.format(FORMAT, minute, second));
    }

    public static void start(Context context, PunchClockBean bean) {
        if (bean != null) {
            Intent intent = new Intent(context, PunchTimingActivity.class);
            intent.putExtra(PUNCH_CLOCK_BEAN, bean);
            context.startActivity(intent);
        }
    }
}
