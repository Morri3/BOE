package com.team.boeboard.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.team.boeboard.entity.Announce;
import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.form.announce.BackAnnounceDto;
import com.team.boeboard.form.announce.GetAnnounceDto;
import com.team.boeboard.mq.MqMessage;
import com.team.boeboard.repository.AnnounceRepository;
import com.team.boeboard.repository.UsersRepository;
import com.team.boeboard.service.AnnounceService;
import com.team.boeboard.utils.Constants;
import com.team.boeboard.utils.TimeHandler;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnnounceServiceImpl implements AnnounceService {

    @Autowired
    private AnnounceRepository announceRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private AmqpTemplate mqService;//mq


    //增加公告
    @Override
    public void AddAnnounce(GetAnnounceDto dto,String user_token) throws ParseException {
        TimeHandler timeHandler = new TimeHandler();

        String id = (String) StpUtil.getLoginIdByToken(user_token);
        String user_name = usersRepository.findById(Long.parseLong(id)).getUsername();
        String start_time = dto.getStart_time();
        String finish_time = dto.getFinish_time();
        String astatus = "";

        Date date_start = timeHandler.StringToDate(start_time);
        Date date_finish = timeHandler.StringToDate(finish_time);
        Date now = new Date();

        int time_legal = timeHandler.CompareTime(date_start,date_finish);
        if(time_legal > 0){
            throw new BoeBoardServiceException("开始时间不能迟于结束时间");
        }

        int compareToStart = timeHandler.CompareTime(now,date_start);
        int compareToFinish = timeHandler.CompareTime(now,date_finish);

        if (compareToStart < 0 ){
            astatus = "待发布";
        }
        if(compareToStart >0 && compareToFinish < 0){
            astatus = "发布中";
        }
        if(compareToFinish > 0){
            astatus = "发布结束";
        }

        //保存到数据库
        Announce announce = new Announce();
        int a_id = announceRepository.findMaxId() + 1;
        announce.setId(a_id);
        announce.setContent(dto.getContent());
        announce.setText_color(dto.getText_color());
        announce.setText_size(dto.getText_size());
        announce.setBackground_color(dto.getBackground_color());
        announce.setStart_time(start_time);
        announce.setFinish_time(finish_time);
        announce.setAstatus(astatus);
        announce.setAuthor(user_name);
        announce.setCreatetime(new Date());

        announceRepository.save(announce);

        //通过消息队列发送消息给AndroidStudio端
        MqMessage msg = new MqMessage(MqMessage.CATEGORY_ANNOUNCE);
        msg.appendContent("content", dto.getContent());
        msg.appendContent("text_color",dto.getText_color());
        msg.appendContent("text_size",dto.getText_size());
        msg.appendContent("background_color",dto.getBackground_color());
        msg.appendContent("start_time",dto.getStart_time());
        msg.appendContent("finish_time",dto.getFinish_time());
        mqService.convertAndSend(Constants.QUE_ANNOUNCE, msg.stringfy());

    }

    //删除公告
    @Override
    public void DeleteAnnounce(String content) {
        announceRepository.deleteByName(content);
    }

    //获取所有公告
    @Override
    public List<BackAnnounceDto> GetAllAnnounces() throws BoeBoardServiceException {
        List<BackAnnounceDto> dtos = new ArrayList<>();
        List<Announce> list = announceRepository.GetAllAnnounces();
        list.forEach(announce -> {
            BackAnnounceDto dto = new BackAnnounceDto();
            dto.setContent(announce.getContent());
            dto.setStart_time(announce.getStart_time());
            dto.setAstatus(announce.getAstatus());
            dto.setAuthor(announce.getAuthor());
            dto.setCreatetime(announce.getCreatetime());

            dtos.add(dto);
        });
        return dtos;
    }
}
