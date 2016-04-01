package com.sb.meeting.ui.vo;

import java.io.Serializable;

/**
 * 资质证书 vo
 * Created by sun on 2016/3/31.
 */
public class CertificateVO implements Serializable {
    private String name;
    private String organization;
    private String thumb;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
