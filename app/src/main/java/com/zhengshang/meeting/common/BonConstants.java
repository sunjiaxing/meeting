package com.zhengshang.meeting.common;

/**
 * 常量类
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

    public static final String PATH_TAKE_PHOTO = PATH_ROOT + "photo/";


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
     * 保存用户登录状态时间
     */
    public static final long TIME_TO_SAVE_LOGIN_STATE = 604800000;// 7天


    /**
     * 获取新闻的长度
     */
    public static final int LIMIT_GET_NEWS = 20;
    /**
     * 默认显示的新闻栏目的数量
     */
    public static final int LIMIT_DEFAULT_NEWS_TYPE = 5;
    /**
     * 评论最多显示字数
     */
    public static final int LIMIT_COMMENT_LENGTH = 50;
    /**
     * 剩余logo图片的数量 剩余10张
     */
    public static final int LIMIT_LOGO_LAST = 10;


    public static final int LENGTH_SHOW_SUMMARY = 50;

    public static final int AVATART_WHITH = 400;


    /**
     * 跳转到新闻详情
     */
    public static final int REQUEST_TO_DETAIL = 0x1008;

    //    public static final String SERVER_URL = "http://192.168.1.103:8080/";
    //    public static final String SERVER_URL = "http://192.168.1.28:8080/api/";
    public static final String SERVER_URL = "http://192.168.1.27:8888/api/";
    public static String SLASH = "/";
    public static final String WEB_RESOURCE_URL = "http://192.168.1.27/";
    public static final String ROOT_USER_AVATAR_URL = WEB_RESOURCE_URL + "phpsso_server/uploadfile/avatar/";

    public enum UserAvatarType {
        TYPE_30X30("30X30.jpg"),
        TYPE_45X45("45x45.jpg"),
        TYPE_90x90("90x90.jpg"),
        TYPE_180x180("180x180.jpg");

        private String type;

        UserAvatarType(String value) {
            this.type = value;
        }

        public String getValue() {
            return type;
        }
    }

    public enum BottomMenuSelected {
        NEWS, GOODS
    }
}
