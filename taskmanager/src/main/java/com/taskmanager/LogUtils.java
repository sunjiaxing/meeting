package com.taskmanager;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 日志 打印
 * Created by sun on 2016/1/22.
 */
public class LogUtils {

    private static final String TAG = "TaskManager";
    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "taskmanager/log";

    public static void e(String error) {
        Log.e(TAG, error);
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    /**
     * 日志写入文件中
     *
     * @param msg 消息
     */
    public static void writeInFile(String msg) {
        writeInFile(msg, ROOT_PATH, "log.m");
    }

    /**
     * 日志写入文件中
     *
     * @param msg      消息
     * @param fileDir  目录
     * @param fileName 文件名
     */
    public static void writeInFile(String msg, String fileDir, String fileName) {
        if (!TaskManager.DEBUG) {
            return;
        }
        try {
            File dir = new File(fileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(msg.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
