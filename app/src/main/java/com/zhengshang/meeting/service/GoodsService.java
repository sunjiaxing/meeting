package com.zhengshang.meeting.service;

import android.content.Context;
import android.graphics.Bitmap;

import com.zhengshang.meeting.common.BonConstants;
import com.zhengshang.meeting.common.FileUtil;
import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.dao.GoodsDao;
import com.zhengshang.meeting.dao.entity.Goods;
import com.zhengshang.meeting.remote.GoodsRO;
import com.zhengshang.meeting.remote.IParam;
import com.zhengshang.meeting.remote.dto.GoodsDto;
import com.zhengshang.meeting.remote.dto.NameAndValueDto;
import com.zhengshang.meeting.ui.vo.GoodsCategoryVO;
import com.zhengshang.meeting.ui.vo.GoodsVO;
import com.zhengshang.meeting.ui.vo.ImageVO;
import com.zhengshang.meeting.ui.vo.ValidTimeVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
//        // 构建临时数据
//        GoodsCategoryVO vo = new GoodsCategoryVO();
//        vo.setName("酒水");
//        vo.setId(1);
//        showData.add(vo);
//        vo = new GoodsCategoryVO();
//        vo.setName("办公用品");
//        vo.setId(2);
//        showData.add(vo);
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
//        // 构建临时数据
//        ValidTimeVO vo = new ValidTimeVO();
//        vo.setName("一周内有效");
//        vo.setId(1);
//        showData.add(vo);
//        vo = new ValidTimeVO();
//        vo.setName("两周内有效");
//        vo.setId(2);
//        showData.add(vo);
//        vo = new ValidTimeVO();
//        vo.setName("一月内有效");
//        vo.setId(3);
//        showData.add(vo);
        return showData;
    }

    /**
     * 发布物品
     *
     * @param goodsVO
     * @param mobile
     * @throws JSONException
     */
    public void publishGoods(GoodsVO goodsVO, String mobile) throws JSONException, IOException {
        JSONArray imageJson = new JSONArray();
        // 上传图片
        if (!Utils.isEmpty(goodsVO.getImageList())) {
            String fileName;
            File file;
            for (ImageVO imgVO : goodsVO.getImageList()) {
                file = new File(imgVO.getFilePath());
                if (file.exists()) {
                    fileName = file.getName();
                    file = new File(BonConstants.PATH_COMPRESSED, fileName);
                    if (!file.exists()) {
                        // 执行原图压缩
                        // 压缩图片
                        Bitmap bm = Utils.comp(imgVO.getFilePath());
                        // 判断图片是否进过系统旋转
                        int degree = FileUtil.getBitmapDegree(imgVO.getFilePath());
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
                    imgVO.setUrl(url);
                    JSONObject json = new JSONObject();
                    json.put(IParam.URL, url);
                    json.put(IParam.DESC, imgVO.getDesc());
                    imageJson.put(json);
                    if (imgVO.getFilePath().equals(goodsVO.getCoverUrl())) {
                        goodsVO.setCoverUrl(url);
                    }
                }
            }
        }
        goodsRO.publishGoods(goodsVO, configDao.getUserId(), mobile, imageJson.toString());
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
            goodsDao.insertGoods(goods);

            vo = new GoodsVO();
            vo.setId(dto.getId());
            vo.setName(dto.getGoodsName());
            vo.setCoverUrl(dto.getCoverUrl());
            vo.setScanNum(dto.getScanNum());
            vo.setAttentionNum(dto.getAttentionNum());
            vo.setPublishTime(Utils.formateTime(dto.getPublishTime(), true));
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
                vo.setPublishTime(Utils.formateTime(goods.getPublishTime(), true));
                showData.add(vo);
            }
            return showData;
        }
        return null;
    }
}
