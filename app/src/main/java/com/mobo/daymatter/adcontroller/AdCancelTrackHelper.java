package com.mobo.daymatter.adcontroller;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 检测是否需要取消track事件
 * create by lt 2019-10-08
 */
public class AdCancelTrackHelper {
    private int minDis;
    private float x, y;
    private boolean isCanceled = false;

    public AdCancelTrackHelper(Context context) {
        this.minDis = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 当收到取消事件或者移动时，取消打点
     * @param event 事件
     */
    public void handleTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            isCanceled = false;
            x = event.getRawX();
            y = event.getRawY();
        } else if (event.getActionMasked() == MotionEvent.ACTION_CANCEL) {
            isCanceled = true;
        } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(event.getRawX() - x) > minDis
                    || Math.abs(event.getRawY() - y) > minDis) {
                isCanceled = true;
            }
        }
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}
