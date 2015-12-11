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
import com.zhengshang.meeting.ui.vo.NewsChannelVO;
import com.zhengshang.meeting.ui.vo.NewsDetailVO;
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
        List<NewsChannelVO> showData = null;
        List<NewsChannel> dbData = newsDao.getNewsType(true, "");
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
    public void updateNewsType() {
        new Thread() {
            public void run() {
                try {
                    if (!Utils.isEmpty(getNewsTypeFromWeb(false))) {
                        configDao.setNewsChannelUpdate(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
                        tmp.getIsLock(), tmp.getPosition()));
            }
        }
        return showData;
    }

    /**
     * 根据标记获取新闻栏目
     *
     * @param isUpdateTime 新闻栏目的版本
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
        // 删除本地数据后 添加到系统库中
        newsDao.deleteAfterSaveNewsType(saveData);

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
//			String masterId = masterDao.getId();
            String masterId = "0";
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
                        dto.getIsLock(), dto.getPosition()));
            }
        } else {
            throw new AppException("获取新闻栏目失败！");
        }
        orderTopNewsTypes(showData);
        return showData;
    }

    /**
     * 新闻栏目排序
     *
     * @param result
     */
    private void orderTopNewsTypes(List<NewsChannelVO> result) {
        Collections.sort(result, new NewsTypeOrder());
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
                .getNewsType(false, "0");
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
            for (NewsChannelVO vo : data) {
                NewsChannel type = new NewsChannel();
                type.setTypeId(vo.getTypeId());
                type.setName(vo.getName());
                type.setIsLock(vo.getIsLock());
                type.setIsMine(1);
                type.setPosition(vo.getPosition());
                type.setMasterId("0");
                saveData.add(type);
            }
        }
        return saveData;
    }

    /**
     * 从数据库中获取缓存的新闻
     *
     * @param catId
     * @return
     */
    public List<NewsVO> getNewsFromDB(String catId) {
        List<News> dbData = newsDao.getOnlineNewsFromDB(catId);
        return parseNews2ShowFromDB(dbData);
    }

    /**
     * (新闻) 数据转换
     *
     * @param dbData
     * @return
     */
    private List<NewsVO> parseNews2ShowFromDB(List<News> dbData) {
        List<NewsVO> showData = new ArrayList<>();
        NewsVO vo;
        if (!Utils.isEmpty(dbData)) {
            for (News news : dbData) {
                if (!Utils.isEmpty(news.getTopNews())) {
                    List<NewsVO> topList = new ArrayList<>();
                    for (News top : news.getTopNews()) {
                        topList.add(parseNews2VOFromDB(top));
                    }
                    vo = new NewsVO();
                    vo.setTop(true);
                    vo.setTopNews(topList);
                    showData.add(vo);
                } else {
                    showData.add(parseNews2VOFromDB(news));
                }
            }
        }
        return showData;
    }

    /**
     * (新闻) 将db转换成vo
     *
     * @param news
     * @return
     */
    private NewsVO parseNews2VOFromDB(News news) {
        NewsVO vo = new NewsVO();
        vo.setId(news.getNewsId());
        vo.setTitle(news.getTitle());
        vo.setIconPath(news.getIconPath());
        vo.setSummary(news.getSummary());
        vo.setSubject(news.getSubject() == 1);
        vo.setOrder(news.getOrder());
        vo.setCatId(news.getCatId());
        vo.setRead(news.getIsRead() == 1);
        vo.setTop(news.getTop() == 1);
        vo.setCreateTime(news.getCreateTime());
        vo.setCommentCount(news.getCommentCount());
        vo.setIconAdUrl(news.getIconAdUrl());
        return vo;
    }

    /**
     * (新闻) 获取网络数据
     *
     * @param catId
     * @param minTime
     * @return
     * @throws JSONException
     */
    public List<NewsVO> getNewsListFromWeb(String catId, long minTime)
            throws JSONException {
        // 获取数据
        List<NewsDto> webData = newsRO.refreshNews(catId,
                BonConstants.LIMIT_GET_NEWS, minTime, null);
        // 判断新闻阅读状态
        for (NewsDto newsDto : webData) {
            newsDto.setIsRead(newsDao.getReadState(newsDto.getId(), catId));
        }
        if (minTime == 0) {
            // 初始化 或者 刷新 时，清除原数据
            newsDao.deleteAllByCatId(catId);
            // 设置栏目刷新时间
            configDao.setCatClickTime(catId, System.currentTimeMillis());
        }
        try {
            newsDao.beginTransaction();
            int startIndex = 0;
            // 添加新闻
            if (!Utils.isEmpty(webData)
                    && !Utils.isEmpty(webData.get(0).getTopNews())) {
                startIndex = 1;
                // 有顶部新闻 先添加顶部新闻
                for (NewsDto dto : webData.get(0).getTopNews()) {
                    newsDao.saveNews(parseNews2DBFromDto(dto));
                }
            }
            if (!Utils.isEmpty(webData)) {
                // 添加剩余新闻
                for (int i = startIndex; i < webData.size(); i++) {
                    NewsDto dto = webData.get(i);
                    newsDao.saveNews(parseNews2DBFromDto(dto));
                }
            }
            newsDao.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            newsDao.endTransaction();
        }
        // 数据转换
        return parseNews2ShowFromDto(webData);
    }

    /**
     * (新闻) 从dto转换到db
     *
     * @param dto
     * @return
     */
    private News parseNews2DBFromDto(NewsDto dto) {
        News news = new News();
        news.setNewsId(dto.getId());
        news.setCatId(dto.getCatId());
        news.setTitle(dto.getTitle());
        news.setIconPath(dto.getIconPath());
        news.setSummary(dto.getSummary());
        news.setTop(dto.getTop());
        news.setIsRead(dto.getIsRead());
        news.setSubject(dto.getSubject());
        news.setCreateTime(dto.getCreateTime());
        news.setUpdateTime(dto.getUpdateTime());
        news.setOrder(dto.getOrder());
        news.setCommentCount(dto.getCommentCount());
        news.setIconAdUrl(dto.getIconAdUrl());
        return news;
    }

    /**
     * (新闻) 将dto转换成vo
     *
     * @param dto
     * @return
     */
    private NewsVO parseNews2VOFromDto(NewsDto dto) {
        NewsVO vo = new NewsVO();
        vo.setId(dto.getId());
        vo.setTitle(dto.getTitle());
        vo.setIconPath(dto.getIconPath());
        vo.setSummary(dto.getSummary());
        vo.setSubject(dto.getSubject() == 1);
        vo.setOrder(dto.getOrder());
        vo.setCatId(dto.getCatId());
        vo.setRead(dto.getIsRead() == 1);
        vo.setTop(dto.getTop() == 1);
        vo.setCreateTime(dto.getCreateTime());
        vo.setCommentCount(dto.getCommentCount());
        vo.setIconAdUrl(dto.getIconAdUrl());
        return vo;
    }

    /**
     * 数据转换
     *
     * @param webData
     * @return
     */
    private List<NewsVO> parseNews2ShowFromDto(List<NewsDto> webData) {
        List<NewsVO> showData = new ArrayList<>();
        if (!Utils.isEmpty(webData)) {
            NewsVO vo;
            for (NewsDto dto : webData) {
                if (!Utils.isEmpty(dto.getTopNews())) {
                    List<NewsVO> topList = new ArrayList<>();
                    for (NewsDto top : dto.getTopNews()) {
                        topList.add(parseNews2VOFromDto(top));
                    }
                    vo = new NewsVO();
                    vo.setTop(true);
                    vo.setTopNews(topList);
                    showData.add(vo);
                } else {
                    showData.add(parseNews2VOFromDto(dto));
                }
            }
        }
        return showData;
    }

    /**
     * 网络请求获取新闻详情
     *
     * @param id
     * @param catId
     * @return
     * @throws JSONException
     */
    public NewsDetailVO getNewsDetailFromWeb(String id, String catId)
            throws JSONException {
        // 设置新闻已读状态
        newsDao.setReadState(id, catId, 1);
        // 访问服务器
        NewsDetailDto webData = newsRO.getNewsDetail(id, catId,
                null);
        if (webData != null) {
            return parseDetail2ShowFromDto(webData);
        }
        return null;
    }

    /**
     * (新闻详情)从dto转换到vo
     *
     * @param dto
     * @return
     */
    private NewsDetailVO parseDetail2ShowFromDto(NewsDetailDto dto) {
        NewsDetailVO vo = new NewsDetailVO();
        vo.setId(dto.getId());
        vo.setTitle(dto.getTitle());
        vo.setSummary(dto.getSummary());
        vo.setcFrom(dto.getcFrom());
        vo.setcTime(Utils.formateTime(dto.getcTime(), false));
        vo.setContent(dto.getContent());
        vo.setCanComment(dto.getCanComment() == 0);
        vo.setShortUrl(dto.getShortUrl());
        vo.setWapUrl(dto.getWapUrl());
        vo.setForwardId(dto.getForwardId());
        vo.setRootId(dto.getRootId());
        vo.setCommentNum(dto.getCommentNum());
        vo.setImgs(dto.getImgs());
        vo.setAdId(dto.getAdId());
        vo.setAdTitle(dto.getAdTitle());
        vo.setAdIconUrl(dto.getAdIconUrl());
        vo.setAdUrl(dto.getAdUrl());
        vo.setIconUrl(dto.getIconUrl());
        return vo;
    }

    /**
     * 获取不同类别的点击时间
     *
     * @param catID
     * @return
     */
    public long getCatClickTime(String catID) {
        return configDao.getCatClickTime(catID, System.currentTimeMillis());
    }

    /**
     * 判断是否需要刷新
     *
     * @param catId
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
