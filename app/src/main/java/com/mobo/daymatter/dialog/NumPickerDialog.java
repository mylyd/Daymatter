package com.mobo.daymatter.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mobo.daymatter.R;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.views.BodyWeightPickerView;

/**
 * 喝水界面 menu弹窗
 */
public class NumPickerDialog extends BaseDialog {
    private Context mContext;
    private OnNumValueClickListener listener;
    private BodyWeightPickerView numPicker;


    public NumPickerDialog(Context context) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_num_picker);

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
        numPicker = findViewById(R.id.num_picker);
        setNumPickerMinMax(DrinkManager.getUnitType());
        numPicker.setWrapSelectorWheel(true); //循环滚动
        numPicker.setValue(DrinkManager.WATER_BODY_WEIGHT);
        numPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numPicker.setNumberPickerDividerColor(R.color.number_picker, 0.2);
        numPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                listener.onNumValueClick(oldValue, newValue);
            }
        });
    }

    /**
     * @true kg单位范围
     * @false lbs单位范围
     * @param isUnit
     */
    public void setNumPickerMinMax(boolean isUnit) {
        if (numPicker == null) {
            return;
        }
        numPicker.setMinValue(isUnit ? DrinkManager.WATER_BODY_WEIGHT_MIN_KG : DrinkManager.WATER_BODY_WEIGHT_MIN_LBS);
        numPicker.setMaxValue(isUnit ? DrinkManager.WATER_BODY_WEIGHT_MAX_KG : DrinkManager.WATER_BODY_WEIGHT_MAX_LBS);
    }

    @SuppressLint("NewApi")
    public void show(int value) {
        show();
        if (numPicker == null) {
            return;
        }
        numPicker.setValue(value);
    }

    public interface OnNumValueClickListener {
        void onNumValueClick(int oldValue, int newValue);
    }

    public void setOnTimeValueClickListener(OnNumValueClickListener clickListener) {
        this.listener = clickListener;
    }
}
