package com.rippmn.halloween;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	
	private TTEvent[] getTestData() throws Exception {
		
		
		//this needs to be a dynamic create of these so they are relevant to present time.... (note below approach does not deal with past MIDNIGHT as reporting model assumes a calendar day)
		//setbaseDate to be current time - 3 years - 4 hours
		currentTime=System.currentTimeMillis();
		long baseTime = currentTime-(3*YEAR)-(4*HOUR);
		
		dates = new Date[]{new Date(baseTime+(2*YEAR)), new Date(baseTime+YEAR), new Date(baseTime),
				new Date(baseTime+(2*YEAR)+HOUR), new Date(baseTime+YEAR+HOUR),
				new Date(baseTime+YEAR+(2*HOUR)), new Date(baseTime+(2*HOUR)),
				new Date(baseTime+(2*YEAR)+(3*HOUR)), new Date(baseTime+(3*HOUR)), new Date(currentTime-(4*HOUR))};
		
		int[] counts = new int[] { 0, 0, 0, 1, 2, 3, 4, 5, 6,0 };
		ArrayList<TTEvent> ttes = new ArrayList<TTEvent>(10);
		TTEvent tte;
		for (int i = 0; i < dates.length; i++) {
			tte = new TTEvent();

			ttes.add(tte);
			tte.setCount(counts[i]);
			tte.setEventDateTime(dates[i]);

		}

		TTEvent[] tteArray = new TTEvent[ttes.size()];

		return ttes.toArray(tteArray);

	}
	
	private Integer getTime(Date d){
		return TrickOrTreatReportingEvent.getTime(d);
	}

	@Test
	public void testInitialize() throws Exception {

		MockitoAnnotations.initMocks(this);

		TTEvent[] ttes = this.getTestData();

		TTReportServiceImpl service = new TTReportServiceImpl();
		service.setTrickORTreatEventService(tteService);

		when(tteService.getAllTTEvents()).thenReturn(ttes);

		service.initialize();
		verify(tteService, times(1)).getAllTTEvents();
		assertEquals(4, service.getAllEvents().size());
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

		//add 2014 base date
		assertEquals(0,
				service.getAllEvents().get(0).getYearCounts().get("2014")
						.intValue());
		assertEquals(0,
				service.getAllEvents().get(1).getYearCounts().get("2014")
						.intValue());
		assertEquals(0,
				service.getAllEvents().get(2).getYearCounts().get("2014")
						.intValue());
		assertEquals(0,
				service.getAllEvents().get(3).getYearCounts().get("2014")
						.intValue());

	
	}
	
	//note this test may be moot given we should have a base data point for 2014 as well. but we should see 
	@Test
	public void testUpdateReportAddToEnd()throws Exception{
		MockitoAnnotations.initMocks(this);
		
		TTEvent[] baseData = getTestData();
		TTEvent[] ttes = new TTEvent[1];
		ttes[0] = new TTEvent();
		ttes[0].setCount(100);
		ttes[0].setEventDateTime(new Date(currentTime));

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
		assertEquals(0,
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
		assertEquals(100,
				service.getAllEvents().get(4).getYearCounts().get("2014")
						.intValue());

	}

}
