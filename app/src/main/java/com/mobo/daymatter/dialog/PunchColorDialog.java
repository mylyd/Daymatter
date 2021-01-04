package com.mobo.daymatter.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.adapter.PunchColorAdapter;
import com.mobo.daymatter.utils.BaseUtils;

/**
 * @author : ydli
 * @description 新增打卡界面 颜色选择与图标选择 @type = 0,颜色选择界面 ，type = 1,图标选择界面
 * @time : 19:38
 */
public class PunchColorDialog extends BaseDialog {
    private TextView tvTitle;
    private int mType;
    private Context mContext;
    private OnColorClickListener mListenerColor;
    private OnIconClickListener mListenerIcon;
    private RecyclerView recyclerView;

    public PunchColorDialog(Context context, int Type) {
        //弹框从底部弹出时会被虚拟按键挡住一部分，dialog_soft_input可以解决此问题
        super(context, R.style.dialog_soft_input);
        this.mType = Type;
        this.mContext = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_punch_color);

        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.bottom_dialog_anim_style);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(true);

        //设置dialog横向占比为全屏
        //window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
    }

    private void initView() {
        FrameLayout ffLayout = findViewById(R.id.fragment_layout);
        ViewGroup.LayoutParams params = ffLayout.getLayoutParams();
        tvTitle = findViewById(R.id.tv_title);
        recyclerView = findViewById(R.id.recycler);
        if (mType == 0) {//color
            params.height = BaseUtils.dips2px(mContext, 200);
            tvTitle.setText(R.string.matter_punch_select_color);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
            PunchColorAdapter adapter = new PunchColorAdapter(mContext.getResources().obtainTypedArray(R.array.punch_color)
                    , mContext, mType);
            recyclerView.setAdapter(adapter);
            adapter.setOnColorClickListener(new PunchColorAdapter.OnColorClickListener() {
                @Override
                public void onItemColorClick(int color) {
                    mListenerColor.onItemColorClick(color);
                }
            });
        } else if (mType == 1) {//icon
            params.height = BaseUtils.dips2px(mContext, 410);
            tvTitle.setText(R.string.matter_punch_select_icon);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
            PunchColorAdapter adapter = new PunchColorAdapter(mContext.getResources().obtainTypedArray(R.array.punch_icon),
                    mContext, mType);
            recyclerView.setAdapter(adapter);
            adapter.setOnIconClickListener(new PunchColorAdapter.OnIconClickListener() {
                @Override
                public void onItemIconClick(Drawable icon, int index) {
                    mListenerIcon.onItemIconClick(icon, index);
                }
            });
        }
        ffLayout.setLayoutParams(params);
    }

    public interface OnColorClickListener {
        void onItemColorClick(int color);
    }

    public interface OnIconClickListener {
        void onItemIconClick(Drawable icon, int index);
    }

    public void setOnColorClickListener(OnColorClickListener listener) {
        this.mListenerColor = listener;
    }

    public void setOnIconClickListener(OnIconClickListener listener) {
        this.mListenerIcon = listener;
    }

}
