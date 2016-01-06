package com.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by sun on 2015/12/1.
 */
public class TaskManager {
    private static BlockingQueue<Task> taskQueue = new LinkedBlockingDeque<>();

    public static BlockingQueue<Task> getTaskQueue() {
        return taskQueue;
    }

    /**
     * 并发执行任务
     *
     * @param task
     * @param context
     */
    public static void pushTask(Task task, Context context) {
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
     * @param task
     * @param context
     */
    public static void pushTaskWithQueue(Task task, Context context) {
        try {
            taskQueue.put(task);
            Intent intent = new Intent(context, TaskService.class);
            intent.putExtra(TaskKey.KEY_WITH_QUEUE, true);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pushTask(Task task, Activity activity) {
        if (task != null) {
            task.setClassName(activity.getLocalClassName());
            pushTask(task, activity.getBaseContext());
        }
    }

    public static void pushTaskWithQueue(Task task, Activity activity) {
        if (task != null) {
            task.setClassName(activity.getLocalClassName());
            pushTaskWithQueue(task, activity.getBaseContext());
        }
    }

    public static void clearAllTask() {

    }

    public static void clearTaskByClassName(String className) {

    }
}




