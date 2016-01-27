package com.taskmanager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 任务管理
 * Created by sun on 2015/12/1.
 */
public class TaskManager {

    public static boolean DEBUG = true;

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    private static BlockingQueue<Task> taskQueue = new LinkedBlockingDeque<>();

    public static BlockingQueue<Task> getTaskQueue() {
        return taskQueue;
    }

    /**
     * 并发执行任务
     *
     * @param task    任务
     * @param context 上下文
     */
    private static void pushTask(Task task, Context context) {
        try {
            taskQueue.put(task);
            context.startService(new Intent(context, TaskService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 放入到任务队列中排队执行
     *
     * @param task    任务
     * @param context 上下文
     */
    private static void pushTaskWithQueue(Task task, Context context) {
        try {
            taskQueue.put(task);
            Intent intent = new Intent(context, TaskService.class);
            intent.putExtra(TaskKey.KEY_WITH_SINGLE, true);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 并发执行任务
     *
     * @param task     任务
     * @param activity activity
     */
    public static void pushTask(Task task, Activity activity) {
        if (task != null) {
            task.setClassName(activity.toString());
            pushTask(task, activity.getBaseContext());
        }
    }

    /**
     * 放入到任务队列中排队执行
     *
     * @param task     任务
     * @param activity activity
     */
    public static void pushTaskWithQueue(Task task, Activity activity) {
        if (task != null) {
            task.setClassName(activity.toString());
            pushTaskWithQueue(task, activity.getBaseContext());
        }
    }

    /**
     * 并发执行任务
     *
     * @param task     任务
     * @param fragment fragment
     */
    public static void pushTask(Task task, Fragment fragment) {
        pushTask(task, fragment.getActivity());
    }

    /**
     * 放入到任务队列中排队执行
     *
     * @param task     任务
     * @param fragment fragment
     */
    public static void pushTaskWithQueue(Task task, Fragment fragment) {
        pushTaskWithQueue(task, fragment.getActivity());
    }

    public static void clearAllTask() {

    }

    public static void clearTaskByClassName(String className) {

    }
}




