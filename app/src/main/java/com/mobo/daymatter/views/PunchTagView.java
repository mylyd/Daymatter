package com.mobo.daymatter.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * 视图是根据打卡信息中的颜色、和图片生成的视图
 */
public class PunchTagView extends androidx.appcompat.widget.AppCompatImageView {
    private Drawable mDrawable;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PunchTagView(Context context) {
        super(context);
    }

    public PunchTagView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PunchTagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void updateView(int color, int drawableRes) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        setImageResource(drawableRes);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int radius = Math.min(width >> 1, height >> 1);
        canvas.drawCircle(width >> 1, height >> 1, radius, mPaint);
        super.onDraw(canvas);
    }
}
