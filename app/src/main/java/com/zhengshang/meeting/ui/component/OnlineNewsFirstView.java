package com.zhengshang.meeting.ui.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.vo.NewsVO;

/**
 * 新闻顶部viewpager
 * 
 * @author sun
 * 
 */
public class OnlineNewsFirstView extends FrameLayout implements
		OnPageChangeListener {
	private ChildViewPager viewPager;
	private View[] views = null;
	private ImageView[] dianView = new ImageView[4];
	MyPagerAdapter adapter;
	private Context context;
	private int currentViewID;

	private TextView tvNewsTitle = null;
	private ImageView ivNewsImage = null;
	private TextView tvSubjectLogo = null;
	private LinearLayout dianLayout;
	private RelativeLayout adLayout;
	private List<NewsVO> topList;
	private OnClickFirstView clickFirstView;
	private int count;
	private int index;
	private boolean stop = true;
	private int position;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == position) {
				if (!stop) {
					viewPager.setCurrentItem(index, true);
					index = index + 1 >= count ? 0 : index + 1;
					handler.sendEmptyMessageDelayed(position,
							BonConstants.TIME_TO_CHANGE_HEAD_PIC);
				}
			}
		};
	};
	private AbsListView.LayoutParams params;

	public OnlineNewsFirstView(Context context, int layoutId,
			OnClickFirstView onClickFirstView) {
		super(context);
		if (layoutId != -1) {
			inflate(context, layoutId, this);
			this.context = context;
			int screenW = Utils.getScreenWidth(context);
			// 480 250
			int adH = screenW * 250 / 480;
			params = new AbsListView.LayoutParams(screenW, adH);
			this.clickFirstView = onClickFirstView;
			viewPager = (ChildViewPager) findViewById(R.id.viewPager);
			viewPager.setOnPageChangeListener(this);
			viewPager.setOnSingleTouchListener(new ChildViewPager.OnSingleTouchListener() {
				@Override
				public void onSingleTouch() {
					if (clickFirstView != null) {
						clickFirstView.onClickView(topList.get(currentViewID));
					}
				}
			});
			dianLayout = (LinearLayout) findViewById(R.id.dianLayout);
			dianLayout.setVisibility(View.GONE);
			dianView[0] = (ImageView) findViewById(R.id.image1);
			dianView[1] = (ImageView) findViewById(R.id.image2);
			dianView[2] = (ImageView) findViewById(R.id.image3);
			dianView[3] = (ImageView) findViewById(R.id.image4);
			adLayout = (RelativeLayout) findViewById(R.id.adLayout);
		}
	}

	/**
	 * 开始滚动
	 */
	public void startScroll(int position) {
		if (stop) {
			stop = false;
			this.position = position;
			index = 0;
			handler.sendEmptyMessage(position);
		}
	}

	/**
	 * 停止滚动
	 */
	public void stopScroll(int position) {
		stop = true;
		if (handler != null) {
			handler.removeMessages(position);
		}
	}

	/**
	 * 初始化数据
	 */
	public void initData(NewsVO news) {
		// 隐藏并重置全部圆点
		for (int i = 0; i < dianView.length; i++) {
			dianView[i].setBackgroundResource(R.mipmap.dian_bg);
			dianView[i].setVisibility(View.GONE);
		}
		try {
			topList = new ArrayList<>(news.getTopNews());
			// Collections.copy(topList, news.topNews);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// topList = news.topNews;
		if (Utils.isEmpty(topList)) {
			adLayout.setVisibility(View.GONE);
			return;
		} else {
			adLayout.setVisibility(View.VISIBLE);
			setLayoutParams(params);
			count = topList.size();
		}
		View view = null;
//		 顶部图片新闻按order排序
//		orderTopNews(topList);
		// 创建view数组
		views = new View[topList.size()];
		// 循环添加
		for (int j = 0; j < views.length; j++) {
			view = inflate(context, R.layout.item_head_news_viewpager, null);
			// 获取显示图片的组件
			ivNewsImage = (ImageView) view.findViewById(R.id.iv_news_image);
			// 获取显示文字的组件
			tvNewsTitle = (TextView) view.findViewById(R.id.tv_news_title);
			// 获取专题图标组件
			tvSubjectLogo = (TextView) view.findViewById(R.id.tv_subject_logo);
			// 设置显示文字
			tvNewsTitle.setText(topList.get(j).getTitle());
			// 设置显示图片
			ImageLoader.getInstance().displayImage(
					topList.get(j).getIconPath(), ivNewsImage,
					ImageOption.createNomalOption());
			// 设置专题图标是否显示
			if (topList.get(j).isSubject()) {
				// 是专题
				tvSubjectLogo.setVisibility(VISIBLE);
			} else {
				// 不是专题
				tvSubjectLogo.setVisibility(GONE);
			}
			views[j] = view;
		}
		// if (adapter == null) {
		adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);
		// } else {
		// adapter.notifyDataSetChanged();
		// }
		dianLayout.setVisibility(View.VISIBLE);
		currentViewID = 0;
		index = 0;
		viewPager.setCurrentItem(index);
		// 根据新闻数量显示圆点
		if (topList.size() > 1 && topList.size() <= 4) {
			dianLayout.setVisibility(View.VISIBLE);
			for (int i = 0; i < topList.size(); i++) {
				dianView[i].setVisibility(View.VISIBLE);
			}
			setImageBackground(0);
		} else {
			dianLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 顶部图片新闻适配器
	 * 
	 * @author sun
	 * 
	 */
	class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return topList != null ? topList.size() : 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int pos, Object object) {
			try {
				((ViewPager) container).removeView(views[pos]);
			} catch (Exception e) {
				
			}
		}

		@Override
		public Object instantiateItem(View container, int pos) {
			((ViewPager) container).addView(views[pos]);
			return views[pos];
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		setImageBackground(position);
		currentViewID = position;
	}

	/**
	 * 设置选中的 点 的背景
	 * 
	 * @param selectItemsIndex
	 */
	private void setImageBackground(int selectItemsIndex) {
		for (int i = 0; i < dianView.length; i++) {
			if (i == selectItemsIndex) {
				dianView[i].setBackgroundResource(R.mipmap.dian);
			} else {
				dianView[i].setBackgroundResource(R.mipmap.dian_bg);
			}
		}
	}

	/**
	 * 顶部图片新闻排序
	 * 
	 * @param topNews
	 */
//	public void orderTopNews(List<NewsVO> topNews) {
//		Collections.sort(topNews, new NewsModelOrderByOrder());
//	}

	/**
	 * 顶部图片新闻排序类
	 * 
	 * @author sun
	 * 
	 */
//	public class NewsModelOrderByOrder implements Comparator<NewsVO> {
//
//		@Override
//		public int compare(NewsVO newsModel_1, NewsVO newsModel_2) {
//			if (newsModel_1 != null && newsModel_2 != null) {
//				if (newsModel_1.getOrder() > newsModel_2.getOrder()) {
//					return -1;
//				} else if (newsModel_1.getOrder() < newsModel_1.getOrder()) {
//					return 1;
//				}
//			}
//			return 0;
//		}
//	}

	/**
	 * 点击顶部滚动图接口
	 * 
	 * @author sun
	 * 
	 */
	public interface OnClickFirstView {
		void onClickView(NewsVO model);
	}
}
