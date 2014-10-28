package com.rippmn.halloween;

import java.util.Date;

import org.springframework.web.client.RestTemplate;

public class RestClientTrickOrTreatEventService implements
		TrickOrTreatEventService {

	@Override
	public TTEvent[] getAllTTEvents() {
		RestTemplate restTemplate = new RestTemplate();
		//TODO fix links
		TTEvent[] es = restTemplate.getForObject("http://localhost:8080/getTTs", TTEvent[].class);
		
		return es;
	}

	@Override
	public TTEvent[] getEventsAfterDate(Date d) {
		RestTemplate restTemplate = new RestTemplate();
		//TODO fix links
		TTEvent[] es = restTemplate.getForObject("http://localhost:8080/getTTsAfter", TTEvent[].class);
		
		return es;
	}

}
