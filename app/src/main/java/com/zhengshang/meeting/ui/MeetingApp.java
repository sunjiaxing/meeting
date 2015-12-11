package com.zhengshang.meeting.ui;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.MD5;

import java.io.File;

/**
 * Created by sun on 2015/12/10.
 */
public class MeetingApp extends Application implements Thread.UncaughtExceptionHandler {

    private static MeetingApp instance;
    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        Thread.setDefaultUncaughtExceptionHandler(this);
        initImageLoader();
    }

    /**
     * 初始化imageLoader
     *
     * @author sun
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
                .diskCache(new UnlimitedDiskCache(new File(BonConstants.PATH_IMAGE_CACHE))) // default
                .diskCacheSize(50 * 1024 * 1024)// 缓存目录大小 50M
                .diskCacheFileCount(200)// 缓存文件数量 200
                .diskCacheFileNameGenerator(new FileNameGenerator() {
                    @Override
                    public String generate(String imageUri) {
                        return MD5.md5Lower(imageUri);
                    }
                })// 文件名称 自定义md5
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }

    public static MeetingApp getInstance() {

        return instance;
    }
}
