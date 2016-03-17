package com.sb.meeting.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sb.meeting.common.MessageType;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.ui.MeetingApp;
import com.sb.meeting.ui.activity.NewsDetailActivity_;
import com.sb.meeting.ui.activity.TestActivity_;
import com.sb.meeting.ui.activity.WelcomeActivity_;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class PushServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[PushServiceReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server..
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[PushServiceReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[PushServiceReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[PushServiceReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[PushServiceReceiver] 用户点击打开了通知");
            clickNotification(context, bundle);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[PushServiceReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[PushServiceReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[PushServiceReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    /**
     * 用户点击通知
     *
     * @param context context
     * @param bundle  bundle
     */
    private void clickNotification(Context context, Bundle bundle) {
        try {
            JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
            if (json.has(IParam.TYPE)) {
                String type = json.getString(IParam.TYPE);
                if (MessageType.TYPE_NEWS_NOTIFICATION.equals(type)) {
                    // 新闻推送  打开新闻
                    NewsDetailActivity_.intent(context)
                            .flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .extra(IParam.NEWS_ID, json.getInt(IParam.NEWS_ID))
                            .extra(IParam.TITLE, "新闻详情")
                            .start();
                } else if (MessageType.TYPE_GOODS_CHECK_RESULT.equals(type)) {
                    // 物品审核结果 消息通知

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
