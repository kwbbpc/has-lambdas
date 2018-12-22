package com.broadway.has.lambda.weather.averager.temperature.daos;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class TemperatureDao {
	
	private String nodeId;
	private DateTime timeslot;
	private DateTime lastUpdated;
	private AvgTempDao temp;
	private List<UpdateRecord> datesIncludedInAverage;
	
	
	
	
	public TemperatureDao() {
		super();
		this.nodeId = "";
		this.timeslot = DateTime.now();
		this.lastUpdated = DateTime.now();
		this.datesIncludedInAverage = new ArrayList<UpdateRecord>();
		this.temp = new AvgTempDao();
	}
	public TemperatureDao(String nodeId, DateTime timeslot, DateTime lastUpdated, AvgTempDao temp,
			List<UpdateRecord> datesIncludedInAverage) {
		super();
		this.nodeId = nodeId;
		this.timeslot = timeslot;
		this.lastUpdated = lastUpdated;
		this.temp = temp;
		this.datesIncludedInAverage = datesIncludedInAverage;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public DateTime getTimeslot() {
		return timeslot;
	}
	public void setTimeslot(DateTime timeslot) {
		this.timeslot = timeslot;
	}
	public DateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(DateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public AvgTempDao getTemp() {
		return temp;
	}
	public void setTemp(AvgTempDao temp) {
		this.temp = temp;
	}
	public List<UpdateRecord> getDatesIncludedInAverage() {
		return datesIncludedInAverage;
	}
	public void setDatesIncludedInAverage(List<UpdateRecord> datesIncludedInAverage) {
		this.datesIncludedInAverage = datesIncludedInAverage;
	}

	
	
}
