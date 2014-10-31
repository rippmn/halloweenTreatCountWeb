package com.rippmn.halloween.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.rippmn.halloween.service.TrickOrTreaterReportService;

@EnableScheduling
public class UpdateReportDataJob{

		@Autowired
		TrickOrTreaterReportService service;
	
		@Scheduled(cron="0 0/2 17-21 * * *")
		public void theTask(){
			service.updateReport();
		}

}
