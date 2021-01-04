package com.mobo.daymatter.adcontroller;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

import com.mobo.daymatter.utils.MoboLogger;

/**
 * 点击控制辅助类，控制事件拦截
 * created by tliu
 * 2019-09-26
 */
public class ClickConfigHelper {
    private static final String TAG = "ClickConfigHelper";
    private static final int MESSAGE_CLICK_TIME_CHANGE = 1;

    private boolean clickable = false;

    public boolean isAdmobAd = false;

    public boolean isFacebookClickable = false;

    public boolean isAdmobClickable = false;

    private boolean isClickInShortTime = false;

    public boolean isInSpecialArea = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_CLICK_TIME_CHANGE) {
                isClickInShortTime = false;
            }
        }
    };


    public ClickConfigHelper() {
    }

    /**
     * 拦截触摸事件下发，以达到控制点击效果
     * @param ev 事件对象
     * @return 是否拦截
     */
    public boolean interceptTouchEvent(MotionEvent ev) {
        if (isAdmobAd) {
            clickable = isAdmobClickable;
        } else {
            clickable = isFacebookClickable;
        }
        MoboLogger.debug(TAG, "is admob ="+ isAdmobAd +", clickable ="+ clickable);

        // 可点击时，判断是否重复点击
        if (clickable) {
            return handleTouchEvent(ev);
        }
        if (isInSpecialArea) {
            MoboLogger.warn(TAG, "in special area");
            return handleTouchEvent(ev);
        }

        MoboLogger.warn(TAG, "unable click area");
        return true;
    }

    /**
     * 短时间内不可重复点击处理
     * @param ev 触摸对象
     * @return 是否需要拦截
     */
    private boolean handleTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (isClickInShortTime) {
            MoboLogger.warn(TAG, "wait for a moment");
            return true;
        } else {
            MoboLogger.warn(TAG, "dispatch click");
            changeClickState();
            return false;
        }
    }

    /**
     * 在防止重复点击区域触发点击或者触摸事件后，进行状态改变，以达到下次在短时间内拦截事件的目的；
     * 并在时间段之外去掉限制状态
     */
    private void changeClickState() {
        isClickInShortTime = true;
        Message message = new Message();
        message.what = MESSAGE_CLICK_TIME_CHANGE;
        handler.sendMessageDelayed(message, 2000);
    }
}
