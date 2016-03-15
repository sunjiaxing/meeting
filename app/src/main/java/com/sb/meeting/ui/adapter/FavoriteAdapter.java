package com.sb.meeting.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sb.meeting.R;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.vo.FavoriteVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏列表适配器
 */
public class FavoriteAdapter extends QuickFlingAdapter {
    private List<FavoriteVO> data;
    private LayoutParams params;

    public FavoriteAdapter(Context ctx) {
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
     */
    public void setData(List<FavoriteVO> list) {
        this.data = new ArrayList<>(list);
    }

    @Override
    public int getCount() {
        return !Utils.isEmpty(data) ? data.size() : 0;
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
        FavoriteVO vo = data.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_favorite_item, null);
            viewHolder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_title);
            viewHolder.tvSummary = (TextView) convertView
                    .findViewById(R.id.tv_summary);
            viewHolder.ivIcon = (ImageView) convertView
                    .findViewById(R.id.iv_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设置图片，判断图片的网址是不是URL
        if (!Utils.isEmpty(vo.getIconUrl())) {
            viewHolder.ivIcon.setVisibility(View.VISIBLE);
            viewHolder.ivIcon.setLayoutParams(params);
            ImageLoader.getInstance().displayImage(vo.getIconUrl(),
                    viewHolder.ivIcon, ImageOption.createNomalOption());
        } else {
            // url无效
            viewHolder.ivIcon.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(vo.getTitle());
        viewHolder.tvTitle.setTextColor(Color.BLACK);
        viewHolder.tvSummary
                .setText(Html.fromHtml(vo.getSummary() != null ? vo
                        .getSummary() : ""));

        return convertView;
    }

    public class ViewHolder {
        TextView tvTitle;
        TextView tvSummary;
        ImageView ivIcon;
    }
}
