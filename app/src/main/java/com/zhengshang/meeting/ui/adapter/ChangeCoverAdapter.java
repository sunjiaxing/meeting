package com.zhengshang.meeting.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.ImageOption;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 切换封面 列表适配器
 * Created by sun on 2016/2/22.
 */
public class ChangeCoverAdapter extends QuickFlingAdapter {

    private List<String> list;
    private LayoutParams params;

    public ChangeCoverAdapter(Context context, GridView gv) {
        super(context);
        int w = Utils.getScreenWidth(context) / 3;
        params = new LayoutParams(w, w);
        gv.setOnScrollListener(new PauseOnScrollListener(ImageLoader
                .getInstance(), true, false));
    }

    public void setData(List<String> imgPath) {
        this.list = new ArrayList<>(imgPath);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.layout_item_choose_image, null);
            viewHolder = new ViewHolder();
            viewHolder.layoutNomal = convertView
                    .findViewById(R.id.layout_nomal);
            viewHolder.layoutCamera = convertView
                    .findViewById(R.id.layout_camera);
            viewHolder.ivImage = (ImageView) convertView
                    .findViewById(R.id.iv_image);
            viewHolder.ivSelectTip = (ImageView) convertView
                    .findViewById(R.id.iv_select_tip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.layoutCamera.setVisibility(View.GONE);
        viewHolder.layoutNomal.setVisibility(View.VISIBLE);
        viewHolder.ivSelectTip.setVisibility(View.GONE);
        String path = list.get(position);
        viewHolder.ivImage.setLayoutParams(params);
        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(path), viewHolder.ivImage,
                ImageOption.createNomalOption());
        return convertView;
    }

    class ViewHolder {
        View layoutNomal;
        View layoutCamera;
        ImageView ivImage;
        ImageView ivSelectTip;
    }
}
