package com.broadway.has.lambda.gethumidity.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


//@JsonSerialize(using = HumidityResponse.Serializer.class)
public class HumidityResponse {

    private final String nodeId;
    private final String sensorType;
    private final Integer resolution;
    private List<HumidityEntry> humidity;

    private static final Logger logger = LoggerFactory.getLogger(HumidityResponse.class);

    public HumidityResponse(){
        this.nodeId = null;
        this.sensorType = null;
        this.resolution = null;
        this.humidity = new ArrayList<>();
    }

    public HumidityResponse(String nodeId, Integer resolution, String sensorType,
                            List<HumidityEntry> temperaturesToReturn) {
        this.nodeId = nodeId;
        this.resolution = resolution;
        this.sensorType = sensorType;
        this.humidity = temperaturesToReturn;
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

    public List<HumidityEntry> getHumidity() {
        return humidity;
    }

}


