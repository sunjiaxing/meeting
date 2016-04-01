package com.sb.meeting.dao.entity;

/**
 * 学员数据库实体类
 * Created by sun on 2016/3/30.
 */
public class Student {

    public static final String KEY_COLUMN_TABLE_ID = "table_id";
    public static final String KEY_COLUMN_STUDENT_ID = "student_id";
    public static final String KEY_COLUMN_STUDENT_NAME = "student_name";
    public static final String KEY_COLUMN_POSITION = "position";
    public static final String KEY_COLUMN_PHONE = "phone";
    public static final String KEY_COLUMN_IS_PUBLIC = "is_public";
    public static final String KEY_COLUMN_COMPANY_ID = "company_id";
    public static final String KEY_COLUMN_COMPANY_NAME = "company_name";
    public static final String KEY_COLUMN_AVATAR_URL = "avatar_url";
    public static final String KEY_COLUMN_AREA = "area";
    public static final String KEY_COLUMN_CLASS_NAME = "class_name";
    public static final String KEY_COLUMN_CLASS_POSITION = "class_position";

    public static final String KEY_TABLE_NAME = "student";

    public static final String CREAT_TABLE = "CREATE TABLE " + KEY_TABLE_NAME
            + " (" + KEY_COLUMN_TABLE_ID + " INTEGER PRIMARY KEY,"
            + KEY_COLUMN_COMPANY_ID + " INTEGER,"
            + KEY_COLUMN_STUDENT_ID + " INTEGER,"
            + KEY_COLUMN_COMPANY_NAME + " TEXT,"
            + KEY_COLUMN_STUDENT_NAME + " TEXT,"
            + KEY_COLUMN_POSITION + " TEXT,"
            + KEY_COLUMN_PHONE + " TEXT,"
            + KEY_COLUMN_IS_PUBLIC + " INTEGER,"
            + KEY_COLUMN_AVATAR_URL + " TEXT,"
            + KEY_COLUMN_CLASS_NAME + " TEXT ,"
            + KEY_COLUMN_CLASS_POSITION + " TEXT ,"
            + KEY_COLUMN_AREA + " TEXT "
            + ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + KEY_TABLE_NAME;
    public static final String DELETE_TABLE_DATA = "DELETE FROM "
            + KEY_TABLE_NAME;


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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassPosition() {
        return classPosition;
    }

    public void setClassPosition(String classPosition) {
        this.classPosition = classPosition;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
