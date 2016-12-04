package com.cabin.rest;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.cabin.entity.Failure;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class FailureRest extends BaseRest {

    public List<Failure> getFailures() {
        ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        String uri = getHost() + "/get/allFailures";

        WebResource webResource = Client.create(clientConfig).resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Failure> failures = mapper.readValue(output, mapper.getTypeFactory().constructCollectionType(List.class, Failure.class));
            System.out.println("failures: " + failures);
            return failures;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void reportFailure(Long computerId, Long failureId) {
        ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        String uri = getHost() + "/put/report";
        uri += "?computerId=" + computerId;
        uri += "?falureId=" + failureId;

        WebResource webResource = Client.create(clientConfig).resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").put(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("failure: " + output);
    }
}
