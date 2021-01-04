package com.mobo.daymatter.adcontroller;

import android.view.MotionEvent;

import com.mobo.daymatter.manager.GraySwitch;
import com.mobo.daymatter.utils.MoboLogger;

import ad.mobo.base.bean.PullKey;
import ad.mobo.base.view.AbsEventIntercepter;
import ad.mobo.base.view.AbsNativeDisplayView;

public class EventController extends AbsEventIntercepter {
    private ClickConfigHelper clickHelper = new ClickConfigHelper();
    private AdCancelTrackHelper cancelTrackHelper;
    private AdClickAreaManager areaManager;
    private boolean isFacebookChoiceArea = false;

    public EventController(AbsNativeDisplayView view, String key, boolean defValue) {
        super(view);
        clickHelper.isAdmobClickable = GraySwitch.getInstance().isSwitchOn(key, defValue);
        clickHelper.isFacebookClickable = clickHelper.isAdmobClickable;
    }

    @Override
    public boolean onInterceptEvent(MotionEvent motionEvent) {
        // mobo 广告不处理
        if (view.getInfo().type.equals(PullKey.MOBOAPPS)) {
            MoboLogger.warn("ClickConfigHelper", "mobo ad not intercept!");
            return false;
        }

        if (isFacebookChoiceArea) {
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                isFacebookChoiceArea = false;
            }
            return false;
        }

        if (cancelTrackHelper == null) {
            cancelTrackHelper = new AdCancelTrackHelper(view.getContext());
        }

        cancelTrackHelper.handleTouchEvent(motionEvent);

        if (areaManager == null) {
            areaManager = new AdClickAreaManager(new AdViewHolder(view.getTitleView(),
                    view.getIconContainer(), view.getContentContainer(),
                    view.getCtaView(), view.getDesView(), view.getTagView(),
                    view.getChoiceContainer()));
            clickHelper.isAdmobAd = view.getInfo().type.contains(PullKey.ADMOB);
        }

        if (MotionEvent.ACTION_DOWN == motionEvent.getActionMasked()) {
            areaManager.begainPoint(motionEvent.getRawX(), motionEvent.getRawY());
            areaManager.checkArea();
            clickHelper.isInSpecialArea = areaManager.isCtaArea() || areaManager.isChoiceArea()
                    || (view.isVedioType() && areaManager.isMediaArea());
        }

        if (MotionEvent.ACTION_UP == motionEvent.getActionMasked()) {
            if (!cancelTrackHelper.isCanceled() && clickHelper.interceptTouchEvent(motionEvent)) {
                return true;
            }
            isFacebookChoiceArea = areaManager.isChoiceArea() && view.getInfo().type.equals(PullKey.FACEBOOK);
        }

        return false;
    }

}
