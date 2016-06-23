package com.cabin.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class PrizesRuleRest {

	private static final String HOST_SERVICES = "http://localhost:8080/cabin-web/";

	public Double getBonification(long id, int points) {
		ClientConfig clientConfig = new DefaultClientConfig();

		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

		String uri = HOST_SERVICES + "/get/prizeByLevel";
		uri += "?id=" + id;
		uri += "&points=" + points;

		WebResource webResource = Client.create(clientConfig).resource(uri);
		ClientResponse response = webResource.accept("application/json").type("application/json")
				.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);

		return Double.parseDouble(output);
	}
}
