package com.mobo.daymatter.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.DateBean;
import com.mobo.daymatter.beans.LeftBean;
import com.mobo.daymatter.beans.ReminderBean;
import com.mobo.daymatter.beans.RepeatMode;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;
import com.mobo.daymatter.utils.MoboLogger;
import com.mobo.daymatter.utils.ReminderUtil;
import com.mobo.daymatter.utils.ShareUtils;
import com.mobo.daymatter.views.ReminderShareView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReminderShareActivity extends BaseActivity implements View.OnClickListener {
    public static final String REMINDER_INFO = "reminder_info";
    private ImageView mBackView;
    private TextView mTitleView;
    private TextView mEditView;
    private ReminderShareView mShareContentView;
    private Button mShareButton;

    private TextView mContentTopView, mContentCenterView, mContentBottomView;
    private ReminderBean mBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_share);

        initView();
        initData();
    }

    public void initView() {
        mTitleView = findViewById(R.id.title);

        mContentTopView = findViewById(R.id.share_top);
        mContentCenterView = findViewById(R.id.share_center);
        mContentBottomView = findViewById(R.id.share_bottom);

        mShareContentView = findViewById(R.id.share_info);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        findViewById(R.id.edit).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
    }

    public void initData() {
        mBean = getIntent().getParcelableExtra(REMINDER_INFO);
        refreshData();
    }

    private void refreshData() {
        if (mBean == null) return;
        mTitleView.setText(mBean.getName());

        LeftBean bean = ReminderUtil.getLeftBean(this, mBean);
        mContentTopView.setText(bean.getTitle());
        mContentCenterView.setText(String.valueOf(bean.getDay()));
        mContentBottomView.setText(bean.getTarget());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back:
                finish();
                break;
            case R.id.edit:
                ReminderEditActivity.startEditorForResult(this, mBean, 1000);
                break;
            case R.id.save:
                FirebaseTracker.get().track(MyTracker.EVENTDETAIL_SAVE);
                Bitmap saveBit = ShareUtils.viewSaveToImage(mShareContentView);
                try {
                    ShareUtils.saveReminderBitmap(this, saveBit, mBean.getName() + ".png");
                    Toast.makeText(this, R.string.matter_reminder_save_success, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    MoboLogger.error("ReminderShareActivity", "save failed!");
                    Toast.makeText(this, R.string.matter_reminder_save_failed, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.share:
                FirebaseTracker.get().track(MyTracker.EVENTDETAIL_SHARE);
                Bitmap bitmap = ShareUtils.viewSaveToImage(mShareContentView);
                try {
                    ShareUtils.shareImage(this, ShareUtils.saveReminderBitmap(this, bitmap, mBean.getName() + ".png"));
                } catch (IOException e) {
                    MoboLogger.error("ReminderShareActivity", "share failed!");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mBean = data.getParcelableExtra(REMINDER_INFO);
            refreshData();
        }
    }

    public static void start(Context context, ReminderBean bean) {
        Intent intent = new Intent(context, ReminderShareActivity.class);
        intent.putExtra(REMINDER_INFO, bean);
        context.startActivity(intent);
    }
}
