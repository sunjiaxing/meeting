package com.sb.meeting.service;

import android.content.Context;
import android.graphics.Bitmap;

import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.FileUtil;
import com.sb.meeting.common.Utils;
import com.sb.meeting.dao.GoodsDao;
import com.sb.meeting.dao.entity.CheckingGoods;
import com.sb.meeting.dao.entity.Goods;
import com.sb.meeting.remote.GoodsRO;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.remote.dto.GoodsDetailDto;
import com.sb.meeting.remote.dto.GoodsDto;
import com.sb.meeting.remote.dto.NameAndValueDto;
import com.sb.meeting.ui.vo.GoodsCategoryVO;
import com.sb.meeting.ui.vo.GoodsVO;
import com.sb.meeting.ui.vo.ImageVO;
import com.sb.meeting.ui.vo.ValidTimeVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 易物 service
 * Created by sun on 2016/2/23.
 */
public class GoodsService extends BaseService {

    private GoodsRO goodsRO;
    private GoodsDao goodsDao;

    public GoodsService(Context context) {
        super(context);
        goodsRO = new GoodsRO(context);
        goodsDao = new GoodsDao(context);
    }

    /**
     * 获取物品分类
     *
     * @return
     * @throws JSONException
     */
    public List<GoodsCategoryVO> getGoodsCategories() throws JSONException {
        List<GoodsCategoryVO> showData = new ArrayList<>();
        List<NameAndValueDto> webData = goodsRO.getGoodsCategories();
        if (!Utils.isEmpty(webData)) {
            GoodsCategoryVO vo;
            for (NameAndValueDto dto : webData) {
                vo = new GoodsCategoryVO();
                vo.setName(dto.getName());
                vo.setId(dto.getValue());
                showData.add(vo);
            }
        }
        return showData;
    }

    /**
     * 获取有效时间
     *
     * @return
     * @throws JSONException
     */
    public List<ValidTimeVO> getValidTime() throws JSONException {
        List<ValidTimeVO> showData = new ArrayList<>();
        List<NameAndValueDto> webData = goodsRO.getValidTime();
        if (!Utils.isEmpty(webData)) {
            ValidTimeVO vo;
            for (NameAndValueDto dto : webData) {
                vo = new ValidTimeVO();
                vo.setId(dto.getValue());
                vo.setName(dto.getName());
                showData.add(vo);
            }
        }
        return showData;
    }

    /**
     * 发布物品
     *
     * @param goodsVO 物品对象
     * @param mobile  手机号
     * @throws JSONException
     */
    public void publishGoods(GoodsVO goodsVO, String mobile, String uuid) throws JSONException {
        JSONArray imageJson = new JSONArray();
        // 上传图片
        if (!Utils.isEmpty(goodsVO.getImageList())) {
            String fileName;
            File file;
            for (ImageVO imgVO : goodsVO.getImageList()) {
                if (imgVO.getUrl().startsWith(IParam.HTTP)) {
                    continue;
                }
                file = new File(imgVO.getUrl());
                if (file.exists()) {
                    fileName = file.getName();
                    file = new File(BonConstants.PATH_COMPRESSED, fileName);
                    if (!file.exists()) {
                        // 执行原图压缩
                        // 压缩图片
                        Bitmap bm = Utils.comp(imgVO.getUrl());
                        // 判断图片是否进过系统旋转
                        int degree = FileUtil.getBitmapDegree(imgVO.getUrl());
                        if (degree != 0) {
                            bm = FileUtil.rotateBitmapByDegree(bm, degree);
                        }
                        file = Utils.savePic(bm, BonConstants.PATH_COMPRESSED
                                + fileName);
                        if (bm != null && !bm.isRecycled()) {
                            bm.recycle();
                        }
                    }
                    String url = goodsRO.uploadFile(file);
                    JSONObject json = new JSONObject();
                    json.put(IParam.URL, url);
                    json.put(IParam.DESC, imgVO.getDesc());
                    imageJson.put(json);
                    if (imgVO.getUrl().equals(goodsVO.getCoverUrl())) {
                        goodsVO.setCoverUrl(url);
                    }
                }
            }
        }
        int id = goodsRO.publishGoods(goodsVO, configDao.getUserId(), mobile, imageJson.toString());
        goodsDao.addCheckingDataId(id, uuid);
    }

