package com.broadway.has.lambda.weather.averager;

import com.fasterxml.jackson.databind.JsonNode;

public interface Averager {
	
	void average(JsonNode sqsEventBody) throws Exception;
	

}
