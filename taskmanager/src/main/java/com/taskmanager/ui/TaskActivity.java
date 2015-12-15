package com.taskmanager.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.taskmanager.TaskKey;

/**
 * 所有Activity的父类
 * Created by sun on 2015/12/10.
 *
 * 子类 实现 onTaskSuccess方法 处理任务成功之后的操作
 * 如需 手动处理任务失败情况，则需手动重写 onTaskFail方法
 */
public abstract class TaskActivity extends FragmentActivity {

    private BroadcastReceiver receiver;
    protected Toast mToast;// 统一toast

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(getPackageName(), getLocalClassName() + " onReceive ");
                int taskAction = intent.getIntExtra(TaskKey.KEY_TASK_ACTION, -1);
                if (intent.getBooleanExtra(TaskKey.KEY_TASK_SUCCESS, false)) {
                    // 任务执行成功
                    onTaskSuccess(taskAction, intent.getSerializableExtra(TaskKey.KEY_RETURN_DATA));
                } else {
                    onTaskFail(taskAction, intent.getStringExtra(TaskKey.KEY_ERROR_MESSAGE));
                }
            }
        };
        registerReceiver(receiver, new IntentFilter(this.getLocalClassName()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    protected abstract void onTaskSuccess(int action, Object data);

    protected void onTaskFail(int action, String errorMessage) {
        showToast(errorMessage);
        Log.e(getLocalClassName() + action, errorMessage);
    }

    /**
     * 显示小提示
     */
    public void showToast(String text) {
        if (mToast != null) {
            mToast.setText(text);
            mToast.show();
        } else {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
    }
}
