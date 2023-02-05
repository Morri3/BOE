package com.team.boeboard.service;

import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.users.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UsersService {
    /**
     * 添加用户
     *
     * @param registerDto
     * @return
     * @throws BoeBoardServiceException
     */
    UsersDto register(RegisterDto registerDto) throws BoeBoardServiceException;

    /**
     * 用户登录
     *
     * @param loginDto
     * @return
     * @throws BoeBoardServiceException
     */
    String login(LoginDto loginDto) throws BoeBoardServiceException;

    /**
     * 获取用户信息
     *
     * @param token
     * @return
     * @throws BoeBoardServiceException
     */
    UsersEditDto getUserInfo(String token) throws BoeBoardServiceException;

    /**
     * 编辑用户信息
     *
     * @param usersEditDto
     * @return
     * @throws BoeBoardServiceException
     */
    UsersEditDto edit(UsersEditDto usersEditDto) throws BoeBoardServiceException;

    /**
     * 启用
     *
     * @param usersInfoDto
     * @return
     * @throws BoeBoardServiceException
     */
    UsersStatusDto enable(UsersInfoDto usersInfoDto) throws BoeBoardServiceException;

    /**
     * 停用
     *
     * @param usersInfoDto
     * @return
     * @throws BoeBoardServiceException
     */
    UsersStatusDto disable(UsersInfoDto usersInfoDto) throws BoeBoardServiceException;

    /**
     * 删除用户
     *
     * @param usersInfoDto
     * @return
     * @throws BoeBoardServiceException
     */
    UsersStatusDto delete(UsersInfoDto usersInfoDto) throws BoeBoardServiceException;

    /**
     * 查询用户
     *
     * @param usersInfoDto
     * @return
     * @throws BoeBoardServiceException
     */
    List<UsersSearchResultDto> search(UsersSearchDto usersInfoDto) throws BoeBoardServiceException;

    /**
     * 获取所有用户
     *
     * @return
     * @throws BoeBoardServiceException
     */
    List<UsersSearchResultDto> getAll() throws BoeBoardServiceException;

    /**
     * 用户登出
     *
     * @param token
     * @return
     * @throws BoeBoardServiceException
     */
    String logout(String token) throws BoeBoardServiceException;
}
