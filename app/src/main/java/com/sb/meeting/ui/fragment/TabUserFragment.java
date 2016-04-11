package com.sb.meeting.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.UserService;
import com.sb.meeting.ui.activity.AttentionGoodsActivity_;
import com.sb.meeting.ui.activity.ChooseImageActivity_;
import com.sb.meeting.ui.activity.FavoriteActivity_;
import com.sb.meeting.ui.activity.ImageActivity_;
import com.sb.meeting.ui.activity.LoginActivity_;
import com.sb.meeting.ui.activity.PublishedGoodsActivity_;
import com.sb.meeting.ui.activity.SettingActivity_;
import com.sb.meeting.ui.component.CircleImageView;
import com.sb.meeting.ui.vo.ImageVO;
import com.sb.meeting.ui.vo.UserVO;
import com.soundcloud.android.crop.Crop;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的 页面
 * Created by sun on 2016/3/22.
 */
@EFragment(R.layout.layout_tab_user)
public class TabUserFragment extends BaseFragment {

    @ViewById(R.id.iv_setting)
    ImageView ivSetting;
    @ViewById(R.id.iv_user_avatar)
    CircleImageView ivUserAvatar;
    @ViewById(R.id.tv_user_name)
    TextView tvUserName;
    @ViewById(R.id.tv_favorite)
    TextView tvFavorite;
    @ViewById(R.id.tv_published_goods)
    TextView tvPublished;
    @ViewById(R.id.tv_attention_goods)
    TextView tvAttention;
    @ViewById(R.id.tv_to_login)
    TextView tvToLogin;
    @ViewById(R.id.layout_menu)
    LinearLayout layoutMenu;

    private UserService userService;

    private static final int REQUEST_TO_LOGIN = 0x3222;
    private static final int REQUEST_TO_SETTING = 0x4901;
    private static final int REQUEST_TO_SELECT_IMAGE = 0x4101;
    private UserVO userInfo;
    private File cropAvatar;

    @AfterViews
    void init() {
        userService = new UserService(getActivity());
    }

    public void refreshView() {
        if (userService.checkLoginState()) {
            if (userInfo == null) {
                userInfo = userService.getLoginUserInfo();
            }
            layoutMenu.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.VISIBLE);
            tvToLogin.setVisibility(View.GONE);
            Utils.displayImage(userInfo.getUserAvatar(), ivUserAvatar, ImageOption.createNomalOption());
            tvUserName.setText(userInfo.getNickName());
        } else {
            ivUserAvatar.setImageResource(R.mipmap.default_item_pic);
            layoutMenu.setVisibility(View.GONE);
            tvUserName.setVisibility(View.GONE);
            tvToLogin.setVisibility(View.VISIBLE);
        }
    }

    @Click(R.id.tv_to_login)
    void toLogin() {
        LoginActivity_.intent(this).startForResult(REQUEST_TO_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TO_LOGIN && resultCode == Activity.RESULT_OK) {
            refreshView();
        } else if (requestCode == REQUEST_TO_SETTING && resultCode == Activity.RESULT_OK) {
            refreshView();
        } else if (requestCode == REQUEST_TO_SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            List<String> imageList = (List<String>) data.getSerializableExtra(IParam.CONTENT);
            uploadUserAvatar(imageList);
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {

        }
    }

    private void uploadUserAvatar(List<String> imageList) {
        try {
            if (!Utils.isEmpty(imageList)) {
                String path = imageList.get(0);
                File dir = new File(BonConstants.PATH_TEMP);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                cropAvatar = new File(dir, "avatar.png");
                if (!cropAvatar.exists()) {
                    cropAvatar.createNewFile();
                }
                Crop.of(Uri.fromFile(new File(path)), Uri.fromFile(cropAvatar))
                        .asSquare().withMaxSize(BonConstants.MAX_AVATAR_WIDTH, BonConstants.MAX_AVATAR_HEIGHT)
                        .start(getActivity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到设置界面
     */
    @Click(R.id.iv_setting)
    void toSetting() {
        SettingActivity_.intent(this).startForResult(REQUEST_TO_SETTING);
    }

    /**
     * 跳转到收藏列表
     */
    @Click(R.id.tv_favorite)
    void toFavorite() {
        FavoriteActivity_.intent(this).start();
    }

    /**
     * 跳转到  我发布的物品页面
     */
    @Click(R.id.tv_published_goods)
    void toPublished() {
        PublishedGoodsActivity_.intent(this).start();
    }

    /**
     * 跳转到 我关注的物品页面
     */
    @Click(R.id.tv_attention_goods)
    void toAttention() {
        AttentionGoodsActivity_.intent(this).start();
    }

    @Click(R.id.iv_user_avatar)
    void updateUserInfo() {
        if (userService.checkLoginState()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(new String[]{"查看", "修改"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        // 查看
                        if (!Utils.isEmpty(userInfo.getUserAvatar())) {
                            List<ImageVO> list = new ArrayList<>();
                            ImageVO vo = new ImageVO();
                            vo.setUrl(userInfo.getUserAvatar());
                            list.add(vo);
                            ImageActivity_.intent(TabUserFragment.this).extra(IParam.IMAGES, (Serializable) list).start();
                        } else {
                            showToast("暂无头像信息");
                        }
                    } else {
                        // 修改
                        ChooseImageActivity_.intent(TabUserFragment.this)
                                .extra(IParam.IS_SELECT_USER_HEAD, true)
                                .startForResult(REQUEST_TO_SELECT_IMAGE);
                    }
                }
            }).show();
        }
    }
}
