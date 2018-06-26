package com.imooc.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author 潘畅
 * @date 2018/5/29 19:47
 */
public final class FileUtil {

    /**
     * 将文件上传至服务器，，并返回文件在服务器上的名称
     * @param multipartFile 上传文件
     * @param savePath 在服务器的保存文件夹
     * @return 在服务器上的名称
     * @throws IOException
     */
    public static String save(MultipartFile multipartFile, String savePath) throws IOException {
        File fileFolder = new File(savePath);
        //判断“保存路径”是否存在，不存在，则创建
        if (!fileFolder.exists()){
            fileFolder.mkdirs();
        }
        /*
         * getOriginalFilename()：返回上传时的文件名
         * getName()：仅返回文件名，不包含路径
         */
        File file = getFile(savePath, multipartFile.getOriginalFilename());
        /*
         * transferTo(file)：Spring容器提供的一个“MultipartFile”对象（该对象实现接收上传文件）的方法，实现将接
         *                   收到的上传文件转移到指定位置
         */
        multipartFile.transferTo(file);
        return file.getName();
    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile()){
            if (file.delete()){
                return true;
            }
        }
        return false;
    }

    private static File getFile(String savePath, String originalFileName){
        /*
         * 获取文件在服务器上的地址：
         * savePath：配置文件中读取的“保存路径”
         * System.currentTimeMillis() + "_"：防止同名的文件无法上传，所以在路径上加了个时间戳
         * multipartFile.getOriginalFilename()：上传文件的名字
         */
        String fileName = System.currentTimeMillis() + originalFileName;
        File file = new File(savePath + fileName);
        if (file.exists()){
            /*
             * 作用：主要实现对同名文件的处理。若有同名文件，则再次调用该方法，那么“fileName”会随时间而变，这样就可以
             *       实现文件名唯一。
             */
            return getFile(savePath, originalFileName);
        }
        return file;
    }

    public static String getFileNameByUrl(String url){
        if (!StringUtils.isBlank(url) && url.contains("/")){
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return null;
    }
}
