package com.taskmanager;

/**
 * 任务执行结果 监听
 * Created by sun on 2015/12/1.
 */
public interface TaskResultListener {
    /**
     * 执行成功
     * @param className  要发送广播的action
     * @param action  任务的编号
     * @param returnData  返回数据
     */
    void onSuccess(String className, int action, Object returnData);

    /**
     * 执行失败
     * @param className 要发送广播的action
     * @param action 任务编号
     * @param errorMsg  异常提示信息
     */
    void onFail(String className, int action, String errorMsg);
}
