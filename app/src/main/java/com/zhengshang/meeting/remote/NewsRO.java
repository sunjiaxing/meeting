package com.zhengshang.meeting.remote;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.dao.ConfigDao;
import com.zhengshang.meeting.exeception.AppException;
import com.zhengshang.meeting.remote.dto.NewsChannelDto;
import com.zhengshang.meeting.remote.dto.NewsDetailDto;
import com.zhengshang.meeting.remote.dto.NewsDto;

/**
 * news ro
 *
 * @author sun 2015年12月10日14:53:47
 */
public class NewsRO extends BaseRO {

    private ConfigDao configDao;

    public NewsRO(Context context) {
        super(context);
        configDao = ConfigDao.getInstance(context);
    }

    public enum RemoteNewsUrl implements IBaseURL {
        GET_NEWS_TYPE("categories"), GET_NEWS_LIST("listForOrientation"), NEWS_DETAIL(
                "detailNew");
        private static final String NAMESPACE = "news";
        private String url;

        RemoteNewsUrl(String mapping) {
            url = NAMESPACE + BonConstants.SLASH + mapping;
        }

        @Override
        public String getURL() {

            return url;
        }
    }

    /**
     * 根据标记获取新闻栏目
     *
     * @param time  新闻栏目的版本
     * @param token
     * @throws JSONException
     */
    public Map<String, Object> getNewsTypeByFlag(long time, String token)
            throws JSONException {
        Map<String, Object> res = new HashMap<>();
        String url = getServerUrl()
                + RemoteNewsUrl.GET_NEWS_TYPE.getURL() + IParam.WENHAO
                + IParam.TIME + IParam.EQUALS_STRING + time;
        String result = httpGetRequest(url, getHeaderParam(IParam.TOKEN, token));
        if (!Utils.isEmpty(result)) {
            JSONObject json = new JSONObject(result);
            if (json.getInt(IParam.STATUS) == 1) {
                JSONArray jsonArray = json.getJSONArray(IParam.TYPES);
                if (jsonArray != null && jsonArray.length() > 0) {
                    // 设置新闻栏目更新状态
                    res.put(IParam.TIME, json.getLong(IParam.TIME));
                    // 实例化集合并创建变量
                    List<NewsChannelDto> typeList = new ArrayList<>();
                    NewsChannelDto newsType = null;
                    // 循环解析栏目信息
                    for (int i = 0; i < jsonArray.length(); i++) {
                        newsType = new NewsChannelDto();
                        newsType.parseJson(jsonArray.getJSONObject(i));
                        typeList.add(newsType);
                    }
                    res.put(IParam.LIST, typeList);
                }
                return res;
            } else {
                throw new AppException(json.getInt(IParam.ERROR_CODE));
            }
        } else {
            throw new AppException(mContext.getString(R.string.netconnecterror));
        }
    }

    /**
     * @param catId   : 分类id
     * @param limit   ：每次获取的长度
     * @param minTime 加载更多使用的
     * @param token
     * @return
     * @throws JSONException
     */
    public List<NewsDto> refreshNews(String catId, int limit, long minTime,
                                     String token) throws JSONException {
        List<NewsDto> newsList;
        List<NewsDto> topList;
        String url = getServerUrl()
                + RemoteNewsUrl.GET_NEWS_LIST.getURL() + IParam.WENHAO
                + IParam.CATID + IParam.EQUALS_STRING + catId + IParam.AND
                + IParam.LIMIT + IParam.EQUALS_STRING + limit + IParam.AND
                + IParam.MIN_TIME + IParam.EQUALS_STRING + minTime;
        String result = httpGetRequest(url, getHeaderParam(IParam.TOKEN, token));
        if (!Utils.isEmpty(result)) {
            JSONObject json = new JSONObject(result);
            NewsDto news;
            if (json.getInt(IParam.STATUS) == 1) {
                topList = new ArrayList<>();
                newsList = new ArrayList<>();
                JSONArray array = json.getJSONArray(IParam.NEWS);
                // 先解析新闻
                for (int i = 0; i < array.length(); i++) {
                    news = new NewsDto();
                    news.parseJson(array.getJSONObject(i));
                    news.setCatId(catId);
                    if (news.getTop() == 1) {
                        topList.add(news);
                    } else {
                        newsList.add(news);
                    }
                }
                // 排序
                Collections.sort(topList, new NewsOrderByCreateTime());
                Collections.sort(newsList, new NewsOrderByCreateTime());
                if (topList.size() > 0) {
                    NewsDto model = topList.get(0);
                    model.setTop(1);
                    model.setTopNews(topList);
                    newsList.add(0, model);
                }
                return newsList;
            } else {
                throw new AppException(json.getInt(IParam.ERROR_CODE));
            }
        } else {
            throw new AppException(mContext.getString(R.string.netconnecterror));
        }
    }

    private class NewsOrderByCreateTime implements Comparator<NewsDto> {

