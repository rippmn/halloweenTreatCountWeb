package com.rippmn.halloween;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.springframework.web.client.RestTemplate;

public class TTReportServiceImpl implements TrickOrTreaterReportService {

	//TrickOrTreatReportingEvent latest;
	ArrayList<TrickOrTreatReportingEvent> ttres = new ArrayList<TrickOrTreatReportingEvent>();
	TreeMap<Integer, TrickOrTreatReportingEvent> ttresByTime = new TreeMap<Integer, TrickOrTreatReportingEvent>();
	
	@Override
	public List<TrickOrTreatReportingEvent> getAllEvents() {
		//RestTemplate restTemplate = new RestTemplate();
		//TTEvent[] es = restTemplate.getForObject("http://localhost:8080/getTTs", TTEvent[].class);
		
		return null;
		
				
	}
	
	public static void main(String[] args)throws Exception{
		TTReportServiceImpl tt = new TTReportServiceImpl();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date d = sdf.parse("2014-10-31 18:00:01");
		
		tt.initialize();
		
		System.out.println("done");
		
	}
	
	
	public void initialize(){
		RestTemplate restTemplate = new RestTemplate();
		TTEvent[] es = restTemplate.getForObject("http://localhost:8080/getTTs", TTEvent[].class);
		
		for(TTEvent tte: es){
			TrickOrTreatReportingEvent ttre =  ttresByTime.get(TrickOrTreatReportingEvent.getTime(tte.getEventDateTime()));
			if(ttre == null){
				ttre = new TrickOrTreatReportingEvent(tte.getEventDateTime(), tte.getCount());
				
				//transfer reporting totals
				//we cannot do this until we have a set of data  
				
				ttresByTime.put(ttre.getTime(), ttre);
			}else{
				ttre.putEvent(tte.getEventDateTime(), tte.getCount());
			}
			
			//at the end of this we will have a consoliated count of events by time per haps we whould also make a total available in the same series....
			
		}
		
		ttres.addAll(ttresByTime.values());		
				
	}
	
	private void addEvent(TTEvent tte){
	}

}
