package com.rippmn.halloween.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rippmn.halloween.domain.TTEvent;
import com.rippmn.halloween.domain.TrickOrTreatReportingEvent;

@Component
public class TTReportServiceImpl implements TrickOrTreaterReportService {

	@Autowired
	private TrickOrTreatEventService ttes;

	public void setTrickORTreatEventService(TrickOrTreatEventService ttes) {
		this.ttes = ttes;
	}

	private ArrayList<TrickOrTreatReportingEvent> ttres;

	private TreeMap<Integer, TrickOrTreatReportingEvent> ttresByTime = new TreeMap<Integer, TrickOrTreatReportingEvent>();

	@Override
	public List<TrickOrTreatReportingEvent> getAllEvents() {

		return ttres;

	}

	// we will start at 5:00 (there will be event for all years)
	// every time quartz job fires get the prior year events and transfer to the
	// next cycle

	@PostConstruct
	public void initialize() {
		
		System.out.println("Initializing the events cache");
		TTEvent[] es = ttes.getAllTTEvents();

		TreeMap<Date, Integer> ttes = new TreeMap<Date, Integer>();

		for (TTEvent tte : es) {
			ttes.put(tte.getEventDateTime(), tte.getCount());
		}

		for (Date d : ttes.keySet()) {

			this.addEvent(d, ttes.get(d), false);

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

	private void addEvent(Date d, Integer count, boolean updateTotals) {
		TrickOrTreatReportingEvent ttre = ttresByTime
				.get(TrickOrTreatReportingEvent.getTime(d));
		if (ttre == null) {
			ttre = new TrickOrTreatReportingEvent(d, count);
			ttresByTime.put(ttre.getTime(), ttre);
			if (updateTotals) {
				// get the lower key and transfer data
				TrickOrTreatReportingEvent oldTTRE = ttresByTime.lowerEntry(
						ttre.getTime()).getValue();

				for (String year : oldTTRE.getYearCounts().keySet()) {
					int lastCount = oldTTRE.getYearCounts().get(year);
					int currentCount = ttre.getYearCounts().get(year) != null ? ttre
							.getYearCounts().get(year) : 0;
					ttre.getYearCounts().put(year, lastCount + currentCount);

				}
			}
		} else {
			ttre.putEvent(d, count);
		}

		// traverse rest of set after this and add this event as well
		if (updateTotals) {
			for (Integer key : ttresByTime.tailMap(ttre.getTime(), false)
					.keySet()) {
				ttresByTime.get(key).putEvent(d, count);
			}
		}
	}

	public void updateReport() {
		// get the current events from service
		// anything in last two mins
		Date d = new Date(System.currentTimeMillis() - 120000);

		TTEvent[] ttesEvents = ttes.getEventsAfterDate(d);

		if (ttesEvents != null && ttesEvents.length > 0) {
			for (TTEvent tte : ttesEvents) {
				this.addEvent(tte.getEventDateTime(), tte.getCount(), true);
			}
		}

		this.updateList();

	}

	private void updateList() {
		// TODO this would be cooler if and only if we could add to existing
		// list instead of creating new one (future)

		if (ttres != null) {

			// need to add the next entry
			if (ttres.size() > 0) {
				Integer lastKey = ttres.get(ttres.size() - 1).getTime();
				// note we always add this need a path to not add
				// if the time is not greater than the next key we should not
				// add
				Integer nextKey = ttresByTime.higherKey(lastKey);
				if (nextKey != null && TrickOrTreatReportingEvent.getTime(new Date()) > nextKey) {
					ttres.add(ttresByTime.get(nextKey));
				}
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
