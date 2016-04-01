package com.sb.meeting.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.YellowPageService;
import com.sb.meeting.ui.activity.CompanyVIPDetailActivity_;
import com.sb.meeting.ui.adapter.CompanyListAdapter;
import com.sb.meeting.ui.adapter.StudentListAdapter;
import com.sb.meeting.ui.component.RefreshListView;
import com.sb.meeting.ui.vo.CompanyVO;
import com.sb.meeting.ui.vo.StudentVO;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 黄页 fragment
 * Created by sun on 2016/3/28.
 */
@EFragment(R.layout.layout_tab_yp)
public class TabYellowPageFragment extends BaseFragment implements RefreshListView.OnRefreshLoadMoreListener {

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
    private List<CompanyVO> companyList;
    private YPType ypType;
    private YellowPageService yellowPageService;
    private int companyPageIndex;
    private String companyName;
    private int companyAreaId;
    private CompanyListAdapter companyListAdapter;
    private int studentPageIndex;
    private String studentName;
    private int classId;
    private int studentAreaId;
    private StudentListAdapter studentListAdapter;

    public enum YPType {
        STUDENT, COMPANY
    }

    @AfterViews
    void init() {
        anim = (AnimationDrawable) ivLoadingIn.getBackground();
        yellowPageService = new YellowPageService(getActivity());
        lvCompany.setVisibility(View.GONE);
        lvCompany.setOnRefreshListener(this);
        lvCompany.setLastUpdateTimeRelateObject(this);
        lvStudent.setVisibility(View.GONE);
        lvStudent.setOnRefreshListener(this);
        lvStudent.setLastUpdateTimeRelateObject(this);
    }

    public void refreshView() {
        if (Utils.isEmpty(studentList) && Utils.isEmpty(companyList)) {
            switchToStudent();
        }
    }

    /**
     * 切换到学员名片页面
     */
    @Click(R.id.tv_student)
    void switchToStudent() {
        ypType = YPType.STUDENT;
        tvCompany.setBackgroundColor(Color.WHITE);
        tvStudent.setBackgroundColor(Color.TRANSPARENT);
        if (!Utils.isEmpty(studentList)) {
            // 直接切换显示
            stopLoadingSelf();
            lvCompany.setVisibility(View.GONE);
            lvStudent.setVisibility(View.VISIBLE);
        } else {
            // 获取数据
            startLoadingSelf();
            TaskManager.pushTask(new Task(TaskAction.ACTION_INIT_STUDENT) {
                @Override
                protected void doBackground() throws Exception {
                    setReturnData(yellowPageService.getStudentListFromDB());
                }
            }, this);
        }
    }

    /**
     * 切换到企业列表页面
     */
    @Click(R.id.tv_company)
    void switchToCompany() {
        ypType = YPType.COMPANY;
        tvCompany.setBackgroundColor(Color.TRANSPARENT);
        tvStudent.setBackgroundColor(Color.WHITE);
        if (!Utils.isEmpty(companyList)) {
            // 直接切换显示
            stopLoadingSelf();
            lvCompany.setVisibility(View.VISIBLE);
            lvStudent.setVisibility(View.GONE);
        } else {
            // 获取数据
            startLoadingSelf();
            TaskManager.pushTask(new Task(TaskAction.ACTION_INIT_COMPANY) {
                @Override
                protected void doBackground() throws Exception {
                    setReturnData(yellowPageService.getCompanyListFromDB());
                }
            }, this);
        }
    }

