package com.team.boeboard.service;

import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.materials.BackToWebDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MaterialService {

    //上传图片
    String upload(String fileName,String fileUrl,long size,String user_name,String dpi,int category) throws BoeBoardServiceException;

    //获取所有图片信息
    List<BackToWebDto> getall_photos() throws BoeBoardServiceException;

    //按素材名称模糊查找
    List<BackToWebDto> SelectByName(String name) throws BoeBoardServiceException;

    //删除图片
    void deleteFile(String fileName) throws BoeBoardServiceException;

    //修改文件名
    void updateFile(String old_name,String new_name) throws BoeBoardServiceException;
}
