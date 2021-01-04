package com.mobo.daymatter.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mobo.daymatter.R;
import com.mobo.daymatter.manager.DrinkManager;

import static java.lang.Integer.parseInt;

/**
 * 喝水界面 menu弹窗
 */
public class DrinkEditGoalDialog extends BaseDialog implements View.OnClickListener {
    public static int DIALOG_CALCULATE = 0;
    public static int DIALOG_NONE = 1;
    private Context mContext;
    public TextView mCancel;
    private TextView mConfirm;
    private EditText tvCapacity;
    private int Value;
    private int mPosition;
    private OnItemClickListener listener;
    private boolean isUnit = true;
    private long time;


    public DrinkEditGoalDialog(Context context) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_goal);

        Window window = this.getWindow();
        /*WindowManager.LayoutParams params = window.getAttributes();
        params.dimAmount = 0f;
        window.setAttributes(params);*/
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.bottom_dialog_anim_style);
        setCanceledOnTouchOutside(false);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView();
    }

    private void initView() {
        mCancel = findViewById(R.id.dialog_cancel);
        mConfirm = findViewById(R.id.dialog_confirm);
        findViewById(R.id.dialog_add).setOnClickListener(this);
        findViewById(R.id.dialog_subtract).setOnClickListener(this);
        tvCapacity = findViewById(R.id.tv_capacity);
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    public void show(int value, int position, long tm) {
        show();
        this.Value = value;
        this.mPosition = position;
        this.time = tm;
        tvCapacity.setText(String.valueOf(Value));
    }

    public void setUnit(boolean item) {
        this.isUnit = item;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel:
                listener.onItemCancelClick(parseInt(tvCapacity.getText().toString()), mPosition);
                dismiss();
                break;
            case R.id.dialog_add:
                int diff_add = isUnit ? DrinkManager.CAPACITY_VALUE_KG : DrinkManager.CAPACITY_VALUE_LBS;
                if (Value < 0) {
                    tvCapacity.setText(String.valueOf(diff_add));
                    return;
                }
                if (Value < diff_add) {
                    Value = diff_add;
                } else {
                    if (Value % diff_add == 0) {
                        Value = Value + diff_add;
                    } else {
                        Value = Value + (diff_add - (Value % diff_add));
                    }
                }
                tvCapacity.setText(String.valueOf(Value));
                break;
            case R.id.dialog_subtract:
                int diff_subtract = isUnit ? DrinkManager.CAPACITY_VALUE_KG : DrinkManager.CAPACITY_VALUE_LBS;
                if (Value <= DrinkManager.WATER_CAPACITY_MIN) {
                    Toast.makeText(mContext, R.string.drink_toast_capacity_lowest, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Value < diff_subtract) {
                    Value = DrinkManager.WATER_CAPACITY_MIN;
                } else {
                    if (Value % diff_subtract == 0) {
                        Value = Value - diff_subtract;
                    } else {
                        Value = Value - (Value % diff_subtract);
                    }
                }
                tvCapacity.setText(String.valueOf(Value));
                break;
            case R.id.dialog_confirm:
                if (TextUtils.isEmpty(tvCapacity.getText().toString())) {
                    return;
                }
                listener.onItemConfirmClick(parseInt(tvCapacity.getText().toString()), mPosition, time);
                dismiss();
                break;
        }
    }

    public interface OnItemClickListener {
        void onItemConfirmClick(int value, int position, long time);

        void onItemCancelClick(int value, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.listener = clickListener;
    }
}
