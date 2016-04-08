package com.sb.meeting.ui;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qiniu.android.storage.UploadManager;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.MD5;
import com.taskmanager.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * 全局 application
 * Created by sun on 2015/12/10.
 */
public class MeetingApp extends Application implements Thread.UncaughtExceptionHandler {

    private static MeetingApp instance;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();
    private UploadManager uploadManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Thread.setDefaultUncaughtExceptionHandler(this);
        initImageLoader();
        ShareSDK.initSDK(this,BonConstants.KEY_SHARE);
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

    /**
     * 获取七牛 云存储 上传manager
     *
     * @return
     */
    public synchronized UploadManager getUploadManager() {
        if (uploadManager == null) {
            uploadManager = new UploadManager();
        }
        return uploadManager;
    }

    /**
     * 初始化imageLoader
     */
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                        // 设置线程的优先级
                .denyCacheImageMultipleSizesInMemory()
                        // 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LRULimitedMemoryCache(2 * 1024 * 1024))
                .diskCache(new LimitedAgeDiskCache(new File(BonConstants.PATH_IMAGE_CACHE), null, new FileNameGenerator() {
                    @Override
                    public String generate(String imageUri) {
                        return MD5.md5Lower(imageUri);
                    }
                }, BonConstants.TIME_TO_CLEAR_IMAGE_CACHE))
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        try {
            // 处理 缓存目录 添加 .nomedia 文件  禁止MediaScanner扫描
            File dir = new File(BonConstants.PATH_COMPRESSED);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, ".nomedia");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 收集设备参数信息
        collectDeviceInfo(this);
        // 保存日志文件
        saveCrashInfo2File(ex);
        // 应用退出
        System.exit(0);
    }

    public static MeetingApp getInstance() {

        return instance;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
                infos.put("osVersion", Build.VERSION.RELEASE);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {

            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     */
    public void saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        LogUtils.writeInFile(sb.toString());
    }
}
