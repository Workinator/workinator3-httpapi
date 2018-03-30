package com.allardworks.workinator3.httpapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by jaya on 2/28/18.
 * k?
 */
public class JsonUtility {
    private final static ObjectMapper json = new ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    private JsonUtility() {
    }

    public static ObjectMapper json() {
        return json;
    }
}
