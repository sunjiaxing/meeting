package com.zhengshang.meeting.service;

import android.content.Context;

import com.zhengshang.meeting.common.Utils;
import com.zhengshang.meeting.remote.GoodsRO;
import com.zhengshang.meeting.remote.dto.NameAndValueDto;
import com.zhengshang.meeting.ui.vo.GoodsCategoryVO;
import com.zhengshang.meeting.ui.vo.ValidTimeVO;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 易物 service
 * Created by sun on 2016/2/23.
 */
public class GoodsService extends BaseService {

    private GoodsRO goodsRO;

    public GoodsService(Context context) {
        super(context);
        goodsRO = new GoodsRO(context);
    }

    /**
     * 获取物品分类
     *
     * @return
     * @throws JSONException
     */
    public List<GoodsCategoryVO> getGoodsCategories() throws JSONException {
        List<GoodsCategoryVO> showData = new ArrayList<>();
//        List<NameAndValueDto> webData = goodsRO.getGoodsCategories();
//        if (!Utils.isEmpty(webData)) {
//            GoodsCategoryVO vo;
//            for (NameAndValueDto dto : webData) {
//                vo = new GoodsCategoryVO();
//                vo.setName(dto.getName());
//                vo.setId(dto.getValue());
//                showData.add(vo);
//            }
//        }
        // 构建临时数据
        GoodsCategoryVO vo = new GoodsCategoryVO();
        vo.setName("酒水");
        vo.setId(1);
        showData.add(vo);
        vo = new GoodsCategoryVO();
        vo.setName("办公用品");
        vo.setId(2);
        showData.add(vo);
        return showData;
    }

    /**
     * 获取有效时间
     * @return
     * @throws JSONException
     */
    public List<ValidTimeVO> getValidTime() throws JSONException {
        List<ValidTimeVO> showData = new ArrayList<>();
//        List<NameAndValueDto> webData = goodsRO.getValidTime();
//        if (!Utils.isEmpty(webData)) {
//            ValidTimeVO vo;
//            for (NameAndValueDto dto : webData) {
//                vo = new ValidTimeVO();
//                vo.setId(dto.getValue());
//                vo.setName(dto.getName());
//                showData.add(vo);
//            }
//        }
        // 构建临时数据
        ValidTimeVO vo = new ValidTimeVO();
        vo.setName("一周内有效");
        vo.setId(1);
        showData.add(vo);
        vo = new ValidTimeVO();
        vo.setName("两周内有效");
        vo.setId(2);
        showData.add(vo);
        vo = new ValidTimeVO();
        vo.setName("一月内有效");
        vo.setId(3);
        showData.add(vo);
        return showData;
    }
}
