package com.mobo.daymatter.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.PunchClockBean;
import com.mobo.daymatter.beans.PunchClockTargetMode;
import com.mobo.daymatter.dialog.PunchColorDialog;
import com.mobo.daymatter.dialog.PunchCustomizeTimeDialog;
import com.mobo.daymatter.dialog.PunchDayDialog;
import com.mobo.daymatter.dialog.PunchDeleteDialog;
import com.mobo.daymatter.manager.PunchClockManager;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;

/**
 * @author : ydli
 * @description 打卡新增任务界面
 * @time : 19:38
 */
public class PunchEditActivity extends BaseActivity implements View.OnClickListener {
    public static final String IS_EDIT = "IS_EDIT";
    public static final String PUNCH_BEAN = "PUNCHBEAN";

    private TextView mTitle;
    private EditText edTitle;
    private CardView cvColor;
    private ImageView ivIcon;
    private TextView tvEvery;
    private TextView tvFrequency;
    private PunchColorDialog punchColorDialog;
    private PunchColorDialog punchIconDialog;
    private PunchDayDialog punchEvery;
    private PunchDayDialog punchFrequency;
    private PunchClockBean punchClockBean;
    private ImageView ivTime;
    private ImageView ivCustomize;
    private TextView tvMode;
    private ImageView ivMode;
    private ImageView ivConsuming;
    private PunchCustomizeTimeDialog punchCustomizeTimeDialog;
    private PunchClockTargetMode punchClockTargetMode;
    private boolean isEdit;
    private LinearLayout llEditLayout;
    private TextView btnSave;
    private PunchClockBean punchEditBean;
    private PunchDeleteDialog punchDeleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_edit);
        isEdit = getIntent().getBooleanExtra(IS_EDIT, false);
        if (isEdit) {
            punchEditBean = getIntent().getParcelableExtra(PUNCH_BEAN);
        }
        initView();
        initData();
        ClickListener();
    }

    private void initView() {
        llEditLayout = findViewById(R.id.ll_edit_layout);
        findViewById(R.id.back).setOnClickListener(this);
        mTitle = findViewById(R.id.title);
        edTitle = findViewById(R.id.ed_punch_title);
        findViewById(R.id.punch_color_item).setOnClickListener(this);
        findViewById(R.id.punch_icon_item).setOnClickListener(this);
        findViewById(R.id.punch_every_day_item).setOnClickListener(this);
        findViewById(R.id.punch_frequency_item).setOnClickListener(this);
        findViewById(R.id.tv_delete).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        ivTime = findViewById(R.id.iv_time);
        ivCustomize = findViewById(R.id.iv_customize);
        ivConsuming = findViewById(R.id.iv_consuming);
        ivMode = findViewById(R.id.iv_mode);
        tvMode = findViewById(R.id.tv_mode_name);
        ivTime.setOnClickListener(this);
        ivCustomize.setOnClickListener(this);
        ivConsuming.setOnClickListener(this);
        ivMode.setOnClickListener(this);
        cvColor = findViewById(R.id.cv_punch_color_item);
        ivIcon = findViewById(R.id.iv_punch_icon_item);
        tvEvery = findViewById(R.id.tv_punch_every_day_item);
        tvFrequency = findViewById(R.id.iv_punch_frequency_item);
        tvFrequency.setText(R.string.matter_punch_ed_no);
        btnSave = findViewById(R.id.save_area);
        btnSave.setOnClickListener(this);
        punchColorDialog = new PunchColorDialog(this, 0);
        punchIconDialog = new PunchColorDialog(this, 1);
        punchEvery = new PunchDayDialog(this, 0);
        punchFrequency = new PunchDayDialog(this, 1);
        punchCustomizeTimeDialog = new PunchCustomizeTimeDialog(this);
        punchDeleteDialog = new PunchDeleteDialog(this);
    }

    private void initData() {
        punchClockBean = new PunchClockBean();
        if (isEdit) {//编辑模式
            llEditLayout.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            mTitle.setText(R.string.matter_punch_edit_title);
            if (punchEditBean == null) {
                return;
            }
            punchClockBean = PunchClockBean.copy(punchEditBean);
            punchClockTargetMode = punchClockBean.getTargetMode();
            initEditData(punchClockBean);
        } else {//新增模式
            btnSave.setVisibility(View.VISIBLE);
            llEditLayout.setVisibility(View.GONE);
            mTitle.setText(R.string.matter_punch_add_title);
            //新增密室下设置默认参数 time = 25 ,timeMode为倒计时
            punchClockBean.setTime(25);
            punchClockBean.setTimeMode(PunchClockBean.TIME_DOWN);
            punchClockBean.setColorPos(Color.parseColor("#FF83FA"));
            punchClockBean.setBitmapPos(0);
            punchClockBean.setRepeatMode(PunchClockBean.REPEAT_ONE_DAY);
            punchClockTargetMode = new PunchClockTargetMode();
            punchClockTargetMode.setMode(PunchClockTargetMode.MODE_NONE);
        }
    }

    /**
     * 填充编辑 模式下各项数据
     */
    private void initEditData(PunchClockBean bean) {
        if (bean == null) {
            return;
        }
        //填充标题
        edTitle.setText(bean.getName());
        //填充时间
        if (bean.getTime() == 25) {
            ivTime.setImageResource(R.drawable.ic_punch_time_selected);
            ivCustomize.setImageResource(R.drawable.ic_punck_customize);
        } else {
            ivTime.setImageResource(R.drawable.ic_punch_time);
            ivCustomize.setImageResource(R.drawable.ic_punck_customize_selected);
        }
        //填充时间模式
        if (bean.getTimeMode() == PunchClockBean.TIME_NONE) {
            ivConsuming.setImageResource(R.drawable.ic_not_time_consuming_selected);
            ivMode.setImageResource(R.drawable.ic_positive_timing);
        } else if (bean.getTimeMode() == PunchClockBean.TIME_DOWN) {
            ivConsuming.setImageResource(R.drawable.ic_not_time_consuming);
            ivMode.setImageResource(R.drawable.ic_positive_timing_selected);
            tvMode.setText(R.string.matter_punch_positive_countdown);
        } else if (bean.getTimeMode() == PunchClockBean.TIME_UP) {
            ivConsuming.setImageResource(R.drawable.ic_not_time_consuming);
            ivMode.setImageResource(R.drawable.ic_positive_timing_selected);
            tvMode.setText(R.string.matter_punch_positive_countup);
        }
        //填充颜色
        cvColor.setCardBackgroundColor(bean.getColorPos());
        //填充icon
        ivIcon.setImageDrawable(getResources().obtainTypedArray(R.array.punch_icon).getDrawable(bean.getBitmapPos()));
        //填充重复模式
        if (bean.getRepeatMode() == PunchClockBean.REPEAT_ONE_DAY) {
            tvEvery.setText(R.string.matter_punch_every_day);
        } else if (bean.getRepeatMode() == PunchClockBean.REPEAT_ONE_WEEK) {
            tvEvery.setText(R.string.matter_punch_weekly);
        } else if (bean.getRepeatMode() == PunchClockBean.REPEAT_FOREVER) {
            tvEvery.setText(R.string.matter_punch_per_year);
        } else if (bean.getRepeatMode() == PunchClockBean.REPEAT_ONE_TIMES) {
            tvEvery.setText(R.string.matter_punch_once);
        }
        //填充目标模式
        if (bean.getTargetMode().getMode() == PunchClockTargetMode.MODE_NONE) {
            tvFrequency.setText(R.string.matter_punch_ed_no);
        } else if (bean.getTargetMode().getMode() == PunchClockTargetMode.MODE_COUNT) {
            tvFrequency.setText(String.format(getResources().getString(R.string.matter_punch_frequency_c),
                    bean.getTargetMode().getCount()));
        } else if (bean.getTargetMode().getMode() == PunchClockTargetMode.MODE_TIMES) {
            tvFrequency.setText(String.format(getResources().getString(R.string.matter_punch_frequency_m),
                    bean.getTargetMode().getTimes()));
        }
    }

    private void ClickListener() {
        punchColorDialog.setOnColorClickListener(new PunchColorDialog.OnColorClickListener() {
            @Override
            public void onItemColorClick(int color) {
                //color为选择的色值 ,值例 Color.parseColor("#000000")
                cvColor.setCardBackgroundColor(color);
                punchClockBean.setColorPos(color);
                if (punchColorDialog.isShowing()) {
                    punchColorDialog.dismiss();
                }
            }
        });

        punchIconDialog.setOnIconClickListener(new PunchColorDialog.OnIconClickListener() {
            @Override
            public void onItemIconClick(Drawable icon, int index) {
                //icon为返回的图标，Drawable类型 ,index为地址位置，0-29整数，在arrays punch_icon拿
                ivIcon.setImageDrawable(icon);
                punchClockBean.setBitmapPos(index);
                if (punchIconDialog.isShowing()) {
                    punchIconDialog.dismiss();
                }
            }
        });
        punchEvery.setOnEveryClickListener(new PunchDayDialog.OnEveryClickListener() {
            @Override
            public void onItemClick(int[] context, int index) {
                switch (index) {
                    case 0:
                        //选择每天
                        tvEvery.setText(context[0]);
                        punchClockBean.setRepeatMode(PunchClockBean.REPEAT_ONE_DAY);
                        break;
                    case 1:
                        //选择每周
                        tvEvery.setText(context[1]);
                        punchClockBean.setRepeatMode(PunchClockBean.REPEAT_ONE_WEEK);
                        break;
                    case 2:
                        //选择每年
                        tvEvery.setText(context[2]);
                        punchClockBean.setRepeatMode(PunchClockBean.REPEAT_FOREVER);
                        break;
                    case 3:
                        //选择仅一次
                        tvEvery.setText(context[3]);
                        punchClockBean.setRepeatMode(PunchClockBean.REPEAT_ONE_TIMES);
                        break;
                }
            }
        });
        punchFrequency.setOnFrequencyClickListener(new PunchDayDialog.OnFrequencyClickListener() {
            @Override
            public void onItemClick(int checked, int index) {
                switch (checked) {
                    case 0:
                        //选择执行多少次，index是次数
                        tvFrequency.setText(String.format(getResources().getString(R.string.matter_punch_frequency_c), index));
                        punchClockTargetMode.setMode(PunchClockTargetMode.MODE_COUNT);
                        punchClockTargetMode.setCount(index);
                        break;
                    case 1:
                        //选择耗时多久，index是分钟
                        tvFrequency.setText(String.format(getResources().getString(R.string.matter_punch_frequency_m), index));
                        punchClockTargetMode.setMode(PunchClockTargetMode.MODE_TIMES);
                        punchClockTargetMode.setTimes(index);
                        break;
                    case 2:
                        //选择无
                        tvFrequency.setText(R.string.matter_punch_ed_no);
                        punchClockTargetMode.setMode(PunchClockTargetMode.MODE_NONE);
                        punchClockTargetMode.setCount(1);
                        punchClockTargetMode.setTimes(25);
                        break;
                }
            }
        });
        punchCustomizeTimeDialog.setOnFrequencyClickListener(new PunchCustomizeTimeDialog.OnTimeClickListener() {
            @Override
            public void onItemClick(int index) {
                //自定义时间 ，index为时间，单位分钟
                ivTime.setImageResource(R.drawable.ic_punch_time);
                ivCustomize.setImageResource(R.drawable.ic_punck_customize_selected);
                punchClockBean.setTime(index);
            }
        });
        punchDeleteDialog.setOnDeleteClickListener(new PunchDeleteDialog.OnDeleteClickListener() {
            @Override
            public void onItemClick() {
                //确定删除
                PunchClockManager.get().init(PunchEditActivity.this).deleteDetailData(punchEditBean, true);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.punch_color_item:
                if (punchColorDialog != null && !punchColorDialog.isShowing()) {
                    punchColorDialog.show();
                }
                break;
            case R.id.punch_icon_item:
                if (punchIconDialog != null && !punchIconDialog.isShowing()) {
                    punchIconDialog.show();
                }
                break;
            case R.id.punch_every_day_item:
                if (punchEvery != null && !punchEvery.isShowing()) {
                    punchEvery.show();
                    if (isEdit) {
                        if (punchClockBean.getRepeatMode() == PunchClockBean.REPEAT_ONE_DAY) {
                            tvEvery.setText(R.string.matter_punch_every_day);
                            punchEvery.setTvSelected(0);
                        } else if (punchClockBean.getRepeatMode() == PunchClockBean.REPEAT_ONE_WEEK) {
                            tvEvery.setText(R.string.matter_punch_weekly);
                            punchEvery.setTvSelected(1);
                        } else if (punchClockBean.getRepeatMode() == PunchClockBean.REPEAT_FOREVER) {
                            tvEvery.setText(R.string.matter_punch_per_year);
                            punchEvery.setTvSelected(2);
                        } else if (punchClockBean.getRepeatMode() == PunchClockBean.REPEAT_ONE_TIMES) {
                            tvEvery.setText(R.string.matter_punch_once);
                            punchEvery.setTvSelected(3);
                        }
                    }
                }
                break;
            case R.id.punch_frequency_item:
                if (punchFrequency != null && !punchFrequency.isShowing()) {
                    punchFrequency.show();
                }
                break;
            case R.id.iv_time:
                ivTime.setImageResource(R.drawable.ic_punch_time_selected);
                ivCustomize.setImageResource(R.drawable.ic_punck_customize);
                Toast.makeText(this, R.string.matter_punch_time_value, Toast.LENGTH_SHORT).show();
                punchClockBean.setTime(25);
                break;
            case R.id.iv_customize:
                //自定义
                if (punchCustomizeTimeDialog != null && !punchCustomizeTimeDialog.isShowing()) {
                    punchCustomizeTimeDialog.show();
                }
                break;
            case R.id.iv_consuming:
                //不耗时
                ivConsuming.setImageResource(R.drawable.ic_not_time_consuming_selected);
                ivMode.setImageResource(R.drawable.ic_positive_timing);
                punchClockBean.setTimeMode(PunchClockBean.TIME_NONE);
                break;
            case R.id.iv_mode:
                ivConsuming.setImageResource(R.drawable.ic_not_time_consuming);
                ivMode.setImageResource(R.drawable.ic_positive_timing_selected);
                if (tvMode.getText().toString().equals(getString(R.string.matter_punch_positive_countup))) {
                    //正计时,点击设置成倒计时
                    tvMode.setText(R.string.matter_punch_positive_countdown);
                    punchClockBean.setTimeMode(PunchClockBean.TIME_DOWN);
                } else if (tvMode.getText().toString().equals(getString(R.string.matter_punch_positive_countdown))) {
                    //倒计时，点击设置成正计时
                    tvMode.setText(R.string.matter_punch_positive_countup);
                    punchClockBean.setTimeMode(PunchClockBean.TIME_UP);

                }
                break;
            case R.id.tv_delete:
                //编辑模式下删除按钮
                if (punchDeleteDialog != null && !punchDeleteDialog.isShowing()) {
                    punchDeleteDialog.show();
                }
                break;
            case R.id.tv_save:
                //编辑模式下保存按钮
                String edTitleEdit = edTitle.getText().toString();
                if (TextUtils.isEmpty(edTitleEdit)) {
                    Toast.makeText(this, R.string.matter_title_value_cannot_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                punchClockBean.setName(edTitleEdit);
                PunchClockManager.get().init(this).editDetailData(punchEditBean, punchClockBean);
                finish();
                break;
            case R.id.save_area:
                String edTitleStr = edTitle.getText().toString();
                if (TextUtils.isEmpty(edTitleStr)) {
                    Toast.makeText(this, R.string.matter_title_value_cannot_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                punchClockBean.setName(edTitleStr);
                if (punchClockBean == null || punchClockTargetMode == null) {
                    Toast.makeText(this, R.string.matter_value_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                punchClockBean.setTargetMode(punchClockTargetMode);
                PunchClockManager.get().init(this).addDetailData(punchClockBean, true);
                FirebaseTracker.get().track(MyTracker.ADDTASK_CLICK_SAVE);
                finish();
                break;

        }
    }

    /**
     * @param activity
     * @param punchClockBean
     * @param requestCode
     * @param isEdit         是否是编辑模式
     */
    public static void startNew(Activity activity, @Nullable PunchClockBean punchClockBean, int requestCode, boolean isEdit) {
        Intent intent = new Intent(activity, PunchEditActivity.class);
        intent.putExtra(IS_EDIT, isEdit);
        if (isEdit && punchClockBean != null) {
            intent.putExtra(PUNCH_BEAN, punchClockBean);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @param fragment
     * @param punchClockBean
     * @param requestCode
     * @param isEdit         是否是编辑模式
     */
    public static void startNew(Fragment fragment, @Nullable PunchClockBean punchClockBean, int requestCode, boolean isEdit) {
        Intent intent = new Intent(fragment.getContext(), PunchEditActivity.class);
        intent.putExtra(IS_EDIT, isEdit);
        if (isEdit && punchClockBean != null) {
            intent.putExtra(PUNCH_BEAN, punchClockBean);
        }
        fragment.startActivityForResult(intent, requestCode);
    }
}
