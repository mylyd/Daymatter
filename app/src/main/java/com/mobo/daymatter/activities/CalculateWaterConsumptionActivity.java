package com.mobo.daymatter.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobo.daymatter.R;
import com.mobo.daymatter.adapter.BaseFragmentAdapter;
import com.mobo.daymatter.framents.CalculateBodyWeightFragment;
import com.mobo.daymatter.framents.CalculateCarryOutFragment;
import com.mobo.daymatter.framents.CalculateGenderFragment;
import com.mobo.daymatter.framents.CalculateLoadingFragment;
import com.mobo.daymatter.framents.CalculateSleepingTimeFragment;
import com.mobo.daymatter.framents.CalculateWakeUpTimeFragment;
import com.mobo.daymatter.framents.ViewPagerFragment;
import com.mobo.daymatter.views.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : ydli
 * @description 计算饮水量流程
 * @time :
 */
public class CalculateWaterConsumptionActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private NoScrollViewPager viewPager;
    private ImageView ivProgress;
    private List<Fragment> fragments;
    private MyAdapter mAdapter;
    private FrameLayout frameLayout;
    private String[] titles;
    private TextView tvNext;
    private TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_water_consumption);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        title = findViewById(R.id.tv_title);
        viewPager = findViewById(R.id.viewpager);
        ivProgress = findViewById(R.id.viewpager_progress);
        tvNext = findViewById(R.id.viewpager_next);
        tvNext.setOnClickListener(this);
        tvSkip = findViewById(R.id.tv_skip);
        tvSkip.setOnClickListener(this);
        frameLayout = findViewById(R.id.title_layout);
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(CalculateGenderFragment.newInstance());
        fragments.add(CalculateBodyWeightFragment.newInstance());
        fragments.add(CalculateWakeUpTimeFragment.newInstance());
        fragments.add(CalculateSleepingTimeFragment.newInstance());
        fragments.add(CalculateLoadingFragment.newInstance());
        fragments.add(CalculateCarryOutFragment.newInstance());
        mAdapter = new MyAdapter(getSupportFragmentManager());

        titles = new String[]{
                getString(R.string.drink_gender),
                getString(R.string.drink_body_weight),
                getString(R.string.drink_wake_up_time),
                getString(R.string.drink_sleeping_time),
                "",
                getString(R.string.drink_water_goals)};
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    title.setText(titles[position]);
                    tvNext.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                    tvNext.setText(R.string.next);
                    ivProgress.setImageResource(R.drawable.ic_progress_1_4);
                    tvSkip.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    title.setText(titles[position]);
                    tvNext.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                    tvNext.setText(R.string.next);
                    ivProgress.setImageResource(R.drawable.ic_progress_2_4);
                    tvSkip.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    title.setText(titles[position]);
                    tvNext.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                    tvNext.setText(R.string.next);
                    ivProgress.setImageResource(R.drawable.ic_progress_3_4);
                    tvSkip.setVisibility(View.VISIBLE);
                } else if (position == 3) {
                    title.setText(titles[position]);
                    tvNext.setText(R.string.start_calculating);
                    tvNext.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                    ivProgress.setImageResource(R.drawable.ic_progress_4_4);
                    tvSkip.setVisibility(View.VISIBLE);
                } else if (position == 4) {
                    frameLayout.setVisibility(View.GONE);
                    tvNext.setVisibility(View.GONE);
                    ivProgress.setVisibility(View.GONE);
                    getWindow().getDecorView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(5);
                        }
                    }, 2000);
                } else if (position == 5) {
                    tvSkip.setVisibility(View.GONE);
                    ivProgress.setVisibility(View.GONE);
                    title.setText(titles[position]);
                    tvNext.setText(R.string.ok);
                    tvNext.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
        title.setText(titles[0]);
    }


    /**
     * @param fragment
     * @param requestCode
     */
    public static void startNew(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), CalculateWaterConsumptionActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @param activity
     * @param requestCode
     */
    public static void startNew(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CalculateWaterConsumptionActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_skip:
            case R.id.viewpager_next:
                if (viewPager == null) {
                    return;
                }
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        //性别
                        viewPager.setCurrentItem(1);
                        break;
                    case 1:
                        //体重
                        viewPager.setCurrentItem(2);
                        break;
                    case 2:
                        //起床
                        viewPager.setCurrentItem(3);
                        break;
                    case 3:
                        //睡觉
                        viewPager.setCurrentItem(4);
                        break;
                    case 5:
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager == null){
            finish();
            return;
        }
        switch (viewPager.getCurrentItem()) {
            case 1:
                //体重
                viewPager.setCurrentItem(0);
                break;
            case 2:
                //起床
                viewPager.setCurrentItem(1);
                break;
            case 3:
                //睡觉
                viewPager.setCurrentItem(2);
                break;
            default:
                finish();
                break;
        }
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }
}
