package com.mobo.daymatter.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.mobo.daymatter.R;

/**
 * @author : ydli
 * @time : 19-10-22 下午2:25
 * @description 评分引导弹窗
 */
public class RatingDialog extends BaseDialog {
    private static final String TAG = "RatingDialog";
    private Activity context;
    public TextView mTitle;
    private TextView mContent;
    public AppCompatRatingBar compatRatingBar;
    private Button mSubmit;
    private Button mCancel;
    private float mRatingNum = 0.0f;
    private OnRateListener onRateListener;

    public RatingDialog(Activity context, OnRateListener listener) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.context = context;
        this.onRateListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rating);

        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_dialog_anim_style);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog横向占比为全屏
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
        setListener();
    }

    private void initView() {
        mTitle = findViewById(R.id.tv_title);
        mContent = findViewById(R.id.tv_content);
        compatRatingBar = findViewById(R.id.ratingBar);
        mCancel = findViewById(R.id.btn_cancel);
        mSubmit = findViewById(R.id.btn_submit);
    }

    /**
     * =控件监听，用于实时通过显示具体对应的UI
     */
    private void setListener() {
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                compatRatingBar.setRating(0.0f);
            }
        });
        compatRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    mRatingNum = rating;
                }
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRatingNum > 3.0f) {
                    if (onRateListener != null) {
                        onRateListener.onHeightLevel();
                    }
                } else {
                    if (onRateListener != null) {
                        onRateListener.onLowLevel();
                    }
                }
                dismiss();
            }
        });
    }

    public interface OnRateListener {
        void onHeightLevel();
        void onLowLevel();
    }
}
