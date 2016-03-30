package com.sb.meeting.dao;

import android.content.ContentValues;
import android.content.Context;

import com.sb.meeting.dao.entity.Company;
import com.sb.meeting.exeception.AppException;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业 dao
 * Created by sun on 2016/3/30.
 */
public class CompanyDao extends BaseDao {
    public CompanyDao(Context context) {
        super(context);
    }

    /**
     * 删除所有数据
     */
    public void deleteAllData() {
        db.execSQL(Company.DELETE_TABLE_DATA);
    }

    /**
     * 插入数据
     *
     * @param company 企业对象
     */
    public void insert(Company company) {
        ContentValues values = new ContentValues();
        values.put(Company.KEY_COLUMN_COMPANY_ID, company.getCompanyId());
        values.put(Company.KEY_COLUMN_COMPANY_NAME, company.getCompanyName());
        values.put(Company.KEY_COLUMN_LOGO, company.getLogo());
        values.put(Company.KEY_COLUMN_PRODUCT_DESC, company.getProductDesc());
        values.put(Company.KEY_COLUMN_CAT_IDS, company.getCatIds());
        values.put(Company.KEY_COLUMN_PATTERN, company.getPattern());
        values.put(Company.KEY_COLUMN_COMPANY_TYPE, company.getCompanyType());
        values.put(Company.KEY_COLUMN_AREA, company.getArea());
        values.put(Company.KEY_COLUMN_VIP, company.getIsVIP());
        db.insert(Company.KEY_TABLE_NAME, null, values);
    }

    /**
     * 获取数据列表
     *
     * @return
     */
    public List<Company> getList() {
        List<Company> data = null;
        try {
            sql = "select * from " + Company.KEY_TABLE_NAME;
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                data = new ArrayList<>();
                Company company;
                while (cursor.moveToNext()) {
                    company = new Company();
                    company.setCompanyId(cursor.getInt(cursor.getColumnIndex(Company.KEY_COLUMN_COMPANY_ID)));
                    company.setCompanyName(cursor.getString(cursor.getColumnIndex(Company.KEY_COLUMN_COMPANY_NAME)));
                    company.setLogo(cursor.getString(cursor.getColumnIndex(Company.KEY_COLUMN_LOGO)));
                    company.setProductDesc(cursor.getString(cursor.getColumnIndex(Company.KEY_COLUMN_PRODUCT_DESC)));
                    company.setCatIds(cursor.getString(cursor.getColumnIndex(Company.KEY_COLUMN_CAT_IDS)));
                    company.setPattern(cursor.getString(cursor.getColumnIndex(Company.KEY_COLUMN_PATTERN)));
                    company.setCompanyType(cursor.getString(cursor.getColumnIndex(Company.KEY_COLUMN_COMPANY_TYPE)));
                    company.setArea(cursor.getString(cursor.getColumnIndex(Company.KEY_COLUMN_AREA)));
                    company.setIsVIP(cursor.getInt(cursor.getColumnIndex(Company.KEY_COLUMN_VIP)));
                    data.add(company);
                }
            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        } finally {
            releaseConnection();
        }
        return data;
    }
}
