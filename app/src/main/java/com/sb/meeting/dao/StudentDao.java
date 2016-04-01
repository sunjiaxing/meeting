package com.sb.meeting.dao;

import android.content.ContentValues;
import android.content.Context;

import com.sb.meeting.dao.entity.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * 学员数据库操作类
 * Created by sun on 2016/3/30.
 */
public class StudentDao extends BaseDao {

    public StudentDao(Context context) {
        super(context);
    }

    /**
     * 添加学员
     *
     * @param student 学员对象
     */
    public void insert(Student student) {
        ContentValues values = new ContentValues();
        values.put(Student.KEY_COLUMN_STUDENT_ID, student.getStudentId());
        values.put(Student.KEY_COLUMN_STUDENT_NAME, student.getStudentName());
        values.put(Student.KEY_COLUMN_POSITION, student.getPosition());
        values.put(Student.KEY_COLUMN_PHONE, student.getPhone());
        values.put(Student.KEY_COLUMN_IS_PUBLIC, student.getIsPublic());
        values.put(Student.KEY_COLUMN_COMPANY_ID, student.getCompanyId());
        values.put(Student.KEY_COLUMN_COMPANY_NAME, student.getCompanyName());
        values.put(Student.KEY_COLUMN_CLASS_NAME, student.getClassName());
        values.put(Student.KEY_COLUMN_CLASS_POSITION, student.getClassPosition());
        values.put(Student.KEY_COLUMN_AVATAR_URL, student.getAvatarUrl());
        values.put(Student.KEY_COLUMN_AREA, student.getArea());
        db.insert(Student.KEY_TABLE_NAME, null, values);
    }

    /**
     * 获取学员列表
     *
     * @return
     */
    public List<Student> getList() {
        List<Student> data = null;
        try {
            sql = "select * from " + Student.KEY_TABLE_NAME;
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                data = new ArrayList<>();
                Student student;
                while (cursor.moveToNext()) {
                    student = new Student();
                    student.setStudentId(cursor.getInt(cursor.getColumnIndex(Student.KEY_COLUMN_STUDENT_ID)));
                    student.setStudentName(cursor.getString(cursor.getColumnIndex(Student.KEY_COLUMN_STUDENT_NAME)));
                    student.setPosition(cursor.getString(cursor.getColumnIndex(Student.KEY_COLUMN_POSITION)));
                    student.setPhone(cursor.getString(cursor.getColumnIndex(Student.KEY_COLUMN_PHONE)));
                    student.setIsPublic(cursor.getInt(cursor.getColumnIndex(Student.KEY_COLUMN_IS_PUBLIC)));
                    student.setCompanyId(cursor.getInt(cursor.getColumnIndex(Student.KEY_COLUMN_COMPANY_ID)));
                    student.setCompanyName(cursor.getString(cursor.getColumnIndex(Student.KEY_COLUMN_COMPANY_NAME)));
                    student.setAvatarUrl(cursor.getString(cursor.getColumnIndex(Student.KEY_COLUMN_AVATAR_URL)));
                    student.setClassName(cursor.getString(cursor.getColumnIndex(Student.KEY_COLUMN_CLASS_NAME)));
                    student.setClassPosition(cursor.getString(cursor.getColumnIndex(Student.KEY_COLUMN_CLASS_POSITION)));
                    student.setArea(cursor.getString(cursor.getColumnIndex(Student.KEY_COLUMN_AREA)));
                    data.add(student);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            releaseConnection();
        }
        return data;
    }

    /**
     * 删除所有数据
     */
    public void deleteAllData() {
        db.execSQL(Student.DELETE_TABLE_DATA);
    }
}
