package com.sb.meeting.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sb.meeting.service.UserService;
import com.sb.meeting.ui.fragment.TabUserFragment;
import com.sb.meeting.ui.fragment.TabUserFragment_;
import com.taskmanager.LogUtils;
import com.sb.meeting.R;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.ui.fragment.TabGoodsListFragment;
import com.sb.meeting.ui.fragment.TabGoodsListFragment_;
import com.sb.meeting.ui.fragment.TabNewsFragment;
import com.sb.meeting.ui.fragment.TabNewsFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 首页
 * Created by sun on 2016/2/19.
 */
@EActivity(R.layout.layout_main)
public class MainActivity extends BaseActivity {

    @ViewById(R.id.layout_menu_news)
    LinearLayout layoutNews;
    @ViewById(R.id.iv_news)
    ImageView ivNews;
    @ViewById(R.id.tv_news)
    TextView tvNews;
    @ViewById(R.id.layout_menu_goods)
    LinearLayout layoutGoods;
    @ViewById(R.id.iv_goods)
    ImageView ivGoods;
    @ViewById(R.id.tv_goods)
    TextView tvGoods;
    @ViewById(R.id.iv_user)
    ImageView ivUser;
    @ViewById(R.id.tv_user)
    TextView tvUser;

    private TabNewsFragment tabNewsFragment;
    private TabGoodsListFragment tabGoodsListFragment;
    private TabUserFragment tabUserFragment;
    private BonConstants.BottomMenuSelected menuSelected = BonConstants.BottomMenuSelected.NEWS;
    private Handler handler;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            menuSelected = BonConstants.BottomMenuSelected.values()[savedInstanceState.getInt(IParam.TYPE)];
        }
        handler = new Handler();
        userService = new UserService(this);
    }

    @AfterViews
    void init() {
        if (menuSelected == BonConstants.BottomMenuSelected.NEWS) {
            toNewsPage();
        } else if (menuSelected == BonConstants.BottomMenuSelected.GOODS) {
            toGoodsPage();
        } else if (menuSelected == BonConstants.BottomMenuSelected.USER) {
            toUserPage();
        }
    }

    /**
     * 隐藏其他fragment
     */
    private void hideOtherFragment() {
        tabNewsFragment = (TabNewsFragment) getSupportFragmentManager().findFragmentByTag(IParam.NEWS);
        tabGoodsListFragment = (TabGoodsListFragment) getSupportFragmentManager().findFragmentByTag(IParam.GOODS);
        tabUserFragment = (TabUserFragment) getSupportFragmentManager().findFragmentByTag(IParam.USER);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (tabNewsFragment != null) {
            ft.hide(tabNewsFragment);
        }

        if (tabGoodsListFragment != null) {
            ft.hide(tabGoodsListFragment);
        }

        if (tabUserFragment != null) {
            ft.hide(tabUserFragment);
        }
        ft.commit();
    }

    /**
     * 跳转到 资讯 页面
     */
    @Click(R.id.layout_menu_news)
    void toNewsPage() {
        hideOtherFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (tabNewsFragment == null) {
            tabNewsFragment = new TabNewsFragment_();
            ft.add(R.id.layout_contain, tabNewsFragment, IParam.NEWS);
        } else {
            ft.show(tabNewsFragment);
        }
        ft.commit();
        menuSelected = BonConstants.BottomMenuSelected.NEWS;
        resetMenuStyle();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tabNewsFragment.refreshView();
            }
        }, 50);

    }

    /**
     * 跳转到 易物 页面
     */
    @Click(R.id.layout_menu_goods)
    void toGoodsPage() {
        hideOtherFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (tabGoodsListFragment == null) {
            tabGoodsListFragment = new TabGoodsListFragment_();
            ft.add(R.id.layout_contain, tabGoodsListFragment, IParam.GOODS);
        } else {
            ft.show(tabGoodsListFragment);
        }
        ft.commit();
        menuSelected = BonConstants.BottomMenuSelected.GOODS;
        resetMenuStyle();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tabGoodsListFragment.refreshView();
            }
        }, 50);
    }

    /**
     * 跳转到 我的  页面
     */
    @Click(R.id.layout_menu_user)
    void toUserPage() {
        hideOtherFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (tabUserFragment == null) {
            tabUserFragment = new TabUserFragment_();
            ft.add(R.id.layout_contain, tabUserFragment, IParam.USER);
        } else {
            ft.show(tabUserFragment);
        }
        ft.commit();
        menuSelected = BonConstants.BottomMenuSelected.USER;
        resetMenuStyle();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tabUserFragment.refreshView();
            }
        }, 50);
    }

    /**
     * 重置底部菜单样式
     */
    private void resetMenuStyle() {
        // 恢复默认文字和图标
        tvNews.setTextColor(getResources().getColor(R.color.c_7d8e9d));
        tvGoods.setTextColor(getResources().getColor(R.color.c_7d8e9d));
        tvUser.setTextColor(getResources().getColor(R.color.c_7d8e9d));
        ivNews.setImageResource(R.mipmap.icon_news_nomal);
        ivGoods.setImageResource(R.mipmap.icon_goods_nomal);
        ivUser.setImageResource(R.mipmap.icon_user_nomal);


        switch (menuSelected) {
            case NEWS:
                tvNews.setTextColor(getResources().getColor(R.color.c_ff946e));
                ivNews.setImageResource(R.mipmap.icon_news_selected);
                break;
            case GOODS:
                tvGoods.setTextColor(getResources().getColor(R.color.c_ff946e));
                ivGoods.setImageResource(R.mipmap.icon_goods_selected);
                break;
            case USER:
                tvUser.setTextColor(getResources().getColor(R.color.c_ff946e));
                ivUser.setImageResource(R.mipmap.icon_user_selected);
                break;
        }
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (menuSelected) {
            case NEWS:
                tabNewsFragment.onTaskSuccess(action, data);
                break;
            case GOODS:
                tabGoodsListFragment.onTaskSuccess(action, data);
                break;
        }
    }

    @Override
    protected void onTaskFail(int action, String errorMessage) {
        switch (menuSelected) {
            case NEWS:
                tabNewsFragment.onTaskFail(action, errorMessage);
                break;
            case GOODS:
                tabGoodsListFragment.onTaskFail(action, errorMessage);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.i("main activity onSaveInstanceState");
        outState.putInt(IParam.TYPE, menuSelected.ordinal());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.i("main activity onRestoreInstanceState");
        if (savedInstanceState != null) {
            menuSelected = BonConstants.BottomMenuSelected.values()[savedInstanceState.getInt(IParam.TYPE)];
        }
    }
}
