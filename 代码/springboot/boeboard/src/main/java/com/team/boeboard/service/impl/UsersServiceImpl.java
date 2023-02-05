package com.team.boeboard.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.team.boeboard.entity.Users;
import com.team.boeboard.exception.BoeBoardServiceException;

import com.team.boeboard.form.users.*;
import com.team.boeboard.repository.UsersRepository;
import com.team.boeboard.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {
    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UsersDto register(RegisterDto registerDto) throws BoeBoardServiceException {
        UsersDto res = new UsersDto();//存放结果

        if (registerDto != null) {//vue传入的dto不为空
            //获取传入的dto的信息
            String username = registerDto.getUsername();//账号名
            String userpwd = registerDto.getUserpwd();//密码
            String organ = registerDto.getOrgan();//所属机构
            String userrole = registerDto.getUserrole();//所属角色
            String ustatus = registerDto.getUstatus();//账号状态
            String realname = registerDto.getRealname();//真实姓名
            String useremail = registerDto.getUseremail();//邮箱
            String phone = registerDto.getPhone();//手机号

            //查看是否已经存在该用户名和手机号
            Users user = usersRepository.findIsRegistered(username, phone);
            if (user != null) {//存在用户，返回错误提示信息
                logger.warn("添加失败，用户已经存在");
                res.setFailed(true);
                res.setId(0);
                res.setMemo("添加失败，用户已经存在");
            } else {//不存在用户，添加到数据库
                String md5pwd = SaSecureUtil.md5BySalt(userpwd, "boe");//md5加盐加密处理后的密码
                Date curtime = new Date(System.currentTimeMillis());//当前时间
                usersRepository.register(username, md5pwd, organ, userrole, ustatus, realname,
                        phone, useremail, curtime, curtime, 2);

                //填充返回的dto
                res.setFailed(false);//没发生异常
                res.setId(1);
                res.setUsername(username);
                res.setUseremail(useremail);
                res.setRealname(realname);
                res.setPhone(phone);
                res.setCategory(2);
                res.setMemo("添加成功");
            }
        }
        return res;
    }

    @Override
    public String login(LoginDto loginDto) throws BoeBoardServiceException {
        String res = "";//存放结果

        if (loginDto != null) {//vue传入的dto不为空
            String username = loginDto.getUsername();//获取输入的账号名
            String userpwd = loginDto.getUserpwd();//获取输入的密码

            //查找该账号是否存在
            Users user = usersRepository.findIsRegisteredByUsername(username);
            if (user != null) {//存在该用户
                String pwd = user.getUserpwd();//该用户真正的md5加密后的密码
                //判断输入的密码是否正确
                if (pwd.equals(SaSecureUtil.md5BySalt(userpwd, "boe"))) {//输入的密码正确，登录成功
                    //sa-token标记当前会话用户登录
                    StpUtil.login(user.getId());//id作为登录的标准，登录

                    //获取当前登录用户的token
                    SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
                    String token = saTokenInfo.getTokenValue();

                    //返回token
                    res = token;
                } else {//输入密码错误
                    logger.warn("密码或账号错误，请检查后重新输入");
                    res = "密码或账号错误，请检查后重新输入";
                }
            } else {//不存在该用户
                logger.warn("用户名不存在");
                res = "密码或账号错误，请检查后重新输入";
            }
        }
        return res;
    }

    @Override
    public UsersEditDto getUserInfo(String token) throws BoeBoardServiceException {
        UsersEditDto res = new UsersEditDto();//存放结果

        if (token != null || token != "") {
            //获取token
            String usertoken = token;

            //根据token找用户id
            if (StpUtil.getLoginIdByToken(usertoken) != null) {
                String u_id = (String) StpUtil.getLoginIdByToken(usertoken);
                System.out.println(u_id);//test
                //查找该用户
                Users user = usersRepository.findAUserByUId(Long.parseLong(u_id));
                if (user != null) {
                    //填充返回的dto
                    res.setFailed(false);
                    res.setId(Long.parseLong(u_id));
                    res.setUsername(user.getUsername());
                    res.setUserpwd(user.getUserpwd());//获取数据库中md5加盐加密处理后的密码，而不是明文
                    res.setOrgan(user.getOrgan());
                    res.setUserrole(user.getUserrole());
                    res.setUstatus(user.getUstatus());
                    res.setRealname(user.getRealname());
                    res.setUseremail(user.getUseremail());
                    res.setPhone(user.getPhone());
                    res.setUpdatetime(user.getUpdatetime());//这里的更新时间是数据库中的更新时间，执行该接口不改变updatetime值
                    res.setMemo("获取用户信息成功");
                } else {
                    logger.warn("获取用户信息失败");
                    res.setFailed(true);
                    res.setId(0);
                    res.setMemo("获取用户信息失败");
                }
            } else {
                logger.warn("获取用户信息失败");
                res.setFailed(true);
                res.setId(0);
                res.setMemo("获取用户信息失败");
            }
        } else {
            logger.warn("获取用户信息失败");
            res.setFailed(true);
            res.setId(0);
            res.setMemo("获取用户信息失败");
        }
        return res;
    }

    @Override
    public UsersEditDto edit(UsersEditDto usersEditDto) throws BoeBoardServiceException {
        UsersEditDto res = new UsersEditDto();//存放结果

        if (usersEditDto != null) {
            //获取传入的dto的内容
            String username = usersEditDto.getUsername();//账号名
            String userpwd = usersEditDto.getUserpwd();//密码
            String realname = usersEditDto.getRealname();//真实姓名
            String useremail = usersEditDto.getUseremail();//邮箱
            String phone = usersEditDto.getPhone();//手机号
            String ustatus = usersEditDto.getUstatus();//用户状态

            //根据username查找该用户
            Users user = usersRepository.findUserByUsername(username);
            if (user != null) {
                //修改数据库中该用户的信息
                String md5pwd = SaSecureUtil.md5BySalt(userpwd, "boe");//md5加盐加密处理后的密码
                Date curtime = new Date(System.currentTimeMillis());//当前时间
                usersRepository.modify(md5pwd, ustatus, realname, phone, useremail, curtime,
                        username, user.getPhone());

                //填充返回的dto
                res.setFailed(false);
                res.setId(1);
                res.setUsername(username);
                res.setUserpwd(md5pwd);//md5加盐加密处理后的密码，不是明文
                res.setOrgan(user.getOrgan());
                res.setUserrole(user.getUserrole());
                res.setUstatus(user.getUstatus());
                res.setRealname(realname);
                res.setUseremail(useremail);
                res.setPhone(phone);
                res.setUpdatetime(curtime);
                res.setMemo("修改用户信息成功");
            } else {
                logger.warn("修改用户信息失败");
                res.setFailed(true);
                res.setId(0);
                res.setMemo("修改用户信息失败");
            }
        }
        return res;
    }

    @Override
    public UsersStatusDto enable(UsersInfoDto usersInfoDto) throws BoeBoardServiceException {
        UsersStatusDto res = new UsersStatusDto();//存放结果

        if (usersInfoDto != null) {
            //获取输入的账户名
            String username = usersInfoDto.getUsername();
            //查找该用户
            Users user = usersRepository.findUserByUsername(username);
            if (user != null && user.getUstatus().equals("停用")) {//停用状态的用户才能启用
                //更改用户状态：停用->启用
                Date curtime = new Date(System.currentTimeMillis());//当前时间
                usersRepository.enable(curtime, username);

                //填充返回的dto
                res.setFailed(false);//没发生异常
                res.setId(1);
                res.setUsername(username);
                res.setUpdatetime(curtime);
                res.setMemo("启用成功");
            } else {
                logger.warn("启用失败");
                res.setFailed(true);
                res.setId(0);
                res.setUpdatetime(user.getUpdatetime());//这里是数据库中的updatetime值
                res.setMemo("启用失败");
            }
        }
        return res;
    }

    @Override
    public UsersStatusDto disable(UsersInfoDto usersInfoDto) throws BoeBoardServiceException {
        UsersStatusDto res = new UsersStatusDto();//存放结果

        if (usersInfoDto != null) {
            //获取输入的账户名
            String username = usersInfoDto.getUsername();
            //查找该用户
            Users user = usersRepository.findUserByUsername(username);
            if (user != null && user.getUstatus().equals("启用")) {//启用状态的用户才能启用
                //更改用户状态：启用->停用
                Date curtime = new Date(System.currentTimeMillis());//当前时间
                usersRepository.disable(curtime, username);

                //填充返回的dto
                res.setFailed(false);//没发生异常
                res.setId(1);
                res.setUsername(username);
                res.setUpdatetime(curtime);
                res.setMemo("停用成功");
            } else {
                logger.warn("停用失败");
                res.setFailed(true);
                res.setId(0);
                res.setUpdatetime(user.getUpdatetime());//这里是数据库中的updatetime值
                res.setMemo("停用失败");
            }
        }
        return res;
    }

    @Override
    public UsersStatusDto delete(UsersInfoDto usersInfoDto) throws BoeBoardServiceException {
        UsersStatusDto res = new UsersStatusDto();//存放结果

        if (usersInfoDto != null) {
            //获取输入的账户名
            String username = usersInfoDto.getUsername();
            //查找该用户
            Users user = usersRepository.findUserByUsername(username);
            if (user != null) {
                Date curtime = new Date(System.currentTimeMillis());//当前时间
                usersRepository.deleteUserByUsername(curtime, username);

                //填充返回的dto
                res.setFailed(false);//没发生异常
                res.setId(1);
                res.setUsername(username);
                res.setUpdatetime(curtime);
                res.setMemo("删除用户成功");
            } else {
                logger.warn("删除用户失败");
                res.setFailed(true);
                res.setId(0);
                res.setUpdatetime(user.getUpdatetime());//这里是数据库中的updatetime值
                res.setMemo("删除用户失败");
            }
        }
        return res;
    }

    @Override
    public List<UsersSearchResultDto> search(UsersSearchDto usersInfoDto) throws BoeBoardServiceException {
        List<UsersSearchResultDto> res = new ArrayList<>();//存放结果

        if (usersInfoDto != null) {
            //获取传入dto的信息
            String username = usersInfoDto.getUsername();
            String organ = usersInfoDto.getOrgan();
            String userrole = usersInfoDto.getUserrole();
            String ustatus = usersInfoDto.getUstatus();

            //查询数据库，获取用户列表
            List<Users> list = new ArrayList<>();//存放查询到的用户
            if (ustatus.equals("所有")) {//查询时不限制ustatus
                if (username != null && organ != null && userrole != null) {
                    list = usersRepository.searchUsers1(username, organ, userrole);
                } else if (username == null && organ != null && userrole != null) {
                    list = usersRepository.searchUsers2(organ, userrole);
                } else if (username != null && organ == null && userrole != null) {
                    list = usersRepository.searchUsers3(username, userrole);
                } else if (username != null && organ != null && userrole == null) {
                    list = usersRepository.searchUsers4(username, organ);
                } else if (username == null && organ == null && userrole != null) {
                    list = usersRepository.searchUsers5(userrole);
                } else if (username != null && organ == null && userrole == null) {
                    list = usersRepository.searchUsers6(username);
                } else if (username == null && organ != null && userrole == null) {
                    list = usersRepository.searchUsers7(organ);
                } else if (username == null && organ == null && userrole == null) {
                    list = usersRepository.searchUsers15();
                }
            } else {//状态“启用”或“停用”
                if (username != null && organ != null && userrole != null) {
                    list = usersRepository.searchUsers8(username, organ, userrole, ustatus);
                } else if (username == null && organ != null && userrole != null) {
                    list = usersRepository.searchUsers9(organ, userrole, ustatus);
                } else if (username != null && organ == null && userrole != null) {
                    list = usersRepository.searchUsers10(username, userrole, ustatus);
                } else if (username != null && organ != null && userrole == null) {
                    list = usersRepository.searchUsers11(username, organ, ustatus);
                } else if (username == null && organ == null && userrole != null) {
                    list = usersRepository.searchUsers12(userrole, ustatus);
                } else if (username != null && organ == null && userrole == null) {
                    list = usersRepository.searchUsers13(username, ustatus);
                } else if (username == null && organ != null && userrole == null) {
                    list = usersRepository.searchUsers14(organ, ustatus);
                } else if (username == null && organ == null && userrole == null) {
                    list = usersRepository.searchUsers16(ustatus);
                }
            }

            //构造返回的dto列表
            int index = 1;
            for (Users item : list) {
                UsersSearchResultDto dto = new UsersSearchResultDto();
                dto.setFailed(false);
                dto.setId(index++);
                dto.setUsername(item.getUsername());
                dto.setOrgan(item.getOrgan());
                dto.setUserrole(item.getUserrole());
                dto.setUstatus(item.getUstatus());
                dto.setRealname(item.getRealname());
                dto.setUseremail(item.getUseremail());
                dto.setPhone(item.getPhone());
                dto.setUpdatetime(item.getUpdatetime());
                dto.setCategory(item.getCategory());
                res.add(dto);//添加到结果列表中
            }
        }
        return res;
    }

    @Override
    public List<UsersSearchResultDto> getAll() throws BoeBoardServiceException {
        List<UsersSearchResultDto> res = new ArrayList<>();//存放结果

        //查询数据库
        List<Users> list = usersRepository.findListOfUsers();//获取所有用户

        //遍历list，构造返回dto列表
        for (Users item : list) {
            UsersSearchResultDto dto = new UsersSearchResultDto();
            dto.setFailed(false);
            dto.setId(item.getId());
            dto.setUsername(item.getUsername());
            dto.setOrgan(item.getOrgan());
            dto.setUserrole(item.getUserrole());
            dto.setUstatus(item.getUstatus());
            dto.setRealname(item.getRealname());
            dto.setUseremail(item.getUseremail());
            dto.setPhone(item.getPhone());
            dto.setUpdatetime(item.getUpdatetime());
            dto.setCategory(item.getCategory());
            dto.setMemo("获取用户列表成功");
            res.add(dto);
        }
        return res;
    }

    @Override
    public String logout(String token) throws BoeBoardServiceException {
        String res = "";

        if (token != "" || token != null) {
            //获取token
            String usertoken = token;

            //根据token找用户id
            if (StpUtil.getLoginIdByToken(usertoken) != null) {
                String u_id = (String) StpUtil.getLoginIdByToken(usertoken);

                //查找该用户
                Users user = usersRepository.findAUserByUId(Long.parseLong(u_id));
                if (user != null) {
                    StpUtil.logout();//用户登出
                    res = "用户登出成功";
                }
            }
        }
        return res;
    }
}
