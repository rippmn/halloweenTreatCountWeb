package com.rippmn.halloween.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TrickOrTreatController {

	Logger logger = LoggerFactory.getLogger(TrickOrTreatController.class);
	
	@RequestMapping("/trickOrTreat")
	public String trickOrTreatLanding(){
		return "trickOrTreat.jsp";
	}
	
	@RequestMapping(value="/count", method=RequestMethod.POST)
	public String count(@RequestParam int count){
		return "trickOrTreat.jsp";
	}
	
	@RequestMapping("/report")
	public String report(ModelAndView model){
		return "report.jsp";
	}
}
