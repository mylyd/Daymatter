package com.mobo.daymatter.manager;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mobo.daymatter.network.CommonCallback;
import com.mobo.daymatter.network.RetrofitManager;
import com.mobo.daymatter.network.beans.GrayItem;
import com.mobo.daymatter.network.beans.GrayRequest;
import com.mobo.daymatter.utils.BaseUtils;
import com.mobo.daymatter.utils.MoboLogger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GraySwitch {
    private static final GraySwitch ourInstance = new GraySwitch();
    private static final String GRAY_ITEM_DATA = "gray_item_data";

    public static GraySwitch getInstance() {
        return ourInstance;
    }

    private GrayItem grayItem;

    private GraySwitch() {
    }

    // 确认本地sp已经准备好了；并且当线上数据请求失败时，从本地获取
    public GraySwitch init(Context context) {
        SharedPreferenceManager.getInstance().init(context);
        if (grayItem != null) {
            return this;
        }
        String data = SharedPreferenceManager.getInstance().getString(GRAY_ITEM_DATA, "");
        if (TextUtils.isEmpty(data)) {
            return this;
        }
        grayItem = new Gson().fromJson(data, GrayItem.class);
        return this;
    }

    public void update(GrayItem item) {
        if (item == null) {
            return;
        }
        grayItem = item;
        SharedPreferenceManager.getInstance().putStringAndCommit(GRAY_ITEM_DATA, new Gson().toJson(item));
    }

    public boolean isSwitchOn(String key, boolean defValue) {
        if (grayItem == null) {
            return defValue;
        }
        return grayItem.isSwitchOn(key, defValue);
    }

    public void requestSwitch(final Context context) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("did", BaseUtils.getStringMD5(BaseUtils.getAndroidId(context)));
        //Log.d(TAG, "getDefault " + Locale.getDefault().toString());
        if (Locale.getDefault().toString().equals("zh_CN_#Hans")) {
            queryParams.put("lc", "zh_CN");
        } else {
            queryParams.put("lc", Locale.getDefault().toString());
        }
        queryParams.put("pn", context.getPackageName());
        queryParams.put("appvc", BaseUtils.getVersionCode(context) + "");
        queryParams.put("appvn", BaseUtils.getVersionName(context) + "");
        queryParams.put("os", "android");
        queryParams.put("chn", "ofw");
        queryParams.put("avn", String.valueOf(Build.VERSION.SDK_INT));
        RetrofitManager.INSTANCE.getRequest(GrayRequest.class).getSwitchConfig(queryParams).enqueue(new CommonCallback<GrayItem>() {
            @Override
            public void onResponse(GrayItem response) {
                if (response != null) {
                    MoboLogger.warn("GraySwitch", response.toString());
                    update(response);
                } else {
                    MoboLogger.warn("GraySwitch", "failed");
                    init(context);
                }
            }

            @Override
            public void onFailure(Throwable t, boolean isServerUnavailable) {
                MoboLogger.warn("GraySwitch", "failed");
                init(context);
            }
        });
    }
}
