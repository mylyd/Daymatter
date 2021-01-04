package com.mobo.daymatter.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.daymatter.R;

/**
 * @author : ydli
 * @description 自定义时间界面
 * @time : 19:38
 */
public class PunchCustomizeTimeDialog extends BaseDialog implements View.OnClickListener {
    private Context mContext;
    private OnTimeClickListener mListenerTime;
    private EditText edTime;

    public PunchCustomizeTimeDialog(Context context) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_punch_time);

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
        edTime = findViewById(R.id.ed_time);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                edTime.setText("25");
                dismiss();
                break;
            case R.id.tv_submit:
                String edTimeStr = edTime.getText().toString();
                if (!TextUtils.isEmpty(edTimeStr)) {
                    mListenerTime.onItemClick(Integer.parseInt(edTimeStr));
                    dismiss();
                } else {
                    Toast.makeText(mContext, R.string.matter_punch_time_customize_time, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public interface OnTimeClickListener {
        void onItemClick(int index);
    }

    public void setOnFrequencyClickListener(OnTimeClickListener listener) {
        this.mListenerTime = listener;
    }

}
