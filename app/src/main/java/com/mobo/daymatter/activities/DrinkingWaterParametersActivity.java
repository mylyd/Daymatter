package com.mobo.daymatter.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mobo.daymatter.R;
import com.mobo.daymatter.dialog.NumPickerDialog;
import com.mobo.daymatter.dialog.TimePickerDialog;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.manager.NotificationManager;
import com.mobo.daymatter.manager.SoftKeyBoardListenerManager;
import com.mobo.daymatter.utils.PunchClockUtils;

import static java.lang.Integer.parseInt;

/**
 * @author : ydli
 * @description 设置页对应喝水参数设置页面
 * @time :
 */
public class DrinkingWaterParametersActivity extends BaseActivity implements View.OnClickListener {
    public static String PARAMETERS_VALUE = "parameters_value";
    /**
     * 设置总饮水量
     */
    public static String PARAMETERS_WATER_GOALS = "parameters_water_goals";
    /**
     * 设置单位
     */
    public static String PARAMETERS_UNIT = "parameters_unit";
    /**
     * 设置性别
     */
    public static String PARAMETERS_GENDER = "parameters_gender";
    /**
     * 设置体重
     */
    public static String PARAMETERS_BODY_WEIGHT = "parameters_body_weight";
    /**
     * 设置起床时间
     */
    public static String PARAMETERS_WAKE_UP_TIME = "parameters_wake_up_time";
    /**
     * 设置睡觉时间
     */
    public static String PARAMETERS_SLEEPING_TIME = "parameters_sleeping_time";

    private String setType;
    private TextView tvTitle;
    private ImageView ivKg;
    private ImageView ivLbs;
    private TextView tvProgress;
    private EditText tvCapacity;

    private FrameLayout[] frameLayouts;

    private ImageView[] unitImage;

