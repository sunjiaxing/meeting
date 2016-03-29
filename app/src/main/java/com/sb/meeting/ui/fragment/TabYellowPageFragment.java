package com.sb.meeting.ui.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.Utils;
import com.sb.meeting.ui.component.RefreshListView;
import com.sb.meeting.ui.vo.StudentVO;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 黄页 fragment
 * Created by sun on 2016/3/28.
 */
@EFragment(R.layout.layout_tab_yp)
public class TabYellowPageFragment extends BaseFragment {

    @ViewById(R.id.tv_student)
    TextView tvStudent;
    @ViewById(R.id.tv_company)
    TextView tvCompany;
    @ViewById(R.id.lv_student)
    RefreshListView lvStudent;
    @ViewById(R.id.lv_company)
    RefreshListView lvCompany;

    @ViewById(R.id.layout_loading_yp)
    View layoutLoading;
    @ViewById(R.id.layout_error_yp)
    View layoutError;
    @ViewById(R.id.tv_description)
    TextView tvErrorMsg;
    @ViewById(R.id.btn_refresh)
    Button btnErrorRefresh;
    @ViewById(R.id.iv_loading_in)
    ImageView ivLoadingIn;

    private AnimationDrawable anim;
    private List<StudentVO> studentList;

    public enum YPType {
        STUDENT, COMPANY
    }

    @AfterViews
    void init() {
        anim = (AnimationDrawable) ivLoadingIn.getBackground();
    }

    public void refreshView() {

    }

    public void onTaskSuccess(int action, Object data) {

    }

    public void onTaskFail(int action, String errorMessage) {

    }

    /**
     * 开启loading
     */
    private void startLoadingSelf() {
        if (layoutLoading != null && layoutError != null && anim != null) {
            layoutLoading.setVisibility(View.VISIBLE);
            anim.start();
            layoutError.setVisibility(View.GONE);
        }
    }

    /**
     * 关闭loading
     */
    private void stopLoadingSelf() {
        if (layoutLoading != null && layoutError != null && anim != null) {
            layoutLoading.setVisibility(View.GONE);
            anim.stop();
            layoutError.setVisibility(View.GONE);
        }
    }

    /**
     * 显示错误信息
     *
     * @param msg 错误信息
     */
    private void showErrorMsg(String msg) {
        if (Utils.isEmpty(msg)) {
            msg = getString(R.string.netconnecterror);
        }
        if (!Utils.isEmpty(studentList)) {
            showToast(msg);
        } else {
            layoutError.setVisibility(View.VISIBLE);
            btnErrorRefresh.setText("刷新");
            tvErrorMsg.setText(msg);
        }
    }
}
