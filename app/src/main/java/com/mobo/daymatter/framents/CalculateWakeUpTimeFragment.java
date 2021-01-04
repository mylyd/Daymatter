package com.mobo.daymatter.framents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobo.daymatter.R;
import com.mobo.daymatter.dialog.TimePickerDialog;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.utils.PunchClockUtils;

/**
 * 引导进度
 *
 * @起床时间设置页
 */
public class CalculateWakeUpTimeFragment extends BaseFragment {

    private TextView setTime;
    private TimePickerDialog timePickerDialog;
    private TextView label1;
    private TextView label2;
    private TextView label3;
    private TextView label4;

    public static CalculateWakeUpTimeFragment newInstance() {
        CalculateWakeUpTimeFragment fragment = new CalculateWakeUpTimeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_calculate_wake_up_time;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        Listener();
    }

    private void initView() {
        label1 = findViewById(R.id.sleeping_label_1);
        label2 = findViewById(R.id.sleeping_label_2);
        label3 = findViewById(R.id.sleeping_label_3);
        label4 = findViewById(R.id.sleeping_label_4);
        timePickerDialog = new TimePickerDialog(getContext());
        setTime = findViewById(R.id.set_time);
        setTextTimeData(DrinkManager.getSetTimeHourWakeup(), DrinkManager.getSetTimeMinuteWakeup());
    }

    private void Listener() {
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timePickerDialog != null && !timePickerDialog.isShowing()) {
                    timePickerDialog.show(DrinkManager.getSetTimeHourWakeup(), DrinkManager.getSetTimeMinuteWakeup());
                }
            }
        });
        timePickerDialog.setOnTimeValueClickListener(new TimePickerDialog.OnTimeValueClickListener() {
            @Override
            public void onTimeValueClick(int hour, int minute) {
                setTextTimeData(hour, minute);
                DrinkManager.setDrinkWakeUpTime(PunchClockUtils.getTimeInMillis(hour, minute));
                DrinkManager.setSetTimeWakeup(hour, minute);
            }
        });
    }

    private void setTextTimeData(int h, int m) {
        if (h < 10) {
            label1.setText(String.valueOf(0));
            label2.setText(String.valueOf(h));
        } else if (h < 20) {
            label1.setText(String.valueOf(1));
            label2.setText(String.valueOf(h - 10));
        } else {
            label1.setText(String.valueOf(2));
            label2.setText(String.valueOf(h - 20));
        }

        if (m < 10) {
            label3.setText(String.valueOf(0));
            label4.setText(String.valueOf(m));
        } else if (m < 20) {
            label3.setText(String.valueOf(1));
            label4.setText(String.valueOf(m - 10));
        } else if (m < 30) {
            label3.setText(String.valueOf(2));
            label4.setText(String.valueOf(m - 20));
        } else if (m < 40) {
            label3.setText(String.valueOf(3));
            label4.setText(String.valueOf(m - 30));
        } else if (m < 50) {
            label3.setText(String.valueOf(4));
            label4.setText(String.valueOf(m - 40));
        } else if (m < 60) {
            label3.setText(String.valueOf(5));
            label4.setText(String.valueOf(m - 50));
        }
    }
}
