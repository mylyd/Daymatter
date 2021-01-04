package com.mobo.daymatter.interfaces;

public interface GrayKey {
    // 控制是否展示评分弹框  true：展示，false：不展示，默认ture
    public static final String GRAY_RATE_US_SHOW_CONTROL = "rate_us_show_control";

    // 控制是否弹出升级弹框, 默认false，不强制升级
    String UPDATE_NOTICE_CONTROL = "update_notice_control";

    // 控制是否进行feedback打点
    String FEEDBACK_DATA_UPLOAD_CONTROL = "feedback_data_upload_control";

    /**
     * game图标是否显示
     */
    String EVENT_GAME = "event_game";

    /**
     * 控制纪念日页面下方的原生广告位是否可以点击
     */
    String EVENT_AD_CLICK = "event_ad_click";

    /**
     * 控制打卡页面下方的原生广告位是否可以点击
     */
    String PUNCH_AD_CLICK = "event_ad_click";

    /**
     * 控制设置页面下方的原生广告位是否可以点击
     */
    String SETTING_AD_CLICK = "event_ad_click";
}