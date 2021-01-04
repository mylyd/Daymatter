package com.mobo.daymatter.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.mobo.daymatter.R;
import com.mobo.daymatter.adapter.PunchHistoryAdapter;
import com.mobo.daymatter.beans.PunchClockBean;
import com.mobo.daymatter.beans.PunchClockRecorder;
import com.mobo.daymatter.beans.PunchHistoryItem;
import com.mobo.daymatter.manager.PunchClockManager;
import com.mobo.daymatter.utils.PunchClockUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 打卡记录界面
 */
public class PunchHistoryActivity extends BaseActivity implements CalendarView.OnYearChangeListener, CalendarView.OnCalendarSelectListener {
    private static final String PUNCH_CLOCK_BEAN = "punch_clock_bean";
    private RecyclerView mRecycleView;
    private PunchHistoryAdapter mAdapter;
    private CalendarView mCalendarView;
    private boolean isPosOrder = true; // 是否是正序
    private TextView mPosView, mNegView;
    private ArrayList<PunchHistoryItem> mItems = new ArrayList<>();
    private String lastStr;
    private TextView tvYear;
    private TextView tvMonth;
    private TextView tvWeek;
    private CalendarLayout mCalendarLayout;
    private int mYear;
    private LinearLayout llWeekToYear;
    private java.util.Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punch_histroy);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendar = java.util.Calendar.getInstance();
        mRecycleView = findViewById(R.id.recycler);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PunchHistoryAdapter(this);
        mRecycleView.setAdapter(mAdapter);
        updateAdapter(PunchClockUtils.getCurDay(), getDataTime(calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH)));

        mCalendarLayout = findViewById(R.id.calendarLayout);
        llWeekToYear = findViewById(R.id.ll_weekToYear);
        mCalendarView = findViewById(R.id.calendarView);
        findViewById(R.id.ll_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                llWeekToYear.setVisibility(View.GONE);
                tvMonth.setText(String.valueOf(mYear));
            }
        });
        tvYear = findViewById(R.id.tv_year);
        tvMonth = findViewById(R.id.tv_month);
        tvWeek = findViewById(R.id.tv_week);
        tvYear.setText(String.valueOf(mCalendarView.getCurYear()));
        tvMonth.setText(String.valueOf(mCalendarView.getCurMonth()));
        tvWeek.setText(getWeek(calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1));
        mYear = mCalendarView.getCurYear();
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);

        mPosView = findViewById(R.id.pos_order);
        mNegView = findViewById(R.id.neg_order);
        findViewById(R.id.order_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPosOrder = !isPosOrder;
                if (isPosOrder) {
                    mPosView.setBackgroundResource(R.drawable.order_selected);
                    mPosView.setTextColor(Color.parseColor("#111213"));
                    mNegView.setBackground(null);
                    mNegView.setTextColor(Color.parseColor("#9B9B9D"));
                } else {
                    mNegView.setBackgroundResource(R.drawable.order_selected);
                    mNegView.setTextColor(Color.parseColor("#111213"));
                    mPosView.setBackground(null);
                    mPosView.setTextColor(Color.parseColor("#9B9B9D"));
                }
                mAdapter.refresh(isPosOrder);
            }
        });
    }

    private String getWeek(int index) {
        if (index > 7) {
            return null;
        }
        return getResources().getStringArray(R.array.week_name)[index];
    }

    private String getDataTime(int year, int month, int day) {
        String strMonth = String.valueOf(getResources().getStringArray(R.array.month_name)[month]);
        return String.format("%s  %s,%s", strMonth, String.valueOf(day), String.valueOf(year));
    }

    private boolean updateAdapter(String str, String data) {
        if (!TextUtils.equals(str, lastStr)) {
            lastStr = str;
            ArrayList<PunchClockBean> beans = PunchClockManager.get().getDetailData();
            if (beans == null || beans.isEmpty()) {
                return false;
            }
            ArrayList<String> keys = new ArrayList<>();
            ArrayList<String> temps;
            for (PunchClockBean bean : beans) {
                temps = PunchClockManager.get().getRecorderKeys(bean);
                if (temps == null || temps.isEmpty()) {
                    continue;
                }

                for (String temp : temps) {
                    if (bean.getRepeatMode() == PunchClockBean.REPEAT_ONE_WEEK) {
                        addHistoryItem(bean.getName(), temp, str);
                    } else if (temp.contains(str)) {
                        addHistoryItem(bean.getName(), temp, "");
                    }
                }
            }
            // 数据应该已经是有序的，不过暂时验证不了，暂时不去除代码
            Collections.sort(mItems, new Comparator<PunchHistoryItem>() {
                @Override
                public int compare(PunchHistoryItem o1, PunchHistoryItem o2) {
                    return o1.getTime() >= o2.getTime() ? -1 : 1;
                }
            });
        }
        mAdapter.refresh(mItems, data);
        return false;
    }

    private void addHistoryItem(String name, String temp, String str) {
        ArrayList<PunchClockRecorder> recorders = PunchClockManager.get().getRecorder(temp);
        if (recorders == null || recorders.isEmpty()) {
            return;
        }
        PunchHistoryItem item;
        if (TextUtils.isEmpty(str)) {
            for (PunchClockRecorder recorder : recorders) {
                item = PunchHistoryItem.copy(recorder);
                item.setName(name);
                mItems.add(item);
            }
        } else {
            for (PunchClockRecorder recorder : recorders) {
                if (PunchClockUtils.getSomeDay(recorder.getTime()).equals(str)) {
                    item = PunchHistoryItem.copy(recorder);
                    item.setName(name);
                    mItems.add(item);
                }
            }
        }
    }

    @Override
    public void onYearChange(int year) {
        //年份变化
        mYear = year;
        tvMonth.setText(String.valueOf(year));
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
        Toast.makeText(PunchHistoryActivity.this, String.format("%s : OutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        //日历滑动监听
        llWeekToYear.setVisibility(View.VISIBLE);
        tvYear.setText(String.valueOf(calendar.getYear()));
        tvMonth.setText(String.valueOf(calendar.getMonth()));
        tvWeek.setText(getWeek(calendar.getWeek()));
        mYear = calendar.getYear();
        mItems.clear();
        String str = PunchClockUtils.getSomeDay(calendar.getYear(), calendar.getMonth(), calendar.getDay());
        updateAdapter(str, getDataTime(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay()));
    }
}
