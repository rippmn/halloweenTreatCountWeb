package com.rippmn.halloween.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.rippmn.halloween.domain.TTEvent;

@Component
public class RestClientTrickOrTreatEventService implements
		TrickOrTreatEventService {

	private final static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
	
	@Override
	public TTEvent[] getAllTTEvents() {
		RestTemplate restTemplate = new RestTemplate();
		TTEvent[] es = restTemplate.getForObject("http://localhost:8080/getTTs", TTEvent[].class);
		
		return es;
	}

	@Override
	public TTEvent[] getEventsAfterDate(Date d) {
		RestTemplate restTemplate = new RestTemplate();
		//TODO fix links
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("dateTime", sdf.format(d));
		
		System.out.println("calling get After");
		
		TTEvent[] es = restTemplate.getForObject("http://localhost:8080/getTTsAfter/dateTime/{dateTime}", TTEvent[].class, params);
		
		return es;
	}

}
