package com.mobo.daymatter;

import androidx.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.mobo.daymatter.manager.ConfigManager;
import com.mobo.daymatter.manager.DrinkManager;
import com.mobo.daymatter.manager.GraySwitch;
import com.mobo.daymatter.manager.SharedPreferenceManager;
import com.mobo.daymatter.tracker.FirebaseTracker;

import ad.mobo.mvp.AdManager;
import security.mobo.security.SecurityMgr;

public class MatterApplication extends MultiDexApplication {
    private static MatterApplication application;
    private static boolean hadLaunched;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SharedPreferenceManager.getInstance().init(this);
        FirebaseTracker.get().init(this);
        FacebookSdk.setAutoLogAppEventsEnabled(true);
        // 暂时只有admob广告
        AdManager.getInstance().initContext(this)
                .initAdmob(getString(R.string.gms_app_id))
                .initFacebookAd().updateStrategy();
        SecurityMgr.init(this); // 安全问题
        // 灰度请i去
        GraySwitch.getInstance().requestSwitch(this);
        // 更新全局灰度
        ConfigManager.getInstance().requestConfig(this);
        DrinkManager.init();
        ScreenOnReceiver.registerScreenOn(this);
    }

    public static void setHadLaunched(boolean b) {
        hadLaunched = b;
    }

    public static boolean getHadLaunched() {
        return hadLaunched;
    }

    public static MatterApplication getApplication() {
        return application;
    }
}
