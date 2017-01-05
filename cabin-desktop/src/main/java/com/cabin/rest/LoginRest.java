package com.cabin.rest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.cabin.entity.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class LoginRest extends BaseRest {
	
	final static Logger logger = Logger.getLogger(LoginRest.class);
    
    private static final Long PROFILE_CLIENT_ID = 3L;

    public com.cabin.entity.Client login(String email, String password) {
        ClientConfig clientConfig = new DefaultClientConfig();

        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

        String uri = getHost() + "/post/loginClient";
        User user = new User();
        user.setName(email);
        user.setPass(password);
        user.setProfileId(PROFILE_CLIENT_ID);
        
        logger.info("uri login: " + uri);

        WebResource webResource = Client.create(clientConfig).resource(uri);
        ClientResponse response = webResource.accept("application/json").type("application/json").post(ClientResponse.class, user);

        if (response.getStatus() != 200) {
        	logger.error("error 200");
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        String output = response.getEntity(String.class);

        logger.info("output login: " + output);

        try {
            ObjectMapper mapper = new ObjectMapper();
            com.cabin.entity.Client client = mapper.readValue(output, com.cabin.entity.Client.class);
            System.out.println("client: " + client);
            return client;
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }
}
