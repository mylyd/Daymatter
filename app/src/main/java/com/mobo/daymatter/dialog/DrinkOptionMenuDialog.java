package com.mobo.daymatter.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobo.daymatter.R;
import com.mobo.daymatter.activities.DrinkingWaterParametersActivity;
import com.mobo.daymatter.activities.HistoryRecordActivity;

/**
 * 喝水界面 menu弹窗
 */
public class DrinkOptionMenuDialog extends BaseDialog implements View.OnClickListener {
    private Fragment fragment;
    public TextView mRecord;
    private TextView mEditGoal;


    public DrinkOptionMenuDialog(Fragment context) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.fragment = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_drink_option);

        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = 100;
        //params.dimAmount = 0f;
        window.setAttributes(params);
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        window.setWindowAnimations(R.style.dialog_anim_option_menu);
        setCanceledOnTouchOutside(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView();
    }

    private void initView() {
        mRecord = findViewById(R.id.menu_record);
        mEditGoal = findViewById(R.id.menu_edit_goal);
        mRecord.setOnClickListener(this);
        mEditGoal.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_record:
                HistoryRecordActivity.startNew(fragment,0);
                dismiss();
                break;
            case R.id.menu_edit_goal:
                DrinkingWaterParametersActivity.startNew(fragment, 0,
                        DrinkingWaterParametersActivity.PARAMETERS_WATER_GOALS);
                dismiss();
                break;
        }
    }
}
