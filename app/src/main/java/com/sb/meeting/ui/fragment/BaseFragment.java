package com.sb.meeting.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.activity.BaseActivity;
import com.sb.meeting.ui.component.TlcyDialog;

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

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

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

    public void startLoading(String message, boolean cancelable, DialogInterface.OnCancelListener listener) {
        mActivity.startLoading(message, cancelable, listener);
    }

    public void startLoading(String message, boolean cancelable) {
        startLoading(message, cancelable, null);
    }

    public void startLoading(String message) {
        startLoading(message, false);
    }

    public void startLoading(boolean cancelable) {
        startLoading(null, cancelable);
    }

    public void startLoading() {
        startLoading(null);
    }

    public void stopLoading() {
        mActivity.stopLoading();
    }
}
