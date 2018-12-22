package com.broadway.has.lambda.gettemperatures.request;

import java.util.HashMap;
import java.util.Map;

public class Resolution {

    public static Map<Integer, String> NameMap = new HashMap<Integer, String>(){{
        put(0, "Fine Grain");
        put(1, "Ten Minute");
        put(2, "Hour");
        put(3, "Day");
    }};




}
