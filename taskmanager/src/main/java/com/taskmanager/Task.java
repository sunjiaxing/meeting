package com.taskmanager;

/**
 * 需要执行的任务  封装
 * Created by sun on 2015/12/1.
 */
public abstract class Task implements Runnable {

    private boolean cancel = false;
    private int mAction;
    private String className;
    private Object returnData;
    private String errorMessage;
    private boolean needCallBack = true;
    private TaskResultListener resultListener;

    public void setResultListener(TaskResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public Task(int action, String className) {
        this.mAction = action;
        this.className = className;
    }

    public Task(int action) {
        this.mAction = action;
    }

    public void setReturnData(Object returnData) {
        this.returnData = returnData;
    }

    public void setNeedCallBack(boolean needCallBack) {
        this.needCallBack = needCallBack;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public void run() {
        try {
            doBackground();
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        if (!cancel && resultListener != null && needCallBack) {
            if (errorMessage != null) {
                // 失败
                resultListener.onFail(className, mAction, errorMessage);
            } else {
                // 成功
                resultListener.onSuccess(className, mAction, returnData);
            }
        }
    }

    protected abstract void doBackground() throws Exception;
}
