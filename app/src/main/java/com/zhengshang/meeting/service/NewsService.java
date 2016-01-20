package com.zhengshang.meeting.service;


import java.util.ArrayList;
import java.util.Collections;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.content.Context;

import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.dao.NewsDao;
import com.zhengshang.meeting.dao.entity.News;
import com.zhengshang.meeting.dao.entity.NewsChannel;
import com.zhengshang.meeting.exeception.AppException;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.remote.NewsRO;
import com.zhengshang.meeting.remote.dto.NewsChannelDto;
import com.zhengshang.meeting.remote.dto.NewsDetailDto;
import com.zhengshang.meeting.remote.dto.NewsDto;
import com.zhengshang.meeting.remote.dto.NewsSubjectDto;
import com.zhengshang.meeting.ui.vo.NewsChannelVO;
import com.zhengshang.meeting.ui.vo.NewsDetailVO;
import com.zhengshang.meeting.ui.vo.NewsSubjectVO;
import com.zhengshang.meeting.ui.vo.NewsVO;

/**
 * news service
 */
public class NewsService extends BaseService {

    private NewsRO newsRO;
    private NewsDao newsDao;

    public NewsService(Context context) {
        super(context);
        newsRO = new NewsRO(context);
        newsDao = new NewsDao(context);
    }

    /**
     * 获取新闻分类
     *
     * @return
     * @throws JSONException
     */
    public List<NewsChannelVO> getAllNewsTypes() throws JSONException {
        List<NewsChannelVO> showData;
        List<NewsChannel> dbData = newsDao.getNewsType(true, configDao.getUserId());
        if (!Utils.isEmpty(dbData)) {
            showData = parseNewsChannel2ShowDataFromDB(dbData);
        } else {
            configDao.setNewsTypeState(0);
            List<NewsChannelDto> dtoList = getNewsTypeFromWeb(true);
            showData = parseNewsChannel2ShowDataFromDto(dtoList);
        }
        return showData;
    }

    /**
     * 更新新闻类型
     */
    public void updateNewsType() throws JSONException {
        if (!Utils.isEmpty(getNewsTypeFromWeb(true))) {
            configDao.setNewsChannelUpdate(true);
        }
    }

    /**
     * (新闻分类) 数据转换
     *
     * @param data
     * @return
     */
    private List<NewsChannelVO> parseNewsChannel2ShowDataFromDB(
            List<NewsChannel> data) {
        // 数据转换
        List<NewsChannelVO> showData = new ArrayList<>();
        if (!Utils.isEmpty(data)) {
            for (NewsChannel tmp : data) {
                showData.add(new NewsChannelVO(tmp.getTypeId(), tmp.getName(),
                        tmp.getIsLock() == 1, tmp.getPosition()));
            }
        }
        return showData;
    }

    /**
     * 根据标记获取新闻栏目
     *
     * @param isUpdateTime 是否 记录 更新新闻栏目的时间
     * @return
     * @throws JSONException
     */
    private List<NewsChannelDto> getNewsTypeFromWeb(boolean isUpdateTime)
            throws JSONException {
        Map<String, Object> res = newsRO.getNewsTypeByFlag(
                configDao.getNewsTypeState(), null);
        if (res == null || res.size() == 0) {
            return null;
        }
        List<NewsChannelDto> webData = (List<NewsChannelDto>) res
                .get(IParam.LIST);
        if (Utils.isEmpty(webData)) {
            return null;
        }
        if (isUpdateTime) {
            long time = (Long) res.get(IParam.TIME);
            configDao.setNewsTypeState(time);
        }
        List<NewsChannel> saveData = parseNewsChannel2SaveDataFromDto(webData);
        // 更新
        newsDao.updateNewsType(saveData);

        return webData;
    }

    /**
     * (新闻栏目)数据转换
     *
     * @param data
     * @return
     */
    private List<NewsChannel> parseNewsChannel2SaveDataFromDto(
            List<NewsChannelDto> data) {
        // 数据转换
        List<NewsChannel> saveData = new ArrayList<>();
        if (!Utils.isEmpty(data)) {
            String masterId = configDao.getUserId();
            for (NewsChannelDto dto : data) {
                NewsChannel type = new NewsChannel();
                type.setTypeId(dto.getTypeId());
                type.setName(dto.getName());
                type.setIsLock(dto.getIsLock());
                type.setIsMine(1);
                type.setPosition(dto.getPosition());
                type.setMasterId(masterId);
                saveData.add(type);
            }
        }
        return saveData;
    }

