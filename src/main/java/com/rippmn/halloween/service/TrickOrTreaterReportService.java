package com.rippmn.halloween.service;

import java.util.List;

import com.rippmn.halloween.domain.TrickOrTreatReportingEvent;

public interface TrickOrTreaterReportService {

	public List<TrickOrTreatReportingEvent> getAllEvents();

	public void updateReport();
	
	public TrickOrTreatReportingEvent getLatestEvent();
}
