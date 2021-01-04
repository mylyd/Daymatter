package com.mobo.daymatter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.beans.DrinkBean;
import com.mobo.daymatter.beans.PunchClockBean;
import com.mobo.daymatter.beans.PunchHistoryItem;
import com.mobo.daymatter.framents.DrinkFragment;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.utils.PunchClockUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : ydli
 * @time : 20-5-27 下午3:34
 * @description 喝水历史记录Adapter
 */
public class DrinkRecordingAdapter extends RecyclerView.Adapter<DrinkRecordingAdapter.Holder> {
    private Context mContext;
    private List<DrinkBean> drinkBeans = new ArrayList<>();
    private OnItemClickListener listener;

    public DrinkRecordingAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void refresh(List<DrinkBean> items) {
        drinkBeans.clear();
        if (items != null && !items.isEmpty()) {
            for (DrinkBean bean : items) {
                if (isToDay(bean.getTime())) {
                    drinkBeans.add(bean);
                }
            }
        }
        notifyDataSetChanged();
    }

    public List<DrinkBean> getDrinkBeans() {
        return drinkBeans;
    }

    /**
     * 筛选当日数据
     *
     * @param time
     * @return
     */
    public boolean isToDay(long time) {
        return PunchClockUtils.getYYMMDD(System.currentTimeMillis()).equals(PunchClockUtils.getYYMMDD(time));
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.drink_record_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return drinkBeans == null ? 0 : drinkBeans.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView ivRevise;
        private ImageView ivDelete;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_capacity_time);
            ivRevise = itemView.findViewById(R.id.tv_revise);
            ivDelete = itemView.findViewById(R.id.tv_delete);
        }

        private void bind(final int position) {
            if (drinkBeans == null && drinkBeans.isEmpty()) {
                return;
            }
            final DrinkBean items = drinkBeans.get(position);
            textView.setText(String.format(mContext.getString(R.string.drink_item_capacity), String.valueOf(items.getValue()),
                    items.getUnit(),
                    PunchClockUtils.getHHMMSS(items.getTime())));
            ivRevise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemReviseClick(DrinkManager.getDrinkRecordingBean(), items, position);
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemDeleteClick(DrinkManager.getDrinkRecordingBean(), items, position);
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemReviseClick(List<DrinkBean> beans, DrinkBean bean, int position);

        void onItemDeleteClick(List<DrinkBean> beans, DrinkBean bean, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.listener = clickListener;
    }
}
