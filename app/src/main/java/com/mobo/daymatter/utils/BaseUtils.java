package com.mobo.daymatter.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.gms.ads.formats.AdChoicesView;
import com.google.gson.JsonElement;
import com.mobo.daymatter.interfaces.GrayKey;
import com.mobo.daymatter.interfaces.MatterConstant;
import com.mobo.daymatter.manager.ConfigManager;
import com.mobo.daymatter.manager.GraySwitch;
import com.mobo.daymatter.network.CommonCallback;
import com.mobo.daymatter.network.RetrofitManager;
import com.mobo.daymatter.network.beans.ConfigItem;
import com.mobo.daymatter.network.beans.Feedback;

import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class BaseUtils {
    private BaseUtils(){}

    /**
     * 获取android唯一识别码
     * @param context 上下文
     * @return 返回唯一识别码
     */
    public static String getAndroidId(Context context) {
        String androidId = "null";
        try {
            androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
        }

        if (androidId == null || "".equals(androidId)) {
            androidId = "null";
        }

        return getStringMD5(androidId);
    }

    /**
     * 对字符串进行加密
     * @param plainText 待加密字符串
     * @return 加密后字符串
     */
    public static String getStringMD5(String plainText) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
        } catch (Exception e) {
            return null;
        }
        return encodeHex(md.digest());

    }

    /**
     * 二进制加密为16进制转换
     * @param data 待转换二进制
     * @return 加密后字符串
     */
    public static String encodeHex(byte[] data) {
        if (data == null) {
            return null;
        }
        final String HEXES = "0123456789abcdef";
        int len = data.length;
        StringBuilder hex = new StringBuilder(len * 2);
        for (int i = 0; i < len; ++i) {
            hex.append(HEXES.charAt((data[i] & 0xF0) >>> 4));
            hex.append(HEXES.charAt((data[i] & 0x0F)));
        }
        return hex.toString();
    }

    //版本名
    public static String getVersionName(Context context) {
        PackageInfo info = getPackageInfo(context);
        if (info == null) {
            return "";
        }
        return info.versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        PackageInfo info = getPackageInfo(context);
        if (info == null) {
            return 0;
        }
        return info.versionCode;
    }

    //获取app版本信息
    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentCountry() {
        Locale locale = Locale.getDefault();
        return locale.getCountry();
    }

    public static String getCurrentLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage();
    }

    public static String getChannelId(Context context) {
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null && appInfo.metaData != null) {
                String channelId =
                        appInfo.metaData.get("CYOU_CHANNEL").toString();
                if (channelId != null) {
                    return channelId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getWindowHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 从drawable中获取图片
     * @param drawable
     * @return
     */
    public static Bitmap getBitmap(Drawable drawable) {
        if (drawable == null || drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap result = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(result);
        drawable.draw(canvas);
        return result;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, int resourceId) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (context.getResources().getDimension(resourceId) * scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dips2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 提交反馈信息
     *
     * @param email
     * @param suggestion
     */
    public static void requestFeedBack(Context context, String email, String suggestion, CommonCallback<JsonElement> commonCallback) {
        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("pkgname", context.getPackageName());
        paramMap.put("version", getVersionName(context));
        paramMap.put("vercode", String.valueOf(getVersionCode(context)));
        paramMap.put("did", getAndroidId(context));
        paramMap.put("deviceModel", android.os.Build.MODEL);
        paramMap.put("os", android.os.Build.VERSION.RELEASE);
        paramMap.put("language", getCurrentLanguage());
        paramMap.put("country", getCurrentCountry());
        paramMap.put("channelId", getChannelId(context));
        paramMap.put("resolution", String.valueOf(getWindowHeight(context)));
        paramMap.put("cpu", android.os.Build.BOARD);
        paramMap.put("email", email);
        paramMap.put("message", suggestion);

        RetrofitManager.INSTANCE.getRequest(Feedback.class).postFeedBack(paramMap).enqueue(commonCallback);
    }

    /**
     * 获取是否需要升级, 返回需要升级的提示，不需要返回空
     * @param context
     * @return
     */
    public static String isNeedUpdate(Context context) {
        if (!GraySwitch.getInstance().isSwitchOn(GrayKey.UPDATE_NOTICE_CONTROL, false)) {
            return "";
        }
        ConfigItem item = ConfigManager.getInstance().getConfig();
        if (item != null && item.getData() != null && item.getData().getVersioncode() > BaseUtils.getVersionCode(context)) {
            return item.getData().getUpdateTint();
        }
        return "";
    }

    // 检查x，y坐标是不是在view内
    public  static boolean checkTouchInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }
        if (view.getVisibility() != View.VISIBLE) {
            return false;
        }
        int[] local = new int[2];
        view.getLocationOnScreen(local);
        int w = view.getWidth();
        int h = view.getHeight();
        if (view instanceof ViewGroup && ((ViewGroup) view).getChildAt(0) instanceof AdChoicesView) {
            if (w < 1) {
                w = -48;
            }
            if (h < 1) {
                h = 48;
            }
        }
        RectF rect = new RectF(w < 0 ? (local[0] + w) : local[0],
                local[1],
                w > 0 ? (local[0] + w) : local[0],
                local[1] + h);
        return rect.contains(x, y);
    }

    // 检查x，y坐标是不是在view内
    public  static boolean checkTouchInView(View view, MotionEvent event) {
        if (view == null) {
            return false;
        }
        if (view.getVisibility() != View.VISIBLE) {
            return false;
        }
        int[] local = new int[2];
        view.getLocationOnScreen(local);
        RectF rect = new RectF(local[0], local[1], local[0] + view.getWidth(), local[1] + view.getHeight());
        return rect.contains(event.getRawX(), event.getRawY());
    }

    public static ColorDrawable getColorDrawable(Context context, int resId) {
        return new ColorDrawable(context.getResources().getColor(resId));
    }

    public static void setSize(View view, int width, int height) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            params.width = width;
            params.height = height;
        } else {
            params = new ViewGroup.LayoutParams(width, height);
        }
        view.setLayoutParams(params);
    }

    public static ColorDrawable getColorDrawable(Context context, int resId, int width, int height) {
        ColorDrawable drawable = new ColorDrawable(context.getResources().getColor(resId));
        drawable.setBounds(0, 0, width, height);
        return drawable;
    }

    //首字母转小写
    public static String toUpperCaseFirstOne(String s){
        if(!Character.isUpperCase(s.charAt(0))) {
            s = (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
        return s;
    }

    public static void goToGame(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MatterConstant.GAME_URL));
        context.startActivity(intent);
    }
}
