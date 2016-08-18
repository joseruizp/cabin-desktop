package com.cabin.rest;

import org.codehaus.jackson.map.ObjectMapper;

import com.cabin.entity.Computer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class ComputerRest extends BaseRest {

    public Computer getComputer(long id) {
        ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        String uri = getHost() + "/get/computer";
        uri += "?id=" + id;

        WebResource webResource = Client.create(clientConfig).resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println(output);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Computer computer = mapper.readValue(output, Computer.class);
            System.out.println(computer);
            return computer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
