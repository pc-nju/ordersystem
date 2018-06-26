package com.imooc.service.impl;

import com.imooc.bean.Advertisement;
import com.imooc.constant.FinalName;
import com.imooc.dao.AdvertisementDao;
import com.imooc.dto.AdvertisementDto;
import com.imooc.entity.Page;
import com.imooc.service.AdvertisementService;
import com.imooc.util.FileUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 潘畅
 * @date 2018/5/20 9:57
 */
@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    @Autowired
    private AdvertisementDao advertisementDao;
    /**
     * “@Value”注解用于读取Spring项目中的属性文件的值，value为这样的形式："${key}"
     */
    @Value("${advertisement.imgSavePath}")
    private String imgSavePath;
    @Value("${" + FinalName.KEY_ADVERTISEMENT_PAGE_SIZE + "}")
    private String advertisementPageSize;
    @Value("${advertisement.url}")
    private String imgUrl;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertisementServiceImpl.class);

    @Override
    public boolean addAdvertisement(AdvertisementDto advertisementDto) {
        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(advertisementDto.getTitle());
        advertisement.setImgFileName(advertisementDto.getImgFileName());
        advertisement.setLink(advertisementDto.getLink());
        advertisement.setWeight(advertisementDto.getWeight());

        /*----------------将前端上传的文件保存到服务器---------------------*/
        //从Controller中获取的前端上传的文件
        MultipartFile imgMultipartFile = advertisementDto.getImgFile();
        String fileName;
        try {
            if (imgMultipartFile != null && imgMultipartFile.getSize() > 0){
                //将图片上传至服务器，并返回图片名称
                fileName = FileUtil.save(imgMultipartFile, imgSavePath);
                advertisement.setImgFileName(fileName);
                advertisementDao.insertAdvertisement(advertisement);
                return true;
            }
        } catch (IOException e) {
            //TODO 加日志
            return false;
        }
        return false;
        /*----------------将前端上传的文件保存到服务器---------------------*/
    }

    @Override
    public List<AdvertisementDto> searchByPage(String title, Integer currentPage, Model model) {
        AdvertisementDto advertisementDto = new AdvertisementDto();
        advertisementDto.setTitle(title);
        Page page = new Page();
        page.setCurrentPage(currentPage);
        page.setPageSize(Integer.parseInt(advertisementPageSize));
        advertisementDto.setPage(page);
        List<AdvertisementDto> advertisementDtos = searchByPage(advertisementDto);
        if (model != null){
            model.addAttribute("title", title);
            model.addAttribute("list", advertisementDtos);
            model.addAttribute("searchParam", advertisementDto);
        }
        if (model == null){
            for (AdvertisementDto tempAdvertisementDto : advertisementDtos){
                wrap(tempAdvertisementDto);
            }
        }
        return advertisementDtos;
    }

    private void wrap(AdvertisementDto advertisementDto) {
        if (advertisementDto != null){
            advertisementDto.setImgFileName(null);
            advertisementDto.setId(null);
            advertisementDto.setWeight(null);
        }
    }

    public List<AdvertisementDto> searchByPage(AdvertisementDto advertisementDto) {
        List<AdvertisementDto> advertisementDtos = new ArrayList<>();
        Advertisement advertisement = new Advertisement();

        //Spring框架提供，相同属性的对象，将自动赋值
        BeanUtils.copyProperties(advertisementDto, advertisement);

        List<Advertisement> advertisements = advertisementDao.searchByPage(advertisement);
        if (CollectionUtils.isNotEmpty(advertisements)){
            for (Advertisement tempAdvertisement : advertisements){
                AdvertisementDto tempAdvertisementDto = new AdvertisementDto();
                BeanUtils.copyProperties(tempAdvertisement, tempAdvertisementDto);
                tempAdvertisementDto.setImg(imgUrl+tempAdvertisement.getImgFileName());
                advertisementDtos.add(tempAdvertisementDto);
            }
        }
        return advertisementDtos;
    }

    @Override
    public boolean deleteAdvertisement(Integer id) {
        Advertisement advertisement = new Advertisement();
        advertisement.setId(id);
        //把图片名称找出来，组合成图片路径
        List<Advertisement> advertisements = advertisementDao.selectAdvertisements(advertisement);
        if (CollectionUtils.isNotEmpty(advertisements)){
            String imgFileName = advertisements.get(0).getImgFileName();
            //先删数据库，再删图片，防止事务回滚
            int affectedRow = advertisementDao.deleteAdvertisement(advertisement);
            if (affectedRow == 1){
                //先删除服务器上的图片
                if (FileUtil.deleteFile(imgSavePath + imgFileName)){
                    //todo 加日志
                    LOGGER.info("删除的图片为：" + imgSavePath + imgFileName);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public AdvertisementDto modifyInit(Integer id) {
        Advertisement advertisement = new Advertisement();
        advertisement.setId(id);
        List<Advertisement> advertisements = advertisementDao.selectAdvertisements(advertisement);
        if (CollectionUtils.isNotEmpty(advertisements)){
            AdvertisementDto advertisementDto = new AdvertisementDto();
            advertisementDto.setImg(imgUrl+advertisements.get(0).getImgFileName());
            BeanUtils.copyProperties(advertisements.get(0), advertisementDto);
            return advertisementDto;
        }
        return null;
    }

    @Override
    public boolean modifyAdvertisement(AdvertisementDto advertisementDto) {
        //判断是否有新上传图片，有则将新图片上传到服务器，并删除老图片
        Advertisement advertisement =new Advertisement();
        String oldFileName = FileUtil.getFileNameByUrl(advertisementDto.getImg());
        String newFileName = null;
        if (advertisementDto.getImgFile() != null && advertisementDto.getImgFile().getSize() > 0 ){
            try {
                newFileName = FileUtil.save(advertisementDto.getImgFile(), imgSavePath);
                advertisementDto.setImgFileName(newFileName);
            } catch (IOException e) {
                //todo 加日志
                e.printStackTrace();
            }
        }
        BeanUtils.copyProperties(advertisementDto, advertisement);

        //更新数据库中的记录
        int affectedRow = advertisementDao.updateAdvertisement(advertisement);
        if (affectedRow != 1){
            return false;
        }

        //若fileName不为null，说明有新上传的图片，则需要将旧图片删除
        if (newFileName != null){
            //删除成功，返回true；删除失败，返回false
            return FileUtil.deleteFile(imgSavePath + oldFileName);
        }
        return true;
    }

}
