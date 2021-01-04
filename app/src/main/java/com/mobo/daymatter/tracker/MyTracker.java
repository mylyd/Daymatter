package com.mobo.daymatter.tracker;

/**
 * @author : ydli
 * @description track字段
 * @time : 14:50
 */
public interface MyTracker {
    // 显示event页面
    String EVENT_SHOW = "event_show";

    ////在event页面点击添加事件	//
    String EVENT_CLICK_ADD = "event_click_add";

    //在event页面点击任何一个事件
    String EVENT_CLICK_EVENT = "event_click_event";

    //在添加event页面点击保存
    String ADDEVENT_SAVE = "addevent_save";

    //在事件详情页点击保存图片
    String EVENTDETAIL_SAVE = "eventdetail_save";

    //在事件详情页点击分享
    String EVENTDETAIL_SHARE = "eventdetail_share";

    //展示push提醒
    String EVENT_PUSH_REMIND_SHOW = "event_push_remind_show";

    //点击push提醒
    String EVENT_PUSH_REMIND_CLICK = "event_push_remind_click";

    //点击导航栏的checkin
    String CLICK_CHECKIN = "click_checkin";

    //点击导航栏的event
    String CLICK_EVENT = "click_event";

    //点击导航栏的设置
    String CLICK_SETTING = "click_setting";

    //点击导航栏的calendar
    String CLICK_CALENDAR = "click_calendar";

    //点击导航栏的drink
    String DRINK_TAB_CLICK = "drink_tab_click";

    //点击添加打卡任务
    String CHECK_IN_CLICK_ADD = "check_in_click_add";

    //在添加打卡任务页点击保存
    String ADDTASK_CLICK_SAVE = "addtask_click_save";

    //点击打卡
    String TASK_CLICK_CHECKIN = "task_click_checkin";

    //确定打卡弹框中点击确认
    String CHECKIN_CLICK_OK = "checkin_click_ok";

    //设置页关闭push提醒
    String SETTING_CLICK_PUSHREMIND_OFF = "setting_click_pushremind_off";

    //设置页打开push提醒
    String SETTING_CLICK_PUSHREMIND_ON = "setting_click_pushremind_on";

    //设置页点击用户反馈
    String SETTING_CLICK_FEEDBACK = "setting_click_feedback";

    //设置页点击给我们评分
    String SETTING_CLICK_RATEUS = "setting_click_rateus";

    //设置页点击关于我们
    String SETTING_CLICK_ABOUTUS = "setting_click_aboutus";

    //event页面的game 图标展示
    String EVENT_GAME_SHOW = "event_game_show";

    //点击event页面的game图标
    String EVENT_GAME_CLICK = "event_game_click";

    //展示checkin页面的game图标
    String CHECKIN_GAME_SHOW = "checkin_game_show";

    // 点击checkin页面的game图标
    String CHECKIN_GAME_CLICK = "checkin_game_click";

    // 展示setting页面的game图标
    String SETTING_GAME_SHOW = "setting_game_show";

    // 点击setting页面的game图标
    String SETTING_GAME_CLICK = "setting_game_click";

    // 点击喝水按钮
    String DRINKWATER_CLICK = "drinkwater_click";

    // 展示喝水插页广告
    String DRINKWATER_AD_SHOW = "drinkwater_ad_show";


}
