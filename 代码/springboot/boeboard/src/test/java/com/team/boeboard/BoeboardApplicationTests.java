package com.team.boeboard;

import com.team.boeboard.minio.MinIOService;
import com.team.boeboard.result.ResponseData;
import com.team.boeboard.utils.TimeHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class BoeboardApplicationTests {

	@Autowired
	private MinIOService minIOService;

	@Test
	void contextLoads() {
		String size1 = "1.25MB";
		String size2 = "12KB";

		int index = 0;
		while(size1.charAt(index)!='M'&&size1.charAt(index)!='K'){
			index++;
		}
		double num = Double.valueOf(size1.substring(0,index));
		double size=0;
		if(size1.charAt(index) == 'M'){
			size = num*1024*1024;
		}


		System.out.println("分割结果："+(long)size);
	}
	static double total_size= 0;
	@Test
	void calcuate() throws ParseException {

		TimeHandler timeHandler = new TimeHandler();
		String astatus = "";
		String start_time = "2022-07-03 22:24:23";
		String finish_time = "2022-07-03 22:25:23";

		Date date_start = timeHandler.StringToDate(start_time);
		Date date_finish = timeHandler.StringToDate(finish_time);
		Date now = new Date();

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

		System.out.println("发布状态："+astatus);
	}


}
