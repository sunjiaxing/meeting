package com.sb.meeting.service;

import android.content.Context;

import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.sb.meeting.common.MD5;
import com.sb.meeting.common.Utils;
import com.sb.meeting.dao.ConfigDao;
import com.sb.meeting.remote.BaseRO;
import com.sb.meeting.remote.dto.ConfigDto;
import com.sb.meeting.ui.MeetingApp;

import org.json.JSONException;

import java.io.File;
import java.util.UUID;

/**
 * base Service
 */
public class BaseService {
    protected Context mContext;
    protected ConfigDao configDao;
    private BaseRO baseRO;
    private UploadManager uploadManager;

    public BaseService(Context context) {
        this.mContext = context;
        this.configDao = ConfigDao.getInstance(context);
        this.baseRO = new BaseRO(context);
        uploadManager = MeetingApp.getInstance().getUploadManager();
    }

    /**
     * 上传文件
     *
     * @param file              要上传的文件
     * @param completionHandler 上传回调接口
     */
    public void uploadFile(File file, UpCompletionHandler completionHandler) throws JSONException {
        String fileName = MD5.md5Lower(UUID.randomUUID().toString());
        String token = configDao.getQiniuToken();
        if (Utils.isEmpty(token)) {
            getConfigInfoFromWeb();
        }
        uploadManager.put(file, fileName, token, completionHandler, null);
    }

    /**
     * 获取系统配置信息
     *
     * @throws JSONException
     */
    public void getConfigInfoFromWeb() throws JSONException {
        ConfigDto dto = baseRO.getConfig();
        if (dto != null) {
            configDao.setQiniuToken(dto.getQiniuToken());
        }
    }
}
