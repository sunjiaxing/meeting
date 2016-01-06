package com.zhengshang.meeting.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.view.WindowManager;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sun on 2015/12/10.
 */
public class Utils {
    /**
     * json 数组非空判断
     *
     * @param array
     * @return true 表示空，false表示非空
     */
    public static boolean isEmpty(JSONArray array) {
        if (array != null && array.length() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 字符串非空判断
     *
     * @param str
     * @return true 表示空，false表示非空
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 集合非空判断
     *
     * @param <T>
     * @return true 表示空，false表示非空
     */
    public static <T> boolean isEmpty(List<T> t) {
        if (t != null && t.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 数组非空判断
     *
     * @param <T>
     * @return true 表示空，false表示非空
     */
    public static <T> boolean isEmpty(T[] t) {
        if (t != null && t.length > 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 480;
        } else {
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            return manager.getDefaultDisplay().getWidth();
        }
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 481;
        } else {
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            return manager.getDefaultDisplay().getHeight();
        }
    }

    /**
     * 获取SD卡路径,一般为/sdcard
     *
     * @return
     */
    public static String getSdcardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
        return (int) dpValue;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        }
        return (int) pxValue;
    }

    /**
     * 格式化时间 返回类型【xx月xx日 xx:xx】
     *
     * @param time
     * @return
     */
    public static String formateTime(long time, boolean hasYear) {
        SimpleDateFormat sdf = null;
        if (hasYear) {
            sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("MM月dd日 HH:mm:ss");
        }
        // sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.format(new Date(time));
    }

    /**
     * 网络是否连接
     *
     * @param context
     * @return true表示网络连接
     */
    public static boolean isNetworkValidate(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            // mobile 3G Data Network
            NetworkInfo.State mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState();
            // wifi
            NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .getState();
            // 判断3G网络和wifi网络是否都未连接
            if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
                return true;
            }
            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取版本号的code值
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        int versionCode = 1;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(versionCode);
    }

    /**
     * 保存图片
     *
     * @param url
     */
    public static void saveImageFromUrl(String url, long time) {
        try {
            if (!isEmpty(url)) {
                // 获取图片名称
                String fileName = String.valueOf(time);
                // 创建根目录
                File fileDir = new File(BonConstants.PATH_INIT_IMAGE_CACHE);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                // 创建文件
                File file = new File(fileDir, fileName);
                // 判断文件是否存在
                if (!file.exists()) {
                    // 不存在 下载
                    URL destUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) destUrl
                            .openConnection();
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    FileOutputStream fos = new FileOutputStream(file);
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)) {
                        fos.flush();
                    }
                    fos.close();
                    is.close();
                    // 回收资源
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long formateTimeFromPhpToJava(long time) {
        return Long.parseLong(time + "000");
    }
}
