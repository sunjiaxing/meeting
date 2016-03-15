package com.sb.meeting.ui.component;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sb.meeting.R;
import com.sb.meeting.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享 dialog
 * Created by sun on 2016/3/15.
 */
public class ShareDialog implements AdapterView.OnItemClickListener, View.OnClickListener, PlatformActionListener {

    private Context context;
    private AlertDialog dialog;
    private GridView gridView;
    private TextView tvCancel;
    private Platform.ShareParams shareParam;

    public ShareDialog(Context context) {
        ShareSDK.initSDK(context);
        this.context = context;
        dialog = new android.app.AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_share_dialog);
        gridView = (GridView) window.findViewById(R.id.gv_share);
        gridView.setOnItemClickListener(this);
        tvCancel = (TextView) window.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        List<HashMap<String, Object>> shareList = new ArrayList<>();
        int[] image = {R.mipmap.icon_share_wx, R.mipmap.icon_share_wxt, R.mipmap.icon_share_sina, R.mipmap.icon_share_qq, R.mipmap.icon_share_qzone};
        String[] name = {"微信好友", "微信朋友圈", "新浪微博", "QQ", "QQ空间"};
        for (int i = 0; i < image.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("ItemImage", image[i]);//添加图像资源的ID
            map.put("ItemText", name[i]);//按序号做ItemText
            shareList.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(context, shareList,
                R.layout.layout_item_share,
                new String[]{"ItemImage", "ItemText"},
                new int[]{R.id.iv_image, R.id.tv_text});
        gridView.setAdapter(simpleAdapter);
    }

    public void setCancelButtonOnClickListener(View.OnClickListener Listener) {
        tvCancel.setOnClickListener(Listener);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        gridView.setOnItemClickListener(listener);
    }

    public void setShareParams(Platform.ShareParams shareParams) {
        this.shareParam = shareParams;
    }


    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
        String selected = (String) item.get("ItemText");
        Platform platform;
        if (("QQ").equals(selected)) {
            platform = ShareSDK.getPlatform(QQ.NAME);
        } else if ("微信好友".equals(selected)) {
            platform = ShareSDK.getPlatform(Wechat.NAME);
        } else if ("微信朋友圈".equals(selected)) {
            platform = ShareSDK.getPlatform(WechatMoments.NAME);
        } else if ("新浪微博".equals(selected)) {
            platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        } else if ("QQ空间".equals(selected)) {
            platform = ShareSDK.getPlatform(QZone.NAME);
        } else {
            platform = ShareSDK.getPlatform(QQ.NAME);
        }
        platform.setPlatformActionListener(this);
        platform.share(shareParam);
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Toast.makeText(context, "取消分享", Toast.LENGTH_SHORT).show();
    }
}