package com.sb.meeting.remote;

import android.content.Context;

import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.Utils;
import com.sb.meeting.exeception.AppException;
import com.sb.meeting.remote.dto.CompanyDetailDto;
import com.sb.meeting.remote.dto.CompanyDto;
import com.sb.meeting.remote.dto.StudentDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 黄页 RO
 * Created by sun on 2016/3/29.
 */
public class YellowPageRO extends BaseRO {

    public YellowPageRO(Context context) {
        super(context);
    }

    public enum RemoteCompanyURL implements IBaseURL {
        COMPANY_LIST(IParam.COMPANY_LIST), DETAIL(IParam.COMPANY_DETAIL);
        private static final String NAMESPACE = IParam.YP;
        private String url;

        RemoteCompanyURL(String mapping) {
            url = NAMESPACE + BonConstants.SLASH + mapping;
        }

        @Override
        public String getURL() {
            return url;
        }
    }

    public enum RemoteStudentURL implements IBaseURL {
        LIST(IParam.LIST);
        private static final String NAMESPACE = IParam.STUDENT;
        private String url;

        RemoteStudentURL(String mapping) {
            url = NAMESPACE + BonConstants.SLASH + mapping;
        }

        @Override
        public String getURL() {
            return url;
        }
    }

    /**
     * 获取企业列表
     *
     * @param areaId    区域id
     * @param name      企业名称
     * @param pageIndex 页码
     * @param limit     每页记录条数
     * @return
     * @throws JSONException
     */
    public List<CompanyDto> getCompanyList(int areaId, String name, int pageIndex, int limit) throws JSONException {
        String url = getServerUrl() + RemoteCompanyURL.COMPANY_LIST.getURL();
        Map<String, Object> params = new HashMap<>();
        if (areaId != 0) {
            params.put(IParam.AREA_ID, areaId);
        }
        if (!Utils.isEmpty(name)) {
            params.put(IParam.NAME, name);
        }
        params.put(IParam.PAGE_INDEX, pageIndex);
        params.put(IParam.LIMIT, limit);
        String result = httpPostRequest(url, null, params);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            JSONArray array = json.getJSONArray(IParam.LIST);
            List<CompanyDto> data = new ArrayList<>();
            CompanyDto dto;
            for (int i = 0; i < array.length(); i++) {
                dto = new CompanyDto();
                dto.parseJson(array.getJSONObject(i));
                data.add(dto);
            }
            return data;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 获取学员 列表
     *
     * @param name      学员姓名 （模糊查询）
     * @param classId   班级id
     * @param areaId    区域id
     * @param limit     每页记录条数
     * @param pageIndex 页码
     * @return
     * @throws JSONException
     */
    public List<StudentDto> getStudentList(String name, int classId, int areaId, int limit, int pageIndex) throws JSONException {
        String url = getServerUrl() + RemoteStudentURL.LIST.getURL();
        Map<String, Object> params = new HashMap<>();
        if (!Utils.isEmpty(name)) {
            params.put(IParam.NAME, name);
        }
        if (classId > 0) {
            params.put(IParam.CLASS_ID, classId);
        }
        if (areaId > 0) {
            params.put(IParam.AREA_ID, areaId);
        }
        params.put(IParam.PAGE_INDEX, pageIndex);
        params.put(IParam.LIMIT, limit);
        String result = httpPostRequest(url, null, params);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            JSONArray array = json.getJSONArray(IParam.LIST);
            List<StudentDto> data = new ArrayList<>();
            StudentDto dto;
            for (int i = 0; i < array.length(); i++) {
                dto = new StudentDto();
                dto.parseJson(array.getJSONObject(i));
                data.add(dto);
            }
            return data;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }

    /**
     * 获取企业详情
     *
     * @param companyId 企业id
     * @return
     * @throws JSONException
     */
    public CompanyDetailDto getCompanyDetail(int companyId) throws JSONException {
        String url = getServerUrl() + RemoteCompanyURL.DETAIL.getURL() + IParam.WENHAO + IParam.COMPANY_ID + IParam.EQUALS_STRING + companyId;
        String result = httpGetRequest(url, null);
        JSONObject json = new JSONObject(result);
        if (json.getInt(IParam.STATUS) == 1) {
            JSONObject detail = json.getJSONObject(IParam.DETAIL);
            CompanyDetailDto dto = new CompanyDetailDto();
            dto.parseJson(detail);
            return dto;
        } else {
            throw new AppException(json.getInt(IParam.ERROR_CODE));
        }
    }
}
