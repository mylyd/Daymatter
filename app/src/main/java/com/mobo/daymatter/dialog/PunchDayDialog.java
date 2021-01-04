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
 * @description 新增打卡界面 重复选择与目标选择 @type = 0,重复界面 ，type = 1,目标界面
 * @time : 19:38
 */
public class PunchDayDialog extends BaseDialog implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView tvTitle;
    private int mType;
    private Context mContext;
    private OnEveryClickListener mListenerEvery;
    private OnFrequencyClickListener mListenerFrequency;
    private RecyclerView recyclerView;
    private TextView tvEveryDay;
    private TextView tvOnce;
    private TextView tvWeekly;
    private TextView tvYear;
    private TextView[] tvContext;
    private int[] tvConsuming;
    private LinearLayout mLayoutDay;
    private LinearLayout mLayoutFrequency;
    private CheckBox cbFrequency;
    private CheckBox cbTime;
    private CheckBox cbNo;
    private EditText edFrequency;
    private EditText edTime;

    public PunchDayDialog(Context context, int Type) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.mType = Type;
        this.mContext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_punch_day);

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
        mLayoutDay = findViewById(R.id.layout_day);
        mLayoutFrequency = findViewById(R.id.layout_frequency);
        cbFrequency = findViewById(R.id.cb_frequency);
        cbTime = findViewById(R.id.cb_time);
        cbNo = findViewById(R.id.cb_no);
        cbFrequency.setOnCheckedChangeListener(this);
        cbTime.setOnCheckedChangeListener(this);
        cbNo.setOnCheckedChangeListener(this);
        edFrequency = findViewById(R.id.ed_frequency);
        edTime = findViewById(R.id.ed_time);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_submit).setOnClickListener(this);
        tvEveryDay = findViewById(R.id.item_every_day);
        tvOnce = findViewById(R.id.item_once);
        tvWeekly = findViewById(R.id.item_weekly);
        tvYear = findViewById(R.id.item_per_year);
        tvEveryDay.setOnClickListener(this);
        tvWeekly.setOnClickListener(this);
        tvYear.setOnClickListener(this);
        tvOnce.setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_title);
        tvContext = new TextView[]{tvEveryDay, tvWeekly, tvYear, tvOnce};
        tvConsuming = new int[]{R.string.matter_punch_every_day, R.string.matter_punch_weekly,
                R.string.matter_punch_per_year, R.string.matter_punch_once};
        if (mType == 0) {
            mLayoutDay.setVisibility(View.VISIBLE);
            mLayoutFrequency.setVisibility(View.GONE);
            tvTitle.setText(R.string.matter_punch_not_time_consuming_icon);
            tvContext[0].setText(tvConsuming[0]);
            tvContext[1].setText(tvConsuming[1]);
            tvContext[2].setText(tvConsuming[2]);
            tvContext[3].setText(tvConsuming[3]);
        } else if (mType == 1) {
            mLayoutDay.setVisibility(View.GONE);
            mLayoutFrequency.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.matter_punch_positive_timing_icon);
            cbFrequency.setChecked(true);
        }
    }

    public void setTvSelected(int index) {
        for (TextView tv : tvContext) {
            tv.setBackgroundResource(R.drawable.gray_corner_bg);
        }
        tvContext[index].setBackgroundResource(R.drawable.yellow_corner_bg);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_every_day:
                setTvSelected(0);
                if (mType == 0) {
                    if (tvConsuming == null) {
                        return;
                    }
                    mListenerEvery.onItemClick(tvConsuming, 0);
                }
                dismiss();
                break;
            case R.id.item_weekly:
                setTvSelected(1);
                if (mType == 0) {
                    if (tvConsuming == null) {
                        return;
                    }
                    mListenerEvery.onItemClick(tvConsuming, 1);
                }
                dismiss();
                break;
            case R.id.item_per_year:
                setTvSelected(2);
                if (mType == 0) {
                    if (tvConsuming == null) {
                        return;
                    }
                    mListenerEvery.onItemClick(tvConsuming, 2);
                }
                dismiss();
                break;
            case R.id.item_once:
                setTvSelected(3);
                if (mType == 0) {
                    if (tvConsuming == null) {
                        return;
                    }
                    mListenerEvery.onItemClick(tvConsuming, 3);
                }
                dismiss();
                break;
            case R.id.tv_cancel:
                if (mType == 1){
                    cbFrequency.setChecked(true);
                    edFrequency.setText("20");
                    edTime.setText("25");
                }
                dismiss();
                break;
            case R.id.tv_submit:
                if (mType == 1){
                    String edFrequencyStr;
                    String edTimeStr;
                    int index;
                    if (cbFrequency.isChecked()){
                        index = 0;
                        edFrequencyStr = edFrequency.getText().toString();
                        if (TextUtils.isEmpty(edFrequencyStr)){
                            Toast.makeText(mContext, R.string.matter_value_cannot_empty, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mListenerFrequency.onItemClick(index,Integer.parseInt(edFrequencyStr));
                        dismiss();
                    }else if (cbTime.isChecked()){
                        index = 1;
                        edTimeStr = edTime.getText().toString();
                        if (TextUtils.isEmpty(edTimeStr)){
                            Toast.makeText(mContext, R.string.matter_value_cannot_empty, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mListenerFrequency.onItemClick(index,Integer.parseInt(edTimeStr));
                        dismiss();
                    }else if (cbNo.isChecked()){
                        index = 2;
                        mListenerFrequency.onItemClick(index,-1);
                        dismiss();
                    }else {
                        Toast.makeText(mContext, R.string.matter_select_setup_mode, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_frequency:
                if (b){
                    cbFrequency.setChecked(true);
                    cbTime.setChecked(false);
                    cbNo.setChecked(false);
                }
                break;
            case R.id.cb_time:
                if (b){
                    cbTime.setChecked(true);
                    cbFrequency.setChecked(false);
                    cbNo.setChecked(false);
                }
                break;
            case R.id.cb_no:
                if (b){
                    cbNo.setChecked(true);
                    cbFrequency.setChecked(false);
                    cbTime.setChecked(false);
                }
                break;
        }
    }

    public interface OnEveryClickListener {
        void onItemClick(int[] context, int index);
    }

    public interface OnFrequencyClickListener {
        void onItemClick(int checked, int index);
    }

    public void setOnEveryClickListener(OnEveryClickListener listener) {
        this.mListenerEvery = listener;
    }

    public void setOnFrequencyClickListener(OnFrequencyClickListener listener) {
        this.mListenerFrequency = listener;
    }

}
