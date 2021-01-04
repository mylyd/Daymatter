package com.mobo.daymatter.framents;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.manager.DrinkManager;

/**
 * 引导进度
 *
 * @性别设置页
 */
public class CalculateGenderFragment extends BaseFragment implements View.OnClickListener {

    private FrameLayout maleLayout;
    private FrameLayout femaleLayout;
    private TextView bgMale;
    private TextView bgFemale;

    public static CalculateGenderFragment newInstance() {
        CalculateGenderFragment fragment = new CalculateGenderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_calculate_gender;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViewById(R.id.layout_male).setOnClickListener(this);
        findViewById(R.id.layout_female).setOnClickListener(this);
        bgMale = findViewById(R.id.tv_bg_male);
        bgFemale = findViewById(R.id.tv_bg_female);
        if (DrinkManager.getDrinkGender().equals(DrinkManager.GENDER_MALE)){
            bgMale.setBackgroundResource(R.drawable.gender_male_selected);
            bgFemale.setBackgroundResource(R.drawable.gender_unselected);
        }else if (DrinkManager.getDrinkGender().equals(DrinkManager.GENDER_FEMALE)){
            bgMale.setBackgroundResource(R.drawable.gender_unselected);
            bgFemale.setBackgroundResource(R.drawable.gender_female_selected);
        }else {
            bgMale.setBackgroundResource(R.drawable.gender_unselected);
            bgFemale.setBackgroundResource(R.drawable.gender_unselected);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.layout_male) {
            bgMale.setBackgroundResource(R.drawable.gender_male_selected);
            bgFemale.setBackgroundResource(R.drawable.gender_unselected);
            DrinkManager.setDrinkGender(DrinkManager.GENDER_MALE);
        } else if (view.getId() == R.id.layout_female) {
            bgMale.setBackgroundResource(R.drawable.gender_unselected);
            bgFemale.setBackgroundResource(R.drawable.gender_female_selected);
            DrinkManager.setDrinkGender(DrinkManager.GENDER_FEMALE);
        }
    }
}
