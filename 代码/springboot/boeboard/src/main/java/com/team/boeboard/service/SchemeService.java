package com.team.boeboard.service;

import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.scheme.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SchemeService {
    /**
     * 添加计划
     *
     * @param token,schemeAddInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    SchemeDto add(String token, SchemeAddInputDto schemeAddInputDto) throws BoeBoardServiceException;

    /**
     * 获取所有计划
     *
     * @return
     * @throws BoeBoardServiceException
     */
    List<SchemeAllInfoDto> getAll() throws BoeBoardServiceException;

    /**
     * 计划详情
     *
     * @param schemeInfoAndDeleteInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    SchemeInfoDto info(SchemeInfoAndDeleteInputDto schemeInfoAndDeleteInputDto) throws BoeBoardServiceException;

    /**
     * 编辑计划
     *
     * @param schemeEditInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    SchemeDto edit(SchemeEditInputDto schemeEditInputDto) throws BoeBoardServiceException;

    /**
     * 编辑计划
     *
     * @param schemeInfoAndDeleteInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    SchemeDeleteDto delete(SchemeInfoAndDeleteInputDto schemeInfoAndDeleteInputDto) throws BoeBoardServiceException;

    /**
     * 查找计划
     *
     * @param schemeSearchInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    List<SchemeDto> search(SchemeSearchInputDto schemeSearchInputDto) throws BoeBoardServiceException;

    /**
     * 复制计划
     *
     * @param token,schemeReproduceInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    long reproduce(String token, SchemeReproduceInputDto schemeReproduceInputDto) throws BoeBoardServiceException;

    /**
     * 重新发布计划
     *
     * @param schemeRepublishInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    long republish(SchemeRepublishInputDto schemeRepublishInputDto) throws BoeBoardServiceException;

}
