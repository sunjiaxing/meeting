package com.zhengshang.meeting.ui.component;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.vo.NewsChannelVO;

import java.util.List;


/**
 * 由于Gallery会自动居中等不可控原因,自定义此Gallery,用于分类展示
 */
public class MyGallery extends HorizontalScrollView implements OnClickListener {
	Context context;
	int itemWidth = 0;
	int itemDimenWidth = 0;
	private long l = 0;

	public interface TlcyGalleryListener {
		/**
		 * 点击回调,如果处理了回调,则返回true,否则返回false
		 */
		boolean onItemClick(int position);

		/**
		 * 状态回调 state 0为不可滚动,1为只可以向左滚动,2为只可以向右滚动,3为还可以向左向右滚动
		 */
		void onState(int state);
	}

	LayoutInflater inflater;
	LinearLayout layout = null;
	TlcyGalleryListener listener = null;
	/**
	 * 被选中的项
	 */
	int selected = -1;
	/**
	 * 子项数目
	 */
	int childNum = -1;
	/**
	 * 选中的背景
	 */
	int bg;

	public MyGallery(Context context) {
		super(context);
		this.context = context;
	}

	public MyGallery(Context context, AttributeSet set) {
		super(context, set);
		this.context = context;
	}

	public MyGallery(Context context, AttributeSet set, int style) {
		super(context, set, style);
		this.context = context;
	}

