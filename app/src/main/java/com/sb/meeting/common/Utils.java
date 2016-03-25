package com.sb.meeting.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sb.meeting.remote.IParam;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用辅助类
 * Created by sun on 2015/12/10.
 */
public class Utils {
    /**
     * json 数组非空判断
     *
     * @param array json 数组
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
     * @param str 字符串
     * @return true 表示空，false表示非空
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 集合非空判断
     *
     * @param <T> list集合
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
     * @param <T> 数组
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
     *
     * @param context
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
     *
     * @param context
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
     *
     * @param context
     * @param dpValue
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
     *
     * @param context
     * @param pxValue
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
     * @param hasYear
     * @return
     */
    public static String formateTime(long time, boolean hasYear) {
        SimpleDateFormat sdf;
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

    /**
     * 格式化评论时间
     *
     * @param millsDate 时间的毫秒值
     * @return
     */
    public static String formateCommentTime(long millsDate) {
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Date formateDate = new Date(millsDate);
        SimpleDateFormat sdf;
        String headStr;
        if (today == formateDate.getDate()) {
            headStr = "今天";
        } else if (today - 1 == formateDate.getDate()) {
            headStr = "昨天";
        } else if (today - 2 == formateDate.getDate()) {
            headStr = "前天";
        } else {
            headStr = "MM-dd";
        }
        sdf = new SimpleDateFormat(headStr + " HH:mm");
        return sdf.format(formateDate);
    }

    /**
     * 格式化 时间
     *
     * @param time    时间
     * @param pattern 格式
     * @return
     */
    public static String formateTime(long time, String pattern) {
        Date formateDate = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(formateDate);
    }

    /**
     * 获取用户头像链接
     *
     * @param userId 用户id
     * @param type   头像类型
     * @return 头像链接
     */
    public static String getUserAvatar(String userId, BonConstants.UserAvatarType type) {
        if (Utils.isEmpty(userId)) {
            return null;
        }
        try {
            int id = Integer.parseInt(userId);
            return BonConstants.ROOT_USER_AVATAR_URL
                    + (int) Math.ceil(id / 10000d) + BonConstants.SLASH
                    + (int) Math.ceil(id % 10000d / 1000) + BonConstants.SLASH
                    + userId + BonConstants.SLASH + type.getValue();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 截取 新闻 描述
     *
     * @param summary 新闻描述
     * @return
     */
    public static String substringSummary(String summary) {
        if (isEmpty(summary)) {
            return null;
        }
        if (summary.length() > BonConstants.LENGTH_SHOW_SUMMARY) {
            return summary.substring(0, BonConstants.LENGTH_SHOW_SUMMARY);
        }
        return summary;
    }

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     */
    public static boolean checkPhoneValid(String phone) {
        Pattern pattern = Pattern.compile(BonConstants.PhonePattern);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 按 尺寸压缩后 再按质量压缩
     *
     * @param path
     * @return
     */
    public static Bitmap comp(String path) {
        Bitmap image = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            image = BitmapFactory.decodeFile(path, options);
            options.inJustDecodeBounds = false;
            File file = new File(path);
            if (file.length() / 1024 > 1024) {
                options.inSampleSize = 4;
            } else if (file.length() / 1024 > 500) {
                options.inSampleSize = 2;
            }
            int w = options.outWidth;
            // 现在主流手机比较多是 16:9 分辨率，所以高和宽我们设置为
            int hh = 16;
            int ww = 9;

            int newH = w * hh / ww;
            options.outWidth = ww;
            options.outHeight = newH;
            image = BitmapFactory.decodeFile(path, options);
            return compressImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 只按质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) { // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;// 每次都减少5%
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
    }

    /**
     * 保存图片
     *
     * @param b
     * @param strFileName
     * @return
     */
    public static File savePic(Bitmap b, String strFileName) {
        FileOutputStream fos;
        try {
            File dir = new File(BonConstants.PATH_TEMP);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fos = new FileOutputStream(strFileName);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return new File(strFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机号
     *
     * @param c
     */
    public static String getPhoneNumber(Context c) {
        try {
            TelephonyManager tm = (TelephonyManager) c
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String phone = tm.getLine1Number();
            if (phone.startsWith("+86")) {
                phone = phone.replace("+86", "");
            }
            return phone;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换 double科学技术法
     *
     * @param price
     * @return
     */
    public static String parseDouble(double price) {
        return parseDouble(price, "#.00");
    }

    public static String parseDouble(double price, String pattern) {
        DecimalFormat sf = new DecimalFormat(pattern);
        return sf.format(price);
    }

    /**
     * 加载图片
     *
     * @param path      图片路径
     * @param imageView ImageView
     * @param options   option
     */
    public static void displayImage(String path, ImageView imageView, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(
                path.startsWith(IParam.HTTP) ? path : ImageDownloader.Scheme.FILE.wrap(path),
                imageView,
                options);
    }

    /**
     * 加载图片
     *
     * @param path      路径
     * @param imageView ImageView
     * @param options   option
     * @param listener  监听
     */
    public static void displayImage(String path, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(
                path.startsWith(IParam.HTTP) ? path : ImageDownloader.Scheme.FILE.wrap(path),
                imageView,
                options, listener);
    }

    /**
     * 删除图片链接 域名部分
     * @param url
     * @return
     */
    public static String deleteHeadOfUrl(String url) {
        return url.replace(BonConstants.SERVER_URL, "");
    }
}
