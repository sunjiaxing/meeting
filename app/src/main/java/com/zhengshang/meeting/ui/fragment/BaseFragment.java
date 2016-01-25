package com.zhengshang.meeting.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhengshang.meeting.ui.activity.BaseActivity;
import com.zhengshang.meeting.ui.component.TlcyDialog;

/**
 * fragement基类
 *
 * @author sun
 */
public class BaseFragment extends Fragment {
    private BaseActivity mActivity;

    /**
     * 显示小提示
     */
    public void showToast(String text) {
        mActivity.showToast(text);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BaseActivity) {
            mActivity = (BaseActivity) getActivity();
        }
    }

    /**
     * 显示小提示
     */
    public void showToastInChildThread(String text) {
        mActivity.showToastInChildThread(text);
    }

    /**
     * 显示提示框
     */
    public Dialog showAlert(String text) {
        return mActivity.showAlert(text);
    }

    /**
     * 显示确定框
     */
    public Dialog showAlert(String titile, String text, String ok,
                            String cancel, TlcyDialog.TlcyDialogListener okListener,
                            TlcyDialog.TlcyDialogListener cancelListener) {
        return mActivity.showAlert(titile, text, ok, cancel, okListener,
                cancelListener);
    }

    /**
     * 此方法自动判断当前activity是否正在显示， 并作出直接显示还是等显示再在弹出dialog
     */
    public Dialog showAlert(String text, TlcyDialog.TlcyDialogListener okListener,
                            TlcyDialog.TlcyDialogListener cancelListener) {
        return mActivity.showAlert(text, okListener, cancelListener);
    }
}
