package com.mobo.daymatter.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.ReminderBean;
import com.mobo.daymatter.manager.ReminderManager;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;
import com.mobo.daymatter.utils.ReminderUtil;

import java.util.Calendar;

public class ReminderEditActivity extends BaseActivity implements View.OnClickListener {
    private static final String REMINDER_EDIT_INFO = "reminder_edit_info";
    private ReminderBean mEditBean, mNewBean;
    private TextView mTitleView;
    private View mDeleteView;
    private TextView mDataHintView;
    private EditText mEditView;
    private Switch mSwitchView;

    private Spinner mSpinner;
    private int mSelectedPos;
    private DatePickerDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_reminder_edit);
        findViewById(R.id.back).setOnClickListener(this);
        mTitleView = findViewById(R.id.title);
        mDeleteView = findViewById(R.id.delete_area);
        mEditBean = getIntent().getParcelableExtra(REMINDER_EDIT_INFO);
        if (mEditBean == null) {
            mTitleView.setText(R.string.matter_reminder_new);
            mDeleteView.setVisibility(View.GONE);
            mNewBean = new ReminderBean();
            mNewBean.setOriginalTime(System.currentTimeMillis());
            mNewBean.setReminderTime(System.currentTimeMillis());
        } else {
            mTitleView.setText(R.string.matter_reminder_edit);
            mDeleteView.setOnClickListener(this);
            mNewBean = ReminderBean.copy(mEditBean);
            mDeleteView.setVisibility(mNewBean.isDefault() ? View.GONE : View.VISIBLE);
        }

        findViewById(R.id.save_area).setOnClickListener(this);

        mEditView = findViewById(R.id.reminder_title);
        mEditView.setText(mNewBean.getName());

        mDataHintView = findViewById(R.id.date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mNewBean.getReminderTime());
        mDataHintView.setText(ReminderUtil.getReminderTargetFormat(this, calendar));
        mDataHintView.setOnClickListener(this);

        mSwitchView = findViewById(R.id.is_top);
        if (mEditBean != null) {
            mSwitchView.setChecked(mEditBean.isNeedTop());
        }

        mSpinner = findViewById(R.id.repeat_mode);
        mSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item_text, R.id.text, getResources().getStringArray(R.array.repeat_name)));
        if (mEditBean != null) {
            mSpinner.setSelection(mEditBean.getRepeatMode());
        }
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSelectedPos = 0;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back:
                finish();
                break;
            case R.id.delete_area:
                ReminderManager.get().deleteReminder(mEditBean);
                startActivity(new Intent(this, DayMatterActivity.class));
                finish();
                break;
            case R.id.save_area:
                if (mEditBean == null) {
                    FirebaseTracker.get().track(MyTracker.ADDEVENT_SAVE);
                }
                mNewBean.setName(mEditView.getText().toString());
                mNewBean.setNeedTop(mSwitchView.isChecked());
                mNewBean.setRepeatMode(mSelectedPos);
                ReminderManager.get().editReminder(mEditBean, mNewBean);
                Intent data = new Intent();
                data.putExtra(ReminderShareActivity.REMINDER_INFO, mNewBean);
                setResult(Activity.RESULT_OK, data);
                finish();
                break;
            case R.id.date:
                showDatePickDialog();
                break;

        }

    }

    private void showDatePickDialog() {
        if (mDialog == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mNewBean.getReminderTime());
            mDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth);
                    if (mEditBean == null) {
                        mNewBean.setOriginalTime(calendar.getTimeInMillis());
                    }
                    mNewBean.setReminderTime(calendar.getTimeInMillis());
                    mDataHintView.setText(ReminderUtil.getReminderTargetFormat(ReminderEditActivity.this, calendar));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        mDialog.show();
    }

    public static void startNew(Context context) {
        context.startActivity(new Intent(context, ReminderEditActivity.class));
    }

    public static void startEditorForResult(Activity activity, ReminderBean bean, int requestCode) {
        Intent intent = new Intent(activity, ReminderEditActivity.class);
        intent.putExtra(REMINDER_EDIT_INFO, bean);
        activity.startActivityForResult(intent, requestCode);
    }
}
