package com.sb.meeting.common;

/**
 * 常量类
 * Created by sun on 2015/12/10.
 */
public class BonConstants {
    public static final Boolean DEBUG = true;
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
     * 临时目录
     */
    public static final String PATH_TEMP = PATH_ROOT + "temp/";
    /**
     * 图片压缩之后的目录
     */
    public static final String PATH_COMPRESSED = PATH_ROOT + "compressed/";


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

    public static final long TIME_TO_CLEAR_IMAGE_CACHE = 604800000;// 7天


    /**
     * 获取新闻的长度
     */
    public static final int LIMIT_GET_NEWS = 20;

    public static final int LIMIT_GET_GOODS = 3;

    public static final int LIMIT_GET_COMPANY = 4;

    public static final int LIMIT_GET_STUDENT = 10;

    public static final int LIMIT_GET_PRODUCT = 1000;

    public static final int LIMIT_GET_PUBLISHED_GOODS = 10;

    public static final int LIMIT_GET_ATTENTION_GOODS = 10;
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
     * shareSDK分享使用的appKey
     **/
    public static final String KEY_SHARE = "106207c3ea504";
    /**
     * shareSDK短信验证码使用的appKey
     */
    public static final String KEY_SMS = "1155c3ae971fa";
    public static final String SECRET_SMS = "cdfa9cd205af7e35750df8ea47b6dd4c";

    public static final String PhonePattern = "^1[3|4|5|7|8][0-9]\\d{8}$";
//    public static final String PhonePattern = "^0{0,1}(13[0-9]|15[3-9]|15[0-2]|18[0-9]|17[5-8]|14[0-9]|170|171)[0-9]{8}$";

    // public static final String SERVER_URL = "http://192.168.1.111:8080/";
//    public static final String SERVER_URL = "http://192.168.1.111:8080/api/";
    public static final String SERVER_URL = "http://192.168.1.27:8888/sample-api/";
    //    public static final String SERVER_URL = "http://192.168.1.27:8888/api/";
    public static String SLASH = "/";
    public static final String WEB_RESOURCE_URL = "http://192.168.1.27/";
    public static final String ROOT_USER_AVATAR_URL = WEB_RESOURCE_URL + "phpsso_server/uploadfile/avatar/";

    public static final String HEAD_IMAGE = "http://7xspim.com2.z0.glb.qiniucdn.com/";

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
        NEWS, GOODS, YP, USER
    }
}
