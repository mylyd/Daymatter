package com.mobo.daymatter.manager;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mobo.daymatter.network.CommonCallback;
import com.mobo.daymatter.network.RetrofitManager;
import com.mobo.daymatter.network.beans.ConfigItem;
import com.mobo.daymatter.network.beans.ConfigRequest;
import com.mobo.daymatter.utils.BaseUtils;
import com.mobo.daymatter.utils.MoboLogger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConfigManager {
    private static final ConfigManager ourInstance = new ConfigManager();

    public static ConfigManager getInstance() {
        return ourInstance;
    }

    private ConfigManager() {
    }

    private static final String CONFIG_ITEM_DATA = "config_item_data";

    private ConfigItem configItem;

    // 确认本地sp已经准备好了；并且当线上数据请求失败时，从本地获取
    public ConfigManager loadFromLocal(Context context) {
        SharedPreferenceManager.getInstance().init(context);
        if (configItem != null) {
            return this;
        }
        String data = SharedPreferenceManager.getInstance().getString(CONFIG_ITEM_DATA, "");
        if (TextUtils.isEmpty(data)) {
            return this;
        }
        configItem = new Gson().fromJson(data, ConfigItem.class);
        return this;
    }

    public void update(ConfigItem item) {
        if (item == null) {
            return;
        }
        configItem = item;
        SharedPreferenceManager.getInstance().putStringAndCommit(CONFIG_ITEM_DATA, new Gson().toJson(configItem));
    }

    public ConfigItem getConfig() {
        return configItem;
    }

    public ConfigItem.ConfigBean getConfigBean() {
        return configItem == null ? null : configItem.getData();
    }

    public void requestConfig(final Context context) {
        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("packageName", context.getPackageName());
        queryParams.put("language", Locale.getDefault().getLanguage());
        queryParams.put("country", Locale.getDefault().getCountry());
        queryParams.put("versionCode", String.valueOf(BaseUtils.getVersionCode(context)));
        queryParams.put("sdkVersion", String.valueOf(Build.VERSION.SDK_INT));
        RetrofitManager.INSTANCE.getRequest(ConfigRequest.class).getConfigRequest(queryParams)
                .enqueue(new CommonCallback<ConfigItem>() {
                    @Override
                    public void onResponse(ConfigItem response) {
                        if (response != null) {
                            MoboLogger.debug("ConfigManager", response.toString());
                            update(response);
                        } else {
                            MoboLogger.debug("ConfigManager", "failed");
                            loadFromLocal(context);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, boolean isServerUnavailable) {
                        MoboLogger.debug("ConfigManager", "failed");
                        loadFromLocal(context);
                    }
                });
    }
}
