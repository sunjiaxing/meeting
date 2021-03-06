package com.sb.meeting.exeception;

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
            case 1005:
                str = "新闻不存在";
                break;
            case 1006:
                str = "参数不正确";
                break;
            case 1007:
                str = "专题不存在";
                break;
            case 1008:
                str = "已经收藏过了";
                break;
            case 1009:
                str = "手机号码已注册，可直接登录";
                break;
            case 10010:
                str = "验证码不正确";
                break;
            case 10011:
                str = "验证码超时，请重新获取";
                break;
            default:
                str = "系统异常,请稍后重试！";
                break;
        }
        return str;
    }
}
