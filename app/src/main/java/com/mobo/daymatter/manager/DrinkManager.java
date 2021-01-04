package com.mobo.daymatter.manager;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobo.daymatter.MatterApplication;
import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.DrinkBean;
import com.mobo.daymatter.beans.drinkrecord.RecordNumBean;
import com.mobo.daymatter.beans.drinkrecord.RecordProgressBean;
import com.mobo.daymatter.beans.drinkrecord.RecordTotalBean;
import com.mobo.daymatter.utils.PunchClockUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author : ydli
 * @time : 20-5-26 下午5:33
 * @description 喝水数据管理类
 */
public class DrinkManager {
    public static String NOTIFICATION_ITEM_ID = "NOTIFICATION_ITEM_ID";
    public static int NOTIFICATION_ID = 1001;

    /**
     * 当日喝水总量值
     */
    private static String DRINK_TOTAL = "drink_total";

    /**
     * 每次喝水单次容量
     */
    private static String DRINK_CAPACITY = "drink_capacity";

    /**
     * 喝水进度 （ <= 喝水总量）
     */
    private static String DRINK_PROGRESS = "drink_progress";

    /**
     * 单位 （kg.ml && lbs.floz）
     */
    private static String DRINK_UNIT = "drink_unit";

    /**
     * 性别 （男 && 女 && 保密）
     */
    private static String DRINK_GENDER = "drink_gender";

    /**
     * 体重 （kg）
     */
    private static String DRINK_BODY_WEIGHT = "drink_body_weight";

    /**
     * 起床时间 （ms）
     */
    private static String DRINK_WAKEUP_TIME = "drink_wakeup_time";

    /**
     * 睡觉时间 （ms）
     */
    private static String DRINK_SLEEPING_TIME = "drink_sleeping_time";

    /**
     * 下次喝水时间
     */
    private static String DRINK_NEXT_WATER_TIME = "drink_next_water_time";

    /**
     * 喝水记录
     */
    private static String DRINK_RECORDING_BEAN = "drink_recording_bean";
    /**
     * 喝水周期记录
     */
    //次数
    private static String DRINKING_WATER_RECORDS_NUM = "drinking_water_records_num";
    //目标
    private static String DRINKING_WATER_RECORDS_TOTAL = "drinking_water_records_total";
    //喝水量
    private static String DRINKING_WATER_RECORDS_PROGRESS = "drinking_water_records_progress";

    /**
     * 设置时间记忆留存
     */
    private static String DRINKING_WATER_SET_TIME_HOUR_WAKEUP = "drinking_water_set_time_hour_wakeup";
    private static String DRINKING_WATER_SET_TIME_MINUTE_WAKEUP = "drinking_water_set_time_minute_wakeup";
    private static String DRINKING_WATER_SET_TIME_HOUR_SLEEPING = "drinking_water_set_time_hour_sleeping";
    private static String DRINKING_WATER_SET_TIME_MINUTE_SLEEPING = "drinking_water_set_time_minute_sleeping";

    /**
     * 记录每日时间
     */
    private static String DRINKING_WATER_DATE_TIME = "drinking_water_date_time";

    //每次容量减少数量
    public static int CAPACITY_VALUE_KG = 100;
    public static int CAPACITY_VALUE_LBS = 1;
    //每次总量减少数量
    public static int TOTAL_VALUE_KG = 100;
    public static int TOTAL_VALUE_LBS = 5;

    //单位 默认值 UNIT_KG
    public static String UNIT_ML = "ml";
    public static String UNIT_LBS = "lbs";
    public static String UNIT_FLOZ = "floz";
    public static String UNIT_KG = "kg";

    //性别 默认值 GENDER_CONFIDENTIAL
    public static String GENDER_MALE = "Male";
    public static String GENDER_FEMALE = "Female";
    public static String GENDER_CONFIDENTIAL = "Confidential";

    //进度 默认值 0
    public static int WATER_PROGRESS = 0;

    //喝水总量 默认值 1980
    public static int WATER_TOTAL = 1980;

    //喝水单次值 默认值 200
    public static int WATER_CAPACITY_KG = 200;
    public static int WATER_CAPACITY_LBS = 10;

    //喝水最小值
    public static int WATER_CAPACITY_MIN = 0;

