package com.sb.meeting.ui.activity;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sb.meeting.R;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.ui.vo.CertificateVO;
import com.sb.meeting.ui.vo.ImageVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业资质 Activity
 * Created by sun on 2016/4/1.
 */
@EActivity(R.layout.layout_company_certificate)
public class CompanyCertificateActivity extends BaseActivity {
    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.listView)
    ListView listView;
    private List<CertificateVO> certificateList;
    private int screenWidth;
    private List<ImageVO> images;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("企业资质");
        certificateList = (List<CertificateVO>) getIntent().getSerializableExtra(IParam.CONTENT);
        screenWidth = Utils.getScreenWidth(this);
        if (!Utils.isEmpty(certificateList)) {
            listView.setAdapter(new CertificateAdapter());
        }

    }

    @Click(R.id.iv_back)
    void back() {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @ItemClick(R.id.listView)
    void onItemClick(int position) {
        if (Utils.isEmpty(images)) {
            images = new ArrayList<>();
            // 构建数据
            for (CertificateVO vo : certificateList) {
                ImageVO img = new ImageVO();
                img.setUrl(vo.getThumb());
                img.setDesc(vo.getName() + "【发证机构】" + vo.getOrganization());
                images.add(img);
            }
        }
        ImageActivity_.intent(this)
                .extra(IParam.IMAGES, (Serializable) images)
                .extra(IParam.INDEX, position)
                .start();
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {

    }

    class CertificateAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return !Utils.isEmpty(certificateList) ? certificateList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return certificateList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.layout_item_certificate, null);
                viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
                viewHolder.tvText = (TextView) convertView.findViewById(R.id.tv_text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            CertificateVO vo = certificateList.get(position);
            Utils.displayImage(vo.getThumb(), viewHolder.ivImage, ImageOption.createNomalOption(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    // screenW    imgW
                    //   ?        imgH
                    int x = screenWidth * loadedImage.getHeight() / loadedImage.getWidth();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, x);
                    ImageView iv = (ImageView) view;
                    iv.setLayoutParams(layoutParams);
                    iv.setImageBitmap(loadedImage);
                }
            });
            viewHolder.tvText.setText(vo.getName() + "【发证机构】" + vo.getOrganization());
            return convertView;
        }

        class ViewHolder {
            ImageView ivImage;
            TextView tvText;
        }
    }
}
