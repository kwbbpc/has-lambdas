package com.broadway.has.lambda.gethumidity.db;

import com.broadway.has.lambda.gethumidity.response.HumidityResponse;
import org.joda.time.DateTime;

public interface DatabaseManager {


    HumidityResponse getHumidity(String nodeId, DateTime startDate, DateTime endDate, Integer limit) throws Exception;


}
