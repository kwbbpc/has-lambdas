package com.broadway.has.lambda.weather.averager.humidity.daos;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class HumidityDao {
	
	private String nodeId;
	private DateTime timeslot;
	private DateTime lastUpdated;
	private AvgHumidity humidity;
	private List<UpdateRecord> datesIncludedInAverage;
	
	
	
	
	public HumidityDao() {
		super();
		this.nodeId = "";
		this.timeslot = DateTime.now();
		this.lastUpdated = DateTime.now();
		this.datesIncludedInAverage = new ArrayList<UpdateRecord>();
		this.humidity = new AvgHumidity();
	}
	public HumidityDao(String nodeId, DateTime timeslot, DateTime lastUpdated, AvgHumidity humidity,
					   List<UpdateRecord> datesIncludedInAverage) {
		super();
		this.nodeId = nodeId;
		this.timeslot = timeslot;
		this.lastUpdated = lastUpdated;
		this.humidity = humidity;
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
	public AvgHumidity getHumidity() {
		return humidity;
	}
	public void setHumidity(AvgHumidity humidity) {
		this.humidity = humidity;
	}
	public List<UpdateRecord> getDatesIncludedInAverage() {
		return datesIncludedInAverage;
	}
	public void setDatesIncludedInAverage(List<UpdateRecord> datesIncludedInAverage) {
		this.datesIncludedInAverage = datesIncludedInAverage;
	}

	
	
}
