package com.mobo.daymatter.framents;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.mobo.daymatter.R;

/**
 * 引导进度
 * @Loading设置页
 */
public class CalculateLoadingFragment extends BaseFragment {

    private ImageView ivLoading;

    public static CalculateLoadingFragment newInstance() {
        CalculateLoadingFragment fragment = new CalculateLoadingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_calculate_loading;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ivLoading = findViewById(R.id.iv_loading);
        ivLoading.setAnimation(StartAnimator());
    }

    private RotateAnimation StartAnimator(){
        RotateAnimation rotate  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(2000);//设置动画持续周期
        rotate.setRepeatCount(-1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        //rotate.setStartOffset(10);//执行前的等待时间
        return rotate;
    }
}
