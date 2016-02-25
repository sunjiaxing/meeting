package com.zhengshang.meeting.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * 自定义 下拉刷新 header
 * Created by sun on 2016/2/25.
 */
public class CustomerPtrHeader extends FrameLayout implements PtrUIHandler{
    public CustomerPtrHeader(Context context) {
        super(context);
    }

    public CustomerPtrHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerPtrHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {

    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

    }
}
