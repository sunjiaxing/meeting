package com.sb.meeting.dao.entity;

/**
 * 企业数据库实体类
 * Created by sun on 2016/3/30.
 */
public class Company {

    public static final String KEY_COLUMN_TABLE_ID = "table_id";
    public static final String KEY_COLUMN_COMPANY_ID = "company_id";
    public static final String KEY_COLUMN_COMPANY_NAME = "company_name";
    public static final String KEY_COLUMN_LOGO = "logo";
    public static final String KEY_COLUMN_PRODUCT_DESC = "product_desc";
    public static final String KEY_COLUMN_CAT_IDS = "cat_ids";
    public static final String KEY_COLUMN_PATTERN = "pattern";
    public static final String KEY_COLUMN_COMPANY_TYPE = "company_type";
    public static final String KEY_COLUMN_VIP = "vip";
    public static final String KEY_COLUMN_AREA = "area";

    public static final String KEY_TABLE_NAME = "company";

    public static final String CREAT_TABLE = "CREATE TABLE " + KEY_TABLE_NAME
            + " (" + KEY_COLUMN_TABLE_ID + " INTEGER PRIMARY KEY,"
            + KEY_COLUMN_COMPANY_ID + " INTEGER,"
            + KEY_COLUMN_COMPANY_NAME + " TEXT,"
            + KEY_COLUMN_LOGO + " TEXT,"
            + KEY_COLUMN_PRODUCT_DESC + " TEXT,"
            + KEY_COLUMN_CAT_IDS + " TEXT,"
            + KEY_COLUMN_PATTERN + " TEXT,"
            + KEY_COLUMN_COMPANY_TYPE + " TEXT ,"
            + KEY_COLUMN_VIP + " INTEGER ,"
            + KEY_COLUMN_AREA + " TEXT "
            + ")";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
            + KEY_TABLE_NAME;
    public static final String DELETE_TABLE_DATA = "DELETE FROM "
            + KEY_TABLE_NAME;

    private int companyId;
    private String companyName;
    private String logo;
    private String productDesc;
    private String catIds;
    private String pattern;
    private String companyType;
    private int isVIP;
    private String area;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getCatIds() {
        return catIds;
    }

    public void setCatIds(String catIds) {
        this.catIds = catIds;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public int getIsVIP() {
        return isVIP;
    }

    public void setIsVIP(int isVIP) {
        this.isVIP = isVIP;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