    /**
     * (新闻栏目)数据转换
     *
     * @param data
     * @return
     */
    private List<NewsChannelVO> parseNewsChannel2ShowDataFromDto(
            List<NewsChannelDto> data) {
        // 数据转换
        List<NewsChannelVO> showData = new ArrayList<>();
        if (!Utils.isEmpty(data)) {
            for (NewsChannelDto dto : data) {
                showData.add(new NewsChannelVO(dto.getTypeId(), dto.getName(),
                        dto.getIsLock() == 1, dto.getPosition()));
            }
        } else {
            throw new AppException("获取新闻栏目失败！");
        }
        Collections.sort(showData, new NewsTypeOrder());
        return showData;
    }

    /**
     * 获取新闻专题内容
     *
     * @param specialId 专题id
     * @return
     * @throws JSONException
     */
    public NewsSubjectVO getNewsSubject(int specialId) throws JSONException {
        NewsSubjectDto dto = newsRO.getNewsSubject(specialId);
        if (dto != null) {
            // 设置已读状态
            newsDao.setReadState(specialId, 1);
            // 数据转换
            NewsSubjectVO vo = new NewsSubjectVO();
            vo.setId(dto.getId());
            vo.setTitle(dto.getTitle());
            vo.setBanner(dto.getBanner());
            vo.setDescription(dto.getDescription());
            if (!Utils.isEmpty(dto.getNewsDtoList())) {
                for (NewsDto newsDto : dto.getNewsDtoList()) {
                    vo.getNewsVOList().add(parseToVO(newsDto));
                }
            }
            return vo;
        }
        return null;
    }

    /**
     * 新闻栏目排序类
     */
    public class NewsTypeOrder implements Comparator<NewsChannelVO> {
        @Override
        public int compare(NewsChannelVO type1, NewsChannelVO type2) {

            return type1.getPosition() - type2.getPosition();
        }
    }

    /**
     * 获取用户自定义新闻分类数据
     *
     * @return
     * @throws JSONException
     */
    public List<NewsChannelVO> getUserNewsTypes() throws JSONException {
        List<NewsChannelVO> showData = null;
        List<NewsChannel> dbData = newsDao
                .getNewsType(false, configDao.getUserId());
        if (!Utils.isEmpty(dbData)) {
            showData = parseNewsChannel2ShowDataFromDB(dbData);
        }
        return showData;
    }

    /**
     * (新闻栏目)数据转换
     *
     * @param data
     * @return
     */
    private List<NewsChannel> parseNewsChannel2SaveDataFromVO(
            List<NewsChannelVO> data) {
        // 数据转换
        List<NewsChannel> saveData = new ArrayList<>();
        if (!Utils.isEmpty(data)) {
            String masterId = configDao.getUserId();
            for (NewsChannelVO vo : data) {
                NewsChannel type = new NewsChannel();
                type.setTypeId(vo.getTypeId());
                type.setName(vo.getName());
                type.setIsLock(vo.isLock() ? 1 : 0);
                type.setIsMine(1);
                type.setPosition(vo.getPosition());
                type.setMasterId(masterId);
                saveData.add(type);
            }
        }
        return saveData;
    }

    /**
     * 从数据库中获取缓存的新闻
     *
     * @param catId 栏目id
     * @return
     */
    public List<NewsVO> getNewsFromDB(String catId) {
        List<News> dbData = newsDao.getOnlineNewsFromDB(catId);
        // 处理缓存数据
        List<NewsVO> showData = null;
        List<NewsVO> topData;
        if (!Utils.isEmpty(dbData)) {
            showData = new ArrayList<>();
            topData = new ArrayList<>();
            NewsVO vo;
            for (News news : dbData) {
                vo = parseToVO(news);
                if (vo.isTop()) {
                    topData.add(vo);
                } else {
                    showData.add(vo);
                }
            }
            // 有滚动新闻  把滚动新闻放在list顶部
            if (!Utils.isEmpty(topData)) {
                // 排序
                Collections.sort(topData, new NewsOrderByCreateTime());
                Collections.sort(showData, new NewsOrderByCreateTime());
                NewsVO newsVO = topData.get(0);
                newsVO.setTopNews(topData);
                showData.add(0, newsVO);
            } else {
                Collections.sort(showData, new NewsOrderByCreateTime());
            }
        }
        return showData;
    }

