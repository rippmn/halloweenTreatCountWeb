package com.rippmn.halloween;

import java.util.Date;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
public class UpdateReportDataJob{

		@Scheduled(cron="0 0/2 * * * *")
		public void theTask(){
			System.out.println("*****"+new Date());
		}

}
