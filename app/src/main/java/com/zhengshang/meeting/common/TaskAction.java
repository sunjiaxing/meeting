package com.zhengshang.meeting.common;

import com.taskmanager.TaskKey;

/**
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

}
