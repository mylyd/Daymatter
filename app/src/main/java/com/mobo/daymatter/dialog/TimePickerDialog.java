package com.mobo.daymatter.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.mobo.daymatter.R;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.utils.PunchClockUtils;
import com.mobo.daymatter.views.MyTimePickerView;

import java.util.Calendar;

/**
 * 喝水界面 menu弹窗
 */
public class TimePickerDialog extends BaseDialog {
    private Context mContext;
    private OnTimeValueClickListener listener;
    private MyTimePickerView timePicker;


    public TimePickerDialog(Context context) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time_picker);

        Window window = this.getWindow();
        /*WindowManager.LayoutParams params = window.getAttributes();
        params.dimAmount = 0f;
        window.setAttributes(params);*/
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_dialog_anim_style);
        setCanceledOnTouchOutside(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        initView();
    }

    @SuppressLint("NewApi")
    private void initView() {
        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(8);  //设置当前小时
        timePicker.setMinute(30); //设置当前分（0-59）
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS); //设置当前分（0-59）
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                listener.onTimeValueClick(hour, minute);
            }
        });
    }

    @SuppressLint("NewApi")
    public void show(int hour, int minute) {
        show();
        if (timePicker == null) {
            return;
        }
        timePicker.setHour(hour);  //设置当前小时
        timePicker.setMinute(minute); //设置当前分（0-59）
    }

    public interface OnTimeValueClickListener {
        void onTimeValueClick(int hour, int minute);
    }

    public void setOnTimeValueClickListener(OnTimeValueClickListener clickListener) {
        this.listener = clickListener;
    }
}
