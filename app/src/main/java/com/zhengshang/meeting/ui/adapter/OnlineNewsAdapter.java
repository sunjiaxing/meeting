package com.zhengshang.meeting.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.vo.NewsVO;

/**
 * 新闻列表适配器
 */
public class OnlineNewsAdapter extends QuickFlingAdapter {
	private NewsVO news;
	private List<NewsVO> data;
	private boolean hasTop = false;
	private LayoutParams params;

	public OnlineNewsAdapter(Context ctx) {
		super(ctx);
		int screenW = Utils.getScreenWidth(context);
		int imgW = screenW / 4;
		int imgH = imgW / 4 * 3;
		params = new LayoutParams(imgW, imgH);
	}

	/**
	 * 设置数据
	 *
	 * @param list 数据集合
     * @param hasTop 是否有顶部滚动数据
	 */
	public void setData(List<NewsVO> list, boolean hasTop) {
		this.data = new ArrayList<>(list);
		this.hasTop = hasTop;
	}

	@Override
	public int getCount() {
		return hasTop && !Utils.isEmpty(data) ? data.size() - 1 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		try {
			news = data.get(hasTop ? position + 1 : position);
		} catch (Exception e) {
			e.printStackTrace();
			return convertView;
		}
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.online_news_item, null);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.onlinetitle);
			viewHolder.instro = (TextView) convertView
					.findViewById(R.id.onlineinstro);
			viewHolder.img = (ImageView) convertView
					.findViewById(R.id.onlinepic);
			viewHolder.tvSubjectLogo = (TextView) convertView
					.findViewById(R.id.tv_subject_logo);
			viewHolder.tvCommentCount = (TextView) convertView
					.findViewById(R.id.tv_comment_count);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 设置图片，判断图片的网址是不是URL
		if (!Utils.isEmpty(news.getIconPath())) {
			viewHolder.img.setVisibility(View.VISIBLE);
			viewHolder.img.setLayoutParams(params);
			ImageLoader.getInstance().displayImage(news.getIconPath(),
					viewHolder.img, ImageOption.createNomalOption());
		} else {
			// url无效
			viewHolder.img.setVisibility(View.GONE);
		}
		viewHolder.title.setText(news.getTitle());
		// 判断新闻阅读状态
		if (news.isRead()) {
			// 已读
			// 设置颜色
			viewHolder.title.setTextColor(Color.GRAY);
		} else {
			// 未读
			viewHolder.title.setTextColor(Color.BLACK);
		}
		// 判断是否是专题
		if (news != null && news.isSubject()) {
			// 是专题
			viewHolder.tvSubjectLogo.setVisibility(View.VISIBLE);
			// 隐藏评论条数显示
			viewHolder.tvCommentCount.setVisibility(View.GONE);
			// 判断子标题字数 保留25个字
			// if (news != null && news.getSummary().length() > 25) {
			// viewHolder.instro.setText(news.getSummary().substring(0, 25)
			// + "...");
			// } else {0
			viewHolder.instro.setText(Html.fromHtml(news.getSummary()));
			// }
		} else {
			// 不是专题
			viewHolder.tvSubjectLogo.setVisibility(View.GONE);
			// 显示评论条数
			viewHolder.tvCommentCount.setVisibility(View.VISIBLE);
			// 设置评论条数
			//viewHolder.tvCommentCount.setText(news.getCommentCount() + "评论");
			// 判断子标题字数 保留30个字
			// if (news != null && !Utils.isEmpty(news.getSummary())
			// && news.getSummary().length() > 26) {
			// viewHolder.instro.setText(news.getSummary().substring(0, 26)
			// + "...");
			// } else {
			viewHolder.instro
					.setText(Html.fromHtml(news.getSummary() != null ? news
							.getSummary() : ""));
			// }
		}
		// 隐藏评论条数显示
		viewHolder.tvCommentCount.setVisibility(View.GONE);
		return convertView;
	}

	public class ViewHolder {
		TextView title;
		TextView instro;
		ImageView img;
		TextView tvSubjectLogo;
		TextView tvCommentCount;
	}
}
