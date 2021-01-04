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
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.mobo.daymatter.R;

/**
 * @author : ydli
 * @description 自定义时间界面
 * @time : 19:38
 */
public class PunchDeleteDialog extends BaseDialog implements View.OnClickListener {
    private Context mContext;
    private OnDeleteClickListener mListenerDelete;

    public PunchDeleteDialog(Context context) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_punch_delete);

        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.bottom_dialog_anim_style);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(true);

        //设置dialog横向占比为全屏
        //window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_submit:
                mListenerDelete.onItemClick();
                break;
        }
    }

    public interface OnDeleteClickListener {
        void onItemClick();
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.mListenerDelete = listener;
    }

}
