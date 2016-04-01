package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 企业荣誉证书dto
 * Created by sun on 2016/3/31.
 */
public class CertificateDto {

    /**
     * name : 证书1
     * organization : 发证机构
     * thumb : http://xxxxxxx.jpg
     */

    private String name;
    private String organization;
    private String thumb;

    public String getName() {
        return name;
    }

    public String getOrganization() {
        return organization;
    }

    public String getThumb() {
        return thumb;
    }

    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.NAME)) {
            this.name = json.getString(IParam.NAME);
        }
        if (json.has(IParam.ORGANIZATION)) {
            this.organization = json.getString(IParam.ORGANIZATION);
        }
        if (json.has(IParam.THUMB)) {
            this.thumb = json.getString(IParam.THUMB);
        }
    }
}
