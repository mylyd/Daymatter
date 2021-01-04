package com.mobo.daymatter.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

public final class GradeUtils {
    private static final String GOOGLE_PLAY_APP_URL_FOR_LAUNCHER = "https://play.google.com/store/apps/details?id=com.mobo.daymatter.dailyreminder";

    public static void gotoGooglePlay(Context context) {
        gotoGooglePlay(context, "", true);
    }

    public static void gotoGooglePlay(Context context, String faildMessage, boolean isShort) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(GOOGLE_PLAY_APP_URL_FOR_LAUNCHER));
            browserIntent.setClassName("com.android.vending",
                    "com.android.vending.AssetBrowserActivity");
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        } catch (Exception e) {
            try {
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(GOOGLE_PLAY_APP_URL_FOR_LAUNCHER));
                context.startActivity(browserIntent2);
            } catch (Exception e1) {
                if (!TextUtils.isEmpty(faildMessage)) {
                    Toast.makeText(context, faildMessage, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