    /**
     * 任务执行成功 回调
     *
     * @param action task标志
     * @param data   返回数据
     */
    public void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_INIT_COMPANY:// 初始化企业列表
                if (data != null) {
                    stopLoadingSelf();
                    companyList = (List<CompanyVO>) data;
                    refreshCompanyUI();
                    if (!Utils.isEmpty(companyList) && companyList.size() != BonConstants.LIMIT_GET_COMPANY) {
                        lvCompany.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_REMOVE);
                    }
                }
                refreshCompany();
                break;
            case TaskAction.ACTION_REFRESH_COMPANY:// 刷新企业列表
                lvCompany.onRefreshComplete();
                stopLoadingSelf();
                if (data != null) {
                    companyList = (List<CompanyVO>) data;
                    refreshCompanyUI();
                    if (!Utils.isEmpty(companyList) && companyList.size() != BonConstants.LIMIT_GET_COMPANY) {
                        lvCompany.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_REMOVE);
                    } else {
                        lvCompany.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                    }
                } else {
                    showErrorMsg("暂无数据");
                }
                break;
            case TaskAction.ACTION_LOAD_MORE_COMPANY:// 加载更多企业
                if (data != null) {
                    List<CompanyVO> moreData = (List<CompanyVO>) data;
                    if (Utils.isEmpty(moreData) || (!Utils.isEmpty(moreData) && moreData.size() != BonConstants.LIMIT_GET_COMPANY)) {
                        lvCompany.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_OVER);
                    } else {
                        lvCompany.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                    }
                    companyList.addAll(moreData);
                    refreshCompanyUI();
                }
                break;
            case TaskAction.ACTION_INIT_STUDENT:// 初始化学员名片
                if (data != null) {
                    stopLoadingSelf();
                    studentList = (List<StudentVO>) data;
                    refreshStudentUI();
                    if (!Utils.isEmpty(studentList) && studentList.size() != BonConstants.LIMIT_GET_STUDENT) {
                        lvStudent.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_REMOVE);
                    }
                }
                refreshStudent();
                break;
            case TaskAction.ACTION_REFRESH_STUDENT:// 刷新学员列表
                lvStudent.onRefreshComplete();
                stopLoadingSelf();
                if (data != null) {
                    studentList = (List<StudentVO>) data;
                    refreshStudentUI();
                    if (!Utils.isEmpty(studentList) && studentList.size() != BonConstants.LIMIT_GET_STUDENT) {
                        lvStudent.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_REMOVE);
                    } else {
                        lvStudent.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                    }
                } else {
                    showErrorMsg("暂无数据");
                }
                break;
            case TaskAction.ACTION_LOAD_MORE_STUDENT:// 加载更多学员列表
                if (data != null) {
                    List<StudentVO> moreData = (List<StudentVO>) data;
                    if (Utils.isEmpty(moreData) || (!Utils.isEmpty(moreData) && moreData.size() != BonConstants.LIMIT_GET_STUDENT)) {
                        lvStudent.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_OVER);
                    } else {
                        lvStudent.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NORMAL);
                    }
                    studentList.addAll(moreData);
                    refreshStudentUI();
                }
                break;
        }
    }

    /**
     * 刷新学员列表
     */
    private void refreshStudent() {
        studentPageIndex = 0;
        TaskManager.pushTask(new Task(TaskAction.ACTION_REFRESH_STUDENT) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(yellowPageService.getStudentList(studentName, classId, studentAreaId, studentPageIndex));
            }
        }, this);
    }

    /**
     * 刷新 学员列表 UI界面
     */
    private void refreshStudentUI() {
        if (!Utils.isEmpty(studentList)) {
            lvCompany.setVisibility(View.GONE);
            lvStudent.setVisibility(View.VISIBLE);
            if (studentListAdapter == null) {
                studentListAdapter = new StudentListAdapter(getActivity());
                studentListAdapter.setData(studentList);
                lvStudent.setAdapter(studentListAdapter);
            } else {
                studentListAdapter.setData(studentList);
                studentListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 刷新 企业列表
     */
    private void refreshCompany() {
        companyPageIndex = 0;
        TaskManager.pushTask(new Task(TaskAction.ACTION_REFRESH_COMPANY) {
            @Override
            protected void doBackground() throws Exception {
                setReturnData(yellowPageService.getCompanyList(companyName, companyAreaId, companyPageIndex));
            }
        }, this);
    }

    /**
     * 刷新企业列表UI界面
     */
    private void refreshCompanyUI() {
        if (!Utils.isEmpty(companyList)) {
            lvCompany.setVisibility(View.VISIBLE);
            lvStudent.setVisibility(View.GONE);
            if (companyListAdapter == null) {
                companyListAdapter = new CompanyListAdapter(getActivity());
                companyListAdapter.setData(companyList);
                lvCompany.setAdapter(companyListAdapter);
            } else {
                companyListAdapter.setData(companyList);
                companyListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 任务执行失败 回调
     *
     * @param action       task标志
     * @param errorMessage 错误信息
     */
    public void onTaskFail(int action, String errorMessage) {
        switch (action) {
            case TaskAction.ACTION_REFRESH_COMPANY:
                lvCompany.onRefreshComplete();
                showToast(errorMessage);
                break;
            case TaskAction.ACTION_LOAD_MORE_COMPANY:
                lvCompany.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NETWORK_DISABLE);
                showToast(errorMessage);
                break;
            case TaskAction.ACTION_REFRESH_STUDENT:
                lvStudent.onRefreshComplete();
                showToast(errorMessage);
                break;
            case TaskAction.ACTION_LOAD_MORE_STUDENT:
                lvStudent.onLoadMoreComplete(RefreshListView.LoadMoreState.LV_NETWORK_DISABLE);
                showToast(errorMessage);
                break;
            default:
                stopLoadingSelf();
                showErrorMsg(errorMessage);
                break;
        }
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
        if ((!Utils.isEmpty(studentList) && ypType == YPType.STUDENT)
                || (!Utils.isEmpty(companyList) && ypType == YPType.COMPANY)) {
            showToast(msg);
        } else {
            layoutError.setVisibility(View.VISIBLE);
            btnErrorRefresh.setText("刷新");
            tvErrorMsg.setText(msg);
        }
    }

    @Override
    public void onRefresh() {
        if (ypType == YPType.COMPANY) {
            refreshCompany();
        } else if (ypType == YPType.STUDENT) {
            refreshStudent();
        }
    }

    @Override
    public void onLoadMore() {
        if (ypType == YPType.COMPANY) {
            companyPageIndex++;
            TaskManager.pushTask(new Task(TaskAction.ACTION_LOAD_MORE_COMPANY) {
                @Override
                protected void doBackground() throws Exception {
                    setReturnData(yellowPageService.getCompanyList(companyName, companyAreaId, companyPageIndex));
                }
            }, this);
        } else if (ypType == YPType.STUDENT) {
            studentPageIndex++;
            TaskManager.pushTask(new Task(TaskAction.ACTION_LOAD_MORE_STUDENT) {
                @Override
                protected void doBackground() throws Exception {
                    setReturnData(yellowPageService.getStudentList(studentName, classId, studentAreaId, studentPageIndex));
                }
            }, this);
        }
    }

    @Click(R.id.btn_refresh)
    void clickRefresh() {
        startLoadingSelf();
        onRefresh();
    }

    @ItemClick({R.id.lv_company, R.id.lv_student})
    void onItemClick(int position) {
        if (ypType == YPType.COMPANY) {
            CompanyVO vo = companyList.get(position);
            // TODO 判断 vip  跳转不同页面
            CompanyVIPDetailActivity_.intent(this).extra(IParam.COMPANY_ID, vo.getCompanyId()).start();
        } else if (ypType == YPType.STUDENT) {

        }
    }
}
