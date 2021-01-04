package com.mobo.daymatter.adcontroller;

import android.view.View;

import com.mobo.daymatter.utils.BaseUtils;

/**
 * 辅助获取点击区域字符串
 */
public class AdClickAreaHelper {
    private static final String AD_CHOICE = "ad_choice";

    private static final String AD_TITLE = "ad_title";

    private static final String AD_TAG = "ad_tag";

    private static final String AD_DEC = "ad_description";

    private static final String AD_ICON = "ad_icon";

    private static final String AD_IMG = "ad_media";

    private static final String AD_CTA = "ad_cta_button";

    private static final String AD_UNKOWN = "ad_unkown";

    private static final AdClickAreaHelper ourInstance = new AdClickAreaHelper();

    public static AdClickAreaHelper getInstance() {
        return ourInstance;
    }

    private AdClickAreaHelper() {
    }

    private String clickArea = AD_UNKOWN;

    private float x, y;

    public String getClickArea() {
        return clickArea;
    }

    public void initEventPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean checkCta(View view) {
        return checkSomeArea(view, AD_CTA);
    }

    public boolean checkImg(View view) {
        return checkSomeArea(view, AD_IMG);
    }

    public boolean checkTitle(View view) {
        return checkSomeArea(view, AD_TITLE);
    }

    public boolean checkTag(View view) {
        return checkSomeArea(view, AD_TAG);
    }

    public boolean checkDec(View view) {
        return checkSomeArea(view, AD_DEC);
    }

    public boolean checkChoice(View view) {
        return checkSomeArea(view, AD_CHOICE);
    }

    public boolean checkIcon(View view) {
        return checkSomeArea(view, AD_ICON);
    }

    public void resetUnkown() {
        clickArea = AD_UNKOWN;
    }

    private boolean checkSomeArea(View view, String tag) {
        if (BaseUtils.checkTouchInView(view, x, y)) {
            this.clickArea = tag;
            return true;
        }
        return false;
    }

    public boolean isCtaArea() {
        return AD_CTA.equals(clickArea);
    }

    public boolean isChoiceArea() {
        return AD_CHOICE.equals(clickArea);
    }

    public boolean isMediaArea() {
        return AD_IMG.equals(clickArea);
    }
}
