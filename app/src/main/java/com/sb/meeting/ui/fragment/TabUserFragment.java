package com.sb.meeting.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sb.meeting.R;
import com.sb.meeting.common.ImageOption;
import com.sb.meeting.service.UserService;
import com.sb.meeting.ui.activity.FavoriteActivity_;
import com.sb.meeting.ui.activity.LoginActivity_;
import com.sb.meeting.ui.vo.UserVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 我的 页面
 * Created by sun on 2016/3/22.
 */
@EFragment(R.layout.layout_tab_user)
public class TabUserFragment extends BaseFragment {

    @ViewById(R.id.iv_setting)
    ImageView ivSetting;
    @ViewById(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @ViewById(R.id.tv_user_name)
    TextView tvUserName;
    @ViewById(R.id.tv_favorite)
    TextView tvFavorite;
    @ViewById(R.id.tv_published_goods)
    TextView tvPublished;
    @ViewById(R.id.tv_attention_goods)
    TextView tvAttention;
    @ViewById(R.id.layout_not_login)
    View layoutNotLogin;
    @ViewById(R.id.layout_user_center)
    View layoutUserCenter;

    private UserService userService;

    private static final int REQUEST_TO_LOGIN = 0x3222;
    private UserVO userInfo;

    @AfterViews
    void init() {
        userService = new UserService(getActivity());
    }

    public void refreshView() {
        if (userService.checkLoginState()) {
            if (userInfo == null) {
                userInfo = userService.getLoginUserInfo();
            }
            layoutNotLogin.setVisibility(View.GONE);
            layoutUserCenter.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(userInfo.getUserAvatar(), ivUserAvatar, ImageOption.createNomalOption());
            tvUserName.setText(userInfo.getNickName());
        } else {
            layoutUserCenter.setVisibility(View.GONE);
            layoutNotLogin.setVisibility(View.VISIBLE);
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
        }
    }

    /**
     * 跳转到设置界面
     */
    @Click(R.id.iv_setting)
    void toSetting() {

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

    }

    /**
     * 跳转到 我关注的物品页面
     */
    @Click(R.id.tv_attention_goods)
    void toAttention() {

    }


}
