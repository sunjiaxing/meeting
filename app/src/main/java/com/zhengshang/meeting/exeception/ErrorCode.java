package com.zhengshang.meeting.exeception;

public class ErrorCode {
	/**
	 * 匹配错误码
	 * 
	 * @param errorCode
	 * @return
	 */
	public static String matchError(int errorCode) {
		String str = null;
		switch (errorCode) {
		case 1000:
			str = "服务器异常。";
			break;
		default:
			str = "系统异常,请稍后重试！";
			break;
		}
		return str;
	}
}
