package com.zhengshang.meeting.common;

/**
 * Created by sun on 2015/12/10.
 */
public class BonConstants {
    /**
     * 根路径
     */
    public static final String PATH_ROOT = Utils.getSdcardPath() + "/meeting/";
    /**
     * 图片缓存路径
     */
    public static final String PATH_IMAGE_CACHE = PATH_ROOT + "Cache/";
    /**
     * 应用首页图片缓存路径
     */
    public static final String PATH_INIT_IMAGE_CACHE = PATH_ROOT + "init/";


    /**
     * 数据库名
     */
    public static final String DATABASE_NAME = "content.db";
    /**
     * 数据库版本
     */
    public static final int DATABASE_VERSION = 1;

    /**
     * 顶部图片替换时间
     */
    public static final long TIME_TO_CHANGE_HEAD_PIC = 3000;// 3秒
    /**
     * 删除logo图片的间隔时间 5天
     */
    public static final long TIME_TO_DELETE_LOGO = 432000000;// 5天
    /**
     * 点击不同新闻栏目时 刷新的时间间隔
     */
    public static final long TIME_TO_REFESH_DATA = 600000;// 10分钟


    /**
     * 获取新闻的长度
     */
    public static final int LIMIT_GET_NEWS = 20;
    /**
     * 默认显示的新闻栏目的数量
     */
    public static final int LIMIT_DEFAULT_NEWS_TYPE = 5;
    /**
     * 剩余logo图片的数量 剩余10张
     */
    public static final int LIMIT_LOGO_LAST = 10;


    /**
     * 跳转到新闻详情
     */
    public static final int REQUEST_TO_DETAIL = 0x1008;

    //    public static final String SERVER_URL = "http://192.168.1.103:8080/";
    //    public static final String SERVER_URL = "http://192.168.1.28:8080/api/";
    public static final String SERVER_URL = "http://192.168.1.27:8888/api/";
    public static String SLASH = "/";
}
