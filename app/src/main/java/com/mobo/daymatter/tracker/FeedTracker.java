package com.mobo.daymatter.tracker;

import android.content.Context;

import com.google.gson.JsonElement;
import com.mobo.daymatter.interfaces.GrayKey;
import com.mobo.daymatter.manager.GraySwitch;
import com.mobo.daymatter.network.CommonCallback;
import com.mobo.daymatter.utils.BaseUtils;
import com.mobo.daymatter.utils.MoboLogger;

public class FeedTracker {
    private static final String SUGGESTION = "feedback report";
    private CommonCallback<JsonElement> commonCallback = new CommonCallback<JsonElement>() {
        @Override
        public void onResponse(JsonElement response) {
            MoboLogger.debug("FeedTracker", "feedback report success!");
        }

        @Override
        public void onFailure(Throwable t, boolean isServerUnavailable) {
            MoboLogger.debug("FeedTracker", "feedback report failed!");
        }
    };

    private FeedTracker() {}

    public static FeedTracker get() {
        return Inner.tracker;
    }

    public void track(Context context, String email, CommonCallback<JsonElement> callback) {
        if (GraySwitch.getInstance().isSwitchOn(GrayKey.FEEDBACK_DATA_UPLOAD_CONTROL, true)) {
            BaseUtils.requestFeedBack(context, email, SUGGESTION, callback != null ? callback : commonCallback);
        }
    }

    public void track(Context context, String email) {
        if (GraySwitch.getInstance().isSwitchOn(GrayKey.FEEDBACK_DATA_UPLOAD_CONTROL, true)) {
            BaseUtils.requestFeedBack(context, email, SUGGESTION, commonCallback);
        }
    }

    private static class Inner {
        private static FeedTracker tracker = new FeedTracker();
    }
}