    /**
     * 转换成 vo
     *
     * @param news 数据库存储实体
     * @return
     */
    private NewsVO parseToVO(News news) {
        NewsVO vo = new NewsVO();
        vo.setId(news.getNewsId());
        vo.setCatId(news.getCatId());
        vo.setTitle(news.getTitle());
        vo.setSummary(Utils.substringSummary(news.getSummary()));
        vo.setSubject(news.getSubject() == 1);
        vo.setSubjectId(news.getSubjectId());
        vo.setIconPath(news.getIconPath());
        vo.setCreateTime(news.getCreateTime());
        vo.setIsOpenBlank(news.getIsOpenBlank() == 1);
        vo.setRead(news.getIsRead() == 1);
        vo.setTop(news.getTop() == 1);
        vo.setIconAdUrl(news.getIconAdUrl());
        return vo;
    }

    /**
     * (新闻) 获取网络数据
     *
     * @param catId   栏目id
     * @param minTime 最小时间
     * @return
     * @throws JSONException
     */
    public List<NewsVO> getNewsListFromWeb(String catId, long minTime)
            throws JSONException {
        List<NewsVO> showData = new ArrayList<>();
        // 获取数据
        List<NewsDto> webData = newsRO.refreshNews(catId, BonConstants.LIMIT_GET_NEWS, minTime);
        try {
            News news;
            NewsVO vo;
            int readState;
            List<News> dbData = new ArrayList<>();
            List<NewsVO> topData = new ArrayList<>();
            // 同时转换为 vo 和 db
            for (NewsDto dto : webData) {
                readState = newsDao.getReadState(dto.getId(), catId);
                vo = parseToVO(dto);
                vo.setCatId(catId);
                vo.setRead(readState == 1);

                news = parseToDB(dto);
                news.setCatId(catId);
                news.setIsRead(readState);

                // 添加db集合
                dbData.add(news);
                // 添加vo集合
                if (vo.isTop()) {
                    topData.add(vo);
                } else {
                    showData.add(vo);
                }
            }
            // 添加缓存
            newsDao.beginTransaction();
            if (minTime == 0) {
                // 初始化 或者 刷新 时，清除原数据
                newsDao.deleteAllByCatId(catId);
                // 设置栏目刷新时间
                configDao.setCatClickTime(catId, System.currentTimeMillis());
            }
            newsDao.saveNews(dbData);
            // 处理 top新闻
            if (!Utils.isEmpty(topData)) {
                // 排序
                Collections.sort(topData, new NewsOrderByCreateTime());
                Collections.sort(showData, new NewsOrderByCreateTime());
                vo = topData.get(0);
                vo.setTopNews(topData);
                showData.add(0, vo);
            } else {
                Collections.sort(showData, new NewsOrderByCreateTime());
            }
            newsDao.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            newsDao.endTransaction();
        }
        // 数据转换
        return showData;
    }

    /**
     * 转换 成VO
     *
     * @param dto 新闻 网络请求返回数据 对象
     * @return
     */
    private NewsVO parseToVO(NewsDto dto) {
        NewsVO vo = new NewsVO();
        vo.setId(dto.getId());
        vo.setSummary(Utils.substringSummary(dto.getSummary()));
        vo.setTop(dto.getTop() == 1);
        vo.setCreateTime(dto.getCreateTime());
        vo.setSubject(dto.getIsSpecial() == 1);
        vo.setSubjectId(dto.getSpecialId());
        if (dto.getIsAd() == 1) {
            // 广告单独处理
            vo.setTitle(dto.getAdTitle());
            vo.setIconPath(dto.getAdImgUrl());
            vo.setIconAdUrl(dto.getAdUrl());
            vo.setIsOpenBlank(dto.getIsOpenBlank() == 1);
        } else {
            vo.setTitle(dto.getTitle());
            if (dto.getTop() == 1) {
                vo.setIconPath(dto.getImgUrl());
            } else {
                vo.setIconPath(dto.getIconUrl());
            }
        }
        return vo;
    }

