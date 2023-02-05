package com.team.boeboard.service;

import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.home.HomeInformDto;
import org.springframework.stereotype.Service;

@Service
public interface HomeService {
    /**
     * 获取首页信息
     *
     * @return
     * @throws BoeBoardServiceException
     */
    HomeInformDto GetHomeInfo() throws BoeBoardServiceException;
}
