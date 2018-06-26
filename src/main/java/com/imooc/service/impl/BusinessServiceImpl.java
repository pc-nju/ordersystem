package com.imooc.service.impl;

import com.imooc.bean.Business;
import com.imooc.bean.Dictionary;
import com.imooc.constant.DictionaryConstant;
import com.imooc.constant.FinalName;
import com.imooc.dao.BusinessDao;
import com.imooc.dao.DictionaryDao;
import com.imooc.dto.BusinessDto;
import com.imooc.dto.BusinessListDto;
import com.imooc.entity.Page;
import com.imooc.service.BusinessService;
import com.imooc.util.FileUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 潘畅
 * @date 2018/5/31 21:42
 */
@Service
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    private DictionaryDao dictionaryDao;

    @Autowired
    private BusinessDao businessDao;

    @Value("${business.imgSavePath}")
    private String imgSavePath;
    @Value("${business.pageSize}")
    private String businessPageSize;
    @Value("${business.url}")
    private String imgUrl;

    @Override
    public List<Dictionary> getDictionariesByType(String dictionaryType) {
        Dictionary dictionary = new Dictionary();
        dictionary.setType(dictionaryType);
        List<Dictionary> dictionaries = dictionaryDao.selectDictionaries(dictionary);
        if (CollectionUtils.isNotEmpty(dictionaries)){
            return dictionaries;
        }
        return null;
    }

    @Override
    public boolean addBusiness(BusinessDto businessDto) {
        MultipartFile imgFile = businessDto.getImgFile();
        try {
            if (imgFile != null && imgFile.getSize() > 0){
                String imgFileName = FileUtil.save(imgFile, imgSavePath);
                Business business = new Business();
                BeanUtils.copyProperties(businessDto, business);
                business.setImgFileName(imgFileName);
                int affectedRow = businessDao.insertBusiness(business);
                if (affectedRow == 1){
                    return true;
                }
            }
        } catch (IOException e) {
            //todo 加日志
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<BusinessDto> searchBusinessByPage(Model model) {
        BusinessDto businessDto = new BusinessDto();
        Page page = new Page();
        page.setPageSize(Integer.parseInt(businessPageSize));
        businessDto.setPage(page);
        List<BusinessDto> businessDtoList = searchByPage(businessDto);
        model.addAttribute(FinalName.RESUKT_LIST, businessDtoList);
        model.addAttribute(FinalName.SEARCH_PARAM, businessDto);
        return searchByPage(businessDto);
    }

    @Override
    public List<BusinessDto> searchBusinessByPage(String searchKey, String city, String category, Integer currentPage, Model model) {
        BusinessDto businessDto = new BusinessDto();
        businessDto.setSearchKey(searchKey);
        businessDto.setCity(city);
        businessDto.setCategory(category);
        Page page = new Page();
        page.setCurrentPage(currentPage);
        page.setPageSize(Integer.parseInt(businessPageSize));
        businessDto.setPage(page);
        List<BusinessDto> businessDtoList = searchByPage(businessDto);
        model.addAttribute(FinalName.RESUKT_LIST, businessDtoList);
        model.addAttribute(FinalName.SEARCH_PARAM, businessDto);
        model.addAttribute(FinalName.SEARCH_KEY, businessDto.getSearchKey());
        return businessDtoList;
    }

    @Override
    public boolean deleteBusiness(Integer id) {
        Business business = new Business();
        business.setId(id);
        List<Business> businessList = businessDao.selectBusinesses(business);
        if (CollectionUtils.isNotEmpty(businessList)){
            String imgFileName = businessList.get(0).getImgFileName();
            //删除数据库记录
            int affectedRow = businessDao.deleteBusiness(business);
            if (affectedRow == 1){
                //删除服务器上对应的图片
                if (!StringUtils.isBlank(imgFileName)){
                    if (FileUtil.deleteFile(imgSavePath + imgFileName)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public BusinessDto searchBusinessById(Integer id) {
        BusinessDto businessDto = new BusinessDto();
        Business business = new Business();
        business.setId(id);
        List<Business> businessList = businessDao.selectBusinesses(business);
        if (CollectionUtils.isNotEmpty(businessList)){
            BeanUtils.copyProperties(businessList.get(0), businessDto);
            businessDto.setImg(imgUrl + businessDto.getImgFileName());
        }
        return businessDto;
    }

    @Override
    public boolean modifyBusiness(BusinessDto businessDto) {
        if (businessDto == null){
            return false;
        }
        String oldImgFileName = FileUtil.getFileNameByUrl(businessDto.getImg());
        Business business = new Business();
        business.setId(businessDto.getId());
        List<Business> businessList = businessDao.selectBusinesses(business);
        if (CollectionUtils.isNotEmpty(businessList)) {
            //先更新数据库里的信息
            String newImgFileName = "";
            //先看有没有上传新的图片
            MultipartFile imgFile = businessDto.getImgFile();
            if (imgFile != null && imgFile.getSize() > 0) {
                //上传新图片
                try {
                    newImgFileName = FileUtil.save(imgFile, imgSavePath);
                    businessDto.setImgFileName(newImgFileName);
                } catch (IOException e) {
                    //todo 加日志
                    e.printStackTrace();
                }
            }
            //更新数据库中的记录
            BeanUtils.copyProperties(businessDto, business);
            int affectedRow = businessDao.updateBusiness(business);
            if (affectedRow != 1){
                return false;
            }
            if (!StringUtils.isBlank(newImgFileName)){
                //删除旧的图片
                FileUtil.deleteFile(imgSavePath + oldImgFileName);
            }
        }
        return true;
    }

    @Override
    public BusinessListDto searchBusinessForApi(BusinessDto businessDto) {
        BusinessListDto businessListDto = new BusinessListDto();
        if (businessDto != null){
            //前端分类code采用的是拼音，城市采用的是汉字，后端code统一采用的是数字字符串，要做一个转换，后端能识别
            if (!transferDictionaryCode(businessDto)){
                return null;
            }
            /*
             *     前端是从0开始，第0页，第1页，若“currentPage + 1”不这样做，那么第0页和第1页的数据是一样的，
             * 因为Page会把0处理为1
             */
            businessDto.getPage().setCurrentPage(businessDto.getPage().getCurrentPage() + 1);
            List<BusinessDto> businessDtoList = searchByPage(businessDto);
            wrapBusinessDtoList(businessDtoList, businessListDto, businessDto);
        }
        return businessListDto;
    }

    @Override
    public BusinessListDto searchHomeListForApi(BusinessDto businessDto) {
        //若为推荐列表，则设置该标志为true
        businessDto.setRecommended(true);
        return searchBusinessForApi(businessDto);
    }

    @Override
    public BusinessDto searchBusinessDetailForApi(BusinessDto businessDto) {
        List<BusinessDto> businessDtoList = search(businessDto, true);
        if (CollectionUtils.isNotEmpty(businessDtoList)){
            return businessDtoList.get(0);
        }
        return null;
    }

    /**
     * 前端分类code采用的是拼音/汉字，城市采用的是汉字，后端code统一采用的是数字字符串，要做一个转换，后端能识别
     */
    private boolean transferDictionaryCode(BusinessDto businessDto) {
        //处理全部分类
        if (FinalName.ALL_CATEGORY.equals(businessDto.getCategory())){
            businessDto.setCategory(null);
        }
        //处理普通分类
        if (businessDto.getCategory() != null){
            Dictionary categoryDictionary = getDictionary(DictionaryConstant.TYPE_DICTIONARY_CATEGORY, businessDto.getCategory());
            if (categoryDictionary == null){
                return false;
            }
            businessDto.setCategory(categoryDictionary.getCode());
        }

        //后台，城市和类别采用的是编码，前台传过来的是中文，需要将中文转换成对应的编码
        if (businessDto.getCity() != null){
            Dictionary cityDictionary = getDictionary(DictionaryConstant.TYPE_DICTIONARY_CITY, businessDto.getCity());
            //没有该分类
            if (cityDictionary == null){
                return false;
            }
            businessDto.setCity(cityDictionary.getCode());
        }
        return true;
    }

    private Dictionary getDictionary(String type, String name) {
        if (!StringUtils.isBlank(type) && !StringUtils.isBlank(name)){
            Dictionary dictionary = new Dictionary();
            dictionary.setType(type);
            dictionary.setName(name);
            List<Dictionary> dictionaryList = dictionaryDao.selectDictionaries(dictionary);
            if (CollectionUtils.isNotEmpty(dictionaryList)){
                return dictionaryList.get(0);
            }
        }
        return null;
    }

    private List<BusinessDto> searchByPage(BusinessDto businessDto) {
        Business business = new Business();
        BeanUtils.copyProperties(businessDto, business);
        List<Business> businessList = businessDao.searchByPage(business);
        List<BusinessDto> businessDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(businessList)){
            for (Business tempBusiness : businessList){
                BusinessDto tempBusinessDto = new BusinessDto();
                BeanUtils.copyProperties(tempBusiness, tempBusinessDto);
                tempBusinessDto.setImg(imgUrl + tempBusinessDto.getImgFileName());
                businessDtoList.add(tempBusinessDto);
            }
        }
        return businessDtoList;
    }

    /**
     * 包装结果集
     */
    private void wrapBusinessDtoList(List<BusinessDto> businessDtoList, BusinessListDto businessListDto, BusinessDto businessDto) {
        if (CollectionUtils.isNotEmpty(businessDtoList)){
            for (BusinessDto tempBusinessDto : businessDtoList){
                businessListDto.getData().add(wrapBusinessDto(tempBusinessDto, false));
            }
            if (businessDto.getPage().getCurrentPage() < businessDto.getPage().getTotalPage()){
                businessListDto.setHasMore(true);
            }
        }
    }

    /**
     * 包装返回给前端的商户信息
     * @param businessDto 待包装商户对象
     * @param isBusinessDetailNeeded 是否需要商户详情
     * @return 包装好的商户
     */
    private BusinessDto wrapBusinessDto(BusinessDto businessDto, boolean isBusinessDetailNeeded) {
        BusinessDto newBusinessDto = new BusinessDto();
        if (businessDto != null){
            if (!isBusinessDetailNeeded){
                newBusinessDto.setId(businessDto.getId());
                newBusinessDto.setTitle(businessDto.getTitle());
                newBusinessDto.setSubTitle(businessDto.getSubtitle());
                newBusinessDto.setImg(businessDto.getImg());
                newBusinessDto.setPrice(businessDto.getPrice());
                newBusinessDto.setDistance(businessDto.getDistance());
                //将数据库里的number换成前端的mumber(前端写错了，但是得和前端保持一致)
                newBusinessDto.setMumber(businessDto.getNumber());
            } else {
                newBusinessDto.setImg(businessDto.getImg());
                newBusinessDto.setTitle(businessDto.getTitle());
                newBusinessDto.setSubTitle(businessDto.getSubtitle());
                newBusinessDto.setStar(businessDto.avgStar());
                newBusinessDto.setPrice(businessDto.getPrice());
                newBusinessDto.setDesc(businessDto.getDesc());
            }
        }
        return newBusinessDto;
    }

    private List<BusinessDto> search(BusinessDto businessDto, boolean isDetailNeeded) {
        if (businessDto == null){
            return null;
        }
        Business business = new Business();
        BeanUtils.copyProperties(businessDto, business);
        List<Business> businessList = businessDao.selectBusinesses(business);
        if (CollectionUtils.isEmpty(businessList)){
            return null;
        }
        return wrapBusinessList(businessList, isDetailNeeded);
    }

    private List<BusinessDto> wrapBusinessList(List<Business> businessList, boolean isDetailNeeded) {
        List<BusinessDto> businessDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(businessList)){
            for (Business business : businessList){
                businessDtoList.add(wrapBusiness(business, isDetailNeeded));
            }
        }
        return businessDtoList;
    }

    private BusinessDto wrapBusiness(Business business, boolean isDetailNeeded) {
        BusinessDto businessDto = new BusinessDto();
        if (business != null){
            businessDto.setImg(imgUrl + business.getImgFileName());
            if (isDetailNeeded){
                businessDto.setTitle(business.getTitle());
                businessDto.setSubTitle(business.getSubtitle());
                businessDto.setPrice(business.getPrice());
                businessDto.setStar(business.avgStar());
                businessDto.setDesc(business.getDesc());
            }
        }
        return businessDto;
    }
}
