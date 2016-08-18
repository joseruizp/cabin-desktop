package com.cabin.rest;

import com.cabin.common.PropertiesLoader;

public class BaseRest {

    private static final String PROPERTIES_FILE_NAME = "datos.properties";

    protected PropertiesLoader propertiesLoader = new PropertiesLoader(PROPERTIES_FILE_NAME);

    protected String getHost() {
        return propertiesLoader.getValue("host_servicios");
    }
}
