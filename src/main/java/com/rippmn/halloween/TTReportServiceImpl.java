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

	public void setTrickORTreatEventService(TrickOrTreatEventService ttes) {
		this.ttes = ttes;
	}

	private ArrayList<TrickOrTreatReportingEvent> ttres; // = new
															// ArrayList<TrickOrTreatReportingEvent>();
	private TreeMap<Integer, TrickOrTreatReportingEvent> ttresByTime = new TreeMap<Integer, TrickOrTreatReportingEvent>();

	@Override
	public List<TrickOrTreatReportingEvent> getAllEvents() {

		return ttres;

	}

	// we will start at 5:00 (there will be event for all years)
	// every time quartz job fires get the prior year events and transfer to the
	// next cycle

	public void initialize() {
		TTEvent[] es = ttes.getAllTTEvents();

		TreeMap<Date, Integer> ttes = new TreeMap<Date, Integer>();

		for (TTEvent tte : es) {
			ttes.put(tte.getEventDateTime(), tte.getCount());
		}

		for (Date d : ttes.keySet()) {

			// everything below here is method
			this.addEvent(d, ttes.get(d));

		}

		// now we need to update the totals (transfer prior counts)
		TrickOrTreatReportingEvent lastEvent = null;
		for (TrickOrTreatReportingEvent event : ttresByTime.values()) {
			if (lastEvent != null) {
				for (String year : lastEvent.getYearCounts().keySet()) {
					int lastCount = lastEvent.getYearCounts().get(year);
					int currentCount = event.getYearCounts().get(year) != null ? event
							.getYearCounts().get(year) : 0;
					event.getYearCounts().put(year, lastCount + currentCount);
				}
			}
			lastEvent = event;

		}

		this.updateList();

	}

	private void addEvent(Date d, Integer count) {
		Integer last;
		TrickOrTreatReportingEvent ttre = ttresByTime
				.get(TrickOrTreatReportingEvent.getTime(d));
		if (ttre == null) {
			ttre = new TrickOrTreatReportingEvent(d, count);
			ttresByTime.put(ttre.getTime(), ttre);
		} else {
			ttre.putEvent(d, count);
		}

	}

	public void updateReport() {
		// get the current events from service
		// anything in last two mins
		Date d = new Date(System.currentTimeMillis() - 120000);

		TTEvent[] ttesEvents = ttes.getEventsAfterDate(d);

		for (TTEvent tte : ttesEvents) {
			this.addEvent(tte.getEventDateTime(), tte.getCount());
		}

		Integer time = TrickOrTreatReportingEvent.getTime(new Date());

		TrickOrTreatReportingEvent event = ttresByTime.get(time);
		TrickOrTreatReportingEvent lastEvent = ttresByTime.lowerEntry(time)
				.getValue();

		for (String year : lastEvent.getYearCounts().keySet()) {
			int lastCount = lastEvent.getYearCounts().get(year);
			int currentCount = event.getYearCounts().get(year) != null ? event
					.getYearCounts().get(year) : 0;
			event.getYearCounts().put(year, lastCount + currentCount);
		}

		this.updateList();

	}

	private void updateList() {
		// TODO this would be cooler if and only if we could add to existing
		// list instead of creating new one (future)

		if (ttres != null) {
			
			//need to add the next entry
			if(ttres.size() > 0){
				Integer lastKey = ttres.get(ttres.size()-1).getTime();
				ttres.add(ttresByTime.higherEntry(lastKey).getValue());
			}
		
		} else {// this is the initial load
			ttres = new ArrayList<TrickOrTreatReportingEvent>();

			Integer currentTime = TrickOrTreatReportingEvent
					.getTime(new Date());
			if (ttresByTime.floorKey(currentTime) != null) {
				// the returnable subset to be up to date
				ttres = new ArrayList<TrickOrTreatReportingEvent>(ttresByTime
						.headMap(currentTime).values());
			}
		}

		//

		// if (ttresByTime
		// .floorKey(TrickOrTreatReportingEvent.getTime(new Date())) != null) {
		// // the returnable subset to be up to date
		// ttres = new ArrayList<TrickOrTreatReportingEvent>(ttresByTime
		// .headMap(TrickOrTreatReportingEvent.getTime(new Date()),
		// false).values());
		// } else {
		// ttres = new ArrayList<TrickOrTreatReportingEvent>();
		// ttres.add(ttresByTime.firstEntry().getValue());
		// }
	}

}
