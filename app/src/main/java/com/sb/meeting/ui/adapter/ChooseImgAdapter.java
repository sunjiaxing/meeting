package com.sb.meeting.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.sb.meeting.R;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.activity.BaseActivity;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 图片列表适配器
 * Created by sun on 2016/2/22.
 */
public class ChooseImgAdapter extends QuickFlingAdapter {

    private List<String> list;
    private LayoutParams params;
    private List<String> selectedImg = new ArrayList<>();
    private OnSelectImageListener l;
    private int last = 9;
    private boolean isSelectHead = false;

    public ChooseImgAdapter(Context context, GridView gv, int last) {
        super(context);
        int w = Utils.getScreenWidth(context) / 3;
        params = new LayoutParams(w, w);
        this.last = last;
        gv.setOnScrollListener(new PauseOnScrollListener(ImageLoader
                .getInstance(), true, false));
    }

    public void setData(List<String> imgPath) {
        this.list = new ArrayList<>(imgPath);
    }

    public void setOnSelectedListener(OnSelectImageListener l) {
        this.l = l;
    }

    @Override
    public int getCount() {
        return list.size() + 1;
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
        if (position == 0) {
            viewHolder.layoutCamera.setVisibility(View.VISIBLE);
            viewHolder.layoutCamera.setLayoutParams(params);
            viewHolder.layoutNomal.setVisibility(View.GONE);
        } else {
            viewHolder.layoutCamera.setVisibility(View.GONE);
            viewHolder.layoutNomal.setVisibility(View.VISIBLE);
            if (isSelectHead || last == 1) {
                viewHolder.ivSelectTip.setVisibility(View.GONE);
            } else {
                viewHolder.ivSelectTip.setVisibility(View.VISIBLE);
            }
            viewHolder.ivSelectTip.setTag(position - 1);
            viewHolder.ivSelectTip.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    String path = list.get(pos);
                    if (selectedImg.contains(path)) {
                        selectedImg.remove(path);
                        ((ImageView) v)
                                .setImageResource(R.mipmap.picture_icon_unselected);
                    } else {
                        if (selectedImg.size() == last) {
                            ((BaseActivity) context).showToast("最多选择" + last
                                    + "张图片！");
                        } else {
                            selectedImg.add(path);
                            ((ImageView) v)
                                    .setImageResource(R.mipmap.picture_icon_selected);
                        }
                    }
                    if (l != null) {
                        l.onSelected(selectedImg);
                    }
                }
            });
            String path = list.get(position - 1);
            viewHolder.ivImage.setTag(path);
            viewHolder.ivImage.setLayoutParams(params);
            ImageLoader.getInstance().displayImage(path, viewHolder.ivImage,
                    ImageOption.createNomalOption());
            if (selectedImg.contains(path)) {
                viewHolder.ivSelectTip
                        .setImageResource(R.mipmap.picture_icon_selected);
            } else {
                viewHolder.ivSelectTip
                        .setImageResource(R.mipmap.picture_icon_unselected);
            }
        }
        return convertView;
    }

    class ViewHolder {
        View layoutNomal;
        View layoutCamera;
        ImageView ivImage;
        ImageView ivSelectTip;
    }

    public interface OnSelectImageListener {
        void onSelected(List<String> seletedImg);
    }

    public void isHead(boolean head) {
        this.isSelectHead = head;
    }

}
