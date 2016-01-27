package com.zhengshang.meeting.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.taskmanager.ui.TaskActivity;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.ui.component.TlcyDialog;

/**
 * activity 基类 主要封装 toast 、 dialog 、 loading等内容
 * Created by sun on 2015/12/9.
 */
public abstract class BaseActivity extends TaskActivity {

    private Dialog tempDialog;// 处理连续快速点击出现多个对话框的情况
    private Dialog toDialog = null;
    private boolean isShowing = true;// 当前activity是否显示中
    private Dialog loadingDialog;



    public void startLoading(String message, boolean cancelable, DialogInterface.OnCancelListener listener) {
        if (loadingDialog == null || !loadingDialog.isShowing()) {
            loadingDialog = new Dialog(this, R.style.loadingDialogStyle);
            loadingDialog.setContentView(R.layout.layout_loading_dialog);
            TextView tvText = (TextView) loadingDialog.findViewById(R.id.tv_message);
            if (!Utils.isEmpty(message)) {
                tvText.setText(message);
            } else {
                tvText.setText(getString(R.string.loading));
            }
            loadingDialog.setCancelable(cancelable);
            loadingDialog.setOnCancelListener(listener);
            loadingDialog.show();
        }
    }

    public void startLoading(String message, boolean cancelable) {
        startLoading(message, cancelable, null);
    }

    public void startLoading(String message) {
        startLoading(message, false);
    }

    public void startLoading(boolean cancelable){
        startLoading(null,cancelable);
    }

    public void startLoading() {
        startLoading(null);
    }

    public void stopLoading() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
            loadingDialog = null;
        }
    }

    /**
     * 显示小提示
     */
    public void showToastLongTime(String text) {
        if (mToast != null) {
            mToast.setText(text);
            mToast.show();
        } else {
            mToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
    }

    /**
     * 在子线程中显示小提示
     */
    public void showToastInChildThread(String text) {
        try {
            Looper.prepare();
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
            Looper.loop();
        } catch (Exception e) {

        }
    }

    public Dialog showDialog(Dialog dialog) {
        if (tempDialog != null && tempDialog.isShowing()) {
            tempDialog.dismiss();
        }
        if (isShowing) {
            tempDialog = dialog;
            tempDialog.show();
        } else {
            toDialog = dialog;
        }
        return dialog;
    }

    public Dialog showAlert(String text) {
        return showDialog(new TlcyDialog(this)
                .setTitle(getString(R.string.tip)).setMessage(text)
                .setOnlyOkPositiveMethod(getString(R.string.ok)));
    }

    public Dialog showAlert(String title, String text, String ok,
                            String cancel, TlcyDialog.TlcyDialogListener okListener,
                            TlcyDialog.TlcyDialogListener cancelListener) {
        return showDialog(new TlcyDialog(this).setTitle(title).setMessage(text)
                .setButton(ok, cancel, okListener, cancelListener));
    }

    public Dialog showAlert(String title, String text, String text2, String ok,
                            String cancel, TlcyDialog.TlcyDialogListener okListener,
                            TlcyDialog.TlcyDialogListener cancelListener) {
        return showDialog(new TlcyDialog(this).setTitle(title).setMessage(text)
                .setMessageTwo(text2)
                .setButton(ok, cancel, okListener, cancelListener));
    }

    public Dialog showAlert(String text, TlcyDialog.TlcyDialogListener okListener,
                            TlcyDialog.TlcyDialogListener cancelListener) {
        return showDialog(new TlcyDialog(this)
                .setTitle(getString(R.string.tip))
                .setMessage(text)
                .setButton(getString(R.string.ok), getString(R.string.cancel),
                        okListener, cancelListener));
    }

    @Override
    protected void onPause() {
        super.onPause();
        isShowing = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowing = true;
        if (toDialog != null) {
            toDialog.show();
            toDialog = null;
        }
    }
}