    //体重 默认值 60kg 对等算式 总喝水量 1980默认值 = 体重60 *33
    public static int WATER_BODY_WEIGHT = 60;
    //体重范围 min ~ max
    public static int WATER_BODY_WEIGHT_MIN_KG = 20;
    public static int WATER_BODY_WEIGHT_MAX_KG = 200;
    public static int WATER_BODY_WEIGHT_MIN_LBS = 40;
    public static int WATER_BODY_WEIGHT_MAX_LBS = 400;

    //体重选择器范围值 0~ 200* 21是 偏移值
    public static int ITEM_VIEW_RECYCLERVIEW_SIZE_KG = 221;
    public static int ITEM_VIEW_RECYCLERVIEW_SIZE_LBS = 465;
    //处理刻度不是从0开始，主动定位到15
    public static int ITEM_VIEW_POSITION = 10;

    //起床 时间 默认值 8:30
    public static int TIME_HOUR_WAKEUP = 8;
    public static int TIME_MINUTE_WAKEUP = 30;

    //睡觉 时间 默认值 22:30
    public static int TIME_HOUR_SLEEPING = 22;
    public static int TIME_MINUTE_SLEEPING = 30;

    //下次喝水 时间 默认值 起床时间
    public static long WATER_DRINK_TIME = PunchClockUtils.getTimeInMillis(TIME_HOUR_WAKEUP, TIME_MINUTE_WAKEUP);


    public static void init() {
        SharedPreferenceManager.getInstance().init(MatterApplication.getApplication());
    }

    /**
     * 获取起床记忆时间小时
     *
     * @return
     */
    public static int getSetTimeHourWakeup() {
        return SharedPreferenceManager.getInstance().getInt(DRINKING_WATER_SET_TIME_HOUR_WAKEUP, TIME_HOUR_WAKEUP);
    }

    /**
     * 获取起床记忆时间分钟
     *
     * @return
     */
    public static int getSetTimeMinuteWakeup() {
        return SharedPreferenceManager.getInstance().getInt(DRINKING_WATER_SET_TIME_MINUTE_WAKEUP, TIME_MINUTE_WAKEUP);
    }

    /**
     * 获取起床记忆时间分钟
     *
     * @return
     */
    public static int getSetTimeHourSleeping() {
        return SharedPreferenceManager.getInstance().getInt(DRINKING_WATER_SET_TIME_HOUR_SLEEPING, TIME_HOUR_SLEEPING);
    }

    /**
     * 获取起床记忆时间分钟
     *
     * @return
     */
    public static int getSetTimeMinuteSleeping() {
        return SharedPreferenceManager.getInstance().getInt(DRINKING_WATER_SET_TIME_MINUTE_SLEEPING, TIME_MINUTE_SLEEPING);
    }

    /**
     * 设置起床记忆时间
     */
    public static void setSetTimeWakeup(int hour, int minute) {
        SharedPreferenceManager.getInstance().putInt(DRINKING_WATER_SET_TIME_HOUR_WAKEUP, hour).apply();
        SharedPreferenceManager.getInstance().putInt(DRINKING_WATER_SET_TIME_MINUTE_WAKEUP, minute).apply();
    }

    /**
     * 设置睡觉记忆时间
     */
    public static void setSetTimeSleeping(int hour, int minute) {
        SharedPreferenceManager.getInstance().putInt(DRINKING_WATER_SET_TIME_HOUR_SLEEPING, hour).apply();
        SharedPreferenceManager.getInstance().putInt(DRINKING_WATER_SET_TIME_MINUTE_SLEEPING, minute).apply();
    }

    /**
     * 获取喝水总量值
     *
     * @return
     */
    public static int getDrinkTotal() {
        return SharedPreferenceManager.getInstance().getInt(DRINK_TOTAL, WATER_TOTAL);
    }

    /**
     * 设置喝水总量值
     */
    public static void setDrinkTotal(int values) {
        SharedPreferenceManager.getInstance().putInt(DRINK_TOTAL, values).apply();
    }

    /**
     * 获取喝水单次值
     *
     * @return
     */
    public static int getDrinkCapacity() {
        return SharedPreferenceManager.getInstance().getInt(DRINK_CAPACITY, getUnitType() ? WATER_CAPACITY_KG : WATER_CAPACITY_LBS);
    }

    /**
     * 设置喝水单次值
     */
    public static void setDrinkCapacity(int values) {
        SharedPreferenceManager.getInstance().putInt(DRINK_CAPACITY, values).apply();
    }

