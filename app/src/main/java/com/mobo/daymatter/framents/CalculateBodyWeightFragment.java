package com.mobo.daymatter.framents;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.adapter.BodyWeightItemAdapter;
import com.mobo.daymatter.dialog.NumPickerDialog;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.manager.PagerSnapLayoutManager;
import com.mobo.daymatter.views.BodyWeightPickerView;

/**
 * 引导进度
 *
 * @体重设置页
 */
public class CalculateBodyWeightFragment extends BaseFragment {

    private TextView posOrder;
    private TextView negOrder;
    private TextView tvNum;
    private NumPickerDialog numPickerDialog;

    public static CalculateBodyWeightFragment newInstance() {
        CalculateBodyWeightFragment fragment = new CalculateBodyWeightFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_calculate_body_weight;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        posOrder = findViewById(R.id.pos_order);
        negOrder = findViewById(R.id.neg_order);
        tvNum = findViewById(R.id.tv_num);

        numPickerDialog = new NumPickerDialog(getContext());

        tvNum.setText(DrinkManager.getDrinkBodyWeight() +
                (DrinkManager.getUnitType() ? DrinkManager.UNIT_KG : DrinkManager.UNIT_LBS));
        if (DrinkManager.getUnitType()) {
            UnitSwitch(0,false);
        } else {
            UnitSwitch(1,false);
        }
    }

    private void initListener() {
        posOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kg
                UnitSwitch(0,true);
            }
        });
        negOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lbs
                UnitSwitch(1,true);
            }
        });
        findViewById(R.id.set_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numPickerDialog != null && !numPickerDialog.isShowing()) {
                    numPickerDialog.show(DrinkManager.getDrinkBodyWeight());
                }
            }
        });

        numPickerDialog.setOnTimeValueClickListener(new NumPickerDialog.OnNumValueClickListener() {
            @Override
            public void onNumValueClick(int oldValue, int newValue) {
                tvNum.setText(String.format("%s%s", newValue,
                        DrinkManager.getUnitType() ? DrinkManager.UNIT_KG : DrinkManager.UNIT_LBS));
                DrinkManager.setDrinkBodyWeight(newValue);
            }
        });
    }

    private void UnitSwitch(int unit,boolean isSwitch) {
        if (unit == 0) {
            //kg
            if (isSwitch){
                DrinkManager.LbsToKg();
            }
            posOrder.setBackgroundResource(R.drawable.order_selected);
            posOrder.setTextColor(Color.parseColor("#111213"));
            negOrder.setBackground(null);
            negOrder.setTextColor(Color.parseColor("#9B9B9D"));
            DrinkManager.setDrinkUnit(DrinkManager.UNIT_ML);
            tvNum.setText(DrinkManager.getDrinkBodyWeight() +
                    (DrinkManager.getUnitType() ? DrinkManager.UNIT_KG : DrinkManager.UNIT_LBS));
            posOrder.setEnabled(false);
            negOrder.setEnabled(true);
        } else if (unit == 1) {
            //lbs
            if (isSwitch){
                DrinkManager.KgToLbs();
            }
            negOrder.setBackgroundResource(R.drawable.order_selected);
            negOrder.setTextColor(Color.parseColor("#111213"));
            posOrder.setBackground(null);
            posOrder.setTextColor(Color.parseColor("#9B9B9D"));
            DrinkManager.setDrinkUnit(DrinkManager.UNIT_LBS);
            tvNum.setText(DrinkManager.getDrinkBodyWeight() +
                    (DrinkManager.getUnitType() ? DrinkManager.UNIT_KG : DrinkManager.UNIT_LBS));
            posOrder.setEnabled(true);
            negOrder.setEnabled(false);
        }
        numPickerDialog.setNumPickerMinMax(DrinkManager.getUnitType());
    }
}
