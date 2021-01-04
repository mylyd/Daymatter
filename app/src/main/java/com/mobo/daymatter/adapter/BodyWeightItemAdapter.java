package com.mobo.daymatter.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.manager.DrinkManager;

/**
 * @author : ydli
 * @description 自定义体重选择器
 * @time : 20-6-1 下午3:33
 */
public class BodyWeightItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnItemClickListener mListener;

    public BodyWeightItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return (position % 5 == 0) ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_body_weight_long, parent, false));
        } else {
            return new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_body_weight_short, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        if (DrinkManager.getUnitType()){
            return DrinkManager.ITEM_VIEW_RECYCLERVIEW_SIZE_KG;
        }else {
            return DrinkManager.ITEM_VIEW_RECYCLERVIEW_SIZE_LBS;
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(int position) {
            mListener.onItemColorClick(position);
        }
    }

    public interface OnItemClickListener {
        void onItemColorClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

}
