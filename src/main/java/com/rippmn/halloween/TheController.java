package com.rippmn.halloween;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TheController {

	@Autowired
	TrickOrTreaterReportService service;
	
	@RequestMapping("/callit")
	public List<TrickOrTreatReportingEvent> callIt(){
		
		return service.getAllEvents();
		
	}
	
}
