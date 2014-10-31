package com.rippmn.halloween.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.rippmn.halloween.domain.TrickOrTreatReportingEvent;
import com.rippmn.halloween.service.TrickOrTreatEventService;
import com.rippmn.halloween.service.TrickOrTreaterReportService;

@Controller
public class TrickOrTreatController {

	@Autowired
	TrickOrTreatEventService service;
	
	@Autowired
	TrickOrTreaterReportService ttrService;
	
	Logger logger = LoggerFactory.getLogger(TrickOrTreatController.class);
	
	@RequestMapping("/trickOrTreat")
	public String trickOrTreatLanding(){
		return "trickOrTreat";
	}
	
	@RequestMapping(value="/count", method=RequestMethod.POST)
	public String count(@RequestParam int count){
		System.out.println("count:"+count);
		service.trickOrTreatEvent(count);
		return "trickOrTreat";
	}
	
	@RequestMapping("/report")
	public ModelAndView report(){
		
		ModelAndView model = new ModelAndView("report");
		
		TrickOrTreatReportingEvent ttre = ttrService.getLatestEvent();
		
		if(ttre != null){
			model.addObject("counts", ttre.getYearCounts());
		}
		
		return model;
	}
	
	@RequestMapping("/")
	public ModelAndView home(){
		
		return report();
	}
}
