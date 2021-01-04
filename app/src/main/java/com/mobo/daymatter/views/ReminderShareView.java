package com.mobo.daymatter.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.mobo.daymatter.R;

/**
 * 存粹一个视图包裹
 */
public class ReminderShareView extends FrameLayout {
    public ReminderShareView(Context context) {
        this(context, null);
    }

    public ReminderShareView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReminderShareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context, attrs);
    }

    private void initLayout(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ReminderShareView);
        int resourceId = array.getResourceId(R.styleable.ReminderShareView_android_layout, 0);
        if (resourceId != 0) {
            LayoutInflater.from(context).inflate(resourceId, this, true);
        }
        array.recycle();
    }

    public void setLayoutId(int resourceId) {
        if (resourceId != 0) {
            LayoutInflater.from(getContext()).inflate(resourceId, this, true);
        }
        requestLayout();
    }
}
