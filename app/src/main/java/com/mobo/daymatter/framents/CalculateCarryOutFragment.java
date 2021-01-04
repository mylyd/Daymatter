package com.mobo.daymatter.framents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobo.daymatter.R;
import com.mobo.daymatter.manager.DrinkManager;

/**
 * 引导进度
 *
 * @设置完成
 */
public class CalculateCarryOutFragment extends BaseFragment {

    public static CalculateCarryOutFragment newInstance() {
        CalculateCarryOutFragment fragment = new CalculateCarryOutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_calculate_carry_out;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView water = findViewById(R.id.tv_water);
        DrinkManager.setDrinkTotal(DrinkManager.getDrinkBodyWeight() * 33);
        water.setText(DrinkManager.getDrinkTotal() + " " +
                (DrinkManager.getUnitType() ? DrinkManager.UNIT_ML : DrinkManager.UNIT_FLOZ));
    }

}
