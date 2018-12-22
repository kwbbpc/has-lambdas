package com.broadway.has.lambda.gettemperatures.response;

import com.broadway.has.lambda.gettemperatures.JsonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//@JsonSerialize(using = TemperatureResponse.Serializer.class)
public class TemperatureResponse {

    private final String nodeId;
    private final String sensorType;
    private final Integer resolution;
    private List<TemperatureEntry> temperatures;

    private static final Logger logger = LoggerFactory.getLogger(TemperatureResponse.class);

    public TemperatureResponse(){
        this.nodeId = null;
        this.sensorType = null;
        this.resolution = null;
        this.temperatures = new ArrayList<>();
    }

    public TemperatureResponse(String nodeId, Integer resolution, String sensorType,
                               List<TemperatureEntry> temperaturesToReturn) {
        this.nodeId = nodeId;
        this.resolution = resolution;
        this.sensorType = sensorType;
        this.temperatures = temperaturesToReturn;
    }

    public String getNodeId() {
        return nodeId;
    }


    public Integer getResolution() {
        return resolution;
    }

    public String getSensorType() {
        return sensorType;
    }

    public List<TemperatureEntry> getTemperatures() {
        return temperatures;
    }

}


