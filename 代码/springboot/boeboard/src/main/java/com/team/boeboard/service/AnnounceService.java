package com.team.boeboard.service;

import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.announce.BackAnnounceDto;
import com.team.boeboard.form.announce.GetAnnounceDto;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public interface AnnounceService {
    //增加公告
    void AddAnnounce(GetAnnounceDto dto, String user_token) throws BoeBoardServiceException, ParseException;

    //删除公告
    void DeleteAnnounce(String content) throws BoeBoardServiceException;

    //获取所有公告
    List<BackAnnounceDto> GetAllAnnounces() throws BoeBoardServiceException;
}
