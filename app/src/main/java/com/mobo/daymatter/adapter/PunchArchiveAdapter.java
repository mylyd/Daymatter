package com.mobo.daymatter.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.PunchClockBean;
import com.mobo.daymatter.beans.PunchClockRecorder;
import com.mobo.daymatter.beans.PunchClockTargetMode;
import com.mobo.daymatter.manager.PunchClockManager;
import com.mobo.daymatter.views.PunchTagView;

import java.util.ArrayList;
import java.util.List;

public class PunchArchiveAdapter extends RecyclerView.Adapter<PunchArchiveAdapter.Holder> {

    private final ArrayList<PunchClockBean> beanList = new ArrayList<>();
    private Context context;
    private PunchArchiveAdapter.OnItemClickListener listener;
    private int[] mColor;
    private int[] mDrawableRes;

    public PunchArchiveAdapter(Context context, PunchArchiveAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        String[] color = context.getResources().getStringArray(R.array.punch_color);
        int len = color.length;
        mColor = new int[len];
        for (int i = 0; i < len; i++) {
            mColor[i] = Color.parseColor(color[i]);
        }

        TypedArray array = context.getResources().obtainTypedArray(R.array.punch_icon);
        mDrawableRes = new int[array.length()];
        for (int i = 0; i < mDrawableRes.length; i++) {
            mDrawableRes[i] = array.getResourceId(i, -1);
        }
        array.recycle();
    }

    public void refresh(List<PunchClockBean> beans) {
        if (beans != null && !beans.isEmpty()) {
            beanList.clear();
            beanList.addAll(beans);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public PunchArchiveAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PunchArchiveAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.punch_archive_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PunchArchiveAdapter.Holder holder, int position) {
        final PunchClockBean bean = beanList.get(position);
        holder.mNameView.setText(bean.getName());
        holder.mTagView.updateView(bean.getColorPos(), mDrawableRes[bean.getBitmapPos()]);
        // 打卡时间设置
        if (bean.getTimeMode() == PunchClockBean.TIME_NONE) {
            holder.mEachTimeView.setText(R.string.matter_punch_each_time_none);
        } else {
            holder.mEachTimeView.setText(context.getString(R.string.matter_punch_each_time, bean.getTime()));
        }
        // 打卡耗费次数获取
        ArrayList<String> dayKeys = PunchClockManager.get().getRecorderKeys(bean);
        if (bean.getRepeatMode() == PunchClockBean.REPEAT_ONE_WEEK) {
            holder.mCountView.setText(context.getString(R.string.matter_punch_weeks, dayKeys == null ? 0 : dayKeys.size()));
        } else {
            holder.mCountView.setText(context.getString(R.string.matter_punch_days, dayKeys == null ? 0 : dayKeys.size()));
        }

        // 总共打卡情况
        ArrayList<PunchClockRecorder> recorders = PunchClockManager.get().getRecorderByMode(bean);
        if (bean.getTargetMode().getMode() == PunchClockTargetMode.MODE_COUNT) {
            if (bean.getTimeMode() == PunchClockBean.REPEAT_ONE_DAY) {
                holder.mTotalView.setText(context.getString(R.string.matter_punch_total_day, recorders == null ? 0 : recorders.size(), bean.getTargetMode().getCount()));
            } else if (bean.getTimeMode() == PunchClockBean.REPEAT_ONE_WEEK) {
                holder.mTotalView.setText(context.getString(R.string.matter_punch_total_week, recorders == null ? 0 : recorders.size(), bean.getTargetMode().getCount()));
            } else {
                holder.mTotalView.setText(context.getString(R.string.matter_punch_total, recorders == null ? 0 : recorders.size()));
            }
        } else if (bean.getTargetMode().getMode() == PunchClockTargetMode.MODE_TIMES) {
            if (bean.getTimeMode() == PunchClockBean.REPEAT_ONE_DAY) {
                holder.mTotalView.setText(context.getString(R.string.matter_punch_total_day_minute, recorders == null ? 0 : recorders.size() * bean.getTime(), bean.getTargetMode().getTimes()));
            } else if (bean.getTimeMode() == PunchClockBean.REPEAT_ONE_WEEK) {
                holder.mTotalView.setText(context.getString(R.string.matter_punch_total_week_minute, recorders == null ? 0 : recorders.size() * bean.getTime(), bean.getTargetMode().getTimes()));
            } else {
                holder.mTotalView.setText(context.getString(R.string.matter_punch_total_minute, recorders == null ? 0 : recorders.size()));
            }
        } else {
            holder.mTotalView.setText(context.getString(R.string.matter_punch_total, recorders == null ? 0 : recorders.size()));
        }

        if (listener == null) {
            return;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(bean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private PunchTagView mTagView;
        private TextView mNameView, mEachTimeView, mTotalView, mCountView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.punch_name);
            mTagView = itemView.findViewById(R.id.tag_view);
            mEachTimeView = itemView.findViewById(R.id.punch_each_minute);
            mCountView = itemView.findViewById(R.id.punch_times);
            mTotalView = itemView.findViewById(R.id.punch_total_times);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PunchClockBean bean);
    }
}