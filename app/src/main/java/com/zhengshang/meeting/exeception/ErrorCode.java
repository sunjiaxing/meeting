package com.zhengshang.meeting.exeception;

public class ErrorCode {
    /**
     * 匹配错误码
     *
     * @param errorCode 错误码
     * @return
     */
    public static String matchError(int errorCode) {
        String str;
        switch (errorCode) {
            case 1000:
                str = "服务器异常。";
                break;
            case 1001:
                str = "用户尚未注册";
                break;
            case 1002:
                str = "用户名或密码错误";
                break;
            case 1003:
                str = "评论失败，请重试";
                break;
            case 1004:
                str = "评论已被删除";
                break;
            default:
                str = "系统异常,请稍后重试！";
                break;
        }
        return str;
    }
}
