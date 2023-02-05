package com.team.boeboard.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.team.boeboard.entity.Scheme;
import com.team.boeboard.entity.Users;
import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.log.CntLogDto;
import com.team.boeboard.form.log.SchemeAddLogDto;
import com.team.boeboard.form.scheme.*;
import com.team.boeboard.mq.MqMessage;
import com.team.boeboard.repository.ProgrammeRepository;
import com.team.boeboard.repository.SchemeRepository;
import com.team.boeboard.repository.UsersRepository;
import com.team.boeboard.service.SchemeService;
import com.team.boeboard.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SchemeServiceImpl implements SchemeService {
    private final Logger logger = LoggerFactory.getLogger(DevicesServiceImpl.class);

    @Autowired
    private SchemeRepository schemeRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProgrammeRepository programmeRepository;

    @Autowired
    private AmqpTemplate mqService;//mq
    @Autowired
    private RedisTemplate redisTemplate;//缓存

    public static int index = 0;//事件日志下标
    public static int name_index = 0;//复制计划用到的下标

    @Override
    public SchemeDto add(String token, SchemeAddInputDto schemeAddInputDto) throws BoeBoardServiceException {
        SchemeDto res = new SchemeDto();//存放结果
        Date updatetime;

        if (schemeAddInputDto != null && (token != "" || token != null)) {
            //获取传入dto的信息
            String sname = schemeAddInputDto.getSname();
            int strategy = schemeAddInputDto.getStrategy();
            int playmode = schemeAddInputDto.getPlaymode();
            int synchronize = schemeAddInputDto.getSynchronize();
            String startday = schemeAddInputDto.getStartday();
            String endday = schemeAddInputDto.getEndday();
            int cycles = schemeAddInputDto.getCycles();
            String starttime = schemeAddInputDto.getStarttime();
            String endtime = schemeAddInputDto.getEndtime();
            //获取作者用户名
            String author = "";
            if (StpUtil.getLoginIdByToken(token) != null) {
                String u_id = (String) StpUtil.getLoginIdByToken(token);

                //查找该用户
                Users user = usersRepository.findAUserByUId(Long.parseLong(u_id));
                if (user != null) {
                    author = user.getUsername();
                }
            }
            String reviewer = author;//审核人默认就是当前用户author
            long p_id = schemeAddInputDto.getP_id();
            long d_id = schemeAddInputDto.getD_id();

            //判断数据库是否存在该计划
            Scheme scheme = schemeRepository.findSchemeIfAdded(sname);
            if (scheme != null) {//存在该计划
                //返回错误提示信息
                logger.warn("计划已存在，添加失败");
                res.setFailed(true);
                res.setId(0);
                res.setSname(sname);
                res.setMemo("计划已存在，添加失败");

                //存缓存
                ++index;
                SchemeAddLogDto dto = new SchemeAddLogDto();
                dto.setId(index);
                dto.setTime(new Date(System.currentTimeMillis()));
                dto.setContent("[计划]" + res.getSname() + "计划发布失败");
                redisTemplate.opsForValue().set(SchemeAddLogDto.cacheKey(index), JSONObject.toJSONString(dto),
                        2592000, TimeUnit.SECONDS);
                logger.warn("[计划]计划发布失败[{}]", SchemeAddLogDto.cacheKey(index));
            } else {
                //当前时间
                Date curtime = new Date(System.currentTimeMillis());
                updatetime = curtime;
                //构造返回给vue的playdate
                String playdate = startday + "~" + endday;

//                if (playmode == 1) {//按时段播放
                //【目前只设置循环周期为“每天”的情况】
                //Date格式如：2022-01-01 2022-01-03
                //Time格式如：08:00:00 09:00:00

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                try {
                    Date d1 = sdf.parse(startday);
                    Date d2 = sdf.parse(endday);
                    Date t1 = sdf2.parse(starttime);
                    Date t2 = sdf2.parse(endtime);

                    //添加到数据库
                    schemeRepository.addAScheme(d_id, p_id, sname, playmode, cycles, strategy,
                            synchronize, d1, d2, t1, t2, author, reviewer, curtime, curtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                } else if (playmode == 2) {//持续播放
//                    //添加到数据库
//                    schemeRepository.addAScheme2(d_id, p_id, sname, playmode, strategy, synchronize,
//                            author, reviewer, curtime, curtime);
//                }

                //更新节目的状态为“使用中”
                programmeRepository.updateProgramStatus(updatetime, p_id);

                //获取刚刚创建的计划的实体
                long s_id = schemeRepository.searchCreatedScheme();
                Scheme scheme1 = schemeRepository.searchSchemeBySid(s_id);//根据id查找实体

                //处理metarials字段
                String materials = scheme1.getP().getMaterials();//获取数据库中的materials字段
                JSONArray jsonArray = JSONArray.parseArray(materials);//string转json数组

                //获取url、种类
                List<String> list1 = new ArrayList<>();
                List<Integer> list2 = new ArrayList<>();

                boolean video = false;//是否是视频

                for (int i = 0; i < jsonArray.size(); i++) {//遍历json数组
                    JSONObject obj = jsonArray.getJSONObject(i);//获取当前那个对象

                    String tmp1 = obj.getString("murl");//获取url
                    int tmp2 = obj.getIntValue("category");//获取种类

                    if (tmp2 == 3) {//是视频
                        video = true;
                    }
                    list1.add(tmp1);
                    list2.add(tmp2);
                }

                if (video == true) {//是一个视频
                    //和android使用消息队列通信，发送计划给android【视频】
                    MqMessage msg = new MqMessage(MqMessage.CATEGORY_SCHEMEVIDEO);
                    msg.appendContent("d_id", d_id);//设备id
                    msg.appendContent("p_id", p_id);//节目id
                    msg.appendContent("plength", scheme1.getP().getPlength());//节目时长
                    msg.appendContent("murl", list1.get(0));//素材url【list1的第一个元素】
                    msg.appendContent("sname", sname);//计划名称
                    msg.appendContent("playmode", playmode);//播放模式
                    msg.appendContent("cycles", cycles);//循环周期
                    msg.appendContent("strategy", strategy);//循环策略
                    msg.appendContent("synchronize", synchronize);//多屏同步
                    msg.appendContent("startday", startday);//开始日期
                    msg.appendContent("endday", endday);//结束日期
                    msg.appendContent("starttime", starttime);//开始时间
                    msg.appendContent("endtime", endtime);//结束时间
                    msg.appendContent("memo", "发送计划");//备注
                    mqService.convertSendAndReceive(Constants.QUE_SCHEMEVIDEO, msg.stringfy());//msg.stringfy()转json
                } else {//是照片
                    //处理url，让多个url中间以逗号隔开
                    String urls = "";
                    for (int i = 0; i < list1.size(); i++) {//list1和list2大小一样，遍历list1素材列表
                        String url = list1.get(i);
                        if (i == 0) {
                            urls += url;
                        } else if (i > 0) {
                            urls += "," + url;
                        }
                    }

                    //和android使用消息队列通信，发送计划给android【图片】
                    MqMessage msg = new MqMessage(MqMessage.CATEGORY_SCHEME);
                    msg.appendContent("d_id", d_id);//设备id
                    msg.appendContent("p_id", p_id);//节目id
                    msg.appendContent("plength", scheme1.getP().getPlength());//节目时长
                    msg.appendContent("murl", urls);//素材url
                    msg.appendContent("sname", sname);//计划名称
                    msg.appendContent("playmode", playmode);//播放模式
                    msg.appendContent("cycles", cycles);//循环周期
                    msg.appendContent("strategy", strategy);//循环策略
                    msg.appendContent("synchronize", synchronize);//多屏同步
                    msg.appendContent("startday", startday);//开始日期
                    msg.appendContent("endday", endday);//结束日期
                    msg.appendContent("starttime", starttime);//开始时间
                    msg.appendContent("endtime", endtime);//结束时间
                    msg.appendContent("memo", "发送计划");//备注
                    mqService.convertSendAndReceive(Constants.QUE_SCHEME, msg.stringfy());//msg.stringfy()转json
                }

                //填充返回的dto
                res.setFailed(false);//没发生异常
                res.setId(1);
                res.setS_id(s_id);
                res.setSname(scheme1.getSname());
                res.setPlaymode(scheme1.getPlaymode());
                res.setSstatus(scheme1.getSstatus());
                if (playmode == 1) {
                    res.setPlaydate(playdate);
                } else if (playmode == 2) {
                    res.setPlaydate("持续播放");
                }
                res.setAuthor(scheme1.getAuthor());
                res.setReviewer(scheme1.getReviewer());
                res.setUpdatetime(scheme1.getUpdatetime());
                res.setCreatetime(scheme1.getCreatetime());
                res.setP_id(p_id);
                res.setPlength(scheme1.getP().getPlength());
                res.setMurl(list1.get(0));
                res.setMemo("计划添加成功");

                //存缓存
                ++index;
                SchemeAddLogDto dto = new SchemeAddLogDto();
                dto.setId(index);
                dto.setTime(new Date(System.currentTimeMillis()));
                dto.setContent("[计划]" + res.getSname() + "计划发布成功");
                redisTemplate.opsForValue().set(SchemeAddLogDto.cacheKey(index), JSONObject.toJSONString(dto),
                        2592000, TimeUnit.SECONDS);
                logger.warn("[计划]计划发布成功[{}]", SchemeAddLogDto.cacheKey(index));
            }
        }
        //获取缓存中的cnt
        String str = (String) redisTemplate.opsForValue().get(CntLogDto.cacheKey(1));
        CntLogDto dto = JSONObject.parseObject(str, CntLogDto.class);//json转dto

        int nowCnt = 0;
        if (dto != null) {
            nowCnt = dto.getCnt();
        }

        //存日志数量
        CntLogDto cnt = new CntLogDto();
        cnt.setCnt(++nowCnt);
        redisTemplate.opsForValue().set(CntLogDto.cacheKey(1), JSONObject.toJSONString(cnt),
                2592000, TimeUnit.SECONDS);
        logger.warn("[计划]日志数量添加成功[{}]", CntLogDto.cacheKey(1));

        return res;
    }

    @Override
    public List<SchemeAllInfoDto> getAll() throws BoeBoardServiceException {
        List<SchemeAllInfoDto> res = new ArrayList<>();//存放结果
        int index = 1;//下标
        String vue_url = "";//返回给vue的url

        //查找数据库
        List<Scheme> list = schemeRepository.findListOfSchemes();//所有计划存放到列表中

        //遍历list，构造返回dto列表
        for (Scheme item : list) {
            SchemeAllInfoDto dto = new SchemeAllInfoDto();
            dto.setFailed(false);
            dto.setId(index++);
            dto.setS_id(item.getId());
            dto.setSname(item.getSname());
            dto.setPlaymode(item.getPlaymode());
            dto.setSstatus(item.getSstatus());

            //构造playdate
            Date startday = item.getStartday();
            Date endday = item.getEndday();
//                    Date starttime = item.getStarttime();
//                    Date endtime = item.getEndtime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            String d1 = (sdf.format(startday)).substring(0, 10);
            String d2 = (sdf.format(endday)).substring(0, 10);
//                    String t1 = (sdf2.format(starttime)).substring(0,8);
//                    String t2 = (sdf2.format(endtime)).substring(0, 8);

            String playdate = d1 + "~" + d2;

            dto.setPlaydate(playdate);
            dto.setAuthor(item.getAuthor());
            dto.setReviewer(item.getReviewer());
            dto.setUpdatetime(item.getUpdatetime());
            dto.setCreatetime(item.getCreatetime());
            dto.setP_id(item.getP().getId());

            //TODO 素材url加到dto
            //处理metarials字段
            String materials = item.getP().getMaterials();//获取数据库中的materials字段
            JSONArray jsonArray = JSONArray.parseArray(materials);//string转json数组
            //获取url、种类
            JSONObject obj = jsonArray.getJSONObject(0);//获取第一个对象
            vue_url = obj.getString("murl");//获取url
            dto.setMurl(vue_url);

            //TODO 构造duration
            Date starttime = item.getStarttime();
            Date endtime = item.getEndtime();

            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            String t1 = (sdf2.format(starttime)).substring(0, 8);
            String t2 = (sdf2.format(endtime)).substring(0, 8);
            String duration = t1 + "~" + t2;
            dto.setDuration(duration);

            dto.setMemo("获取计划列表成功");
            res.add(dto);
        }

        return res;
    }

    @Override
    public SchemeInfoDto info(SchemeInfoAndDeleteInputDto schemeInfoAndDeleteInputDto) throws BoeBoardServiceException {
        SchemeInfoDto res = new SchemeInfoDto();//存放结果

        if (schemeInfoAndDeleteInputDto != null) {
            long s_id = schemeInfoAndDeleteInputDto.getS_id();//s_id

            //数据库查找该计划的信息
            Scheme scheme = schemeRepository.searchSchemeBySid(s_id);//根据s_id找计划
            if (scheme != null) {//存在该用户
                //构造返回的dto
                res.setFailed(false);
                res.setId(1);
                res.setS_id(scheme.getId());
                res.setSname(scheme.getSname());

                //构造playdate
                Date startday = scheme.getStartday();
                Date endday = scheme.getEndday();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String d1 = (sdf.format(startday)).substring(0, 10);
                String d2 = (sdf.format(endday)).substring(0, 10);
                String playdate = d1 + "~" + d2;

                res.setPlaydate(playdate);
                res.setPlaymode(scheme.getPlaymode());
                res.setStrategy(scheme.getStrategy());
                res.setSynchronize(scheme.getSynchronize());
                res.setSstatus(scheme.getSstatus());
                res.setCreatetime(scheme.getCreatetime());
                res.setCycles(scheme.getCycles());

                //构造duration
                Date starttime = scheme.getStarttime();
                Date endtime = scheme.getEndtime();

                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                String t1 = (sdf2.format(starttime)).substring(0, 8);
                String t2 = (sdf2.format(endtime)).substring(0, 8);
                String duration = t1 + "~" + t2;

                res.setDuration(duration);
                res.setP_id(scheme.getP().getId());
                res.setPlength(scheme.getP().getPlength());
//                res.setMurl(scheme.getP().getUrls());
                res.setMemo("获取计划详情成功");
            } else {
                //返回错误提示信息
                logger.warn("获取计划详情失败");
                res.setFailed(true);
                res.setId(0);
                res.setMemo("获取计划详情失败");
            }
        } else {
            //返回错误提示信息
            logger.warn("获取计划详情失败");
            res.setFailed(true);
            res.setId(0);
            res.setMemo("获取计划详情失败");
        }
        return res;
    }

    @Override
    public SchemeDto edit(SchemeEditInputDto schemeEditInputDto) throws BoeBoardServiceException {
        SchemeDto res = new SchemeDto();

        if (schemeEditInputDto != null) {
            //获取传入dto的信息
            long s_id = schemeEditInputDto.getS_id();
            String sname = schemeEditInputDto.getSname();
            int strategy = schemeEditInputDto.getStrategy();
            int playmode = schemeEditInputDto.getPlaymode();
            int synchronize = schemeEditInputDto.getSynchronize();
            String startday = schemeEditInputDto.getStartday();
            String endday = schemeEditInputDto.getEndday();
            int cycles = schemeEditInputDto.getCycles();
            String starttime = schemeEditInputDto.getStarttime();
            String endtime = schemeEditInputDto.getEndtime();
            long p_id = schemeEditInputDto.getP_id();
            long d_id = schemeEditInputDto.getD_id();

            //判断数据库是否存在该计划
            Scheme scheme = schemeRepository.searchSchemeBySid(s_id);
            if (scheme != null) {//存在该计划
                //当前时间
                Date curtime = new Date(System.currentTimeMillis());
                //构造返回给vue的playdate
                String playdate = startday + "~" + endday;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                try {
                    Date d1 = sdf.parse(startday);
                    Date d2 = sdf.parse(endday);
                    Date t1 = sdf2.parse(starttime);
                    Date t2 = sdf2.parse(endtime);

                    //修改数据库中的记录
                    schemeRepository.updateAScheme(d_id, p_id, sname, playmode, cycles, strategy, synchronize,
                            d1, d2, t1, t2, curtime, s_id);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //判断是否使用了其他节目，若使用了其他节目，把原节目状态改为“未使用”，把新节目状态改为“使用中”；
                //若未改变使用的节目，则不更改节目的信息
                if (p_id != scheme.getP().getId()) {//不相等
                    programmeRepository.changeOldProgram(curtime, scheme.getP().getId());//更改原节目
                    programmeRepository.changeNewProgram(curtime, p_id);//更改新节目
                }

                //填充返回的dto
                res.setFailed(false);//没发生异常
                res.setId(1);
                res.setS_id(s_id);
                res.setSname(scheme.getSname());
                res.setPlaymode(scheme.getPlaymode());
                res.setSstatus(scheme.getSstatus());
                if (playmode == 1) {
                    res.setPlaydate(playdate);
                } else if (playmode == 2) {
                    res.setPlaydate("持续播放");
                }
                res.setAuthor(scheme.getAuthor());
                res.setReviewer(scheme.getReviewer());
                res.setUpdatetime(scheme.getUpdatetime());
                res.setCreatetime(scheme.getCreatetime());
                res.setP_id(scheme.getP().getId());
                res.setPlength(scheme.getP().getPlength());

                //TODO 加到dto
                //处理metarials字段
                String vue_url = "";
                String materials = scheme.getP().getMaterials();//获取数据库中的materials字段
                JSONArray jsonArray = JSONArray.parseArray(materials);//string转json数组
                //获取url、种类
                JSONObject obj = jsonArray.getJSONObject(0);//获取第一个对象
                vue_url = obj.getString("murl");//获取url
                res.setMurl(vue_url);

                res.setMemo("计划编辑成功");
            } else {//不存在
                //返回错误提示信息
                logger.warn("计划不存在，编辑失败");
                res.setFailed(true);
                res.setId(0);
                res.setS_id(s_id);
                res.setMemo("计划不存在，编辑失败");
            }
        } else {
            //返回错误提示信息
            logger.warn("计划编辑失败");
            res.setFailed(true);
            res.setId(0);
            res.setMemo("计划编辑失败");
        }
        return res;
    }

    @Override
    public SchemeDeleteDto delete(SchemeInfoAndDeleteInputDto schemeInfoAndDeleteInputDto) throws BoeBoardServiceException {
        SchemeDeleteDto res = new SchemeDeleteDto();//存放结果

        if (schemeInfoAndDeleteInputDto != null) {
            //获取计划id
            long s_id = schemeInfoAndDeleteInputDto.getS_id();

            //查询是否存在该计划
            Scheme scheme = schemeRepository.searchSchemeBySid(s_id);
            if (scheme != null) {//存在该计划
                Date curtime = new Date(System.currentTimeMillis());//当前时间

                //软删除
                if (!scheme.getSstatus().equals("审核中")) {
                    schemeRepository.deleteSchemeBySId(curtime, s_id);

                    //更新使用的节目状态为“未使用”
                    programmeRepository.deleteProgramByDeleteScheme(curtime, scheme.getP().getId());

                    //填充返回的dto
                    res.setFailed(false);//没发生异常
                    res.setId(scheme.getId());
                    res.setSname(scheme.getSname());
                    res.setUpdatetime(curtime);
                    res.setMemo("计划删除成功");

                    //存缓存
                    ++index;
                    SchemeAddLogDto dto = new SchemeAddLogDto();
                    dto.setId(index);
                    dto.setTime(new Date(System.currentTimeMillis()));
                    dto.setContent("[计划]" + res.getSname() + "计划删除成功");
                    redisTemplate.opsForValue().set(SchemeAddLogDto.cacheKey(index), JSONObject.toJSONString(dto),
                            2592000, TimeUnit.SECONDS);
                    logger.warn("[计划]计划删除成功[{}]", SchemeAddLogDto.cacheKey(index));
                } else {
                    //返回错误提示信息
                    logger.warn("计划删除失败");
                    res.setFailed(true);
                    res.setId(0);
                    res.setMemo("计划删除失败");

                    //存缓存
                    ++index;
                    SchemeAddLogDto dto = new SchemeAddLogDto();
                    dto.setId(index);
                    dto.setTime(new Date(System.currentTimeMillis()));
                    dto.setContent("[计划]" + res.getSname() + "计划删除失败");
                    redisTemplate.opsForValue().set(SchemeAddLogDto.cacheKey(index), JSONObject.toJSONString(dto),
                            2592000, TimeUnit.SECONDS);
                    logger.warn("[计划]计划删除失败[{}]", SchemeAddLogDto.cacheKey(index));
                }
            }
        } else {
            //返回错误提示信息
            logger.warn("计划删除失败");
            res.setFailed(true);
            res.setId(0);
            res.setMemo("计划删除失败");

            //存缓存
            ++index;
            SchemeAddLogDto dto = new SchemeAddLogDto();
            dto.setId(index);
            dto.setTime(new Date(System.currentTimeMillis()));
            dto.setContent("[计划]" + res.getSname() + "计划删除失败");
            redisTemplate.opsForValue().set(SchemeAddLogDto.cacheKey(index), JSONObject.toJSONString(dto),
                    2592000, TimeUnit.SECONDS);
            logger.warn("[计划]计划删除失败[{}]", SchemeAddLogDto.cacheKey(index));
        }
        //获取缓存中的cnt
        String str = (String) redisTemplate.opsForValue().get(CntLogDto.cacheKey(1));
        CntLogDto dto = JSONObject.parseObject(str, CntLogDto.class);//json转dto

        int nowCnt = 0;
        if (dto != null) {
            nowCnt = dto.getCnt();
        }

        //存日志数量
        CntLogDto cnt = new CntLogDto();
        cnt.setCnt(++nowCnt);
        redisTemplate.opsForValue().set(CntLogDto.cacheKey(1), JSONObject.toJSONString(cnt),
                2592000, TimeUnit.SECONDS);
        logger.warn("[计划]日志数量添加成功[{}]", CntLogDto.cacheKey(1));

        return res;
    }

    @Override
    public List<SchemeDto> search(SchemeSearchInputDto schemeSearchInputDto) throws BoeBoardServiceException {
        List<SchemeDto> res = new ArrayList<>();//存放结果

        if (schemeSearchInputDto != null) {
            //获取传入的dto的信息
            String sname = schemeSearchInputDto.getSname();//计划名称
            String sstatus = schemeSearchInputDto.getSstatus();//计划状态

            //计划状态分：待发布、已发布
            if (sname.equals("") && sstatus.equals("所有状态")) {//相当于获取所有计划
//                //调用SchemeServiceImpl的获取所有计划的接口
//                SchemeAllInputDto input = new SchemeAllInputDto();
//                input.setFlag(true);
//                res = SchemeServiceImpl.this.getAll();
                List<Scheme> list = schemeRepository.searchSchemeNoLimited();//获取所有计划
                res = makeReturnDtoList(list);
            } else if (!sname.equals("") && sstatus.equals("所有状态")) {//根据sname筛选
                List<Scheme> list = schemeRepository.searchSchemeBySname(sname);//sname模糊查询
                res = makeReturnDtoList(list);//调用构造返回dto列表的函数
            } else if (sname.equals("") && (sstatus.equals("待发布") || sstatus.equals("已发布"))) {//根据sstatus筛选
                List<Scheme> list = schemeRepository.searchSchemeBySstatus(sstatus);
                res = makeReturnDtoList(list);//调用构造返回dto列表的函数
            } else {//根据sname和sstatus筛选
                List<Scheme> list = schemeRepository.searchSchemeBySnameAndSstatus(sname, sstatus);//sname模糊查询
                res = makeReturnDtoList(list);//调用构造返回dto列表的函数
            }
        } else {
            SchemeDto dto = new SchemeDto();
            dto.setFailed(true);
            dto.setMemo("查询计划失败");
            res.add(dto);
        }
        return res;
    }

    //构造返回dto列表的函数
    public List<SchemeDto> makeReturnDtoList(List<Scheme> list) throws BoeBoardServiceException {
        List<SchemeDto> res = new ArrayList<>();//存放结果
        if (list.size() > 0) {
            int index = 1;//查找计划时计划的下标

            //遍历list
            for (Scheme item : list) {
                SchemeDto dto = new SchemeDto();
                dto.setFailed(false);
                dto.setId(index++);
                dto.setS_id(item.getId());
                dto.setSname(item.getSname());
                dto.setPlaymode(item.getPlaymode());
                dto.setSstatus(item.getSstatus());

                //构造playdate
                Date startday = item.getStartday();
                Date endday = item.getEndday();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String d1 = (sdf.format(startday)).substring(0, 10);
                String d2 = (sdf.format(endday)).substring(0, 10);
                String playdate = d1 + "~" + d2;

                dto.setPlaydate(playdate);
                dto.setAuthor(item.getAuthor());
                dto.setReviewer(item.getReviewer());
                dto.setUpdatetime(item.getUpdatetime());
                dto.setCreatetime(item.getCreatetime());
                dto.setP_id(item.getP().getId());
                dto.setPlength(item.getP().getPlength());
                dto.setMemo("查询计划成功");
                res.add(dto);
            }
        } else {
            SchemeDto dto = new SchemeDto();
            dto.setFailed(true);
            dto.setMemo("查询计划失败");
            res.add(dto);
        }
        return res;
    }

    @Override
    public long reproduce(String token, SchemeReproduceInputDto schemeReproduceInputDto) throws BoeBoardServiceException {
        long id = 0;
        if (schemeReproduceInputDto != null && (token != "" || token != null)) {
            //获取要复制的计划id
            long s_id = schemeReproduceInputDto.getS_id();
//            long u_id = schemeReproduceInputDto.getU_id();

            //查询数据库是否存在该计划
            Scheme scheme = schemeRepository.searchSchemeBySid(s_id);

            //查询数据库是否存在该用户
            Users users = new Users();
            if (StpUtil.getLoginIdByToken(token) != null) {
                String u_id = (String) StpUtil.getLoginIdByToken(token);

                //查找该用户
                Users user = usersRepository.findAUserByUId(Long.parseLong(u_id));
                if (user != null) {
                    users = user;
                }
            }
//            Users user = usersRepository.findAUserByUId(u_id);

            if (scheme != null && users != null) {
                //复制
                Date curtime = new Date(System.currentTimeMillis());//当前时间
                ++name_index;
                if (scheme.getPlaymode() == 1) {//按时段播放
                    schemeRepository.reproduceAScheme(scheme.getD().getId(), scheme.getP().getId(),
                            scheme.getSname() + "(" + name_index + ")", scheme.getPlaymode(), scheme.getCycles(),
                            scheme.getStrategy(), scheme.getSynchronize(), scheme.getStartday(), scheme.getEndday(),
                            scheme.getStarttime(), scheme.getEndtime(), users.getUsername(), users.getUsername(),
                            curtime, curtime);
                } else if (scheme.getPlaymode() == 2) {//持续播放
                    schemeRepository.reproduceAScheme2(scheme.getD().getId(), scheme.getP().getId(),
                            scheme.getSname() + "(" + name_index + ")", scheme.getPlaymode(), scheme.getStrategy(),
                            scheme.getSynchronize(), users.getUsername(), users.getUsername(), curtime, curtime);
                }
                //获取刚刚复制得到的计划的id
                id = schemeRepository.searchCreatedScheme();
            }
        }
        return id;
    }

    @Override
    public long republish(SchemeRepublishInputDto schemeRepublishInputDto) throws BoeBoardServiceException {
        if (schemeRepublishInputDto != null) {
            //获取计划id和用户id
            long s_id = schemeRepublishInputDto.getS_id();
            long u_id = schemeRepublishInputDto.getU_id();

            //查询数据库是否存在该计划
            Scheme scheme = schemeRepository.searchSchemeBySid(s_id);
            //查询数据库是否存在该用户
            Users user = usersRepository.findAUserByUId(u_id);

            if (scheme != null && user != null) {
                //重新发布
                Date curtime = new Date(System.currentTimeMillis());//当前时间
                schemeRepository.republish(user.getUsername(), user.getUsername(), curtime, s_id);
            }

        }
        return schemeRepublishInputDto.getS_id();
    }

}