    private ImageView[] genderImage;
    private TextView tvBodyWeight;
    private TextView tvWakeUpTime;
    private TextView tvSleepingTime;
    private TimePickerDialog tpWakeUpDialog;
    private TimePickerDialog tpSleepingDialog;
    private NumPickerDialog numPickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinking_water_parameters);
        setType = getIntent().getStringExtra(PARAMETERS_VALUE);

        findViewById(R.id.iv_back).setOnClickListener(this);
        if (TextUtils.isEmpty(setType)) {
            return;
        }
        initView();
        initData();
        Listener();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);

        frameLayouts = new FrameLayout[]{
                findViewById(R.id.drink_water_type),
                findViewById(R.id.drink_unit_type),
                findViewById(R.id.drink_gender_type),
                findViewById(R.id.drink_body_weight_type),
                findViewById(R.id.wake_up_time_type),
                findViewById(R.id.sleeping_time_type)};

        unitImage = new ImageView[]{
                findViewById(R.id.iv_kg),
                findViewById(R.id.iv_lbs)};

        genderImage = new ImageView[]{
                findViewById(R.id.iv_male),
                findViewById(R.id.iv_female),
                findViewById(R.id.iv_confidential)};

        //容量界面
        tvProgress = findViewById(R.id.tv_drink_progress);
        findViewById(R.id.total_subtract).setOnClickListener(this);
        findViewById(R.id.total_add).setOnClickListener(this);
        findViewById(R.id.drink_water_calculate).setOnClickListener(this);
        tvCapacity = findViewById(R.id.tv_total);

        //单位界面
        findViewById(R.id.type_kg).setOnClickListener(this);
        findViewById(R.id.type_lbs).setOnClickListener(this);
        ivKg = findViewById(R.id.iv_kg);
        ivLbs = findViewById(R.id.iv_lbs);

        //性别界面
        findViewById(R.id.type_male).setOnClickListener(this);
        findViewById(R.id.type_female).setOnClickListener(this);
        findViewById(R.id.type_confidential).setOnClickListener(this);

        //体重
        tvBodyWeight = findViewById(R.id.tv_drink_body_weight_type);
        tvBodyWeight.setOnClickListener(this);

        //起床时间
        tvWakeUpTime = findViewById(R.id.tv_wake_up_time_type);
        tvWakeUpTime.setOnClickListener(this);

        //睡觉时间
        tvSleepingTime = findViewById(R.id.tv_sleeping_time_type);
        tvSleepingTime.setOnClickListener(this);

        tpWakeUpDialog = new TimePickerDialog(this);
        tpSleepingDialog = new TimePickerDialog(this);
        numPickerDialog = new NumPickerDialog(this);
    }

    private void Listener() {
        tpWakeUpDialog.setOnTimeValueClickListener(new TimePickerDialog.OnTimeValueClickListener() {
            @Override
            public void onTimeValueClick(int hour, int minute) {
                tvWakeUpTime.setText(String.format(getString(R.string.time_text_value), (hour < 10) ? "0" + hour : hour,
                        (minute < 10) ? "0" + minute : minute));
                DrinkManager.setDrinkWakeUpTime(PunchClockUtils.getTimeInMillis(hour, minute));
                DrinkManager.setSetTimeWakeup(hour, minute);
            }
        });

        tpSleepingDialog.setOnTimeValueClickListener(new TimePickerDialog.OnTimeValueClickListener() {
            @Override
            public void onTimeValueClick(int hour, int minute) {
                tvSleepingTime.setText(String.format(getString(R.string.time_text_value), (hour < 10) ? "0" + hour : hour,
                        (minute < 10) ? "0" + minute : minute));
                long time = PunchClockUtils.getTimeInMillis(hour, minute);
                if (time <= DrinkManager.getDrinkWakeUpTime()) {
                    //如果睡觉设置的时间比起床时间早的话，往后顺延一天
                    DrinkManager.setDrinkSleepingTime(time + (60 * 60 * 24 * 1000));
                } else {
                    DrinkManager.setDrinkSleepingTime(time);
                }
                DrinkManager.setSetTimeSleeping(hour, minute);
            }
        });

        numPickerDialog.setOnTimeValueClickListener(new NumPickerDialog.OnNumValueClickListener() {
            @Override
            public void onNumValueClick(int oldValue, int newValue) {
                tvBodyWeight.setText(String.format("%s%s", newValue,
                        DrinkManager.getUnitType() ? DrinkManager.UNIT_KG : DrinkManager.UNIT_LBS));
                DrinkManager.setDrinkBodyWeight(newValue);
            }
        });

        //软键盘显示与收起监听，
        //问题：在软键盘弹出时会关掉容器动画导致容器显示进度动画为0，所以监听键盘关闭时重新刷新动画
        SoftKeyBoardListenerManager.setListener(this, new SoftKeyBoardListenerManager.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                Log.e("keyBoardShow", "keyBoardShow: keyBoardShow");
            }

            @Override
            public void keyBoardHide(int height) {
                if (tvCapacity != null) {
                    if (TextUtils.isEmpty(tvCapacity.getText().toString())) {
                        tvCapacity.setText(String.valueOf(DrinkManager.getDrinkTotal()));
                        return;
                    }
                    int total = parseInt(tvCapacity.getText().toString());
                    int min = DrinkManager.getUnitType() ? (DrinkManager.WATER_BODY_WEIGHT_MIN_KG * 33) : (DrinkManager.WATER_BODY_WEIGHT_MIN_LBS * 33);
                    if (total <= min) {
                        Toast.makeText(DrinkingWaterParametersActivity.this, R.string.drink_toast_total_lowest, Toast.LENGTH_SHORT).show();
                        tvCapacity.setText(String.valueOf(DrinkManager.getDrinkTotal()));
                        return;
                    }
                    DrinkManager.setDrinkTotal(total);
                    UpdateProgress();
                }
            }
        });
    }

    private void initData() {
        if (setType.equals(PARAMETERS_WATER_GOALS)) {
            UpdateVisibility(0);
            //设置喝水上限
            tvTitle.setText(R.string.drink_water_goals);
            UpdateProgress();
        } else if (setType.equals(PARAMETERS_UNIT)) {
            UpdateVisibility(1);
            //设置单位
            tvTitle.setText(R.string.drink_unit);
            UpdateUnitVisibility(DrinkManager.getUnitType() ? 0 : 1);
        } else if (setType.equals(PARAMETERS_GENDER)) {
            UpdateVisibility(2);
            //设置性别
            tvTitle.setText(R.string.drink_gender);
            if (DrinkManager.getDrinkGender().equals(DrinkManager.GENDER_MALE)) {
                UpdateGenderVisibility(0);
            } else if (DrinkManager.getDrinkGender().equals(DrinkManager.GENDER_FEMALE)) {
                UpdateGenderVisibility(1);
            } else {
                UpdateGenderVisibility(2);
            }
        } else if (setType.equals(PARAMETERS_BODY_WEIGHT)) {
            UpdateVisibility(3);
            //设置体重
            tvTitle.setText(R.string.drink_body_weight);
            tvBodyWeight.setText(String.format("%s%s", DrinkManager.getDrinkBodyWeight(),
                    DrinkManager.getUnitType() ? DrinkManager.UNIT_KG : DrinkManager.UNIT_LBS));
        } else if (setType.equals(PARAMETERS_WAKE_UP_TIME)) {
            UpdateVisibility(4);
            //设置起床时间
            tvTitle.setText(R.string.drink_wake_up_time);
            tvWakeUpTime.setText(PunchClockUtils.getHHMM(DrinkManager.getDrinkWakeUpTime()));
        } else if (setType.equals(PARAMETERS_SLEEPING_TIME)) {
            UpdateVisibility(5);
            //设置睡觉时间
            tvTitle.setText(R.string.drink_sleeping_time);
            tvSleepingTime.setText(PunchClockUtils.getHHMM(DrinkManager.getDrinkSleepingTime()));
        }
    }

    /**
     * 控制显示界面
     */
    private void UpdateVisibility(int index) {
        for (FrameLayout llm : frameLayouts) {
            llm.setVisibility(View.GONE);
        }
        frameLayouts[index].setVisibility(View.VISIBLE);
    }

    private void UpdateUnitVisibility(int index) {
        for (ImageView iv : unitImage) {
            iv.setVisibility(View.GONE);
        }
        unitImage[index].setVisibility(View.VISIBLE);
    }

    private void UpdateGenderVisibility(int index) {
        for (ImageView iv : genderImage) {
            iv.setVisibility(View.GONE);
        }
        genderImage[index].setVisibility(View.VISIBLE);
    }

    /**
     * 更新数据显示情况
     */
    private void UpdateProgress() {
        tvProgress.setText(String.format(getString(R.string.drink_cup_capacity),
                String.valueOf(DrinkManager.getDrinkProgress()),
                String.valueOf(DrinkManager.getDrinkTotal()),
                DrinkManager.getDrinkUnit()));
        tvCapacity.setText(String.valueOf(DrinkManager.getDrinkTotal()));
        //更新通知
        NotificationManager.get().showNotification(this,manager);
    }

    /**
     * @param fragment
     * @param requestCode
     * @param type        传入需要设置的页面参数
     */
    public static void startNew(Fragment fragment, int requestCode, String type) {
        Intent intent = new Intent(fragment.getContext(), DrinkingWaterParametersActivity.class);
        intent.putExtra(PARAMETERS_VALUE, type);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity
     * @param requestCode
     * @param type
     */
    public static void startNew(Activity activity, int requestCode, String type) {
        Intent intent = new Intent(activity, DrinkingWaterParametersActivity.class);
        intent.putExtra(PARAMETERS_VALUE, type);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.total_subtract:
                //减少
                DrinkManager.ToTotalSubtract(this);
                UpdateProgress();
                break;
            case R.id.total_add:
                //增加
                DrinkManager.ToTotalAdd(this);
                UpdateProgress();
                break;

            case R.id.type_kg:
                //单位kg
                DrinkManager.setDrinkUnit(DrinkManager.UNIT_ML);
                UpdateUnitVisibility(0);
                DrinkManager.LbsToKg();
                break;
            case R.id.type_lbs:
                //单位lbs
                DrinkManager.setDrinkUnit(DrinkManager.UNIT_LBS);
                UpdateUnitVisibility(1);
                DrinkManager.KgToLbs();
                break;

            case R.id.type_male:
                //性别男
                DrinkManager.setDrinkGender(DrinkManager.GENDER_MALE);
                UpdateGenderVisibility(0);
                break;
            case R.id.type_female:
                //性别女
                DrinkManager.setDrinkGender(DrinkManager.GENDER_FEMALE);
                UpdateGenderVisibility(1);
                break;
            case R.id.type_confidential:
                //性别保密
                DrinkManager.setDrinkGender(DrinkManager.GENDER_CONFIDENTIAL);
                UpdateGenderVisibility(2);
                break;

            case R.id.drink_water_calculate:
                //饮水量设置引导
                CalculateWaterConsumptionActivity.startNew(this, 0);
                finish();
                break;

            case R.id.tv_drink_body_weight_type:
                //体重
                if (numPickerDialog != null && !numPickerDialog.isShowing()) {
                    numPickerDialog.show(DrinkManager.getDrinkBodyWeight());
                }
                break;
            case R.id.tv_wake_up_time_type:
                //起床
                if (tpWakeUpDialog != null && !tpWakeUpDialog.isShowing()) {
                    tpWakeUpDialog.show(DrinkManager.getSetTimeHourWakeup(), DrinkManager.getSetTimeMinuteWakeup());
                }
                break;
            case R.id.tv_sleeping_time_type:
                //睡觉
                if (tpSleepingDialog != null && !tpSleepingDialog.isShowing()) {
                    tpSleepingDialog.show(DrinkManager.getSetTimeHourSleeping(), DrinkManager.getSetTimeMinuteSleeping());
                }
                break;
        }
    }
}
