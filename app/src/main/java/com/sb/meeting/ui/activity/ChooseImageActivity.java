package com.sb.meeting.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sb.meeting.R;
import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.TaskAction;
import com.sb.meeting.common.Utils;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.ui.adapter.ChooseImgAdapter;
import com.sb.meeting.ui.component.TlcyDialog;
import com.taskmanager.Task;
import com.taskmanager.TaskManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择图片 activity
 * Created by sun on 2016/2/22.
 */
@EActivity(R.layout.layout_choose_image)
public class ChooseImageActivity extends BaseActivity implements ChooseImgAdapter.OnSelectImageListener {
    @ViewById(R.id.iv_back)
    ImageView ivBack;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.btn_right)
    Button btnRight;
    @ViewById(R.id.gv_image)
    GridView gvImage;
    @Extra(IParam.LAST_NUM)
    int lastNum = 9;
    @Extra(IParam.IS_SELECT_USER_HEAD)
    boolean isSelectUserHead;


    private List<String> allImgs = new ArrayList<>();
    private Map<String, List<String>> dirAndFile = new LinkedHashMap<>();
    private ChooseImgAdapter adapter;
    private List<String> selectedData;
    private Uri imageUri = null;


    @AfterViews
    void init() {
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("最近图片");
        btnRight.setBackgroundColor(Color.TRANSPARENT);
        btnRight.setText("完成");
        btnRight.setEnabled(false);
        btnRight.setVisibility((isSelectUserHead || lastNum == 1) ? View.GONE
                : View.VISIBLE);
        getImages();
    }

    /**
     * 获取图片
     */
    private void getImages() {
        startLoading("图片获取中...");
        TaskManager.pushTask(new Task(TaskAction.ACTION_GET_SYSTEM_IMAGE) {
            @Override
            protected void doBackground() throws Exception {
                List<String> allImgs = new ArrayList<>();
                Map<String, List<String>> dirAndFile = new LinkedHashMap<>();
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                if (mCursor != null && mCursor.getCount() > 0) {
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        String path = mCursor
                                .getString(mCursor
                                        .getColumnIndex(MediaStore.Images.Media.DATA));

                        // 获取该图片的父路径名
                        File file = new File(path);
                        if (file.exists() && file.length() > 0) {
                            File pa_file = file.getParentFile();
                            String parentName = pa_file.getAbsolutePath();
                            allImgs.add(path);
                            // 根据父路径名将图片放入到mGruopMap中
                            if (!dirAndFile.containsKey(parentName)) {
                                ArrayList<String> chileList = new ArrayList<>();
                                chileList.add(path);
                                dirAndFile.put(parentName, chileList);
                            } else {
                                dirAndFile.get(parentName).add(path);
                            }
                        }
                    }
                    mCursor.close();
                }
                setReturnData(new Object[]{allImgs, dirAndFile});
            }
        }, this);
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {
        switch (action) {
            case TaskAction.ACTION_GET_SYSTEM_IMAGE:
                stopLoading();
                if (data != null) {
                    Object[] array = (Object[]) data;
                    this.allImgs = (List<String>) array[0];
                    this.dirAndFile = (Map<String, List<String>>) array[1];
                    refreshUI();
                }
                break;
        }
    }

    /**
     * 刷新界面
     */
    private void refreshUI() {
        if (allImgs != null) {
            if (adapter == null) {
                adapter = new ChooseImgAdapter(this, gvImage, lastNum);
                adapter.isHead(isSelectUserHead);
                adapter.setOnSelectedListener(this);
                adapter.setData(allImgs);
                gvImage.setAdapter(adapter);
            } else {
                adapter.setData(allImgs);
                adapter.isHead(isSelectUserHead);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSelected(List<String> seletedImg) {
        this.selectedData = seletedImg;
        if (!Utils.isEmpty(seletedImg)) {
            btnRight.setEnabled(true);
            btnRight.setText("完成 (" + seletedImg.size() + "/" + lastNum + ")");
        } else {
            btnRight.setText("完成");
            btnRight.setEnabled(false);
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
        if (position == 0) {
            // 选择拍照
            takePhoto();
        } else {
            if (isSelectUserHead || lastNum == 1) {
                // 判断图片大小
                final String path = allImgs.get(position - 1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                options.inJustDecodeBounds = false;
                if (isSelectUserHead
                        && (options.outWidth < BonConstants.AVATAR_WIDTH || options.outHeight < BonConstants.AVATAR_WIDTH)) {
                    showAlert("消息提示", "所选图片达不到最佳展示效果,确定要使用吗？", "使用", "重新选择",
                            new TlcyDialog.TlcyDialogListener() {

                                @Override
                                public void onClick() {
                                    selectedData = new ArrayList<>();
                                    selectedData.add(path);
                                    onSelectComplete();
                                }
                            }, null);
                } else {
                    selectedData = new ArrayList<>();
                    selectedData.add(path);
                    onSelectComplete();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (imageUri != null) {
                MediaScannerConnection.scanFile(this,
                        new String[]{imageUri.getPath()}, null, null);
                if (Utils.isEmpty(selectedData)) {
                    selectedData = new ArrayList<>();
                }
                selectedData.add(imageUri.getPath());
                onSelectComplete();
            }
        }
    }

    /**
     * 选择结束
     */
    @Click(R.id.btn_right)
    void onSelectComplete() {
        Intent data = new Intent();
        data.putExtra(IParam.CONTENT, (Serializable) selectedData);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (!Utils.isEmpty(selectedData) && selectedData.size() == lastNum) {
            showToast("最多只能选择" + lastNum + "张图片");
            return;
        }
        File dir = new File(BonConstants.PATH_TAKE_PHOTO);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        imageUri = Uri.fromFile(new File(dir, System.currentTimeMillis()
                + ".png"));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
    }
}
