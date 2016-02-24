package com.zhengshang.meeting.common;

import com.taskmanager.TaskKey;

/**
 * 任务 识别 action
 * Created by sun on 2015/12/10.
 */
public interface TaskAction extends TaskKey {
    int ACTION_SAVE_NEWS_TYPE = 0;
    int ACTION_GET_NEWS_TYPE = 1;
    int ACTION_GET_NEWS_FROM_DB = 2;
    int ACTION_REFRESH_NEWS = 3;
    int ACTION_LOAD_MORE_NEWS = 4;
    int ACTION_GET_NEWS_DETAIL = 5;
    int ACTION_GET_NEWS_TEMPLATE = 6;
    int ACTION_LOGIN = 7;
    int ACTION_UPDATE_NEWS_CHANNEL = 8;
    int ACTION_SEND_COMMENT = 9;
    int ACTION_SEND_REPLY = 10;
    int ACTION_GET_USER_INFO = 11;
    int ACTION_GET_NEWS_SUBJECT = 12;
    int ACTION_SET_NEWS_READ_STATE = 13;
    int ACTION_FAVORITE_NEWS = 14;
    int ACTION_GET_FAVORITE_LIST = 15;
    int ACTION_DELETE_FAVORITE = 16;
    int ACTION_DELETE_ALL_FAVORITE = 17;
    int ACTION_GET_SYSTEM_IMAGE = 18;
    int ACTION_GET_GOODS_CATEGORY = 19;
    int ACTION_GET_VALID_TIME = 20;
    int ACTION_GET_GOODS_DETAIL = 21;

}
