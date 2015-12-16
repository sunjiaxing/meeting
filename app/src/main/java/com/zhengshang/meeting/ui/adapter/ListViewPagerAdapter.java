package com.zhengshang.meeting.ui.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.zhengshang.meeting.ui.fragment.NewsPagerItemFragment;


/**
 * viewpager适配器
 * 
 * @author sun
 * 
 */
public class ListViewPagerAdapter extends FragmentPagerAdapter {

	private List<NewsPagerItemFragment> fragmentList = null;

	public ListViewPagerAdapter(FragmentManager childFragmentManager) {
		super(childFragmentManager);
	}

	/**
	 * 设置数据
	 * 
	 * @param fragments
	 */
	public void setData(List<NewsPagerItemFragment> fragments) {
		this.fragmentList = fragments;
	}

	@Override
	public int getCount() {
		return fragmentList != null ? fragmentList.size() : 0;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

//	@Override
//	public int getItemPosition(Object object) {
//
//		return PagerAdapter.POSITION_NONE;
//	}
}
