package com.zhengshang.meeting.dao;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sun on 2015/12/10.
 */
public class ConfigDao {
    private Context mContext;

    private static ConfigDao instance;

    private SharedPreferences getShare() {
        return mContext.getSharedPreferences("configInfo",
                Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEdit() {
        return mContext.getSharedPreferences("configInfo",
                Context.MODE_PRIVATE).edit();
    }

    public static ConfigDao getInstance(Context context) {
        if (instance == null) {
            instance = new ConfigDao(context);
        }
        return instance;
    }

    private ConfigDao(Context context) {
        this.mContext = context;
    }

    private ConfigDao() {
    }

    /**
     * 设置自动滚动状态
     *
     * @param isAuto
     * @return
     */
    public boolean setAutoScrollState(boolean isAuto) {

        return getEdit().putBoolean("autoScroll", isAuto).commit();
    }

    /**
     * 获取自动滚动状态
     *
     * @return
     */
    public boolean getAutoScrollState() {
        return getShare().getBoolean("autoScroll", true);
    }

    /**
     * 获取initImage显示时间
     *
     * @return
     */
    public long getShowInitTime() {
        return getShare().getLong("showInitTime", 0);
    }

    /**
     * 设置initImage显示时间
     *
     * @param time
     * @return
     */
    public boolean setShowInitTime(long time) {
        return getEdit().putLong("showInitTime", time).commit();
    }

    /**
     * 获取上次删除logo图片的时间
     *
     * @return
     */
    public long getDeleteInitTime() {

        return getShare().getLong("deleteInitTime", 0);
    }

    /**
     * 设置initImage显示时间
     *
     * @param time
     * @return
     */
    public boolean setDeleteInitTime(long time) {
        return getEdit().putLong("deleteInitTime", time).commit();
    }

    /**
     * 获取新闻栏目是否更新的状态信息
     *
     * @return
     */
    public long getNewsTypeState() {
        return getShare().getLong("newsTypeState", 0);
    }

    /**
     * 保存新闻栏目更新的状态信息
     *
     * @param newsTypeState
     * @return
     */
    public void setNewsTypeState(long newsTypeState) {
        getEdit().putLong("newsTypeState", newsTypeState).commit();
    }

    /**
     * 设置不同类别的点击时间
     *
     * @param catID
     * @return
     */
    public boolean setCatClickTime(String catID, long time) {
        return getEdit().putLong(catID, time).commit();
    }

    /**
     * 获取不同类别的点击时间
     *
     * @param catID
     * @return
     */
    public long getCatClickTime(String catID, long defaultTime) {
        return getShare().getLong(catID, defaultTime);
    }

    /**
     * 设置新闻栏目更新状态
     *
     * @param isUpdate
     * @return
     */
    public boolean setNewsChannelUpdate(boolean isUpdate) {
        return getEdit().putBoolean("newsChannelUpdate", isUpdate).commit();
    }

    /***
     * 获取新闻栏目更新状态
     *
     * @return
     */
    public boolean getNewsChannelUpdate() {
        return getShare().getBoolean("newsChannelUpdate", false);
    }

    public boolean setString(String key, String value) {
        return getEdit().putString(key, value).commit();
    }

    public boolean setInt(String key, int value) {
        return getEdit().putInt(key, value).commit();
    }


}
