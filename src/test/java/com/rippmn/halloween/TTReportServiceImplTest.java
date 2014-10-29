package com.rippmn.halloween;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class TTReportServiceImplTest {

	@Mock
	TrickOrTreatEventService tteService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private static final long HOUR = 60000*60l;
	private static final long YEAR = 365l*24*60*60000;
	
	private Date[] dates;
	private long currentTime;
	
	private TTEvent[] getTestData(){
		return getTestData(null);
	}
	
	@Before
	public void setCurrentTime(){
		currentTime=System.currentTimeMillis();
	}
	
	private TTEvent[] getTestData(TTEvent... addTte){
		
		
		//this needs to be a dynamic create of these so they are relevant to present time.... (note below approach does not deal with past MIDNIGHT as reporting model assumes a calendar day)
		//setbaseDate to be current time - 3 years - 4 hours
		long baseTime = currentTime-(3*YEAR)-(4*HOUR);
		
		dates = new Date[]{new Date(baseTime+(2*YEAR)), new Date(baseTime+YEAR), new Date(baseTime),
				new Date(baseTime+(2*YEAR)+HOUR), new Date(baseTime+YEAR+HOUR),
				new Date(baseTime+YEAR+(2*HOUR)), new Date(baseTime+(2*HOUR)),
				new Date(baseTime+(2*YEAR)+(3*HOUR)), new Date(baseTime+(3*HOUR)), new Date(currentTime-(4*HOUR)), new Date(currentTime-YEAR+600000), new Date(currentTime-(2*HOUR))};
		
		int[] counts = new int[] { 0, 0, 0, 1, 2, 3, 4, 5, 6,0,1000,600 };
		ArrayList<TTEvent> ttes = new ArrayList<TTEvent>(10);
		TTEvent tte;
		for (int i = 0; i < dates.length; i++) {
			tte = new TTEvent();

			ttes.add(tte);
			tte.setCount(counts[i]);
			tte.setEventDateTime(dates[i]);

		}

		if(addTte != null){
			for(TTEvent newTTE: addTte){
				ttes.add(newTTE);
			}
		}
		
		TTEvent[] tteArray = new TTEvent[ttes.size()];

		return ttes.toArray(tteArray);

	}
	
	private Integer getTime(Date d){
		return TrickOrTreatReportingEvent.getTime(d);
	}

	@Test
	public void testInitialize(){

		MockitoAnnotations.initMocks(this);

		TTEvent[] ttes = this.getTestData();

		TTReportServiceImpl service = new TTReportServiceImpl();
		service.setTrickORTreatEventService(tteService);

		when(tteService.getAllTTEvents()).thenReturn(ttes);

		service.initialize();
		verify(tteService, times(1)).getAllTTEvents();
		assertEquals(4, service.getAllEvents().size());
		verifyBaseData(service);
		verify2014Base(service);
		
	}
	
	private void verifyBaseData(TTReportServiceImpl service){
		
		assertEquals(getTime(dates[0]), service.getAllEvents().get(0).getTime());
		assertEquals(getTime(dates[3]), service.getAllEvents().get(1).getTime());
		assertEquals(getTime(dates[5]), service.getAllEvents().get(2).getTime());
		assertEquals(getTime(dates[7]), service.getAllEvents().get(3).getTime());

		assertEquals(0,
				service.getAllEvents().get(0).getYearCounts().get("2013")
						.intValue());
		assertEquals(0,
				service.getAllEvents().get(0).getYearCounts().get("2012")
						.intValue());
		assertEquals(0,
				service.getAllEvents().get(0).getYearCounts().get("2011")
						.intValue());
		
		assertEquals(1,
				service.getAllEvents().get(1).getYearCounts().get("2013")
						.intValue());
		assertEquals(2,
				service.getAllEvents().get(1).getYearCounts().get("2012")
						.intValue());
		assertEquals(0,
				service.getAllEvents().get(1).getYearCounts().get("2011")
						.intValue());

		assertEquals(1,
				service.getAllEvents().get(2).getYearCounts().get("2013")
						.intValue());
		assertEquals(5,
				service.getAllEvents().get(2).getYearCounts().get("2012")
						.intValue());
		assertEquals(4,
				service.getAllEvents().get(2).getYearCounts().get("2011")
						.intValue());

		assertEquals(6,
				service.getAllEvents().get(3).getYearCounts().get("2013")
						.intValue());
		assertEquals(5,
				service.getAllEvents().get(3).getYearCounts().get("2012")
						.intValue());
		assertEquals(10,
				service.getAllEvents().get(3).getYearCounts().get("2011")
						.intValue());
	}
	
	private void verify2014Base(TTReportServiceImpl service){
		//add 2014 base date
		assertEquals(0,
				service.getAllEvents().get(0).getYearCounts().get("2014")
						.intValue());
		assertEquals(0,
				service.getAllEvents().get(1).getYearCounts().get("2014")
						.intValue());
		assertEquals(600,
				service.getAllEvents().get(2).getYearCounts().get("2014")
						.intValue());
		assertEquals(600,
				service.getAllEvents().get(3).getYearCounts().get("2014")
						.intValue());

	}

	@Test
	public void testNullUpdate() {

		MockitoAnnotations.initMocks(this);

		TTEvent[] baseData = getTestData();
		TTEvent[] ttes = null;
		
		TTReportServiceImpl service = new TTReportServiceImpl();
		service.setTrickORTreatEventService(tteService);

		when(tteService.getAllTTEvents()).thenReturn(baseData);

		service.initialize();

		when(tteService.getEventsAfterDate(any(Date.class))).thenReturn(ttes);

		service.updateReport();
		verify(tteService, times(1)).getEventsAfterDate(any(Date.class));

		assertEquals(4, service.getAllEvents().size());
		verifyBaseData(service);
		verify2014Base(service);
		
	}

	@Test
	public void testEmptyUpdate() {

		MockitoAnnotations.initMocks(this);

		TTEvent[] baseData = getTestData();
		TTEvent[] ttes = new TTEvent[0];
		
		TTReportServiceImpl service = new TTReportServiceImpl();
		service.setTrickORTreatEventService(tteService);

		when(tteService.getAllTTEvents()).thenReturn(baseData);

		service.initialize();

		when(tteService.getEventsAfterDate(any(Date.class))).thenReturn(ttes);

		service.updateReport();
		verify(tteService, times(1)).getEventsAfterDate(any(Date.class));

		assertEquals(4, service.getAllEvents().size());
		verifyBaseData(service);
		verify2014Base(service);
		
	}

	@Test
	public void testZeroCountUpdate() {

		MockitoAnnotations.initMocks(this);

		TTEvent[] baseData = getTestData();
		TTEvent[] ttes = new TTEvent[1];
		ttes[0] = new TTEvent();
		ttes[0].setCount(0);
		ttes[0].setEventDateTime(new Date(currentTime-120000));
		
		TTReportServiceImpl service = new TTReportServiceImpl();
		service.setTrickORTreatEventService(tteService);

		when(tteService.getAllTTEvents()).thenReturn(baseData);

		service.initialize();

		when(tteService.getEventsAfterDate(any(Date.class))).thenReturn(ttes);

		service.updateReport();
		verify(tteService, times(1)).getEventsAfterDate(any(Date.class));

		assertEquals(5, service.getAllEvents().size());
		verifyBaseData(service);
		verify2014Base(service);
		
		assertEquals(6,
				service.getAllEvents().get(4).getYearCounts().get("2013")
						.intValue());
		assertEquals(5,
				service.getAllEvents().get(4).getYearCounts().get("2012")
						.intValue());
		assertEquals(10,
				service.getAllEvents().get(4).getYearCounts().get("2011")
						.intValue());

		assertEquals(600,
				service.getAllEvents().get(4).getYearCounts().get("2014")
						.intValue());

		
	}

	
	@Test
	public void testUpdateReportAddAfterEnd(){
		MockitoAnnotations.initMocks(this);
		
		TTEvent[] baseData = getTestData();
		TTEvent[] ttes = new TTEvent[2];
		ttes[0] = new TTEvent();
		ttes[0].setCount(60);
		ttes[0].setEventDateTime(new Date(currentTime-120000));
		ttes[1] = new TTEvent();
		ttes[1].setCount(40);
		ttes[1].setEventDateTime(new Date(currentTime-120000));

		
		when(tteService.getAllTTEvents()).thenReturn(baseData);

		TTReportServiceImpl service = new TTReportServiceImpl();
		service.setTrickORTreatEventService(tteService);
		service.initialize();
		when(tteService.getEventsAfterDate(any(Date.class))).thenReturn(ttes);

		service.updateReport();
		verify(tteService, times(1)).getEventsAfterDate(any(Date.class));
		assertEquals(5, service.getAllEvents().size());
		assertEquals(TrickOrTreatReportingEvent.getTime(ttes[0].getEventDateTime()), service.getAllEvents().get(4).getTime());
		assertEquals(4, service.getAllEvents().get(4).getYearCounts().size());
		
		assertEquals(6,
				service.getAllEvents().get(3).getYearCounts().get("2013")
						.intValue());
		assertEquals(600,
				service.getAllEvents().get(3).getYearCounts().get("2014")
						.intValue());
		assertEquals(5,
				service.getAllEvents().get(3).getYearCounts().get("2012")
						.intValue());
		assertEquals(10,
				service.getAllEvents().get(3).getYearCounts().get("2011")
						.intValue());
		
		assertEquals(6,
				service.getAllEvents().get(4).getYearCounts().get("2013")
						.intValue());
		assertEquals(5,
				service.getAllEvents().get(4).getYearCounts().get("2012")
						.intValue());
		assertEquals(10,
				service.getAllEvents().get(4).getYearCounts().get("2011")
						.intValue());
		assertEquals(700,
				service.getAllEvents().get(4).getYearCounts().get("2014")
						.intValue());

	}
	
	
	
	@Test
	public void testMidPointAdd(){

		MockitoAnnotations.initMocks(this);

		TTEvent[] baseData = getTestData();
		TTEvent[] ttes = new TTEvent[1];
		ttes[0] = new TTEvent();
		ttes[0].setCount(99);
		ttes[0].setEventDateTime(new Date(currentTime-(3*HOUR)));
		
		TTReportServiceImpl service = new TTReportServiceImpl();
		service.setTrickORTreatEventService(tteService);

		when(tteService.getAllTTEvents()).thenReturn(baseData);

		service.initialize();

		when(tteService.getEventsAfterDate(any(Date.class))).thenReturn(ttes);

		service.updateReport();
		verify(tteService, times(1)).getEventsAfterDate(any(Date.class));

		assertEquals(4, service.getAllEvents().size());
		verifyBaseData(service);
		
		assertEquals(0,
				service.getAllEvents().get(0).getYearCounts().get("2014")
						.intValue());
		assertEquals(99,
				service.getAllEvents().get(1).getYearCounts().get("2014")
						.intValue());
		assertEquals(699,
				service.getAllEvents().get(2).getYearCounts().get("2014")
						.intValue());
		assertEquals(699,
				service.getAllEvents().get(3).getYearCounts().get("2014")
						.intValue());

		
	}

	@Test
	public void testUpdateReportAddToEnd(){
		MockitoAnnotations.initMocks(this);
		
		TTEvent newTTE = new TTEvent();
		newTTE.setCount(10);
		newTTE.setEventDateTime(new Date(currentTime-YEAR-120000));
		
		TTEvent[] baseData = getTestData(newTTE);
		
		TTEvent[] ttes = new TTEvent[2];
		ttes[0] = new TTEvent();
		ttes[0].setCount(100);
		ttes[0].setEventDateTime(new Date(currentTime-120000));

		ttes[1] = new TTEvent();
		ttes[1].setCount(150);
		ttes[1].setEventDateTime(new Date(currentTime-120000));

		
		when(tteService.getAllTTEvents()).thenReturn(baseData);

		TTReportServiceImpl service = new TTReportServiceImpl();
		service.setTrickORTreatEventService(tteService);
		service.initialize();
		when(tteService.getEventsAfterDate(any(Date.class))).thenReturn(ttes);

		service.updateReport();
		verify(tteService, times(1)).getEventsAfterDate(any(Date.class));
		assertEquals(5, service.getAllEvents().size());
		assertEquals(TrickOrTreatReportingEvent.getTime(newTTE.getEventDateTime()), service.getAllEvents().get(4).getTime());
		
		assertEquals(16,
				service.getAllEvents().get(4).getYearCounts().get("2013")
						.intValue());
		assertEquals(5,
				service.getAllEvents().get(4).getYearCounts().get("2012")
						.intValue());
		assertEquals(10,
				service.getAllEvents().get(4).getYearCounts().get("2011")
						.intValue());
		assertEquals(850,
				service.getAllEvents().get(4).getYearCounts().get("2014")
						.intValue());

	}

}
