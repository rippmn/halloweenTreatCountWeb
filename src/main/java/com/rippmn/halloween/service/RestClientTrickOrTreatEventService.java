package com.rippmn.halloween.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.rippmn.halloween.domain.TTEvent;

@Component
public class RestClientTrickOrTreatEventService implements
		TrickOrTreatEventService {

	private final static SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
	private final static String endpoint = System.getenv("REST_ENDPOINT");
	
	@Override
	public TTEvent[] getAllTTEvents() {
		
		System.out.println(endpoint);
		
		RestTemplate restTemplate = new RestTemplate();
		TTEvent[] es = restTemplate.getForObject(endpoint+"/getTTs", TTEvent[].class);
		
		return es;
	}

	@Override
	public TTEvent[] getEventsAfterDate(Date d) {
		RestTemplate restTemplate = new RestTemplate();
		HashMap<String, String> params = new HashMap<String, String>();
		
		params.put("dateTime", sdf.format(d));
		
		//System.out.println("calling get After" +);
		
		TTEvent[] es = restTemplate.getForObject(endpoint+"/getTTsAfter/dateTime/{dateTime}", TTEvent[].class, params);
		
		return es;
	}

	@Override
	public void trickOrTreatEvent(int count) {
		RestTemplate restTemplate = new RestTemplate();
		
		MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
		mvm.add("count", String.valueOf(count));
		
		try{
			restTemplate.postForEntity(endpoint+"/trickOrTreat", mvm, TTEvent.class);
		}catch(HttpClientErrorException e){
			System.out.println(e.getResponseBodyAsString());
		}
		
	}
	
	

}
