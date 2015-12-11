package com.zhengshang.meeting.exeception;
/**
 * 系统已知异常
 * @author sun
 *
 */
public class AppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppException() {
		super();
	}

	public AppException(int errorCode) {
		super(ErrorCode.matchError(errorCode));
	}

	public AppException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AppException(String detailMessage) {
		super(detailMessage);
	}

	public AppException(Throwable throwable) {
		super(throwable);
	}

}
