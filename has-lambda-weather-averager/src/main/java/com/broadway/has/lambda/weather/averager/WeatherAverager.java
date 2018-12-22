package com.broadway.has.lambda.weather.averager;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface WeatherAverager {
	
	void average(JsonNode sqsEventBody) throws Exception;
	

}
