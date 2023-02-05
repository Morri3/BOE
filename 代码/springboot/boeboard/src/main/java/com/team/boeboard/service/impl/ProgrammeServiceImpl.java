package com.team.boeboard.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.team.boeboard.entity.Programme;
import com.team.boeboard.entity.Users;
import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.programme.GetProgrammesDto;
import com.team.boeboard.form.programme.MaterialsDto;
import com.team.boeboard.form.programme.ProgrammeDto;
import com.team.boeboard.repository.MaterialRepository;
import com.team.boeboard.repository.ProgrammeRepository;
import com.team.boeboard.repository.UsersRepository;
import com.team.boeboard.service.MaterialService;
import com.team.boeboard.service.ProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProgrammeServiceImpl implements ProgrammeService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProgrammeRepository programmeRepository;
    @Autowired
    private MaterialRepository materialRepository;


    double total_size = 0;

    //新增节目
    @Override
    public void AddProgramme(ProgrammeDto programmeDto,String user_token) throws BoeBoardServiceException {

        HomeServiceImpl homeServiceimpl = new HomeServiceImpl();
        MaterialServiceImpl materialService = new MaterialServiceImpl();
        String id = (String) StpUtil.getLoginIdByToken(user_token);
        String user_name = usersRepository.findById(Long.parseLong(id)).getUsername();
//        String user_name = programmeDto.getUser_name();
        int p_id = programmeRepository.findMaxId()+1;

        //素材使用的materials
        List<MaterialsDto> materialsDtos = programmeDto.getMaterials();
        //获取素材大小
        materialsDtos.forEach(materialsDto -> {
            String mname = materialsDto.getMname();
            String single_msize = materialRepository.GetMsizeByName(mname);
            double material_size = homeServiceimpl.GetSingleSize(single_msize);
            total_size+=material_size;
        });

        String msize = materialService.setSize((long)total_size);

        String p_name = programmeDto.getP_name();
        if(programmeRepository.findByName(p_name) != 0){
            throw new BoeBoardServiceException("节目名已存在，勿重复添加");
        }

        Programme programme = new Programme();
        programme.setId(p_id);
        programme.setPname(programmeDto.getP_name());
        programme.setDpi(programmeDto.getDpi());
        programme.setPlength(programmeDto.getPlength());
        programme.setPsize(programmeDto.getPsize());
        programme.setPstatus("未使用");
        programme.setAuthor(user_name);
        programme.setCreatetime(new Date());
        programme.setUpdatetime(new Date());
        programme.setMaterials(JSON.toJSONString(materialsDtos));
        programme.setPsize(msize);
        programmeRepository.save(programme);

        total_size = 0;

    }

    //获取所有节目
    @Override
    public List<GetProgrammesDto> GetAllProgramme() {
        List<Programme> list = programmeRepository.GetAll();

        List<GetProgrammesDto> result = this.GetProgramme(list);

        return result;
    }

    //删除节目
    @Override
    public void DeleteProgramme(String p_name) {

        programmeRepository.deleteProgrammeByName(p_name);

    }

    //模糊查找节目
    @Override
    public List<GetProgrammesDto> SearchByName(String content) throws BoeBoardServiceException{
        List<Programme> list = programmeRepository.SelectByName(content);
        if(list.size() == 0){
            throw new BoeBoardServiceException("查找失败");
        }
        List<GetProgrammesDto> result = this.GetProgramme(list);

        return result;
    }

    //按dpi查找
    @Override
    public List<GetProgrammesDto> SearchByDpi(String content) {
        List<Programme> list = programmeRepository.SelectByDpi(content);
        if(list.size() == 0){
            throw new BoeBoardServiceException("查找失败");
        }
        List<GetProgrammesDto> result = this.GetProgramme(list);

        return result;
    }

    //按状态查找
    @Override
    public List<GetProgrammesDto> SearchByStatus(String content) {
        List<Programme> list = programmeRepository.SelectByStatus(content);
        if(list.size() == 0){
            throw new BoeBoardServiceException("查找失败");
        }
        List<GetProgrammesDto> result = this.GetProgramme(list);

        return result;
    }

    @Override
    public void RenameProgramme(String old_name,String new_name) {
        programmeRepository.Rename(old_name,new_name);
    }

    //获取对应的program-list
    public List<GetProgrammesDto> GetProgramme(List<Programme> list){
        List<GetProgrammesDto> dtolist = new ArrayList<>();

        list.forEach(programme -> {
            GetProgrammesDto dto = new GetProgrammesDto();

            List<MaterialsDto> materialsDtos = (List)JSON.parse(programme.getMaterials());

            dto.setP_id(programme.getId());
            dto.setPname(programme.getPname());
            dto.setDpi(programme.getDpi());
            dto.setPlength(programme.getPlength());
            dto.setPsize(programme.getPsize());
            dto.setPstatus(programme.getPstatus());
            dto.setAuthor(programme.getAuthor());
            dto.setCreatetime(programme.getCreatetime());
            dto.setUpdatetime(programme.getUpdatetime());
            dto.setMaterials(materialsDtos);

            dtolist.add(dto);
        });

//        String result = JSON.toJSONString(dtolist);
        return dtolist;
    }
}
