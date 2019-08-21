package com.broadway.has.lambda.gettemperatures.response;


import java.io.IOException;


import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.broadway.has.lambda.gettemperatures.JsonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


//@JsonSerialize(using = SingleTemperatureEntry.CustomSerializer.class)
public class SingleTemperatureEntry implements TemperatureEntry{


    private static final Logger logger = LoggerFactory.getLogger(TemperatureEntry.class);
    
    private final DateTime indexedDate;
    private final DateTime timestamp;

    private final float temperature;
    private final String units;


    public SingleTemperatureEntry(DateTime indexedDate, DateTime timestamp, float temperature, String units) {
        this.indexedDate = indexedDate;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.units = units;
    }
    
    public String getIndexedDate() {
        return indexedDate.toDateTimeISO().toString();
    }
    
    public String getTimestamp() {
        return timestamp.toDateTimeISO().toString();
    }

    public float getTemperature() {
        return temperature;
    }

    public String getUnits() {
        return units;
    }

}
















