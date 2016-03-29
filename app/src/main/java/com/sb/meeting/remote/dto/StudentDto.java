package com.sb.meeting.remote.dto;

import com.sb.meeting.remote.IParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 学员 dto
 * Created by sun on 2016/3/29.
 */
public class StudentDto {

    /**
     * studentId : 1
     * studentName : 张三
     * position : 职位
     * phone : 15911245789
     * isPublic : 0
     * companyId : 2
     * companyName : 公司名称
     * avatarUrl : http://xxxxxxx.jpg
     * className：所在班级名称
     * classPosition：所在班级职务
     * area : 呼和浩特市
     */

    private int studentId;
    private String studentName;
    private String position;
    private String phone;
    private int isPublic;
    private int companyId;
    private String companyName;
    private String avatarUrl;
    private String area;
    private String className;
    private String classPosition;

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getPosition() {
        return position;
    }

    public String getPhone() {
        return phone;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getArea() {
        return area;
    }

    public String getClassName() {
        return className;
    }

    public String getClassPosition() {
        return classPosition;
    }

    /**
     * 解析json
     *
     * @param json
     * @throws JSONException
     */
    public void parseJson(JSONObject json) throws JSONException {
        if (json == null) {
            return;
        }
        if (json.has(IParam.STUDENT_ID)) {
            this.studentId = json.getInt(IParam.STUDENT_ID);
        }
        if (json.has(IParam.STUDENT_NAME)) {
            this.studentName = json.getString(IParam.STUDENT_NAME);
        }
        if (json.has(IParam.POSITION)) {
            this.position = json.getString(IParam.POSITION);
        }
        if (json.has(IParam.PHONE)) {
            this.phone = json.getString(IParam.PHONE);
        }
        if (json.has(IParam.IS_PUBLIC)) {
            this.isPublic = json.getInt(IParam.IS_PUBLIC);
        }
        if (json.has(IParam.COMPANY_ID)) {
            this.companyId = json.getInt(IParam.COMPANY_ID);
        }
        if (json.has(IParam.COMPANY_NAME)) {
            this.companyName = json.getString(IParam.COMPANY_NAME);
        }
        if (json.has(IParam.AVATAR_URL)) {
            this.avatarUrl = json.getString(IParam.AVATAR_URL);
        }
        if (json.has(IParam.CLASS_NAME)) {
            this.className = json.getString(IParam.CLASS_NAME);
        }
        if (json.has(IParam.CLASS_POSITION)) {
            this.classPosition = json.getString(IParam.CLASS_POSITION);
        }
        if (json.has(IParam.AREA)) {
            this.area = json.getString(IParam.AREA);
        }
    }
}
