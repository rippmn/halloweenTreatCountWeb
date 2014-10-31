package com.rippmn.halloween.domain;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.rippmn.halloween.domain.TrickOrTreatReportingEvent;

public class TrickOrTreatReportingEventTest {

	@Test
	public void testGetTime() {
	
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		//test time = 160000, 160000.5 160001,160001.5 160002
		int counterAdd = 0;
		int basetime = Integer.parseInt(sdf.format(new Date(0)));
		
		Integer time = TrickOrTreatReportingEvent.getTime(new Date(counterAdd));
		assertEquals(basetime, time.intValue());
		
		counterAdd+=30000;//0.5
		time = TrickOrTreatReportingEvent.getTime(new Date(counterAdd));
		assertEquals(basetime+2, time.intValue());
		
		counterAdd+=30000; //1
		time = TrickOrTreatReportingEvent.getTime(new Date(counterAdd));
		assertEquals(basetime+2, time.intValue());
		
		counterAdd+=30000; //1.5
		time = TrickOrTreatReportingEvent.getTime(new Date(counterAdd));
		assertEquals(basetime+2, time.intValue());
		
		counterAdd+=30000; //2
		time = TrickOrTreatReportingEvent.getTime(new Date(counterAdd));
		assertEquals(basetime+2, time.intValue());
		
		counterAdd+=30000; //2.5
		time = TrickOrTreatReportingEvent.getTime(new Date(counterAdd));
		assertEquals(basetime+4, time.intValue());
		
		counterAdd+=30000;//3
		time = TrickOrTreatReportingEvent.getTime(new Date(counterAdd));
		assertEquals(basetime+4, time.intValue());
		
		time = TrickOrTreatReportingEvent.getTime(new Date(1000));
		assertEquals(basetime+2, time.intValue());
		
		sdf = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
		
		try{
			time = TrickOrTreatReportingEvent.getTime(sdf.parse("2014-10-31 14:58:01"));
			assertEquals(1500, time.intValue());
		}catch(ParseException pe){
			fail("test exception");
		}
		
		
	}
	
	@Test
	public void testTTETime(){
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		//test time = 160000, 160000.5 160001,160001.5 160002
		int counterAdd = 0;
		int basetime = Integer.parseInt(sdf.format(new Date(0)));
		assertEquals(basetime, new TrickOrTreatReportingEvent(new Date(counterAdd), 0 ).getTime().intValue());
		
		counterAdd+=30000;//0.5
		assertEquals(basetime+2, new TrickOrTreatReportingEvent(new Date(counterAdd), 0 ).getTime().intValue());
		
		counterAdd+=30000; //1
		assertEquals(basetime+2, new TrickOrTreatReportingEvent(new Date(counterAdd), 0 ).getTime().intValue());
		
		counterAdd+=30000; //1.5
		assertEquals(basetime+2, new TrickOrTreatReportingEvent(new Date(counterAdd), 0 ).getTime().intValue());
		
		counterAdd+=30000; //2
		assertEquals(basetime+2, new TrickOrTreatReportingEvent(new Date(counterAdd), 0 ).getTime().intValue());
		
		counterAdd+=30000; //2.5
		assertEquals(basetime+4, new TrickOrTreatReportingEvent(new Date(counterAdd), 0 ).getTime().intValue());
		
		counterAdd+=30000;//3
		assertEquals(basetime+4, new TrickOrTreatReportingEvent(new Date(counterAdd), 0 ).getTime().intValue());
		assertEquals(basetime+2, new TrickOrTreatReportingEvent(new Date(1000), 0 ).getTime().intValue());
	}
	
	@Test
	public void testAddYearCounts(){
		
		String year = new SimpleDateFormat("YYYY").format(new Date(0));
		
		TrickOrTreatReportingEvent ttre = new TrickOrTreatReportingEvent(new Date(0), 3);
		
		assertNotNull(ttre.getYearCounts());
		assertEquals(1, ttre.getYearCounts().size());
		assertEquals(3, ttre.getYearCounts().get(year).intValue());
		ttre.putEvent(new Date(0), 2);
		assertEquals(1, ttre.getYearCounts().size());
		assertEquals(5, ttre.getYearCounts().get(year).intValue());
		ttre.putEvent(new Date(0), 4);
		assertEquals(1, ttre.getYearCounts().size());
		assertEquals(9, ttre.getYearCounts().get(year).intValue());
		
		
	}
	
	@Test
	public void testAddNewYearCounts(){
		
		String year = new SimpleDateFormat("YYYY").format(new Date(0));
		
		TrickOrTreatReportingEvent ttre = new TrickOrTreatReportingEvent(new Date(0), 3);
		assertNotNull(ttre.getYearCounts());
		assertEquals(1, ttre.getYearCounts().size());
		ttre.putEvent(new Date(1000l*60*60*24*365), 4);
		assertEquals(2, ttre.getYearCounts().size());
		ttre.putEvent(new Date(1000l*60*60*24*2000), 5);
		assertEquals(3, ttre.getYearCounts().size());
		assertEquals(3, ttre.getYearCounts().get(year).intValue());
		year = new SimpleDateFormat("YYYY").format(new Date(1000l*60*60*24*365));
		assertEquals(4, ttre.getYearCounts().get(year).intValue());
		
		
	}

}
