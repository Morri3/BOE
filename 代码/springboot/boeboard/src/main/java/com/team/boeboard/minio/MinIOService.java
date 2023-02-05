package com.team.boeboard.minio;


import cn.hutool.core.util.StrUtil;
import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.MaterialUploadDto;
import com.team.boeboard.service.MaterialService;
import com.team.boeboard.utils.FastImageInfo;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import lombok.Data;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@Data
@Component
@Service
public class MinIOService {
    @Value("${minio.address}")
    private String address;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Value("${minio.bucketName}")
    private String bucketName;


    @Autowired
    private MaterialService materialService;


    public MinioClient getMinioClient() {
        try {
            return new MinioClient(address, accessKey, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return
     */
    public boolean bucketExists(MinioClient minioClient, String bucketName) {
        boolean flag = false;
        try {
            flag = minioClient.bucketExists(bucketName);
            if (flag) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     */
    public boolean makeBucket(MinioClient minioClient, String bucketName) {
        try {
            boolean flag = bucketExists(minioClient, bucketName);
            //存储桶不存在则创建存储桶
            if (!flag) {
                minioClient.makeBucket(bucketName);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 上传文件并写入数据库
     *
     * @param file 上传文件
     * @return 成功则返回文件名，失败返回空
     */
    public void uploadFile(MinioClient minioClient, MultipartFile file,String user_name) throws BoeBoardServiceException {

        //创建存储桶
        boolean createFlag = makeBucket(minioClient, bucketName);
        //创建存储桶失败
        if (createFlag == false) {
            throw new BoeBoardServiceException("创建失败");
        }
        try {
            PutObjectOptions putObjectOptions = new PutObjectOptions(file.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
            putObjectOptions.setContentType(file.getContentType());
            String originalFilename = file.getOriginalFilename();
            int pointIndex = originalFilename.lastIndexOf(".");
            //得到文件流
            InputStream inputStream = file.getInputStream();
            //保证文件不重名(并且没有特殊字符)
            String fileName = file.getOriginalFilename();
            minioClient.putObject(bucketName, fileName, inputStream, putObjectOptions);

            //调用MaterialService将数据插入数据库
            //生成url，有效期3天
            String url = minioClient.presignedGetObject("myfile", fileName, 60 * 60 * 72);
            //获取dpi
            String dpi = "无";
            int category;
            String extName = fileName.substring(fileName.indexOf(".") + 1);
            if(extName.equals("jpg") ||extName.equals("png")||extName.equals("jpeg")||extName.equals("gif")) {
                FastImageInfo imageInfo = new FastImageInfo(file.getInputStream());
                int width = imageInfo.getWidth();
                int height = imageInfo.getHeight();
                dpi = width + "*" + height;
                category = 1;
            }else if(extName.equals("mp3")||extName.equals("wav")||extName.equals("cda")){
                category = 2;
            }else {
                category = 3;
            }

            materialService.upload(fileName,url,file.getSize(),user_name,dpi,category);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BoeBoardServiceException("上传失败");
        }
    }

    /**
     * 上传截图
     * @param minioClient
     * @param file
     * @return
     * @throws BoeBoardServiceException
     */
    public MaterialUploadDto UploadScreenshot(MinioClient minioClient, MultipartFile file) throws BoeBoardServiceException {
        //创建存储桶
        boolean createFlag = makeBucket(minioClient, bucketName);
        //创建存储桶失败
        if (createFlag == false) {
            throw new BoeBoardServiceException("创建失败");
        }
        try {
            PutObjectOptions putObjectOptions = new PutObjectOptions(file.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
            putObjectOptions.setContentType(file.getContentType());
            String originalFilename = file.getOriginalFilename();
            //得到文件流
            InputStream inputStream = file.getInputStream();
            //保证文件不重名(并且没有特殊字符)
            String fileName = file.getOriginalFilename();
            minioClient.putObject(bucketName, fileName, inputStream, putObjectOptions);

            //生成url，有效期3天
            String url = minioClient.presignedGetObject("myfile", fileName, 60 * 60 * 72);

            MaterialUploadDto dto = new MaterialUploadDto();
            dto.setFile_name(fileName);
            dto.setUrl(url);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoeBoardServiceException("上传失败");
        }
    }

    /**
     * 下载文件
     *
     * @param originalName 文件路径
     */
    public InputStream downloadFile(MinioClient minioClient, String originalName, HttpServletResponse response) {
        try {
            InputStream file = minioClient.getObject(bucketName, originalName);
            String filename = new String(originalName.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
            if (StrUtil.isNotBlank(originalName)) {
                filename = originalName;
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = file.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, len);
            }
            servletOutputStream.flush();
            file.close();
            servletOutputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除文件
     *
     * @param fileName 文件路径
     * @return
     */
    public boolean deleteFile(MinioClient minioClient, String fileName) {
        try {
            minioClient.removeObject(bucketName, fileName);
            materialService.deleteFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 得到指定文件的InputStream
     *
     * @param originalName 文件路径
     * @return
     */
    public InputStream getObject(MinioClient minioClient, String originalName) {
        try {
            return minioClient.getObject(bucketName, originalName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据文件路径得到预览文件绝对地址
     *
     * @param minioClient
     * @param fileName    文件路径
     * @return
     */
    public String getPreviewFileUrl(MinioClient minioClient, String fileName) {
        try {
            return minioClient.presignedGetObject(bucketName,fileName);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }


    public void testdownload(MinioClient minioClient, String fileName, HttpServletResponse response) throws InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 获取对象的元数据
        String bucketName = "myfile";
        final ObjectStat stat = minioClient.statObject(bucketName, fileName);
        response.setContentType(stat.contentType());
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream is = minioClient.getObject(bucketName, fileName);
        IOUtils.copy(is, response.getOutputStream());
        is.close();
    }
}