    /**
     * 获取喝水进度
     *
     * @return
     */
    public static int getDrinkProgress() {
        return SharedPreferenceManager.getInstance().getInt(DRINK_PROGRESS, WATER_PROGRESS);
    }

    /**
     * 设置喝水进度
     */
    public static void setDrinkProgress(int values) {
        SharedPreferenceManager.getInstance().putInt(DRINK_PROGRESS, values).apply();
    }

    /**
     * 获取水位单位
     *
     * @return
     */
    public static String getDrinkUnit() {
        return SharedPreferenceManager.getInstance().getString(DRINK_UNIT, UNIT_ML);
    }

    /**
     * 设置水位单位
     */
    public static void setDrinkUnit(String values) {
        SharedPreferenceManager.getInstance().putString(DRINK_UNIT, values).apply();
    }

    /**
     * 获取性别
     *
     * @return
     */
    public static String getDrinkGender() {
        return SharedPreferenceManager.getInstance().getString(DRINK_GENDER, GENDER_CONFIDENTIAL);
    }

    /**
     * 设置性别
     */
    public static void setDrinkGender(String values) {
        SharedPreferenceManager.getInstance().putString(DRINK_GENDER, values).apply();
    }

    /**
     * 获取体重
     *
     * @return
     */
    public static int getDrinkBodyWeight() {
        return SharedPreferenceManager.getInstance().getInt(DRINK_BODY_WEIGHT, WATER_BODY_WEIGHT);
    }

    /**
     * 设置体重
     */
    public static void setDrinkBodyWeight(int values) {
        SharedPreferenceManager.getInstance().putInt(DRINK_BODY_WEIGHT, values).apply();
    }

    /**
     * 获取起床时间
     *
     * @return
     */
    public static long getDrinkWakeUpTime() {
        return SharedPreferenceManager.getInstance().getLong(DRINK_WAKEUP_TIME,
                PunchClockUtils.getTimeInMillis(TIME_HOUR_WAKEUP, TIME_MINUTE_WAKEUP));
    }

    /**
     * 设置起床时间
     */
    public static void setDrinkWakeUpTime(long values) {
        SharedPreferenceManager.getInstance().putLong(DRINK_WAKEUP_TIME, values).apply();
        setDrinkNextWaterTime(values);
    }

    /**
     * 获取睡觉时间
     *
     * @return
     */
    public static long getDrinkSleepingTime() {
        return SharedPreferenceManager.getInstance().getLong(DRINK_SLEEPING_TIME,
                PunchClockUtils.getTimeInMillis(TIME_HOUR_SLEEPING, TIME_MINUTE_SLEEPING));
    }

    /**
     * 设置睡觉时间
     */
    public static void setDrinkSleepingTime(long values) {
        SharedPreferenceManager.getInstance().putLong(DRINK_SLEEPING_TIME, values).apply();
    }

    /**
     * 获取下次喝水时间
     *
     * @return
     */
    public static long getDrinkNextWaterTime() {
        return SharedPreferenceManager.getInstance().getLong(DRINK_NEXT_WATER_TIME, WATER_DRINK_TIME);
    }

    /**
     * 设置下次喝水时间
     */
    public static void setDrinkNextWaterTime(long values) {
        SharedPreferenceManager.getInstance().putLong(DRINK_NEXT_WATER_TIME, values).apply();
    }

    /**
     * 获取记录日期
     *
     * @return
     */
    public static String getDrinkDataTime() {
        return SharedPreferenceManager.getInstance().getString(DRINKING_WATER_DATE_TIME, null);
    }

    /**
     * 设置记录日期
     */
    public static void setDrinkDataTime(String time) {
        SharedPreferenceManager.getInstance().putString(DRINKING_WATER_DATE_TIME, time).apply();
    }

    /**
     * 设置喝水记录
     *
     * @param drinkBeans
     */
    public static void setDrinkRecordingBean(List<DrinkBean> drinkBeans) {
        SharedPreferenceManager.getInstance().putString(DRINK_RECORDING_BEAN, new Gson().toJson(drinkBeans)).apply();
    }

