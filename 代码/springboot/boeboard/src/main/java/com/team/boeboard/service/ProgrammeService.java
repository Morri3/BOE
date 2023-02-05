package com.team.boeboard.service;

import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.programme.GetProgrammesDto;
import com.team.boeboard.form.programme.ProgrammeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProgrammeService {

    //新增节目，返回一个SchemeaddDto给到计划
    void AddProgramme(ProgrammeDto programmeDto,String user_token) throws BoeBoardServiceException;

    //获取所有节目,返回json数据串
    List<GetProgrammesDto> GetAllProgramme() throws BoeBoardServiceException;

    //删除节目
    void DeleteProgramme(String p_name) throws BoeBoardServiceException;

    //按名称模糊查找
    List<GetProgrammesDto> SearchByName(String content) throws BoeBoardServiceException;
    //按dpi查找
    List<GetProgrammesDto> SearchByDpi(String content) throws BoeBoardServiceException;
    //按状态查找
    List<GetProgrammesDto> SearchByStatus(String content) throws BoeBoardServiceException;

    //重命名节目名
    void RenameProgramme(String old_name,String new_name) throws BoeBoardServiceException;
}
