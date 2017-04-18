package com.cabin.rest;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class ParameterRest extends BaseRest {

    public Map<Long, Long> getConnectionData() {
        ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        String uri = getHost() + "/get/conectionParameters";

        WebResource webResource = Client.create(clientConfig).resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<Long, Long> parameters = mapper.readValue(output, mapper.getTypeFactory().constructMapType(Map.class, Long.class, Long.class));
            return parameters;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
