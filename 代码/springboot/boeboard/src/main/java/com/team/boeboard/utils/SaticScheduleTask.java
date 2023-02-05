package com.team.boeboard.utils;

import cn.hutool.db.handler.StringHandler;
import com.team.boeboard.entity.Announce;
import com.team.boeboard.exception.BoeBoardServiceException;
import com.team.boeboard.repository.AnnounceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    @Autowired
    private AnnounceRepository announceRepository;


    //3.添加定时任务
    @Scheduled(cron = "0/3 * * * * ?")
    //或直接指定时间间隔，例如：3秒
    //@Scheduled(fixedRate=5000)
    private void updateAnnounce() {
        TimeHandler timeHandler = new TimeHandler();
        List<Announce> announceList = announceRepository.GetAllAnnounces();

        announceList.forEach(announce -> {
            String astatus = announce.getAstatus();
            if(!astatus.equals("发布结束")) {
                String start_time = announce.getStart_time();
                String finish_time = announce.getFinish_time();
                Date date_start = new Date();
                Date date_finish = new Date();
                Date now = new Date();
                try {
                    date_start = timeHandler.StringToDate(start_time);
                    date_finish = timeHandler.StringToDate(finish_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int compareToStart = timeHandler.CompareTime(now, date_start);
                int compareToFinish = timeHandler.CompareTime(now, date_finish);

                if (compareToStart < 0) {
                    astatus = "待发布";
                }
                if (compareToStart > 0 && compareToFinish < 0) {
                    astatus = "发布中";
                }
                if (compareToFinish > 0) {
                    astatus = "发布结束";
                }

                announceRepository.updateStatus(astatus, announce.getId());
            }
        });
    }
}
