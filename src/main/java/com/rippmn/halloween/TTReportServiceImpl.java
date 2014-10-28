package com.rippmn.halloween;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class TTReportServiceImpl implements TrickOrTreaterReportService {

	@Autowired
	private TrickOrTreatEventService ttes;
	
	private ArrayList<TrickOrTreatReportingEvent> ttres; //= new ArrayList<TrickOrTreatReportingEvent>();
	private TreeMap<Integer, TrickOrTreatReportingEvent> ttresByTime = new TreeMap<Integer, TrickOrTreatReportingEvent>();
	
	@Override
	public List<TrickOrTreatReportingEvent> getAllEvents() {

		return ttres;
				
	}
	
	public static void main(String[] args)throws Exception{
		TTReportServiceImpl tt = new TTReportServiceImpl();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date d = sdf.parse("2014-10-31 18:00:01");
		
		tt.initialize();
		
		System.out.println("done");
		
	}
	
	//we will start at 5:00 (there will be event for all years)
	//every time quartz job fires get the prior year events and transfer to the next cycle
	
	public void initialize(){
		TTEvent[] es = ttes.getAllTTEvents();
		
		TreeMap<Date, Integer> ttes = new TreeMap<Date, Integer>();
		
		for(TTEvent tte: es){
			ttes.put(tte.getEventDateTime(), tte.getCount());
		}
		
		for(Date d: ttes.keySet()){
			
			//everything below here is method
			this.addEvent(d, ttes.get(d));
			
		}

		this.updateList();

	}
	
	private void addEvent(Date d, Integer count){
		Integer last;
		TrickOrTreatReportingEvent ttre =  ttresByTime.get(TrickOrTreatReportingEvent.getTime(d));
		if(ttre == null){
			ttre = new TrickOrTreatReportingEvent(d, count);
			ttresByTime.put(ttre.getTime(), ttre);
			last = ttresByTime.lowerKey(ttre.getTime()) ;
			if(last != null){
				//if there is a last event we need to transfer counts
				Map<String, Integer> priorCounts = ttresByTime.get(last).getYearCounts();
				
				for(String year: priorCounts.keySet()){
					Integer currentCount = ttre.getYearCounts().get(year);
					
					if(currentCount == null){
						ttre.getYearCounts().put(year, priorCounts.get(year));
					}else{
						ttre.getYearCounts().put(year, currentCount + priorCounts.get(year));   
					}
					
				}
				
			}else{
				//prior totals are 0 do nothing
			}
		}else{
			ttre.putEvent(d, count);
		}

	}
	
	
	public void updateReport(){
		//get the current events from service 
		//anything in last two mins
		TTEvent[] ttesEvents = ttes.getEventsAfterDate(new Date(System.currentTimeMillis()-120000));
		
		for (TTEvent tte: ttesEvents) {
			this.addEvent(tte.getEventDateTime(), tte.getCount());
		}
	
		this.updateList();
		
		
	}
	
	private void updateList(){
		//TODO this would be cooler if and only if we could add to existing list instead of creating new one (future)
		if(ttresByTime.floorKey(TrickOrTreatReportingEvent.getTime(new Date())) != null){
			//the returnable subset to be up to date
			ttres = new ArrayList<TrickOrTreatReportingEvent>(ttresByTime.headMap(TrickOrTreatReportingEvent.getTime(new Date()), false).values());				
		}else{
			ttres = new ArrayList<TrickOrTreatReportingEvent>();
			ttres.add(ttresByTime.firstEntry().getValue());
		}
	}
	
}