	/**
	 * 设置回调函数
	 */
	public void setOnItemClickListener(TlcyGalleryListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置分类名称列表 res为Item视图,必须是一个Button,bgRes为选中时的背景
	 */
	public void setAdapter(int res, int bgRes, List<NewsChannelVO> mGallery) {

		if (layout == null) {
			inflater = LayoutInflater.from(getContext());
			layout = new LinearLayout(getContext());
			// 设置id
			layout.setId(android.R.id.text1);
			layout.setGravity(Gravity.CENTER_VERTICAL);
			layout.setOnClickListener(this);
			addView(layout);
		} else {
			layout.removeAllViews();
		}
		bg = bgRes;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		itemWidth = (int) (wm.getDefaultDisplay().getWidth() / ((BonConstants.LIMIT_DEFAULT_NEWS_TYPE) + 1));
		if (mGallery != null) {
			for (int i = 0; i < mGallery.size(); i++) {
				// 把button的布局加载进来
				Button button = (Button) inflater.inflate(res, null);

				if (button != null) {
					button.setText(mGallery.get(i).getName());
					button.setTag(i);
					button.setTextSize(16);
					button.setTextColor(getContext().getResources().getColor(
							R.color.news_content_color));
					button.setOnClickListener(this);
//					button.setBackgroundDrawable(mkSelector(-1, bg, -1, -1));
					button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//					Drawable dr = getResources().getDrawable(R.drawable.title_bar_bg);
//					dr.setBounds(0, 0, 100, 3);
//					button.setCompoundDrawables(null, null, null, dr);
					button.setPadding(0, 0, 0, 0);// 不加这句无法使用9png
					layout.addView(button, new LinearLayout.LayoutParams(
							itemWidth, Utils.dip2px(context, 27)));
					if (i != mGallery.size() - 1) {
						ImageView im = new ImageView(context);
						layout.addView(
								im,
								new LinearLayout.LayoutParams(
										1,
										android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
					}
				}
			}
			childNum = mGallery.size();
			setSelected(0);
		}
	}

	/** 设置Selector。 */
	private StateListDrawable mkSelector(int idNormal, int idPressed,
			int idFocused, int idUnable) {
		StateListDrawable bg = new StateListDrawable();
		Drawable normal = idNormal == -1 ? null : context.getResources()
				.getDrawable(idNormal);
		Drawable pressed = idPressed == -1 ? null : context.getResources()
				.getDrawable(idPressed);
		Drawable focused = idFocused == -1 ? null : context.getResources()
				.getDrawable(idFocused);
		Drawable unable = idUnable == -1 ? null : context.getResources()
				.getDrawable(idUnable);
		// View.PRESSED_ENABLED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, pressed);
		// View.ENABLED_FOCUSED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_enabled,
				android.R.attr.state_focused }, focused);
		// View.ENABLED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_enabled }, normal);
		// View.FOCUSED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_focused }, focused);
		// View.WINDOW_FOCUSED_STATE_SET
		bg.addState(new int[] { android.R.attr.state_window_focused }, unable);
		// View.EMPTY_STATE_SET
		bg.addState(new int[] {}, normal);
		return bg;
	}

	private int getItemWidth() {
		if (itemWidth > itemDimenWidth) {
			return itemWidth;
		} else {
			return itemDimenWidth;// itemDimenWidth;
		}
	}

	/**
	 * 返回是否按钮太多超出现实范围,需要滚动
	 */
	public boolean isScroll() {
		int width = 0;// getWidth();
		int btnWidth = getItemWidth();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		if (width < childNum * btnWidth) {
			return true;
		}
		return false;
	}

	/**
	 * 设置某一下为选中状态
	 */
	public void setSelected(int pos) {
		selected = pos;
		Button button;
		ImageView imageView;
		for (int i = 0; i < childNum; i++) {
			button = (Button) layout.getChildAt(2 * i);
			if (i == selected) {
//				button.setTextColor(getResources().getColor(R.color.factory_font_select));
//				button.setTextSize(16);
//				Drawable dr = getResources().getDrawable(R.drawable.title_bar_bg);
//				dr.setBounds(0, 0, Utils.dip2px(context, 200), Utils.dip2px(context, 2));
//				button.setCompoundDrawables(null, null, null, dr);
				button.setTextColor(getResources().getColor(R.color.color_white));
				button.setBackgroundResource(R.drawable.new_shape);
//				button.getPaint().setFakeBoldText(true);
			} else {
				button.getPaint().setFakeBoldText(false);
				button.setTextColor(Color.parseColor("#666666"));
				button.setCompoundDrawables(null, null, null, null);
				button.setBackgroundColor(0);
			}
			if (i != childNum - 1) {
				imageView = (ImageView) layout.getChildAt(2 * i + 1);
				imageView.getLayoutParams().width = 1;
				// imageView.setVisibility(GONE);
				imageView.invalidate();
			}
			button.invalidate();
		}
		layout.invalidate();
	}

	/**
	 * 外部调用setSelected后,需调用此方法滚动到目标选中项
	 */
	public void scrollToSelected() {
		if (selected >= 0 && selected < childNum) {
			int scroolx = getScrollX();
			int width = getWidth();
			int btnWidth = getItemWidth();
			int perShowing = width / btnWidth;// 每页能显示的项数
			int maxShowing = (scroolx + width) / btnWidth;// 显示的最大的项数
			if (selected >= maxShowing) {// 选择的在右边,根据具体效果还需修改
				scrollTo((selected - perShowing + 1) * btnWidth, 0);
			} else {
				if ((maxShowing - selected) >= perShowing) {
					scrollTo(-(selected) * btnWidth, 0);
				}
			}

		}
	}

	/**
	 * 向右滚动一个项
	 */
	public void scrollRight() {
		scrollTo(getScrollX() + getItemWidth(), 0);
	}

	/**
	 * 向左滚动一个项
	 */
	public void scrollLeft() {
		scrollTo(getScrollX() - getItemWidth(), 0);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		int state = 0;
		if (isScroll()) {
			if (getScrollX() == 0) {
				state = 1;
			} else {
				int x = getScrollX(), w = getWidth(), a = childNum
						* getItemWidth();
				if ((x + w) < a) {
					state = 3;
				} else {
					state = 2;
				}
			}
		}
		if (listener != null) {
			listener.onState(state);
		}
	}

	public LinearLayout getLayout() {
		return layout;
	}

	/**
	 * 返回选中的序号,-1为没有选中
	 */
	public int getSelected() {
		return selected;
	}

	@Override
	public void onClick(View v) {
		if (Math.abs(System.currentTimeMillis() - l) > 500) {
			l = System.currentTimeMillis();
			try {
				int pos = Integer.parseInt(v.getTag().toString());
				if (listener != null) {
					if (listener.onItemClick(pos)) {
						setSelected(pos);
						scrollToSelected();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		View view;
		for (int i = 0; i < childNum; i++) {
			view = layout.getChildAt(i);
			if (view != null && view instanceof Button) {
				view.getLayoutParams().width = getItemWidth();
				view.getLayoutParams().height = Utils.dip2px(context, 30);
				view.requestLayout();
			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(event);
	}

}
