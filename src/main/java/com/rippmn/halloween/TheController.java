package com.rippmn.halloween;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TheController {

	TrickOrTreatReportingEvent latest;
	ArrayList<TrickOrTreatReportingEvent> ttres = new ArrayList<TrickOrTreatReportingEvent>();
	HashMap<Integer, TrickOrTreatReportingEvent> ttresByTime = new HashMap<Integer, TrickOrTreatReportingEvent>();
	
	@RequestMapping("/callit")
	public List<TrickOrTreatReportingEvent> callIt(){
		
		
		
		
		ArrayList<TrickOrTreatReportingEvent> events = new ArrayList<TrickOrTreatReportingEvent>();
		
		TrickOrTreatReportingEvent ttre = new TrickOrTreatReportingEvent(new Date(1000l*60*60*24*365*45), 1);
		events.add(ttre);
		
		ttre.putEvent(new Date(1000l*60*60*24*365*43), 2);
		ttre.putEvent(new Date(1000l*60*60*24*365*44), 3);
		
		ttre = new TrickOrTreatReportingEvent(new Date(1000l*60*60*23*365*45), 9);
		events.add(ttre);
		ttre.putEvent(new Date(1000l*60*60*23*365*46), 4);
		ttre.putEvent(new Date(1000l*60*60*23*365*44), 5);
		
		
		
		return events;
		
		//call rest service to get data
		//for each data point
		//get ttre from map by time
		//sync this
		//if null create new ttre
		
		//else add the event to count
		//insert this into the ordered list
		//above will be binary search to place
		//note if above is end of list make this the most recent event
	}
	
}
