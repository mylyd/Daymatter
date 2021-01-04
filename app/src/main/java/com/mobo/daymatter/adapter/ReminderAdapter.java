package com.mobo.daymatter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.LeftBean;
import com.mobo.daymatter.beans.ReminderBean;
import com.mobo.daymatter.utils.ReminderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页提醒列表，适配器
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.Holder>{
    private static final int FIRST = 0;
    private static final int OTHER = 1;
    private final ArrayList<ReminderBean> beanList = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public ReminderAdapter(Context context, List<ReminderBean> beans, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        if (beans != null && !beans.isEmpty()) {
            beanList.addAll(beans);
        }
    }

    public void add(ReminderBean bean) {
        if (bean != null) {
            beanList.add(bean);
            notifyItemInserted(beanList.size() - 1);
        }
    }

    public void refresh(List<ReminderBean> beans) {
        if (beans != null && !beans.isEmpty()) {
            beanList.clear();
            beanList.addAll(beans);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new FirstHolder(LayoutInflater.from(context).inflate(R.layout.reminder_item_first, parent, false));
        } else {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.reminder_item_normal, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(context, beanList.get(position));
        holder.setOnItemClickListener(beanList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? FIRST : OTHER;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        protected TextView mTitleTint, mTintDay;

        public Holder(@NonNull View itemView) {
            super(itemView);
            mTitleTint = itemView.findViewById(R.id.tint_left);
            mTintDay = itemView.findViewById(R.id.tint_day);
        }

        protected void bind(Context context, ReminderBean bean) {
            LeftBean temp = ReminderUtil.getLeftBean(context, bean);
            mTitleTint.setText(temp.getTitle());
            mTintDay.setText(temp.getDay() + "");
        }

        protected void setOnItemClickListener(final ReminderBean bean, final OnItemClickListener listener) {
            if (listener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(bean);
                    }
                });
            }
        }
    }

    public static class FirstHolder extends Holder {
        protected TextView mTintTarget;
        public FirstHolder(@NonNull View itemView) {
            super(itemView);
            mTintTarget = itemView.findViewById(R.id.tint_target);
        }

        @Override
        protected void bind(Context context, ReminderBean bean) {
            LeftBean temp = ReminderUtil.getLeftBean(context, bean);
            mTitleTint.setText(temp.getTitle());
            mTintDay.setText(temp.getDay() + "");
            mTintTarget.setText(temp.getTarget());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ReminderBean bean);
    }
}
