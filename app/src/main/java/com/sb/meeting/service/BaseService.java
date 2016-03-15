package com.sb.meeting.service;

import android.content.Context;

import com.sb.meeting.dao.ConfigDao;
import com.sb.meeting.remote.BaseRO;

/**
 * base Service
 * 
 * @author sun 2014年5月7日16:50:27
 */
public class BaseService {
	protected Context mContext;
	protected ConfigDao configDao;
	private BaseRO baseRO;

	public BaseService(Context context) {
		this.mContext = context;
		this.configDao = ConfigDao.getInstance(context);
		this.baseRO = new BaseRO(context);
	}
}
