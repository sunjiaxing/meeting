package com.sb.meeting.service;

import android.content.Context;

import com.sb.meeting.common.BonConstants;
import com.sb.meeting.common.Utils;
import com.sb.meeting.dao.AreaDao;
import com.sb.meeting.dao.CompanyDao;
import com.sb.meeting.dao.StudentDao;
import com.sb.meeting.dao.entity.Area;
import com.sb.meeting.dao.entity.Company;
import com.sb.meeting.dao.entity.Student;
import com.sb.meeting.remote.IParam;
import com.sb.meeting.remote.YellowPageRO;
import com.sb.meeting.remote.dto.CertificateDto;
import com.sb.meeting.remote.dto.ClassDto;
import com.sb.meeting.remote.dto.CompanyDetailDto;
import com.sb.meeting.remote.dto.CompanyDto;
import com.sb.meeting.remote.dto.ProductDetailDto;
import com.sb.meeting.remote.dto.ProductDto;
import com.sb.meeting.remote.dto.StudentDto;
import com.sb.meeting.ui.vo.CertificateVO;
import com.sb.meeting.ui.vo.ClassVO;
import com.sb.meeting.ui.vo.CompanyDetailVO;
import com.sb.meeting.ui.vo.CompanyVO;
import com.sb.meeting.ui.vo.ImageVO;
import com.sb.meeting.ui.vo.ProductDetailVO;
import com.sb.meeting.ui.vo.ProductVO;
import com.sb.meeting.ui.vo.StudentVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private final StudentDao studentDao;

    public YellowPageService(Context context) {
        super(context);
        areaDao = new AreaDao(context);
        yellowPageRO = new YellowPageRO(context);
        companyDao = new CompanyDao(context);
        studentDao = new StudentDao(context);
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
     * @param pageIndex 页码
     * @return
     * @throws JSONException
     */
    public List<CompanyVO> getCompanyList(String name, int areaId, int pageIndex) throws JSONException {
        List<CompanyVO> showData = null;
        boolean hasTransaction = false;
        try {
            List<CompanyDto> webData = yellowPageRO.getCompanyList(areaId, name, pageIndex, BonConstants.LIMIT_GET_COMPANY);
            if (!Utils.isEmpty(webData)) {
                showData = new ArrayList<>();
                CompanyVO vo;
                if (pageIndex == 0 && Utils.isEmpty(name) && areaId == 0) {
                    // 初始化|更新    删除全部企业数据
                    companyDao.beginTransaction();
                    hasTransaction = true;
                    companyDao.deleteAllData();
                }
                for (CompanyDto dto : webData) {
                    vo = new CompanyVO();
                    vo.setCompanyId(dto.getCompanyId());
                    vo.setCompanyName(dto.getCompanyName());
                    vo.setLogo(dto.getLogo());
                    vo.setProductDesc(dto.getProductDesc());
                    vo.setCatIds(dto.getCatIds());
                    // 处理企业 经营模式
                    StringBuilder pattern = new StringBuilder();
                    if (!Utils.isEmpty(dto.getPattern())) {
                        String[] split = dto.getPattern().split(",");
                        if (!Utils.isEmpty(split)) {
                            for (String str : split) {
                                if (!Utils.isEmpty(str)) {
                                    pattern.append(str);
                                    pattern.append("，");
                                }
                            }
                            pattern.deleteCharAt(pattern.length() - 1);
                            vo.setPattern(pattern.toString());
                        }
                    }
                    vo.setCompanyType(dto.getCompanyType());
                    vo.setArea(dto.getArea());
                    vo.setIsVIP(dto.getIsVIP() == 1);
                    showData.add(vo);
                    if (pageIndex == 0 && Utils.isEmpty(name) && areaId == 0) {
                        // 初始化|更新
                        Company company = new Company();
                        company.setCompanyId(dto.getCompanyId());
                        company.setCompanyName(dto.getCompanyName());
                        company.setLogo(dto.getLogo());
                        company.setProductDesc(dto.getProductDesc());
                        company.setCatIds(dto.getCatIds());
                        company.setPattern(pattern.toString());
                        company.setCompanyType(dto.getCompanyType());
                        company.setArea(dto.getArea());
                        company.setIsVIP(dto.getIsVIP());
                        companyDao.insert(company);
                    }
                }
                if (hasTransaction) {
                    companyDao.setTransactionSuccessful();
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (hasTransaction) {
                companyDao.endTransaction();
            }
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

    /**
     * 从本地缓存中获取学员列表
     *
     * @return
     */
    public List<StudentVO> getStudentListFromDB() {
        List<StudentVO> showData = null;
        List<Student> dbData = studentDao.getList();
        if (!Utils.isEmpty(dbData)) {
            showData = new ArrayList<>();
            StudentVO vo;
            for (Student student : dbData) {
                vo = new StudentVO();
                vo.setStudentId(student.getStudentId());
                vo.setStudentName(student.getStudentName());
                vo.setPosition(student.getPosition());
                // 处理学员联系方式
                if (student.getIsPublic() == 1) {
                    vo.setPhone(student.getPhone());
                } else {
                    // 隐藏部分信息
                    vo.setPhone(Utils.hideMiddleNumber(student.getPhone(), 4));
                }
                vo.setCompanyId(student.getCompanyId());
                vo.setCompanyName(student.getCompanyName());
                vo.setClassName(student.getClassName());
                vo.setClassPosition(student.getClassPosition());
                vo.setAvatarUrl(student.getAvatarUrl());
                vo.setArea(student.getArea());
                showData.add(vo);
            }
        }
        return showData;
    }

    /**
     * 获取学员列表
     *
     * @param studentName 姓名
     * @param classId     班级id
     * @param areaId      地区id
     * @param pageIndex   页码
     * @return
     * @throws JSONException
     */
    public List<StudentVO> getStudentList(String studentName, int classId, int areaId, int pageIndex) throws JSONException {
        List<StudentVO> showData = null;
        boolean hasTransaction = false;
        try {
            List<StudentDto> webData = yellowPageRO.getStudentList(studentName, classId, areaId, BonConstants.LIMIT_GET_STUDENT, pageIndex);
            if (!Utils.isEmpty(webData)) {
                showData = new ArrayList<>();
                StudentVO vo;
                if (pageIndex == 0 && Utils.isEmpty(studentName) && classId == 0 && areaId == 0) {
                    studentDao.beginTransaction();
                    hasTransaction = true;
                    // 初始化 | 刷新
                    studentDao.deleteAllData();
                }
                for (StudentDto dto : webData) {
                    vo = new StudentVO();
                    vo.setStudentId(dto.getStudentId());
                    vo.setStudentName(dto.getStudentName());
                    vo.setPosition(dto.getPosition());
                    // 处理学员联系方式
                    if (dto.getIsPublic() == 1) {
                        vo.setPhone(dto.getPhone());
                    } else {
                        // 隐藏部分信息
                        vo.setPhone(Utils.hideMiddleNumber(dto.getPhone(), 4));
                    }
                    vo.setCompanyId(dto.getCompanyId());
                    vo.setCompanyName(dto.getCompanyName());
                    vo.setClassName(dto.getClassName());
                    if (!Utils.isEmpty(dto.getClassPosition())) {
                        vo.setClassPosition(dto.getClassPosition());
                    } else {
                        vo.setClassPosition("学员");
                    }
                    vo.setAvatarUrl(dto.getAvatarUrl());
                    vo.setArea(dto.getArea());
                    showData.add(vo);
                    if (pageIndex == 0 && Utils.isEmpty(studentName) && classId == 0 && areaId == 0) {
                        // 添加数据库缓存
                        Student student = new Student();
                        student.setStudentId(dto.getStudentId());
                        student.setStudentName(dto.getStudentName());
                        student.setPosition(dto.getPosition());
                        student.setPhone(dto.getPhone());
                        student.setIsPublic(dto.getIsPublic());
                        student.setCompanyId(dto.getCompanyId());
                        student.setCompanyName(dto.getCompanyName());
                        student.setClassName(dto.getClassName());
                        if (!Utils.isEmpty(dto.getClassPosition())) {
                            student.setClassPosition(dto.getClassPosition());
                        } else {
                            student.setClassPosition("学员");
                        }
                        student.setAvatarUrl(dto.getAvatarUrl());
                        student.setArea(dto.getArea());
                        studentDao.insert(student);
                    }
                }
                if (hasTransaction) {
                    studentDao.setTransactionSuccessful();
                }
            }
        } catch (JSONException e) {
            throw e;
        } finally {
            if (hasTransaction) {
                studentDao.endTransaction();
            }
        }
        return showData;
    }

    /**
     * 获取企业详情
     *
     * @param companyId 企业id
     * @return
     * @throws JSONException
     */
    public CompanyDetailVO getCompanyDetail(int companyId) throws JSONException {
        CompanyDetailDto dto = yellowPageRO.getCompanyDetail(companyId);
        CompanyDetailVO vo = null;
        if (dto != null) {
            vo = new CompanyDetailVO();
            vo.setCompanyId(dto.getCompanyId());
            vo.setCompanyName(dto.getCompanyName());
            vo.setLogo(dto.getLogo());
            vo.setProductDesc(dto.getProductDesc());
            vo.setArea(dto.getArea());
            vo.setIsVIP(dto.getIsVIP() == 1);
            // 解析滚动图片
            if (!Utils.isEmpty(dto.getImageList())) {
                JSONArray array = new JSONArray(dto.getImageList());
                List<ImageVO> images = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    ImageVO img = new ImageVO();
                    JSONObject json = array.getJSONObject(i);
                    img.setUrl(json.getString(IParam.URL));
                    img.setDesc(json.getString(IParam.ALT));
                    images.add(img);
                }
                vo.setImageList(images);
            }
            vo.setCompanyIntroduce(dto.getCompanyIntroduce());
            vo.setWebUrl(dto.getWebUrl());
            vo.setContact(dto.getContact());
            vo.setPhone(dto.getPhone());
            vo.setEmail(dto.getEmail());
            vo.setQQ(dto.getQQ());
            vo.setCompanyAddress(dto.getCompanyAddress());
            if (!Utils.isEmpty(dto.getCompanyMap())) {
                String[] split = dto.getCompanyMap().split("\\|");
                vo.setLongitude(Double.parseDouble(split[0]));
                vo.setLatitude(Double.parseDouble(split[1]));
            }
            if (!Utils.isEmpty(dto.getCertificateList())) {
                vo.setCertificateList(new ArrayList<CertificateVO>());
                for (CertificateDto tmp : dto.getCertificateList()) {
                    CertificateVO item = new CertificateVO();
                    item.setName(tmp.getName());
                    item.setOrganization(tmp.getOrganization());
                    item.setThumb(tmp.getThumb());
                    vo.getCertificateList().add(item);
                }
            }
            if (!Utils.isEmpty(dto.getProductList())) {
                vo.setProductList(new ArrayList<ProductVO>());
                for (ProductDto tmp : dto.getProductList()) {
                    ProductVO item = new ProductVO();
                    item.setProductId(tmp.getProductId());
                    item.setProductName(tmp.getProductName());
                    item.setThumb(tmp.getThumb());
                    vo.getProductList().add(item);
                }
            }
        }
        return vo;
    }

    /**
     * 获取产品列表
     *
     * @param companyId 企业id
     * @param pageIndex 页码
     * @return
     * @throws JSONException
     */
    public List<ProductVO> getProductList(int companyId, int pageIndex) throws JSONException {
        List<ProductVO> showData = null;
        List<ProductDto> webData = yellowPageRO.getProductList(companyId, pageIndex, BonConstants.LIMIT_GET_PRODUCT);
        if (!Utils.isEmpty(webData)) {
            showData = new ArrayList<>();
            ProductVO vo;
            for (ProductDto dto : webData) {
                vo = new ProductVO();
                vo.setProductId(dto.getProductId());
                vo.setProductName(dto.getProductName());
                vo.setThumb(dto.getThumb());
                showData.add(vo);
            }
        }
        return showData;
    }

    /**
     * 获取 产品详情
     *
     * @param productId 产品id
     * @return
     * @throws JSONException
     */
    public ProductDetailVO getProductDetail(int productId) throws JSONException {
        ProductDetailVO detailVO = null;
        ProductDetailDto dto = yellowPageRO.getProductDetail(productId);
        if (dto != null) {
            detailVO = new ProductDetailVO();
            detailVO.setProductId(dto.getProductId());
            detailVO.setProductName(dto.getProductName());
            detailVO.setThumb(dto.getThumb());
            detailVO.setContent(dto.getContent());
        }
        return detailVO;
    }

    /**
     * 获取省份列表
     *
     * @return
     */
    public List<Area> getProvinceList() {
        return areaDao.getAreaList(0);
    }

    /**
     * 获取班级列表
     *
     * @return
     * @throws JSONException
     */
    public List<ClassVO> getClassList() throws JSONException {
        List<ClassVO> showData = null;
        List<ClassDto> webData = yellowPageRO.getClassList();
        if (!Utils.isEmpty(webData)) {
            showData = new ArrayList<>();
            ClassVO vo;
            for (ClassDto dto : webData) {
                vo = new ClassVO();
                vo.setClassId(dto.getClassId());
                vo.setClassName(dto.getClassName());
                showData.add(vo);
            }
        }
        return showData;
    }
}
