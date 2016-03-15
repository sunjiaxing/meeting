package com.sb.meeting.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private TabNewsFragment tabNewsFragment;
    private TabGoodsListFragment tabGoodsListFragment;
    private BonConstants.BottomMenuSelected menuSelected = BonConstants.BottomMenuSelected.NEWS;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            menuSelected = BonConstants.BottomMenuSelected.values()[savedInstanceState.getInt(IParam.TYPE)];
        }
        handler = new Handler();
    }

    @AfterViews
    void init() {
        if (menuSelected == BonConstants.BottomMenuSelected.NEWS) {
            toNewsPage();
        } else if (menuSelected == BonConstants.BottomMenuSelected.GOODS) {
            toGoodsPage();
        }
    }

    /**
     * 隐藏其他fragment
     */
    private void hideOtherFragment() {
        tabNewsFragment = (TabNewsFragment) getSupportFragmentManager().findFragmentByTag(IParam.NEWS);
        tabGoodsListFragment = (TabGoodsListFragment) getSupportFragmentManager().findFragmentByTag(IParam.GOODS);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (tabNewsFragment != null) {
            ft.hide(tabNewsFragment);
        }

        if (tabGoodsListFragment != null) {
            ft.hide(tabGoodsListFragment);
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
     * 重置底部菜单样式
     */
    private void resetMenuStyle() {
        // 恢复默认文字和图标
        tvNews.setTextColor(getResources().getColor(R.color.news_content_color));
        tvGoods.setTextColor(getResources().getColor(R.color.news_content_color));


        switch (menuSelected) {
            case NEWS:
                tvNews.setTextColor(Color.GREEN);
                break;
            case GOODS:
                tvGoods.setTextColor(Color.GREEN);
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
        LogUtils.i("main activity onActivityResult");
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
