package com.broadway.has.lambda.gettemperatures.db;

import org.joda.time.DateTime;

import com.broadway.has.lambda.gettemperatures.response.TemperatureResponse;

public interface DatabaseManager {


    TemperatureResponse getTemperatures(String nodeId, DateTime startDate, DateTime endDate, Integer limit) throws Exception;


}
