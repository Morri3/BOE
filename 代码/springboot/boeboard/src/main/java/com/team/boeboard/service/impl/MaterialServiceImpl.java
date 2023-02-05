package com.team.boeboard.service.impl;

import com.team.boeboard.entity.Materials;
import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.materials.BackToWebDto;
import com.team.boeboard.repository.MaterialRepository;
import com.team.boeboard.repository.UsersRepository;
import com.team.boeboard.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private UsersRepository usersRepository;

    /**
     * 上传文件（数据库操作）
     * @param fileName   文件名称
     * @param fileUrl    文件url
     * @param size       文件大小
     * @return
     */
    @Override
    public String upload(String fileName,String fileUrl,long size,String user_name,String dpi,int category) {

        Materials materials = new Materials();
        int m_id = materialRepository.findMaxId()+1;

        String msize = this.setSize(size);

        materials.setId(m_id);
        materials.setMname(fileName);
        materials.setMurl(fileUrl);
        materials.setCategory(1);
        materials.setCreatetime(new Date());
        materials.setUpdatetime(new Date());
        materials.setMstatus("未删除");
        materials.setMsize(msize);
        materials.setAuthor(user_name);
        materials.setDpi(dpi);
        materials.setCategory(category);

        materialRepository.save(materials);

        return null;
    }

    /**
     * get所有图片信息
     * @return
     */
    @Override
    public List<BackToWebDto> getall_photos() {
        List<Materials> materials_list = new ArrayList<Materials>();
        materials_list = materialRepository.findAllMaterial();

        List<BackToWebDto> dtolist = this.GetMaterialList(materials_list);

        return dtolist;
    }

    /**
     * 按名称模糊查找所有图片
     * @param name    模糊查找内容
     * @return
     */
    @Override
    public List<BackToWebDto> SelectByName(String name) throws BoeBoardServiceException {
        List<Materials> materials_list = new ArrayList<Materials>();
        materials_list = materialRepository.SelectByName(name);
        if(materials_list.size() == 0){
            throw new BoeBoardServiceException("不存在");
        }
        List<BackToWebDto> dtolist = this.GetMaterialList(materials_list);

        return dtolist;
    }

    /**
     * 删除图片
     * @param fileName
     */
    @Override
    public void deleteFile(String fileName) {
        materialRepository.deleteByName(fileName);
    }

    /**
     * 修改文件名
     * @param old_name
     * @param new_name
     */
    @Override
    public void updateFile(String old_name, String new_name) {
        materialRepository.updateMaterial(old_name,new_name,new Date());
    }

    //文件大小转化
    public String setSize(long size) {
        //获取到的size为：1705230
        int GB = 1024 * 1024 * 1024;//定义GB的计算常量
        int MB = 1024 * 1024;//定义MB的计算常量
        int KB = 1024;//定义KB的计算常量
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (float) GB) + "GB";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (float) MB) + "MB";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (float) KB) + "KB";
        } else {
            resultSize = size + "B   ";
        }
        return resultSize;
    }

    public List<BackToWebDto> GetMaterialList(List<Materials> materialslist){
        List<BackToWebDto> dtolist = new ArrayList<>();

        materialslist.forEach(material ->{
            BackToWebDto dto = new BackToWebDto();
            dto.setMname(material.getMname());
            dto.setMurl(material.getMurl());
            dto.setMsize(material.getMsize());
            dto.setAuthor(material.getAuthor());
            dto.setUpdatetime(material.getUpdatetime());
            dto.setCreatetime(material.getCreatetime());
            dto.setMstatus(material.getMstatus());
            dto.setDpi(material.getDpi());
            dto.setCategory(material.getCategory());
            dtolist.add(dto);
        } );

        return dtolist;
    }

}
