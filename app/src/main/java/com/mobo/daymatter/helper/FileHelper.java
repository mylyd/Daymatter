package com.mobo.daymatter.helper;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

public class FileHelper {
    private static final String REMINDER_SHARE_PATH = "share-pic";

    public static String getReminderSharePath(Context context) {
        if (context == null) {
            return null;
        }
        return context.getDir(REMINDER_SHARE_PATH, Context.MODE_PRIVATE).getAbsolutePath();
    }

    /**
     * 图片是否存在
     *
     * @param layerPath 图片本地路径
     * @return boolean
     */
    public static boolean isFileExists(String layerPath) {
        if (TextUtils.isEmpty(layerPath)) {
            return false;
        }
        File file = new File(layerPath);
        return file.exists() && file.isFile();
    }
}
