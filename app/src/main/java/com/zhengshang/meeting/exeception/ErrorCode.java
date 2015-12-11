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
		case 100010:
		case 20000:
			str = "电话号码已经注册！";
			break;
		case 20001:
			str = "豆粒服务已升级，请重新注册";
			break;
		case 32000:
			str = "已发过申请，请等待好友通过";
			break;
		case 100020:
		case 21000:
			str = "账号或密码错误，请重新输入";
			break;
		case 100021:
			str = "原密码不正确！";
			break;
		case 100022:
			str = "密码修改失败，请稍后重试！";
			break;
		case 100030:
			str = "验证码无效！";
			break;
		case 100040:
			str = "验证码已经发送，请稍等！";
			break;
		case 100041:// 验证码发送不成功，但接口服务正常
			str = "验证码发送失败，请稍后重试！";
			break;
		case 100050:
			str = "电话号码与验证码不一致！";
			break;
		case 100060:
			str = "当前测试版本使用期限已过，请升级正式版。";
			break;
		case 100080:
			str = "服务器异常，请稍后再试。";
			break;
		case 100090:
			str = "评论功能暂时不可用。";
			break;
		case 100091:
			str = "当前新闻不允许评论。";
			break;
		case 100100:
			str = "今天已签到！";
			break;
		case 100110:
			str = "已经有门票,无需重复申请";
			break;
		case 100023:
			str = "您的账号已被冻结,请与豆粒官方联系。";
			break;
		case 100024:
			str = "真实姓名已存在！";
			break;
		case 90000:
			str = "服务器异常。";
			break;
		case 27000:
			str = "获取验证码超时,请稍后重试！";
			break;
		case 100200:
			str = "您上报的价格太高了！";
			break;
		case 100201:
			str = "您上报的价格太低了！";
			break;
		case 100210:
			str = "您已经申请过该试用品!";
			break;
		case 100211:
			str = "产品已过期,不能申请！";
			break;
		case 70002:
			str = "资源已经被收藏过了!";
			break;
		case 30000:
			str = "你和TA已经不是好友了!";
			break;
		case 31000:
			str = "你和TA已经是好友了！";
			break;
		case 51002:
			str = "资源已经被删除,不能转发!";
			break;
		case 59995:
			str = "ta的动态已删除，不能进行评论";
			break;
		case 59996:
			str = "评论已被删除，不能进行回复";
			break;
		case 70003:
			str = "所选标签数量超过最大数量！";
			break;
		case 70004:
			str = "该群不存在！";
			break;
		case 70005:
			str = "定位失败，请打开GPS并重试！";
			break;
		case 70006:
			str = "该群名称已被使用！";
			break;
		case 70007:
			str = "匹配通讯录好友失败";
			break;
		case 70008:
			str = "你感兴趣的太多了哦";
			break;
		// JMessage 的错误信息
		case 800012:
		case 800013:
			str = "您的账号已登出，请重新登录";
			break;
		case 899004:
		case 801004:
			str = "密码错误";
			break;
		case 803001:
		case 803002:
			str = "发送消息失败，系统网络异常";
			break;
		case 803004:
			str = "发送失败，此群已被删除";
			break;
		case 803005:
			str = "您已被请出此群，不能发送消息";
			break;
		case 898030:
			str = "系统繁忙，稍后重试";
			break;
		case 800009:
		case 871104:
			str = "服务器返回错误";
			break;
		case 871102:
		case 871201:
			str = "请求超时，请检查您的网络";
			break;
		default:
			str = "系统异常,请稍后重试！";
			break;
		}
		return str;
	}
}
