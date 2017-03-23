package com.cabin.rest;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class ClientRest extends BaseRest {

    public com.cabin.entity.Client changeLevel(Long rentId, String changeLevel) {
    	ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);

        String uri = getHost() + "/put/changeLevel";
        uri += "?rent_id=" + rentId;
        uri += "&change_level=" + changeLevel;
        
        System.out.println("URL: "+ uri);
        
        WebResource webResource = client.resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").put(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Server response .... \n");
        System.out.println("output update change level: " + output);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(output, com.cabin.entity.Client.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public com.cabin.entity.Client changeBonification(Long rentId, String bonus) {
    	ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);

        String uri = getHost() + "/put/changeBonification";
        uri += "?rent_id=" + rentId;        
        uri += "&bonus=" + bonus;
        
        System.out.println("URL: " + uri);
        
        WebResource webResource = client.resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").put(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Server response .... \n");
        System.out.println("output update change level: " + output);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(output, com.cabin.entity.Client.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    public Double getBonification(Long bonusId) {
    	ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        Client client = Client.create(clientConfig);

        String uri = getHost() + "/get/bonification";
        uri += "?bonus_id=" + bonusId;
        
        System.out.println("URL: " + uri);
        
        WebResource webResource = client.resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        System.out.println("Server response .... \n");
        System.out.println("output update change level: " + output);        
        
        return Double.parseDouble(output);
    }


}
