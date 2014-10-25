package com.rippmn.halloween;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TrickOrTreatReportingEvent {
	
	private HashMap<String, Integer> yearCounts = new HashMap<String, Integer>();
	
	private Integer time;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
	private static final SimpleDateFormat secSdf = new SimpleDateFormat("ss");
	private static final SimpleDateFormat yearSdf = new SimpleDateFormat("YYYY");
	
	public TrickOrTreatReportingEvent(Date eventDate, int count){
		time = getTime(eventDate);
		putEvent(eventDate, count);
		
	}
	
	public void putEvent(Date date, int count){
		String year = yearSdf.format(date);
		
		Integer yearCount = yearCounts.get(year);
		
		if(yearCount ==null){
			yearCount = 0;
		}	
		yearCount += count;
		
		yearCounts.put(year, yearCount);
		
	}
	
	
	public static Integer getTime(Date date){
		
		Integer time = Integer.parseInt(sdf.format(date));
		
		int mod = time%2;
		
		if(mod == 0){
			if(!secSdf.format(date).equals("00")){
				time+=2;
			}
		}else{
			time += time%2;
		}
		
		return time;
		
	}

	public HashMap<String, Integer> getYearCounts() {
		return yearCounts;
	}
	
	public Integer getTime() {
		return time;
	}

	
	

}
