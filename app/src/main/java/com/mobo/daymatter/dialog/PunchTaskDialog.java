package com.mobo.daymatter.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mobo.daymatter.R;

public class PunchTaskDialog extends BaseDialog {
    private TextView mContentView, mTitleView;
    private Button mSubmitView;
    private Button mCancelView;
    private int mTitleId, mContentId, mSubmitId;
    private PunchTaskDialog.OnButtonListener onButtonListener;

    public PunchTaskDialog(Context context) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
    }


    public void setTintContent(int titleId, int contentId, int submitId) {
        mTitleId = titleId;
        mContentId = contentId;
        mSubmitId = submitId;
        refreshView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tint);

        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.bottom_dialog_anim_style);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(true);

        //设置dialog横向占比为全屏
        //window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
        setListener();
    }

    private void initView() {
        mTitleView = findViewById(R.id.tv_title);
        mContentView = findViewById(R.id.tv_content);
        mCancelView = findViewById(R.id.btn_cancel);
        mSubmitView = findViewById(R.id.btn_submit);
        refreshView();
    }

    private void refreshView() {
        if (mTitleView != null && mTitleId != 0) {
            mTitleView.setText(mTitleId);
        }
        if (mContentView != null && mContentId != 0) {
            mContentView.setText(mContentId);
        }
        if (mSubmitView != null && mSubmitId != 0) {
            mSubmitView.setText(mSubmitId);
        }
    }

    /**
     * =控件监听，用于实时通过显示具体对应的UI
     */
    private void setListener() {
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonListener.onSubmitOnListener();
                dismiss();
            }
        });
    }

    public interface OnButtonListener {
        void onSubmitOnListener();
    }

    public void setOnButtonListener(PunchTaskDialog.OnButtonListener buttonListener) {
        this.onButtonListener = buttonListener;
    }
}
