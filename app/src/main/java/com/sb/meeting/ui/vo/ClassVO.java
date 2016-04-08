package com.sb.meeting.ui.vo;

import java.io.Serializable;

/**
 * 班级 vo
 * Created by sun on 2016/4/5.
 */
public class ClassVO implements Serializable {
    private int classId;
    private String className;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
