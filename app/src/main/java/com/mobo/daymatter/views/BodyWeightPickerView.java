package com.mobo.daymatter.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * @author : ydli
 * @time : 20-5-29 上午11:52
 * @description 体重选择器
 */
public class BodyWeightPickerView extends NumberPicker {
    public BodyWeightPickerView(Context context) {
        super(context);
    }

    public BodyWeightPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BodyWeightPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public BodyWeightPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    /**
     * 修改字的大小和颜色
     */
    private void updateView(View view) {
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setTextColor(Color.BLACK); //修改字的颜色
            editText.setTextSize(16);//修改字的大小
        }
    }

    /**
     * 修改分割线的颜色
     */
    public void setNumberPickerDividerColor(int color,double height) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) { //找到mSelectionDivider
                pf.setAccessible(true);
                //设置分割线的颜色
                try {
                    pf.set(this, new ColorDrawable(this.getResources().getColor(color)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (pf.getName().equals("mSelectionDividerHeight")) {
                pf.setAccessible(true);
                try {
                    pf.set(this, height);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