        @Override
        public int compare(NewsDto lhs, NewsDto rhs) {
            if (rhs.getCreateTime() > lhs.getCreateTime()) {
                return 1;
            } else if (rhs.getCreateTime() < lhs.getCreateTime()) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    /**
     * 获取新闻详情
     *
     * @param id
     * @param catId
     * @param token
     * @return
     * @throws JSONException
     */
    public NewsDetailDto getNewsDetail(String id, String catId, String token)
            throws JSONException {
        String url = getServerUrl()
                + RemoteNewsUrl.NEWS_DETAIL.getURL() + IParam.WENHAO
                + IParam.CATID + IParam.EQUALS_STRING + catId + IParam.AND
                + IParam.NEWS_ID + IParam.EQUALS_STRING + id;
        String result = httpGetRequest(url, getHeaderParam(IParam.TOKEN, token));
        if (!Utils.isEmpty(result)) {
            NewsDetailDto newsDetail;
            JSONObject json = new JSONObject(result);
            if (json.getInt(IParam.STATUS) == 1) {
                String temp = json.getString(IParam.DETAIL);
                newsDetail = new NewsDetailDto();
                newsDetail.paserJson(new JSONObject(temp));
                newsDetail.setId(id);
            } else if (json.getInt(IParam.ERROR_CODE) == 100070) {
                throw new AppException("110");
            } else {
                throw new AppException(json.getInt(IParam.ERROR_CODE));
            }
            return newsDetail;
        } else {
            throw new AppException(mContext.getString(R.string.netconnecterror));
        }
    }

    /**
     * 版本更新
     *
     * @return http://update.doulimall.com
     * @throws JSONException
     */
    public Map<String, Object> updateVersion() throws JSONException {
        String result = httpGetRequest("http://update.doulimall.com", null);
        if (!Utils.isEmpty(result)) {
            JSONObject json = new JSONObject(result);
            if (json.has(IParam.VERSION_CODE)) {
                String versionNub = json.getString(IParam.VERSION_CODE);
                if (!Utils.isEmpty(versionNub)
                        && versionNub.compareTo(Utils.getVersionCode(mContext)) > 0) {
                    // 获取更新标题
                    String title = "更新提示";
                    if (json.has(IParam.TITLE)) {
                        title = json.getString(IParam.TITLE);
                    }
                    // 获取更新信息的描述
                    String desc = "有新版本，是否更新？";
                    if (json.has(IParam.DESCRIPTION)) {
                        desc = json.getString(IParam.DESCRIPTION);
                    }
                    // 获取应用的下载地址
                    String url = json.getString(IParam.URL);
                    boolean focus = false;
                    if (json.has(IParam.FOCUS)) {
                        focus = json.getBoolean(IParam.FOCUS);
                    }
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put(IParam.TITLE, title);
                    data.put(IParam.DESC, desc);
                    data.put(IParam.URL, url);
                    data.put(IParam.FOCUS, focus);

                    return data;
                }
            }
            return null;
        } else {
            throw new AppException(mContext.getString(R.string.netconnecterror));
        }
    }

    /**
     * 获取初始化图片
     *
     * @throws JSONException
     */
    public void getInitImage() throws JSONException {
        String result = httpGetRequest(
                getServerUrl()
                        + "logo/getLogo", null);
        if (!Utils.isEmpty(result)) {
            JSONObject json = new JSONObject(result);
            if (json != null && json.getInt(IParam.STATUS) == 1) {
                // 获取更新时间
                if (json.has(IParam.UPDATE_TIME)) {
                    long time = json.getLong(IParam.UPDATE_TIME);
                    // 判断本地默认时间
                    if (configDao.getShowInitTime() == 0) {
                        configDao.setShowInitTime(time);
                    }
                    // // 判断是否更新
                    if (json.has(IParam.LOGO_URL)) {
                        // 获取url地址 并下载
                        Utils.saveImageFromUrl(json.getString(IParam.LOGO_URL),
                                time);
                        // 替换显示时间
                        configDao.setShowInitTime(time);
                        // 整理图片
                        // 判断时间间隔
                        if (configDao.getDeleteInitTime() == 0) {
                            // 第一次操作 记录时间 不删除
                            configDao.setDeleteInitTime(System
                                    .currentTimeMillis());
                            return;
                        }
                        if (System.currentTimeMillis()
                                - configDao.getDeleteInitTime() > BonConstants.TIME_TO_DELETE_LOGO) {
                            deleteLogo();
                        }
                    }
                }
            } else {
                throw new AppException(json.getInt(IParam.ERROR_CODE));
            }
        } else {
            throw new AppException(mContext.getString(R.string.netconnecterror));
        }
    }

    /**
     * 不判断 直接执行删除
     */
    private void deleteLogo() {
        try {
            // 创建根目录
            File fileDir = new File(BonConstants.PATH_INIT_IMAGE_CACHE);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            // 获取文件并排序
            File[] files = fileDir.listFiles();
            if (!Utils.isEmpty(files)) {
                List<File> list = Arrays.asList(files);
                // 判断数量
                if (!Utils.isEmpty(list)
                        && list.size() > BonConstants.LIMIT_LOGO_LAST) {
                    // 执行排序 删除
                    Collections.sort(list, new Comparator<File>() {
                        @Override
                        public int compare(File file1, File file2) {

                            return file2.getName().compareToIgnoreCase(
                                    file1.getName());
                        }
                    });
                    for (int i = BonConstants.LIMIT_LOGO_LAST - 1; i < list
                            .size(); i++) {
                        list.get(i).delete();
                    }
                    // 记录删除时间
                    configDao.setDeleteInitTime(System.currentTimeMillis());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
