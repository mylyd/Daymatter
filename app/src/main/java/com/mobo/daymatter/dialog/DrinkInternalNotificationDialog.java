package com.mobo.daymatter.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
public class DrinkInternalNotificationDialog extends BaseDialog implements View.OnClickListener {
    private Context mContext;
    private TextView mConfirm;
    private OnItemClickListener listener;


    public DrinkInternalNotificationDialog(Context context) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_internal_nitification);

        Window window = this.getWindow();
        /*WindowManager.LayoutParams params = window.getAttributes();
        params.dimAmount = 0f;
        window.setAttributes(params);*/
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dialog_anim_option_menu);
        setCanceledOnTouchOutside(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        initView();
    }

    private void initView() {
        mConfirm = findViewById(R.id.dialog_notification);
        findViewById(R.id.iv_close).setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_notification:
                listener.onItemConfirmClick();
                dismiss();
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    public interface OnItemClickListener {
        void onItemConfirmClick();
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.listener = clickListener;
    }
}