    /**
     * 转换成 数据库存储数据
     *
     * @param dto 新闻 网络请求返回数据 对象
     * @return
     */
    private News parseToDB(NewsDto dto) {
        News news = new News();
        news.setIsAd(dto.getIsAd());
        news.setNewsId(dto.getId());
        news.setSummary(dto.getSummary());
        news.setSubject(dto.getIsSpecial());
        news.setSubjectId(dto.getSpecialId());
        news.setTop(dto.getTop());
        news.setCreateTime(dto.getCreateTime());
        if (dto.getIsAd() == 1) {
            // 广告单独处理
            news.setTitle(dto.getAdTitle());
            news.setIconPath(dto.getAdImgUrl());
            news.setIconAdUrl(dto.getAdUrl());
            news.setIsOpenBlank(dto.getIsOpenBlank());
        } else {
            news.setTitle(dto.getTitle());
            if (dto.getTop() == 1) {
                news.setIconPath(dto.getImgUrl());
            } else {
                news.setIconPath(dto.getIconUrl());
            }
        }
        return news;
    }

    private class NewsOrderByCreateTime implements Comparator<NewsVO> {

        @Override
        public int compare(NewsVO lhs, NewsVO rhs) {
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
     * 网络请求获取新闻详情
     *
     * @param id    新闻id
     * @param catId 栏目id
     * @return
     * @throws JSONException
     */
    public NewsDetailVO getNewsDetailFromWeb(String id, String catId)
            throws JSONException {
        NewsDetailVO vo = null;
        if (!Utils.isEmpty(catId)) {
            // 设置新闻已读状态
            newsDao.setReadState(id, catId, 1);
        }
        // 访问服务器
        NewsDetailDto dto = newsRO.getNewsDetail(id);
        if (dto != null) {
            // 数据转换
            vo = new NewsDetailVO();
            vo.setId(id);
            vo.setTitle(dto.getTitle());
            vo.setContent(dto.getContent());
            vo.setContentUrl(dto.getContentUrl());
            vo.setcFrom(dto.getcFrom());
            vo.setcTime(Utils.formateTime(dto.getcTime(), false));

            vo.setSummary(dto.getSummary());
            vo.setShortUrl(dto.getShortUrl());
            vo.setLongUrl(dto.getLongUrl());
            vo.setIconUrl(dto.getIconUrl());

            vo.setAdId(dto.getAdId());
            vo.setAdTitle(dto.getAdTitle());
            vo.setAdIconUrl(dto.getAdIconUrl());
            vo.setAdUrl(dto.getAdUrl());
        }
        return vo;
    }

    /**
     * 获取不同类别的点击时间
     *
     * @param catID 栏目id
     * @return
     */
    public long getCatClickTime(String catID) {
        return configDao.getCatClickTime(catID, System.currentTimeMillis());
    }

    /**
     * 判断是否需要刷新
     *
     * @param catId 栏目id
     * @return
     */
    public boolean needRefresh(String catId) {
        long lastClickTime = configDao.getCatClickTime(catId, 0);
        if (System.currentTimeMillis() - lastClickTime > BonConstants.TIME_TO_REFESH_DATA) {
            return true;
        }
        return false;
    }

    /**
     * 设置新闻栏目更新状态
     *
     * @param isUpdate
     * @return
     */
    public void setNewsChannelUpdate(boolean isUpdate) {
        configDao.setNewsChannelUpdate(isUpdate);
    }

    /***
     * 获取新闻栏目更新状态
     *
     * @return
     */
    public boolean getNewsChannelUpdate() {
        return configDao.getNewsChannelUpdate();
    }

    /**
     * 更新新闻分类本地数据
     *
     * @param newsTypes
     */
    public void saveNewsType(List<NewsChannelVO> newsTypes) {
        // 数据转换
        List<NewsChannel> saveData = parseNewsChannel2SaveDataFromVO(newsTypes);
        // 修改状态
        newsDao.saveToMyChannel(saveData, "0");
    }


    /**
     * 版本更新
     *
     * @return
     * @throws JSONException
     */
    public Map<String, Object> updateVersion() throws JSONException {
        return newsRO.updateVersion();
    }

    /**
     * 获取初始化图片
     *
     * @throws JSONException
     */
    public void getInitImage() throws JSONException {
        newsRO.getInitImage();
    }

    /**
     * 删除新闻
     *
     * @param id
     */
    public void deleteNewsById(String id) {
        newsDao.deleteNewsByID(id);
    }

}
