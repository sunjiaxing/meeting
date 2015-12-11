package com.zhengshang.meeting.ui.adapter;

import com.zhengshang.meeting.ui.MeetingApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * 用于屏蔽列表快速滑动的适配器父类 如要使用上拉刷新， 滑动加载更多的组件并且想要在快速滑动时不显示图片， 请将自定义适配器继承此类
 * 
 * @author sun
 * 
 */
public abstract class QuickFlingAdapter extends BaseAdapter {
	protected Context context;
	protected LayoutInflater inflater;
	/** 是否快速滑动的标志 */
	protected boolean isQuick = false;

	public QuickFlingAdapter(Context context) {
		this.context = context;
		if (context != null) {
			inflater = LayoutInflater.from(context);
		} else {
			inflater = LayoutInflater.from(MeetingApp.getInstance()
					.getApplicationContext());
		}
	}

	/**
	 * 设置快速滑动状态
	 * 
	 * @param state
	 */
	public void setQuickState(boolean state) {
		this.isQuick = state;
	}
}
