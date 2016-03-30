package com.sb.meeting.service;

import android.content.Context;

import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.Utils;
import com.sb.meeting.dao.AreaDao;
import com.sb.meeting.dao.CompanyDao;
import com.sb.meeting.dao.entity.Company;
import com.sb.meeting.remote.YellowPageRO;
import com.sb.meeting.remote.dto.CompanyDto;
import com.sb.meeting.ui.vo.CompanyVO;
import com.sb.meeting.ui.vo.StudentVO;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 黄页 service
 * Created by sun on 2016/3/30.
 */
public class YellowPageService extends BaseService {

    private final AreaDao areaDao;
    private final YellowPageRO yellowPageRO;
    private final CompanyDao companyDao;

    public YellowPageService(Context context) {
        super(context);
        areaDao = new AreaDao(context);
        yellowPageRO = new YellowPageRO(context);
        companyDao = new CompanyDao(context);
    }

    /**
     * 初始化地区
     */
    public void initArea() {
        try {
            if (!areaDao.hasData()) {
                areaDao.beginTransaction();
                // 导入数据
                InputStream is = mContext.getAssets().open("area.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    areaDao.execSQL(line);
                }
                areaDao.setTransactionSuccessful();
                areaDao.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取企业列表（可用于查询）
     *
     * @param name      企业名称
     * @param areaId    区域id
     * @param pageIndex
     * @return
     * @throws JSONException
     */
    public List<CompanyVO> getCompanyList(String name, int areaId, int pageIndex) throws JSONException {
        List<CompanyVO> showData = new ArrayList<>();
        try {
            List<CompanyDto> webData = yellowPageRO.getCompanyList(areaId, name, pageIndex, BonConstants.LIMIT_GET_COMPANY);
            if (!Utils.isEmpty(webData)) {
                CompanyVO vo;
                if (pageIndex == 0) {
                    // 初始化|更新    删除全部企业数据
                    companyDao.beginTransaction();
                    companyDao.deleteAllData();
                }
                for (CompanyDto dto : webData) {
                    vo = new CompanyVO();
                    vo.setCompanyId(dto.getCompanyId());
                    vo.setCompanyName(dto.getCompanyName());
                    vo.setLogo(dto.getLogo());
                    vo.setProductDesc(dto.getProductDesc());
                    vo.setCatIds(dto.getCatIds());
                    vo.setPattern(dto.getPattern());
                    vo.setCompanyType(dto.getCompanyType());
                    vo.setArea(dto.getArea());
                    vo.setIsVIP(dto.getIsVIP() == 1);
                    showData.add(vo);
                    if (pageIndex == 0) {
                        // 初始化|更新
                        Company company = new Company();
                        company.setCompanyId(dto.getCompanyId());
                        company.setCompanyName(dto.getCompanyName());
                        company.setLogo(dto.getLogo());
                        company.setProductDesc(dto.getProductDesc());
                        company.setCatIds(dto.getCatIds());
                        company.setPattern(dto.getPattern());
                        company.setCompanyType(dto.getCompanyType());
                        company.setArea(dto.getArea());
                        company.setIsVIP(dto.getIsVIP());
                        companyDao.insert(company);
                    }
                }
                if (pageIndex == 0) {
                    companyDao.setTransactionSuccessful();
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            companyDao.endTransaction();
        }
        return showData;
    }

    /**
     * 从缓存中获取企业列表
     *
     * @return
     */
    public List<CompanyVO> getCompanyListFromDB() {
        List<CompanyVO> showData = new ArrayList<>();
        List<Company> dbData = companyDao.getList();
        if (!Utils.isEmpty(dbData)) {
            CompanyVO vo;
            for (Company company : dbData) {
                vo = new CompanyVO();
                vo.setCompanyId(company.getCompanyId());
                vo.setCompanyName(company.getCompanyName());
                vo.setLogo(company.getLogo());
                vo.setProductDesc(company.getProductDesc());
                vo.setCatIds(company.getCatIds());
                vo.setPattern(company.getPattern());
                vo.setCompanyType(company.getCompanyType());
                vo.setArea(company.getArea());
                vo.setIsVIP(company.getIsVIP() == 1);
                showData.add(vo);
            }
        }
        return showData;
    }

    public List<StudentVO> getStudentListFromDB() {
        return null;
    }

    public List<StudentVO> getStudentList(String studentName, int classId, int areaId, int pageIndex) {
        return null;
    }

}
