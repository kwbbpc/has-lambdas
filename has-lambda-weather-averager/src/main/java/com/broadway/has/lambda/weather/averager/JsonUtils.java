package com.broadway.has.lambda.weather.averager;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import jdk.nashorn.internal.ir.ObjectNode;

public class JsonUtils {

    public static final ObjectMapper MAPPER = initiateJacksonMapper();

    private static ObjectMapper initiateJacksonMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

}
