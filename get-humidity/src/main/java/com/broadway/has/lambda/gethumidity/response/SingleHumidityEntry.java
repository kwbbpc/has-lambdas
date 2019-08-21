package com.broadway.has.lambda.gethumidity.response;


import java.io.IOException;


import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = SingleHumidityEntry.CustomSerializer.class)
public class SingleHumidityEntry implements HumidityEntry {


    private static final Logger logger = LoggerFactory.getLogger(HumidityEntry.class);
    
    private final DateTime indexedDate;
    private final DateTime timestamp;

    private final float humidity;
    private final String units;


    public SingleHumidityEntry(DateTime indexedDate, DateTime timestamp, float humidity, String units) {
        this.indexedDate = indexedDate;
        this.timestamp = timestamp;
        this.humidity = humidity;
        this.units = units;
    }
    
    public String getIndexedDate() {
        return indexedDate.toDateTimeISO().toString();
    }
    
    public String getTimestamp() {
        return timestamp.toDateTimeISO().toString();
    }

    public float getHumidity() {
        return humidity;
    }

    public String getUnits() {
        return units;
    }
    
    public class CustomSerializer extends JsonSerializer<SingleHumidityEntry> {


        @Override
        public void serialize(SingleHumidityEntry value, JsonGenerator jgen,
                              SerializerProvider arg2)
            throws IOException, JsonProcessingException {
/*
        	jgen.writeStartObject();
            jgen.writeStringField("temperature", Float.toString(value.getTemperature()));
            jgen.writeStringField("units", value.getUnits());
            jgen.writeStringField("indexedDate", value.getIndexedDate().toDateTimeISO().toString());
            jgen.writeStringField("timestamp", value.getTimestamp().toDateTimeISO().toString());



            jgen.writeEndObject();
  */      	
        }
    }
}
















