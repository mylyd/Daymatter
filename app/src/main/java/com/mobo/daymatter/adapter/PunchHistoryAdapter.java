package com.mobo.daymatter.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.PunchHistoryItem;
import com.mobo.daymatter.utils.PunchClockUtils;

import java.util.ArrayList;

/**
 * 打卡历史记录
 */
public class PunchHistoryAdapter extends RecyclerView.Adapter<PunchHistoryAdapter.Holder> {
    private Context mContext;
    private ArrayList<PunchHistoryItem> historyItems = new ArrayList<>();
    private boolean isPosOrder;
    private String dataDay;

    public PunchHistoryAdapter(Context mContext) {
        this.mContext = mContext;
        isPosOrder = true;
    }

    public void refresh(ArrayList<PunchHistoryItem> items,String getDataTime) {
        this.dataDay = getDataTime;
        historyItems.clear();
        if (items != null && !items.isEmpty()) {
            historyItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void refresh(boolean order) {
        if (isPosOrder != order) {
            isPosOrder = order;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.punch_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (!isPosOrder) {
            position = historyItems.size() - position - 1;
        }
        PunchHistoryItem item = historyItems.get(position);
        holder.mPunchTimeTopView.setText(PunchClockUtils.getHHMM(item.getTime()));
        holder.mPunchTimeEndView.setText(PunchClockUtils.getHHMM(item.getTime() + item.getUsedTime() * 60 * 1000));
        holder.mPunchContentView.setText(mContext.getString(R.string.matter_punch_history_time, item.getName(), item.getUsedTime()));
        if (!TextUtils.isEmpty(dataDay)){
            holder.mPunchDataView.setText(dataDay);
        }
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private TextView mPunchTimeTopView, mPunchContentView,mPunchTimeEndView,mPunchDataView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mPunchContentView = itemView.findViewById(R.id.punch_content);
            mPunchTimeTopView = itemView.findViewById(R.id.punch_time_top);
            mPunchTimeEndView = itemView.findViewById(R.id.punch_time_end);
            mPunchDataView = itemView.findViewById(R.id.punch_data);
        }
    }
}
