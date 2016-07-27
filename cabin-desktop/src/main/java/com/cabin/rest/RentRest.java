package com.cabin.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class RentRest {

    private static final String HOST_SERVICES = "http://localhost:8080/cabin-web/";

    public void rentComputer(Long clientId, Long computerId, String rentTime, String price, String points) {
        ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);

        String uri = HOST_SERVICES + "/put/rentComputer";
        uri += "?client_id=" + clientId;
        uri += "&computer_id=" + computerId;
        uri += "&rent_time=" + rentTime;
        uri += "&price=" + price;
        if (points != null && !points.isEmpty()) {
            uri += "&bonusPoints=" + points;
        }

        WebResource webResource = client.resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").put(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Server response .... \n");
        System.out.println(output);
    }
}
