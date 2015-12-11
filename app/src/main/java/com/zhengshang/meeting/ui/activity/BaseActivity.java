package com.zhengshang.meeting.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.taskmanager.ui.TaskActivity;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.ui.component.TlcyDialog;

/**
 * Created by sun on 2015/12/9.
 */
public abstract class BaseActivity extends TaskActivity {

    private Dialog tempDialog;// 处理连续快速点击出现多个对话框的情况
    private Dialog toDialog = null;
    private boolean isShowing = true;// 当前activity是否显示中

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

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
