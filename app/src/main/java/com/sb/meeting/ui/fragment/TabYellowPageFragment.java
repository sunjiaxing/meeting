package com.sb.meeting.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.dao.entity.Area;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.service.YellowPageService;
import com.sb.meeting.ui.activity.CompanyVIPDetailActivity_;
import com.sb.meeting.ui.adapter.CompanyListAdapter;
import com.sb.meeting.ui.adapter.SearchConditionAdapter;
import com.sb.meeting.ui.adapter.StudentListAdapter;
import com.sb.meeting.ui.component.GridViewWithScroll;
import com.sb.meeting.ui.component.RefreshListView;
import com.sb.meeting.ui.vo.ClassVO;
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

    @ViewById(R.id.sv_search)
    ScrollView svSearch;

    @ViewById(R.id.edit_name)
    EditText editName;
    @ViewById(R.id.gv_province)
    GridViewWithScroll gvArea;
    @ViewById(R.id.gv_class)
    GridViewWithScroll gvClass;
    @ViewById(R.id.tv_name_tip)
    TextView tvNameTip;
    @ViewById(R.id.tv_area_tip)
    TextView tvAreaTip;
    @ViewById(R.id.tv_class_tip)
    TextView tvClassTip;

    private AnimationDrawable anim;
    private List<StudentVO> studentList;
    private List<CompanyVO> companyList;
    private List<Area> areaList;
    private List<ClassVO> classList;
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
    private boolean searchMenuOpen;
    private Animation searchAnimation;
    private SearchConditionAdapter areaAdapter;
    private SearchConditionAdapter classAdapter;

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
        if (Utils.isEmpty(areaList)) {
            TaskManager.pushTask(new Task(TaskAction.ACTION_GET_SEARCH_AREA_CONDITION) {
                @Override
                protected void doBackground() throws Exception {
                    setReturnData(yellowPageService.getProvinceList());
                }
            }, getActivity());
        }

        if (Utils.isEmpty(classList)) {
            TaskManager.pushTask(new Task(TaskAction.ACTION_GET_SEARCH_CLASS_CONDITION) {
                @Override
                protected void doBackground() throws Exception {
                    setReturnData(yellowPageService.getClassList());
                }
            }, getActivity());
        }
    }

    /**
     * 切换到学员名片页面
     */
    @Click(R.id.tv_student)
    void switchToStudent() {
        if (ypType == YPType.STUDENT) {
            return;
        }
        svSearch.setVisibility(View.GONE);
        searchMenuOpen = false;
        ypType = YPType.STUDENT;
        tvCompany.setBackgroundResource(R.drawable.yp_switch_shape_right_nomal);
        tvCompany.setTextColor(Color.parseColor("#E0E0E0"));
        tvStudent.setBackgroundResource(R.drawable.yp_switch_shape_left_selected);
        tvStudent.setTextColor(Color.WHITE);
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
        if (ypType == YPType.COMPANY) {
            return;
        }
        svSearch.setVisibility(View.GONE);
        searchMenuOpen = false;
        ypType = YPType.COMPANY;
        tvStudent.setBackgroundResource(R.drawable.yp_switch_shape_left_nomal);
        tvStudent.setTextColor(Color.parseColor("#E0E0E0"));
        tvCompany.setBackgroundResource(R.drawable.yp_switch_shape_right_selected);
        tvCompany.setTextColor(Color.WHITE);
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
            case TaskAction.ACTION_REFRESH_COMPANY:// 刷新企业列表
            case TaskAction.ACTION_LOAD_MORE_COMPANY:// 加载更多企业
                onCompanyTaskSuccess(action, data);
                break;
            case TaskAction.ACTION_INIT_STUDENT:// 初始化学员名片
            case TaskAction.ACTION_REFRESH_STUDENT:// 刷新学员列表
            case TaskAction.ACTION_LOAD_MORE_STUDENT:// 加载更多学员列表
                onStudentTaskSuccess(action, data);
                break;
            case TaskAction.ACTION_GET_SEARCH_AREA_CONDITION:// 地区查询条件
                if (data != null) {
                    areaList = (List<Area>) data;
                }
                break;
            case TaskAction.ACTION_GET_SEARCH_CLASS_CONDITION://班级查询条件
                if (data != null) {
                    classList = (List<ClassVO>) data;
                }
                break;
        }
    }

    /**
     * 企业 相关任务  成功回调
     *
     * @param action action
     * @param data   data
     */
    private void onCompanyTaskSuccess(int action, Object data) {
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
                    companyList = null;
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
        }
    }

    /**
     * 学员名片相关任务 成功回调
     *
     * @param action action
     * @param data   data
     */
    private void onStudentTaskSuccess(int action, Object data) {
        switch (action) {
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
                    studentList = null;
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
                studentListAdapter = new StudentListAdapter(getActivity()) {

                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        StudentVO vo = studentList.get(position);
                        // TODO 判断 vip  跳转不同页面
                        if (vo.getCompanyId() > 0) {
                            CompanyVIPDetailActivity_.intent(getActivity())
                                    .extra(IParam.COMPANY_ID, vo.getCompanyId())
                                    .start();
                        }

                    }
                };
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

    /**
     * 点击刷新
     */
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
        }
    }

    /**
     * 操作 筛选菜单
     */
    @Click(R.id.tv_search)
    void clickSearchMenu() {
        if (searchMenuOpen) {
            // 关闭
            svSearch.setVisibility(View.GONE);
            searchMenuOpen = false;
        } else {
            // 打开
            if (searchAnimation == null) {
                searchAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.search_condition);
            }
            svSearch.setVisibility(View.VISIBLE);
            svSearch.startAnimation(searchAnimation);
            searchMenuOpen = true;
            if (ypType == YPType.STUDENT) {
                // 加载学员相关信息
                tvNameTip.setText("学员姓名");
                editName.setHint("请输入学员姓名");
                editName.setText(studentName);
                editName.setSelection(editName.length());
                tvAreaTip.setText("学员所在地区");
                tvClassTip.setVisibility(View.VISIBLE);
                gvClass.setVisibility(View.VISIBLE);
                loadClassSearchCondition();
            } else {
                // 加载企业相关信息
                tvNameTip.setText("企业名称");
                editName.setHint("请输入企业名称");
                editName.setText(companyName);
                editName.setSelection(editName.length());
                tvAreaTip.setText("企业所在地区");
                tvClassTip.setVisibility(View.GONE);
                gvClass.setVisibility(View.GONE);
            }
            loadAreaSearchCondition();
        }
    }

    /**
     * 加载班级查询条件
     */
    private void loadClassSearchCondition() {
        // 加载班级信息
        if (classAdapter == null) {
            classAdapter = new SearchConditionAdapter(getActivity());
            classAdapter.setClassData(classList);
            classAdapter.setSelectClass(classId);
            gvClass.setAdapter(classAdapter);
        } else {
            classAdapter.setClassData(classList);
            classAdapter.setSelectClass(classId);
            classAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载地区查询条件
     */
    private void loadAreaSearchCondition() {
        // 加载地区信息
        if (areaAdapter == null) {
            areaAdapter = new SearchConditionAdapter(getActivity());
            areaAdapter.setAreaData(areaList.subList(0, 4));
            areaAdapter.setSelectArea(ypType == YPType.STUDENT ? studentAreaId : companyAreaId);
            gvArea.setAdapter(areaAdapter);
        } else {
            areaAdapter.setAreaData(areaList.subList(0, 4));
            areaAdapter.setSelectArea(ypType == YPType.STUDENT ? studentAreaId : companyAreaId);
            areaAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 选中地区
     *
     * @param position 选中项索引
     */
    @ItemClick(R.id.gv_province)
    void onAreaItemClick(int position) {
        Area area = areaList.get(position);
        if (ypType == YPType.STUDENT) {
            if (studentAreaId == area.getAreaId()) {
                studentAreaId = 0;
            } else {
                studentAreaId = area.getAreaId();
            }
        } else if (ypType == YPType.COMPANY) {
            if (companyAreaId == area.getAreaId()) {
                companyAreaId = 0;
            } else {
                companyAreaId = area.getAreaId();
            }
        }
        loadAreaSearchCondition();
    }

    /**
     * 选中班级
     *
     * @param position 选中项索引
     */
    @ItemClick(R.id.gv_class)
    void onClassItemClick(int position) {
        ClassVO vo = classList.get(position);
        if (classId == vo.getClassId()) {
            classId = 0;
        } else {
            classId = vo.getClassId();
        }
        loadClassSearchCondition();
    }

    /**
     * 确认查询
     */
    @Click(R.id.tv_click_search)
    void clickSearch() {
        // 关闭菜单
        svSearch.setVisibility(View.GONE);
        searchMenuOpen = false;
        // 执行刷新
        if (ypType == YPType.STUDENT) {
            studentName = editName.getText().toString();
            refreshStudent();
        } else {
            companyName = editName.getText().toString();
            refreshCompany();
        }
    }

    /**
     * 清空查询条件
     */
    @Click(R.id.tv_click_reset)
    void clickReset() {
        if (ypType == YPType.STUDENT) {
            studentAreaId = 0;
            studentName = null;
            classId = 0;
            editName.setText("");
            loadClassSearchCondition();
        } else {
            companyAreaId = 0;
            companyName = null;
            editName.setText("");
        }
        loadAreaSearchCondition();
    }
}