    /**
     * 获取喝水记录
     */
    public static List<DrinkBean> getDrinkRecordingBean() {
        Gson gson = new Gson();
        String json = SharedPreferenceManager.getInstance().getString(DRINK_RECORDING_BEAN, null);
        Type type = new TypeToken<List<DrinkBean>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    /**
     * 新增喝水记录
     */
    public static void addDrinkRecordingBean(DrinkBean drinkBean) {
        List<DrinkBean> drinkBeanLists = getDrinkRecordingBean();
        if (drinkBeanLists != null) {
            //去重
            //drinkBeanLists.remove(drinkBean);
        } else {
            drinkBeanLists = new ArrayList<>();
        }
        drinkBeanLists.add(drinkBean);
        //反向排序，最新喝水记录显示在前面
        //Collections.reverse(drinkBeanLists);
        Collections.sort(drinkBeanLists, new Comparator<DrinkBean>() {
            @Override
            public int compare(DrinkBean drinkBean1, DrinkBean drinkBean2) {
                //倒叙*-1，正序*1
                return (int) (-1 * (drinkBean1.getTime() - drinkBean2.getTime()));
            }
        });
        setDrinkRecordingBean(drinkBeanLists);
    }

    /**
     * 设置喝水周期记录
     *
     * @param recordNumBean
     */
    public static void setDrinkRecordNumBean(List<RecordNumBean> recordNumBean) {
        SharedPreferenceManager.getInstance().putString(DRINKING_WATER_RECORDS_NUM, new Gson().toJson(recordNumBean)).apply();
    }

    /**
     * 获取喝水周期记录
     *
     * @喝水次数
     */
    public static List<RecordNumBean> getDrinkRecordNumBean() {
        Gson gson = new Gson();
        String json = SharedPreferenceManager.getInstance().getString(DRINKING_WATER_RECORDS_NUM, null);
        Type type = new TypeToken<List<RecordNumBean>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    /**
     * 新增喝水周期记录
     *
     * @喝水次数
     */
    public static void addDrinkRecordNumBean(RecordNumBean recordNumBean) {
        List<RecordNumBean> recordNumBeans = getDrinkRecordNumBean();
        if (recordNumBeans != null && recordNumBeans.size() != 0) {
            //去重
            recordNumBeans.remove(recordNumBean);
            /*for (RecordNumBean num : recordNumBeans){
                if (num.getRecordTime().equals(recordNumBean.getRecordTime())){
                    recordNumBeans.remove(num);
                }
            }*/
        } else {
            recordNumBeans = new ArrayList<>();
        }
        recordNumBeans.add(recordNumBean);
        setDrinkRecordNumBean(recordNumBeans);
    }

    /**
     * 设置喝水周期记录
     *
     * @param recordTotalBean
     */
    public static void setDrinkRecordTotalBean(List<RecordTotalBean> recordTotalBean) {
        SharedPreferenceManager.getInstance().putString(DRINKING_WATER_RECORDS_TOTAL, new Gson().toJson(recordTotalBean)).apply();
    }

    /**
     * 获取喝水周期记录
     *
     * @喝水目标
     */
    public static List<RecordTotalBean> getDrinkRecordTotalBean() {
        Gson gson = new Gson();
        String json = SharedPreferenceManager.getInstance().getString(DRINKING_WATER_RECORDS_TOTAL, null);
        Type type = new TypeToken<List<RecordTotalBean>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    /**
     * 新增喝水周期记录
     *
     * @喝水目标
     */
    public static void addDrinkRecordTotalBean(RecordTotalBean recordTotalBean) {
        try {
            List<RecordTotalBean> recordTotalBeans = getDrinkRecordTotalBean();
            if (recordTotalBeans != null && recordTotalBeans.size() != 0) {
                //去重
                for (RecordTotalBean num : recordTotalBeans) {
                    if (num == null || num.getRecordTime() == null || recordTotalBean.getRecordTime() == null) {
                        return;
                    }
                    if (num.getRecordTime().equals(recordTotalBean.getRecordTime())) {
                        recordTotalBeans.remove(num);
                    }
                }
            } else {
                recordTotalBeans = new ArrayList<>();
            }
            recordTotalBeans.add(recordTotalBean);
            setDrinkRecordTotalBean(recordTotalBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置喝水周期记录
     *
     * @param recordProgressBean
     */
    public static void setDrinkRecordProgressBean(List<RecordProgressBean> recordProgressBean) {
        SharedPreferenceManager.getInstance().putString(DRINKING_WATER_RECORDS_PROGRESS, new Gson().toJson(recordProgressBean)).apply();
    }

    /**
     * 获取喝水周期记录
     *
     * @喝水量
     */
    public static List<RecordProgressBean> getDrinkRecordProgressBean() {
        Gson gson = new Gson();
        String json = SharedPreferenceManager.getInstance().getString(DRINKING_WATER_RECORDS_PROGRESS, null);
        Type type = new TypeToken<List<RecordProgressBean>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    /**
     * 新增喝水周期记录
     *
     * @喝水量
     */
    public static void addDrinkRecordProgressBean(RecordProgressBean recordProgressBean) {
        try {
            List<RecordProgressBean> recordProgressBeans = getDrinkRecordProgressBean();
            if (recordProgressBeans != null && recordProgressBeans.size() != 0) {
                //去重
                for (RecordProgressBean num : recordProgressBeans) {
                    if (num == null || num.getRecordTime() == null || recordProgressBean.getRecordTime() == null) {
                        return;
                    }
                    if (num.getRecordTime().equals(recordProgressBean.getRecordTime())) {
                        recordProgressBeans.remove(num);
                    }
                }
            } else {
                recordProgressBeans = new ArrayList<>();
            }
            recordProgressBeans.add(recordProgressBean);
            setDrinkRecordProgressBean(recordProgressBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 容量减少
     */
    public static void ToCapacitySubtract(Context context) {
        int capacity = getDrinkCapacity();
        int diff = (getUnitType() ? CAPACITY_VALUE_KG : CAPACITY_VALUE_LBS);
        if (capacity <= WATER_CAPACITY_MIN) {
            Toast.makeText(context, R.string.drink_toast_capacity_lowest, Toast.LENGTH_SHORT).show();
            return;
        }
        int capacityEnd;
        if (capacity < diff) {
            capacityEnd = WATER_CAPACITY_MIN;
        } else {
            if (capacity % diff == 0) {
                capacityEnd = capacity - diff;
            } else {
                capacityEnd = capacity - (capacity % diff);
            }
        }
        setDrinkCapacity(capacityEnd);
    }

    /**
     * 容量增加
     */
    public static void ToCapacityAdd(Context context) {
        int capacity = getDrinkCapacity();
        int diff = (getUnitType() ? CAPACITY_VALUE_KG : CAPACITY_VALUE_LBS);
        if (capacity < 0) {
            setDrinkCapacity(diff);
            return;
        }
        int capacityEnd;
        if (capacity < diff) {
            capacityEnd = diff;
        } else {
            if (capacity % diff == 0) {
                capacityEnd = capacity + diff;
            } else {
                capacityEnd = capacity + (diff - (capacity % diff));
            }
        }
        setDrinkCapacity(capacityEnd);
    }

    /**
     * 总量减少
     */
    public static void ToTotalSubtract(Context context) {
        int total = getDrinkTotal();
        int diff = getUnitType() ? CAPACITY_VALUE_KG : CAPACITY_VALUE_LBS;
        int min = getUnitType() ? (WATER_BODY_WEIGHT_MIN_KG * 33) : (WATER_BODY_WEIGHT_MIN_LBS * 33);
        if (total <= min) {
            Toast.makeText(context, R.string.drink_toast_total_lowest, Toast.LENGTH_SHORT).show();
            setDrinkTotal(min);
            return;
        }

        int totalEnd;
        if (total % diff == 0) {
            totalEnd = total - diff;
        } else {
            totalEnd = total - (total % diff);
        }
        setDrinkTotal(totalEnd);
    }

    /**
     * 总量增加
     */
    public static void ToTotalAdd(Context context) {
        int total = getDrinkTotal();
        int diff = getUnitType() ? CAPACITY_VALUE_KG : CAPACITY_VALUE_LBS;
        int totalEnd;
        if (total % diff == 0) {
            totalEnd = total + diff;
        } else {
            totalEnd = total + (diff - (total % diff));
        }
        setDrinkTotal(totalEnd);
    }

    /**
     * 编辑喝水进度
     *
     * @param context
     * @param progress
     */
    public static void ToDrinkProgress(Context context, int progress) {
        int pg = getDrinkProgress();
        if (progress < 0) {
            Toast.makeText(context, R.string.drink_toast_total_progress, Toast.LENGTH_SHORT).show();
            return;
        }
        if (progress == 0) {
            return;
        }
        int water = (pg + progress);

        if (water <= 0) {
            water = 0;
        }

        setDrinkProgress(water);
        //记录一组喝水数据
        if (progress > 0) {
            addDrinkRecordingBean(ToSetDataDrinkBean(System.currentTimeMillis(),
                    progress, getDrinkTotal(), getDrinkCapacity(), getDrinkUnit()));
        }
    }

    public static DrinkBean ToSetDataDrinkBean(long time, int value, int total, int capacity, String unit) {
        return new DrinkBean(time, value, total, capacity, unit);
    }

    public static void ToAddRecordNumBean() {
        RecordNumBean recordNumBean = new RecordNumBean(PunchClockUtils.getYYMM(System.currentTimeMillis()),
                System.currentTimeMillis());
        addDrinkRecordNumBean(recordNumBean);
    }

    public static void ToAddRecordTotalBean() {
        RecordTotalBean recordNumBean = new RecordTotalBean(PunchClockUtils.getYYMMDD(System.currentTimeMillis()),
                getDrinkTotal());
        addDrinkRecordTotalBean(recordNumBean);
    }

    public static void ToAddRecordProgressBean() {
        RecordProgressBean recordNumBean = new RecordProgressBean(PunchClockUtils.getYYMMDD(System.currentTimeMillis()),
                getDrinkProgress());
        addDrinkRecordProgressBean(recordNumBean);
    }

    /**
     * 计算每次喝水时间间隔
     *
     * @return
     */
    public static long getNextIntervalTime(Context context) {
        //喝水次数
        long num;
        if (getDrinkCapacity() == 0) {
            Toast.makeText(context, R.string.each_time_the_water, Toast.LENGTH_SHORT).show();
            return 0;
        }
        if (getDrinkCapacity() >= getDrinkTotal()) {
            num = 1;
        } else {
            num = (getDrinkTotal() / getDrinkCapacity());
        }

        long time;
        if (getDrinkSleepingTime() < getDrinkWakeUpTime()) {
            //如果睡觉时间获取值小于起床时间，默认将睡觉时间向后推迟一天
            time = (getDrinkSleepingTime() + (24 * 60 * 60 * 1000)) - getDrinkWakeUpTime();
        } else {
            //间隔时间总毫秒
            time = (getDrinkSleepingTime() - getDrinkWakeUpTime());
        }
        //间隔时间
        return (time / num);
    }

    /**
     * 获取下次喝水时间 HH:MM
     *
     * @param startTime
     * @param interval
     * @return
     */
    public static long getNextTime(long startTime, long interval) {
        long sTime = getDrinkWakeUpTime();
        long eTime = getDrinkSleepingTime();
        long time = startTime + interval;
        if (eTime < sTime) {
            //如果睡觉时间获取值小于起床时间，默认将睡觉时间向后推迟一天
            eTime = eTime + (24 * 60 * 60 * 1000);
        }
        if (time <= eTime && time >= sTime) {
            return time;
        } else {
            return startTime;
        }
    }

    public static boolean getUnitType() {
        return getDrinkUnit().equals(UNIT_ML);
    }

    public static double lbs_kg = 0.5;
    public static double oz_ml = 30;

    /**
     * kg切换lbs
     */
    public static void KgToLbs() {
        int progress = getDrinkProgress();
        int total = getDrinkTotal();
        int bodyWeight = getDrinkBodyWeight();
        int capacity = getDrinkCapacity();
        setDrinkProgress(progress / 30);
        setDrinkTotal(total / 30);
        setDrinkBodyWeight(bodyWeight * 2);
        setDrinkCapacity(capacity / 30);
    }

    /**
     * lbs切换kg
     */
    public static void LbsToKg() {
        int progress = getDrinkProgress();
        int total = getDrinkTotal();
        int bodyWeight = getDrinkBodyWeight();
        int capacity = getDrinkCapacity();
        setDrinkProgress(progress * 30);
        setDrinkTotal(total * 30);
        setDrinkBodyWeight(bodyWeight / 2);
        setDrinkCapacity(capacity * 30);
    }

    /**
     * kg转换成lbs
     *
     * @param kg
     * @return
     */
    public static int KgSetLbs(int kg) {
        return kg * 2;
    }

    /**
     * lbs转换成kg
     *
     * @param lbs
     * @return
     */
    public static int LbsSetKg(int lbs) {
        return lbs / 2;
    }

    /**
     * ml转换成floz
     *
     * @param ml
     * @return
     */
    public static int MlSetFloz(int ml) {
        return (int) (ml / oz_ml);
    }

    /**
     * floz转换成ml
     *
     * @param floz
     * @return
     */
    public static int FlozSetMl(int floz) {
        return (int) (floz * oz_ml);
    }
}
