package com.team.boeboard.service;

import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.groupings.GroupingsAddAndEditInputDto;
import com.team.boeboard.form.groupings.GroupingsDeleteInputDto;
import com.team.boeboard.form.groupings.GroupingsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupingsService {
    /**
     * 添加分组
     *
     * @param groupingsAddAndEditInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    GroupingsDto add(GroupingsAddAndEditInputDto groupingsAddAndEditInputDto) throws BoeBoardServiceException;

    /**
     * 编辑分组
     *
     * @param groupingsAddAndEditInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    GroupingsDto edit(GroupingsAddAndEditInputDto groupingsAddAndEditInputDto) throws BoeBoardServiceException;

    /**
     * 删除分组
     *
     * @param groupingsDeleteInputDto
     * @return
     * @throws BoeBoardServiceException
     */
    GroupingsDto delete(GroupingsDeleteInputDto groupingsDeleteInputDto) throws BoeBoardServiceException;

    /**
     * 获取所有分组
     *
     * @return
     * @throws BoeBoardServiceException
     */
    List<GroupingsDto> getAll() throws BoeBoardServiceException;
}