    /**
     * 获取 物品列表
     *
     * @param pageIndex 页码
     * @return
     * @throws JSONException
     */
    public List<GoodsVO> getGoodsList(int pageIndex) throws JSONException {
        List<GoodsDto> webData = goodsRO.getGoodsList(configDao.getUserId(), pageIndex, BonConstants.LIMIT_GET_GOODS);
        List<GoodsVO> showData = new ArrayList<>();
        goodsDao.beginTransaction();
        if (pageIndex == 0) {
            // 刷新
            goodsDao.deleteAll();
        }
        Goods goods;
        GoodsVO vo;
        for (GoodsDto dto : webData) {
            goods = new Goods();
            goods.setId(dto.getId());
            goods.setGoodsName(dto.getGoodsName());
            goods.setCoverUrl(dto.getCoverUrl());
            goods.setScanNum(dto.getScanNum());
            goods.setAttentionNum(dto.getAttentionNum());
            goods.setPublishTime(dto.getPublishTime());
            goods.setMarketPrice(dto.getMarketPrice());
            goods.setExchangePrice(dto.getExchangePrice());
            goods.setValidTimeStr(dto.getValidTimeStr());
            goods.setCount(dto.getCount());
            goods.setAttentionState(dto.getIsAttention());
            goodsDao.insertGoods(goods);

            vo = new GoodsVO();
            vo.setId(dto.getId());
            vo.setName(dto.getGoodsName());
            vo.setCoverUrl(dto.getCoverUrl());
            vo.setScanNum(dto.getScanNum());
            vo.setAttentionNum(dto.getAttentionNum());
            vo.setPublishTime(Utils.formateTime(dto.getPublishTime(), "yyyy/MM/dd"));
            vo.setMarketPrice(dto.getMarketPrice());
            vo.setExchangePrice(dto.getExchangePrice());
            vo.setValidTimeStr(dto.getValidTimeStr());
            vo.setCount(dto.getCount());
            vo.setIsAttention(dto.getIsAttention() == 1);
            showData.add(vo);
        }
        goodsDao.setTransactionSuccessful();
        goodsDao.endTransaction();
        return showData;
    }

    /**
     * 从缓存中获取数据
     *
     * @return
     */
    public List<GoodsVO> getGoodsListFromDB() {
        List<Goods> dbData = goodsDao.getGoodsList();
        if (!Utils.isEmpty(dbData)) {
            List<GoodsVO> showData = new ArrayList<>();
            GoodsVO vo;
            for (Goods goods : dbData) {
                vo = new GoodsVO();
                vo.setId(goods.getId());
                vo.setName(goods.getGoodsName());
                vo.setCoverUrl(goods.getCoverUrl());
                vo.setScanNum(goods.getScanNum());
                vo.setAttentionNum(goods.getAttentionNum());
                vo.setPublishTime(Utils.formateTime(goods.getPublishTime(), "yyyy/MM/dd"));
                vo.setMarketPrice(goods.getMarketPrice());
                vo.setExchangePrice(goods.getExchangePrice());
                vo.setValidTimeStr(goods.getValidTimeStr());
                vo.setCount(goods.getCount());
                vo.setIsAttention(goods.getAttentionState() == 1);
                showData.add(vo);
            }
            return showData;
        }
        return null;
    }

