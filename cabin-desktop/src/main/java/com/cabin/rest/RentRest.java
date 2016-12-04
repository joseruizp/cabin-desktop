package com.cabin.rest;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class RentRest extends BaseRest {

    public Long startRentComputer(Long clientId, Long computerId) {
        ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);

        String uri = getHost() + "/put/startRentComputer";
        uri += "?client_id=" + clientId;
        uri += "&computer_id=" + computerId;

        WebResource webResource = client.resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").put(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Server response .... \n");
        System.out.println("output start rent: " + output);

        return Long.parseLong(output);
    }

    public void endRentComputer(Long rentId, String rentTime, String price) {
        ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);

        String uri = getHost() + "/put/endRentComputer";
        uri += "?rent_id=" + rentId;
        uri += "&rent_time=" + rentTime;
        uri += "&price=" + price;

        WebResource webResource = client.resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").put(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Server response .... \n");
        System.out.println("output end rent: " + output);
    }

    public com.cabin.entity.Client exchangePoints(Long rentId, String bonusPoints) {
        ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);

        String uri = getHost() + "/put/exchangePoints";
        uri += "?rent_id=" + rentId;
        uri += "&bonusPoints=" + bonusPoints;

        WebResource webResource = client.resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").put(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Server response .... \n");
        System.out.println("output exchange: " + output);

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(output, com.cabin.entity.Client.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
