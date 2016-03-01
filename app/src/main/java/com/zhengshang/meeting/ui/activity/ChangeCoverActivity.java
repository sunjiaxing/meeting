package com.zhengshang.meeting.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhengshang.meeting.R;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.ui.adapter.ChangeCoverAdapter;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import java.util.List;

/**
 * 选择图片 activity
 * Created by sun on 2016/2/22.
 */
@EActivity(R.layout.layout_choose_image)
public class ChangeCoverActivity extends BaseActivity {
    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.gv_image)
    GridView gvImage;

    private List<String> allImgs;

    private ChangeCoverAdapter adapter;

    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("切换封面");
        allImgs = (List<String>) getIntent().getSerializableExtra(IParam.CONTENT);
        refreshUI();
    }


    @Override
    protected void onTaskSuccess(int action, Object data) {

    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        if (allImgs != null) {
            if (adapter == null) {
                adapter = new ChangeCoverAdapter(this, gvImage);
                adapter.setData(allImgs);
                gvImage.setAdapter(adapter);
            } else {
                adapter.setData(allImgs);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Click(R.id.iv_back)
    void back() {
        System.gc();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @ItemClick(R.id.gv_image)
    void onItemClick(int position) {
        Intent data = new Intent();
        data.putExtra(IParam.CONTENT, allImgs.get(position));
        setResult(RESULT_OK, data);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
    }
}
