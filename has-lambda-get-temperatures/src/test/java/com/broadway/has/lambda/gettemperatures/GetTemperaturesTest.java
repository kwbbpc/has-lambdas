package com.broadway.has.lambda.gettemperatures;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.broadway.has.lambda.gettemperatures.response.TemperatureResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class GetTemperaturesTest {

    private static Map<String, Object> input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
        input = JsonUtils.MAPPER.readValue("{\n" + 
        		"  \"startTime\": \"2018-08-20T15:16:51.524Z\",\n" + 
        		"  \"endTime\": \"2018-12-30T15:16:51.524Z\",\n" + 
        		"  \"nodeId\": \"0013A200406B8D09\",\n" + 
        		"  \"resolution\": \"0\"\n" + 
        		"}", Map.class);
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testGetTemperatures() throws JsonProcessingException {
        GetTemperatures handler = new GetTemperatures();
        Context ctx = createContext();

        TemperatureResponse output = handler.handleRequest(input, ctx);
        
        String test = JsonUtils.MAPPER.writeValueAsString(output);

        // TODO: validate output here if needed.
        Assert.assertEquals("Hello from Lambda!", output);
    }
}
