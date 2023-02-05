package com.team.boeboard.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.materials.BackToWebDto;
import com.team.boeboard.minio.MinIOService;
import com.team.boeboard.repository.UsersRepository;
import com.team.boeboard.result.ExceptionMsg;
import com.team.boeboard.result.ResponseData;
import com.team.boeboard.service.MaterialService;
import com.team.boeboard.utils.FastImageInfo;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MinIOService minIOService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private UsersRepository usersRepository;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public ResponseData uploadFile(@RequestBody MultipartFile file,@RequestParam String token) throws IOException {
        MinioClient minioClient = minIOService.getMinioClient();

        String id = (String) StpUtil.getLoginIdByToken(token);
        String user_name = usersRepository.findById(Long.parseLong(id)).getUsername();

        if (minioClient == null) {
            return new ResponseData(ExceptionMsg.FAILED,"上传失败，无法连接MinIo服务器");
        }

        minIOService.uploadFile(minioClient, file,user_name);

        return new ResponseData(ExceptionMsg.SUCCESS,"上传成功");
    }

    /**
     * 返回图片list的JSON字符串
     * list格式如下
     * {
     *     "rspCode": "200",
     *     "rspMsg": "操作成功",
     *     "data": [
     *         {
     *             "mname": "bg.jpg",
     *             "murl": "http://localhost:9000/myfile/bg.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20220627%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20220627T032011Z&X-Amz-Expires=259200&X-Amz-SignedHeaders=host&X-Amz-Signature=52d1a507475a3bdd71d69d50fc073ece7e15bf0368d4b85aeb3ec5169c634dd1",
     *             "msize": "106.38KB   ",
     *             "author": "张三",
     *             "updatetime": "2022-06-27 11:20:11",
     *             "createtime": "2022-06-27 11:20:11",
     *             "mstatus": "已删除",
     *             "category":1
     *         },
     *         {
     *             "mname": "bg1.png",
     *             "murl": "http://localhost:9000/myfile/bg1.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20220627%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20220627T032034Z&X-Amz-Expires=259200&X-Amz-SignedHeaders=host&X-Amz-Signature=c5839ecf0a5d65796f3e90df80dddde965e6064c498d5c7bd3bc805908470ed9",
     *             "msize": "18.41MB   ",
     *             "author": "李四",
     *             "updatetime": null,
     *             "createtime": "2022-06-27 11:20:34",
     *             "mstatus": "未删除",
     *             "category":1
     *         }
     */
    @ResponseBody
    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    public ResponseData GetAll(){
        List<BackToWebDto> result =  materialService.getall_photos();

        return new ResponseData(ExceptionMsg.SUCCESS,result);
    }

    /**
     * 按名称模糊查找所有图片文件
     * @param map
     *              {
     *                  "search_name":"zzza"
     *              }
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public ResponseData SearchByName(@RequestBody Map map) {
        String search_name = (String)map.get("search_name");
        List<BackToWebDto> result = new ArrayList<>();
        try{
            result =  materialService.SelectByName(search_name);
        }catch (BoeBoardServiceException e){
            return new ResponseData(ExceptionMsg.FAILED,e);
        }


        return new ResponseData(ExceptionMsg.SUCCESS,result);
    }

    /**
     * 删除图片
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseData Delete_Material(@RequestBody Map map){

        String file_name = (String)map.get("file_name");
        MinioClient minioClient = minIOService.getMinioClient();
        if (minioClient == null) {
            return  new ResponseData(ExceptionMsg.FAILED,"连接MinIO服务器失败");
        }
        boolean flag = minIOService.deleteFile(minioClient,file_name);
        if(flag == true){
            return new ResponseData(ExceptionMsg.SUCCESS,"删除成功");
        }else {
            return new ResponseData(ExceptionMsg.FAILED,"删除失败");
        }
    }

    /**
     * 修改文件名称
     * @param map
     *          {
     *              "old_name":"zzz",
     *              "new_name":"zza"
     *          }
     */
    @ResponseBody
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseData UpdateMaterial(@RequestBody Map map){
        // 分为两步
        // 1、操作material 的 service层修改数据库
        // 2、获取inputstream流转化成MultipartFile重新上传，并删除bucket中原先的文件

        String old_name = (String)map.get("old_name");
        String new_name = (String)map.get("new_name");
        MinioClient minioClient = minIOService.getMinioClient();
        InputStream inputStream = minIOService.getObject(minioClient,old_name); //获取原文件流

        materialService.updateFile(old_name,new_name);

        return new ResponseData(ExceptionMsg.SUCCESS,"修改成功");
    }

    /**
     * 下载文件
     *
     * @param response 返回请求
     * @param fileName 文件名
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public String downloadFile(HttpServletResponse response, @RequestParam String fileName) {
        MinioClient minioClient = minIOService.getMinioClient();
        if (minioClient == null) {
            return "连接MinIO服务器失败";
        }
        return minIOService.downloadFile(minioClient, fileName, response) != null ? "下载成功" : "下载失败";
    }


}
