package com.mobo.daymatter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mobo.daymatter.R;
import com.mobo.daymatter.adapter.BaseFragmentAdapter;
import com.mobo.daymatter.dialog.DrinkInternalNotificationDialog;
import com.mobo.daymatter.framents.DrinkFragment;
import com.mobo.daymatter.framents.PunchClockFragment;
import com.mobo.daymatter.framents.ReminderFragment;
import com.mobo.daymatter.framents.SettingFragment;
import com.mobo.daymatter.framents.ViewPagerFragment;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.manager.NotificationManager;
import com.mobo.daymatter.tracker.FirebaseTracker;
import com.mobo.daymatter.tracker.MyTracker;

import java.util.ArrayList;

/**
 * 应用首界面
 */
public class DayMatterActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BaseFragmentAdapter<ViewPagerFragment> mAdapter;
    private int[] mTabUnselectedImgResId = {R.drawable.reminder_unselected,
            R.drawable.punch_clock_unselected,
            R.drawable.drink_unselected,
            R.drawable.setting_unselected};
    private int[] mTabSelectedImgResId = {R.drawable.reminder_selected,
            R.drawable.punch_clock_selected,
            R.drawable.drink_selected,
            R.drawable.setting_selected};
    private int[] mTabsId = {R.string.matter_tab_home,
            R.string.matter_tab_record,
            R.string.matter_tab_drink,
            R.string.matter_tab_setting};
    private Class<?>[] mFragmentClzList = {ReminderFragment.class,
            PunchClockFragment.class,
            DrinkFragment.class,
            SettingFragment.class};
    private final int mFirstFragmentIndex = 0;
    private LinearLayout mBG;
    private DrinkInternalNotificationDialog notificationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_matter);
        mTabLayout = findViewById(R.id.tab_cool);
        mViewPager = findViewById(R.id.view_page);
        mBG = findViewById(R.id.activity_background);
        notificationDialog = new DrinkInternalNotificationDialog(this);
        initViewPager();
        initTab();
        adapterNavigationBar((View) mTabLayout.getParent());
        notificationDialog.setOnItemClickListener(new DrinkInternalNotificationDialog.OnItemClickListener() {
            @Override
            public void onItemConfirmClick() {
                mViewPager.setCurrentItem(2);
            }
        });
        if (System.currentTimeMillis() >= DrinkManager.getDrinkNextWaterTime() && NotificationManager.get().isNotificationOn()) {
            notificationDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null && getIntent().getBooleanExtra(NotificationManager.IS_FROM_NOTIFICATION, false)) {
            FirebaseTracker.get().track(MyTracker.EVENT_PUSH_REMIND_CLICK);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mViewPager != null && getIntent() != null) {
            mViewPager.setCurrentItem(2);
        }
    }

    private void initViewPager() {
        try {
            final int size = mFragmentClzList.length;
            ArrayList<ViewPagerFragment> fragments = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                ViewPagerFragment fragment = (ViewPagerFragment) mFragmentClzList[i].newInstance();
                fragment.initVisible(i == mFirstFragmentIndex);
                fragments.add(fragment);
            }
            mAdapter = new BaseFragmentAdapter<>(getSupportFragmentManager(), fragments);
            mViewPager.setAdapter(mAdapter);
            mViewPager.setOffscreenPageLimit(size);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (isHave(mTabsId, R.string.matter_tab_drink)) {
                        //  mBG.setBackgroundResource(position == 2 ? R.drawable.activity_drink_bg : R.drawable.activity_matter_bg);
                    }
                    ((ViewPagerFragment) mAdapter.getItem(position)).onFragmentVisible();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            if (mFirstFragmentIndex != 0) {
                mViewPager.setCurrentItem(mFirstFragmentIndex);
            }

        } catch (IllegalAccessException e) {
        } catch (InstantiationException e) {
        }
    }

    private boolean isHave(int[] array, int index) {
        for (int con : array) {
            if (con == index) {
                return true;
            }
        }
        return false;
    }

    private void initTab() {
        mTabLayout.setupWithViewPager(mViewPager);
        final int size = mTabsId.length;
        for (int i = 0; i < size; i++) {
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.matter_tab_item, null);
            ImageView imageView = viewGroup.findViewById(R.id.image_view);
            imageView.setImageResource(i == mFirstFragmentIndex ? mTabSelectedImgResId[i] : mTabUnselectedImgResId[i]);
            TextView textView = viewGroup.findViewById(R.id.text);
            textView.setText(mTabsId[i]);
            viewGroup.setTag(i);
            mTabLayout.getTabAt(i).setCustomView(viewGroup);
        }
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabChanged(tab.getCustomView(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                onTabChanged(tab.getCustomView(), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void onTabChanged(View view, boolean isSelected) {
        if (view.getTag() instanceof Integer) {
            int index = (int) view.getTag();
            ImageView imageView = view.findViewById(R.id.image_view);
            imageView.setImageResource(isSelected ? mTabSelectedImgResId[index] : mTabUnselectedImgResId[index]);
            // 选中进行打点
            if (isSelected) {
                switch (index) {
                    case 0:
                        FirebaseTracker.get().track(MyTracker.CLICK_EVENT);
                        break;
                    case 1:
                        FirebaseTracker.get().track(MyTracker.CLICK_CHECKIN);
                        break;
                    case 2:
                        FirebaseTracker.get().track(MyTracker.DRINK_TAB_CLICK);
                        break;
                    case 3:
                        FirebaseTracker.get().track(MyTracker.CLICK_SETTING);
                        break;
                }
            }
        }
    }


}
