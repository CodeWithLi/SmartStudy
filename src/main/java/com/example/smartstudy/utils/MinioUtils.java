package com.example.smartstudy.utils;

import com.example.smartstudy.config.MinioConfiguration;
import com.example.smartstudy.exception.BaseException;
import io.micrometer.common.util.StringUtils;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * minio配置类,用于上传图片及回显
 */
@Component
@Slf4j
public class MinioUtils {

    @Autowired
    private MinioConfiguration prop;

    @Resource
    private MinioClient minioClient;

    /**
     * 上传图片
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            throw new BaseException("文件为空");
        }
        String fileName = UUID.randomUUID().toString()+ originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileName;
    }

    /**
     * 预览图片
     * @param fileName
     * @return
     */
    public String preview(String fileName) {
        // 查看文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs().builder().bucket(prop.getBucketName()).object(fileName).method(Method.GET).build();
        try {
            String url = minioClient.getPresignedObjectUrl(build);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName){
        try {
            minioClient.removeObject( RemoveObjectArgs.builder().bucket(prop.getBucketName()).object(fileName).build());
        }catch (Exception e){
            log.info("删除桶中的图片失败");
            return false;
        }
        return true;
    }

    /**
     * 创建存储bucket
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}