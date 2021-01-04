package com.mobo.daymatter.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.mobo.daymatter.R;
import com.mobo.daymatter.network.CommonCallback;
import com.mobo.daymatter.utils.BaseUtils;

public class FeedBackDialog extends BaseDialog{
    private static final String TAG = "RatingDialog";
    private Activity mContext;
    public TextView mTitle;
    private EditText mEmail;
    private EditText mSuggestion;
    private Button mSubmit;

    public FeedBackDialog(Activity context) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_feedback);

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
        mEmail = findViewById(R.id.et_email);
        mSuggestion = findViewById(R.id.et_suggestion);
        mSubmit = findViewById(R.id.btn_submit);
    }

    /**
     * =控件监听，用于实时通过显示具体对应的UI
     */
    private void setListener() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String suggestion = mSuggestion.getText().toString();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(suggestion)) {
                    mSubmit.setEnabled(false);
                    BaseUtils.requestFeedBack(mContext, email, suggestion, new CommonCallback<JsonElement>() {
                        @Override
                        public void onResponse(JsonElement response) {
                            Toast.makeText(mContext, "We have received your feedback , thanks !", Toast.LENGTH_SHORT).show();
                            mSubmit.setEnabled(true);
                            mEmail.setText(null);
                            mSuggestion.setText(null);
                            dismiss();
                        }

                        @Override
                        public void onFailure(Throwable t, boolean isServerUnavailable) {
                            mSubmit.setEnabled(true);
                            Toast.makeText(mContext, "feedback failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(mContext, "Email and Suggestions are all need to fill in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
