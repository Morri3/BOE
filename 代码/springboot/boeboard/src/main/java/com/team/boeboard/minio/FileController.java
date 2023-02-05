package com.team.boeboard.minio;

import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.MaterialUploadDto;
import com.team.boeboard.minio.photo.Photo;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/file")
@RestController
public class FileController {
    @Autowired
    private MinIOService minIOService;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public MaterialUploadDto uploadFile(@RequestBody MultipartFile file) throws BoeBoardServiceException {
        MinioClient minioClient = minIOService.getMinioClient();
        if (minioClient == null) {
            throw new BoeBoardServiceException("连接失败");
        }
        MaterialUploadDto dto  = minIOService.UploadScreenshot(minioClient, file);
        return dto;
    }

    /**
     * 下载文件
     *
     * @param response 返回请求
     * @param fileName 文件路径
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public String downloadFile(HttpServletResponse response,@RequestParam String fileName) {
        MinioClient minioClient = minIOService.getMinioClient();
        if (minioClient == null) {
            return "连接MinIO服务器失败";
        }
        return minIOService.downloadFile(minioClient, fileName, response) != null ? "下载成功" : "下载失败";
    }

    /**
     * 删除文件
     *
     * @param fileName 文件路径
     * @return
     */
    @DeleteMapping("/deleteFile/{fileName}")
    public String deleteFile(@PathVariable("fileName") String fileName) {
        MinioClient minioClient = minIOService.getMinioClient();
        if (minioClient == null) {
            return "连接MinIO服务器失败";
        }
        boolean flag = minIOService.deleteFile(minioClient, fileName);
        return flag == true ? "删除成功" : "删除失败";
    }


    /** 列出所有图片信息(图片url、名称)
     */
    @ResponseBody
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<Photo> GetAll(){
        MinioClient minioClient = minIOService.getMinioClient();
        List<Photo> list = new ArrayList<Photo>();
        try {
            // 检查'mybucket'是否存在。
            boolean found = minioClient.bucketExists("myfile");
            if (found) {
                // 列出'my-bucketname'里的对象
                Iterable<Result<Item>> myObjects = minioClient.listObjects("myfile");
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    System.out.println(item.lastModified() + ", " + item.size() + ", " + item.objectName());

                    Photo photo = new Photo();
                    String url = minioClient.presignedGetObject("myfile", item.objectName(), 60 * 60 * 72);
                    photo.setUrl(url);
                    photo.setDescription(item.objectName());
                    list.add(photo);
                }
            } else {
                System.out.println("mybucket does not exist");
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            System.out.println("Error occurred: " + e);
        }
        return list;
    }
}