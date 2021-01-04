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
import com.mobo.daymatter.beans.ReminderBean;

/**
 * @author : ydli
 * @description punch dialog 选择颜色列表 && 选择图标列表
 * @time : 19:38
 */
public class PunchColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private TypedArray mTypedArray;
    private Context mContext;
    private int mType;
    private OnColorClickListener mListenerColor;
    private OnIconClickListener mListenerIcon;
    private String[] imageColor ;



    public PunchColorAdapter(TypedArray mTypedArray, Context mContext, int type) {
        this.mTypedArray = mTypedArray;
        this.mContext = mContext;
        this.mType = type;
        if (mTypedArray != null && type == 0){
            imageColor = mContext.getResources().getStringArray(R.array.punch_color);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mType == 0 ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ColorHolder(LayoutInflater.from(mContext).inflate(R.layout.punch_item_color, parent, false));
        } else {
            return new IconHolder(LayoutInflater.from(mContext).inflate(R.layout.punch_item_icon, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ColorHolder) {
            ((ColorHolder) holder).bind(position);
        } else if (holder instanceof IconHolder) {
            ((IconHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return mTypedArray == null ? 0 : mTypedArray.length();
    }

    public class ColorHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.punch_color_item_card_view);
        }

        public void bind(final int position) {
            if (imageColor == null) {
                imageColor = mContext.getResources().getStringArray(R.array.punch_color);
            }
            cardView.setCardBackgroundColor(Color.parseColor(imageColor[position]));
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListenerColor.onItemColorClick(Color.parseColor(imageColor[position]));
                }
            });
        }
    }

    public class IconHolder extends RecyclerView.ViewHolder {
        private ImageView ivView;

        public IconHolder(@NonNull View itemView) {
            super(itemView);
            ivView = itemView.findViewById(R.id.punch_color_item_image_view);
        }

        public void bind(final int position) {
            if (mTypedArray == null) {
                return;
            }
            ivView.setImageDrawable(mTypedArray.getDrawable(position));
            ivView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListenerIcon.onItemIconClick(mTypedArray.getDrawable(position),position);
                }
            });
        }
    }

    public interface OnColorClickListener {
        void onItemColorClick(int color);
    }

    public interface OnIconClickListener {
        void onItemIconClick(Drawable icon,int index);
    }

    public void setOnColorClickListener(OnColorClickListener listener) {
        this.mListenerColor = listener;
    }

    public void setOnIconClickListener(OnIconClickListener listener) {
        this.mListenerIcon = listener;
    }
}