    /**
     * 获取 物品详情
     *
     * @param goodsId 物品id
     * @return
     * @throws JSONException
     */
    public GoodsVO getGoodsDetail(int goodsId) throws JSONException {
        GoodsDetailDto dto = goodsRO.getGoodsDetail(goodsId, configDao.getUserId());
        if (dto != null) {
            GoodsVO detailVO = new GoodsVO();
            detailVO.setId(dto.getId());
            detailVO.setName(dto.getGoodsName());
            detailVO.setCoverUrl(dto.getCoverUrl());
            detailVO.setNeedCategoryStr(dto.getNeedCategory());
            detailVO.setCategoryStr(dto.getCategory());
            detailVO.setValidTimeStr(dto.getValidTimeStr());
            detailVO.setAttentionNum(dto.getAttentionNum());
            detailVO.setScanNum(dto.getScanNum());
            detailVO.setMarketPrice(dto.getMarketPrice());
            detailVO.setExchangePrice(dto.getExchangePrice());
            detailVO.setPublishTime(Utils.formateTime(dto.getPublishTime(), "yyyy-MM-dd"));
            detailVO.setCount(dto.getCount());
            detailVO.setIsAttention(dto.getIsAttention() == 1);
            JSONArray array = new JSONArray(dto.getImgJson());
            List<ImageVO> imageList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                ImageVO img = new ImageVO();
                img.parseJson(array.getJSONObject(i));
                imageList.add(img);
            }
            detailVO.setImageList(imageList);
            return detailVO;
        }
        return null;
    }

    /**
     * 关注
     *
     * @param goodsId 物品id
     * @return
     * @throws JSONException
     */
    public boolean attention(int goodsId) throws JSONException {
        return goodsRO.attention(goodsId, configDao.getUserId(), 0);
    }

    /**
     * 取消关注
     *
     * @param goodsId 物品id
     * @return
     * @throws JSONException
     */
    public boolean cancelAttention(int goodsId) throws JSONException {
        return goodsRO.attention(goodsId, configDao.getUserId(), 1);
    }

    /**
     * 获取 发布 的物品
     *
     * @param pageIndex 页码
     * @return
     * @throws JSONException
     */
    public List<GoodsVO> getPublishedGoods(int pageIndex) throws JSONException {
        List<GoodsDto> webData = goodsRO.getPublishedGoods(configDao.getUserId(), pageIndex, BonConstants.LIMIT_GET_PUBLISHED_GOODS);
        List<GoodsVO> showData = null;
        if (!Utils.isEmpty(webData)) {
            showData = new ArrayList<>();
            GoodsVO vo;
            for (GoodsDto dto : webData) {
                vo = new GoodsVO();
                vo.setId(dto.getId());
                vo.setName(dto.getGoodsName());
                vo.setCoverUrl(dto.getCoverUrl());
                vo.setExchangePrice(dto.getExchangePrice());
                vo.setMarketPrice(dto.getMarketPrice());
                vo.setScanNum(dto.getScanNum());
                vo.setCount(dto.getCount());
                vo.setAttentionNum(dto.getAttentionNum());
                vo.setPublishTime(Utils.formateTime(dto.getPublishTime(), "yyyy/MM/dd"));
                vo.setValidTimeStr(dto.getValidTimeStr());
                if (dto.getCheckingState() == -1) {
                    vo.setCheckingState(GoodsVO.CheckingState.FAIL);
                } else if (dto.getCheckingState() == 0) {
                    vo.setCheckingState(GoodsVO.CheckingState.CHECKING);
                } else if (dto.getCheckingState() == 1) {
                    vo.setCheckingState(GoodsVO.CheckingState.PASS);
                }
                showData.add(vo);
            }
        }
        return showData;
    }

    /**
     * 获取 我 关注的物品
     *
     * @param pageIndex 页码
     * @return
     * @throws JSONException
     */
    public List<GoodsVO> getAttentionGoods(int pageIndex) throws JSONException {
        List<GoodsDto> webData = goodsRO.getAttentionGoods(configDao.getUserId(), pageIndex, BonConstants.LIMIT_GET_ATTENTION_GOODS);
        List<GoodsVO> showData = null;
        if (!Utils.isEmpty(webData)) {
            showData = new ArrayList<>();
            GoodsVO vo;
            for (GoodsDto dto : webData) {
                vo = new GoodsVO();
                vo.setId(dto.getId());
                vo.setName(dto.getGoodsName());
                vo.setCoverUrl(dto.getCoverUrl());
                vo.setExchangePrice(dto.getExchangePrice());
                vo.setMarketPrice(dto.getMarketPrice());
                vo.setScanNum(dto.getScanNum());
                vo.setCount(dto.getCount());
                vo.setAttentionNum(dto.getAttentionNum());
                vo.setPublishTime(Utils.formateTime(dto.getPublishTime(), "yyyy/MM/dd"));
                vo.setValidTimeStr(dto.getValidTimeStr());
                vo.setIsAttention(true);
                showData.add(vo);
            }
        }
        return showData;
    }

    /**
     * 保存 审核中的数据
     *
     * @param checkingGoods
     */
    public void saveCheckingData(CheckingGoods checkingGoods) {
        checkingGoods.setSendSuccess(-1);
        goodsDao.saveCheckingData(checkingGoods);
    }

    /**
     * 获取审核中的数据
     *
     * @return
     */
    public CheckingGoods getCheckingData() {
        return goodsDao.getCheckingData();
    }

    /**
     * 删除 审核中的物品
     *
     * @param goodsId 物品id
     */
    public void deleteCheckingData(String goodsId) {
        goodsDao.deleteCheckingData(goodsId);
    }

    /**
     * 更新 物品信息
     *
     * @param goodsVO 物品
     * @param mobile  联系方式
     * @throws JSONException
     */
    public void updateGoodsInfo(GoodsVO goodsVO, String mobile) throws JSONException {
        goodsVO.setCoverUrl(Utils.deleteHeadOfUrl(goodsVO.getCoverUrl()));
        JSONArray imageJson = new JSONArray();
        // 上传图片
        if (!Utils.isEmpty(goodsVO.getImageList())) {
            String fileName;
            File file;
            for (ImageVO imgVO : goodsVO.getImageList()) {
                if (imgVO.getUrl().startsWith(IParam.HTTP)) {
                    JSONObject json = new JSONObject();
                    json.put(IParam.URL, Utils.deleteHeadOfUrl(imgVO.getUrl()));
                    json.put(IParam.DESC, imgVO.getDesc());
                    imageJson.put(json);
                    continue;
                }
                file = new File(imgVO.getUrl());
                if (file.exists()) {
                    fileName = file.getName();
                    file = new File(BonConstants.PATH_COMPRESSED, fileName);
                    if (!file.exists()) {
                        // 执行原图压缩
                        // 压缩图片
                        Bitmap bm = Utils.comp(imgVO.getUrl());
                        // 判断图片是否进过系统旋转
                        int degree = FileUtil.getBitmapDegree(imgVO.getUrl());
                        if (degree != 0) {
                            bm = FileUtil.rotateBitmapByDegree(bm, degree);
                        }
                        file = Utils.savePic(bm, BonConstants.PATH_COMPRESSED
                                + fileName);
                        if (bm != null && !bm.isRecycled()) {
                            bm.recycle();
                        }
                    }
                    String url = goodsRO.uploadFile(file);
                    JSONObject json = new JSONObject();
                    json.put(IParam.URL, url);
                    json.put(IParam.DESC, imgVO.getDesc());
                    imageJson.put(json);
                    if (imgVO.getUrl().equals(goodsVO.getCoverUrl())) {
                        goodsVO.setCoverUrl(url);
                    }
                }
            }
        }
        goodsRO.updateGoods(goodsVO, configDao.getUserId(), mobile, imageJson.toString());
    }
}
