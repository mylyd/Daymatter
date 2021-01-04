package com.mobo.daymatter.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.FileProvider;

import com.mobo.daymatter.R;
import com.mobo.daymatter.helper.FileHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareUtils {
    private static final String TEXT = "text/plain";

    /**
     * Share Image
     */
    private static final String IMAGE = "image/*";

    /**
     * Share Video
     */
    private static final String VIDEO = "video/*";

    /**
     * Share File
     */
    private static final String FILE = "*/*";

    public static void shareVedio(Context context, File file) {
        share(context, VIDEO, file);
    }

    public static void shareImage(Context context, File file) {
        share(context, IMAGE, file);
    }

    public static void shareImage(Context context, Bitmap bitmap) {
        share(context, bitmap, context.getString(R.string.share_drinking_history), "HistoryRecord" + ".png");
    }

    public static void shareFile(Context context, File file) {
        share(context, FILE, file);
    }

    public static void share(Context context, String type, File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context, "com.mobo.daymatter.dailyreminder.fileprovider", file);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        }
        context.startActivity(Intent.createChooser(intent, type));

        context.startActivity(intent);
    }

    /**
     * @param context    上下文
     * @param bitmap     bitmap
     * @param shareTitle 分享标题
     * @param path       分享内容存储本地文件名
     */
    public static void share(Context context, Bitmap bitmap, String shareTitle, String path) {
        if (bitmap == null) {
            return;
        }
        File file = null;
        try {
            file = saveReminderBitmap(context, bitmap, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file == null){
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(IMAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context, "com.mobo.daymatter.dailyreminder.fileprovider", file);
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        }
        context.startActivity(Intent.createChooser(intent, shareTitle));
    }

    public static void share(Context context, String title, String shareContent) {
        if (TextUtils.isEmpty(shareContent) || TextUtils.isEmpty(title)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(TEXT);
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static Bitmap viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        view.destroyDrawingCache();
        return bitmap;
    }

    public static File saveReminderBitmap(Context context, Bitmap bitmap, String fileName) throws IOException {
        String path = FileHelper.getReminderSharePath(context);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(path + File.separator + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        out.flush();
        out.close();
        return file;
    }
}
