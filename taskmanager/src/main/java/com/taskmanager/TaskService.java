package com.taskmanager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 执行任务的Service
 * Created by sun on 2015/12/9.
 */
public class TaskService extends Service implements TaskResultListener {
    private ExecutorService singlePool;
    private ExecutorService morePool;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 创建线程池 根据需要 可自行定义
        singlePool = Executors.newSingleThreadExecutor();
        morePool = Executors.newFixedThreadPool(5);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent != null) {
                Task task = TaskManager.getTaskQueue().take();
                if (task != null) {
                    task.setResultListener(this);
                    if (intent.getBooleanExtra(TaskKey.KEY_WITH_QUEUE, false)) {
                        morePool.execute(task);
                    } else {
                        singlePool.execute(task);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onSuccess(String className, int action, Object returnData) {
        Intent intent = new Intent(className);
        intent.putExtra(TaskKey.KEY_TASK_SUCCESS, true);
        intent.putExtra(TaskKey.KEY_TASK_ACTION, action);
        intent.putExtra(TaskKey.KEY_RETURN_DATA, returnData != null ? (Serializable) returnData : null);
        sendBroadcast(intent);
    }

    @Override
    public void onFail(String className, int action, String errorMsg) {
        Intent intent = new Intent(className);
        intent.putExtra(TaskKey.KEY_TASK_SUCCESS, false);
        intent.putExtra(TaskKey.KEY_TASK_ACTION, action);
        intent.putExtra(TaskKey.KEY_ERROR_MESSAGE, errorMsg);
        sendBroadcast(intent);
    }
}
