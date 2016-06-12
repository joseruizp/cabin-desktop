package com.cabin.rest;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.cabin.entity.PunctuationRule;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class PunctuationRuleRest {

	private static final String HOST_SERVICES = "http://localhost:8080/cabin-web/";

	public List<PunctuationRule> getPunctuations(long id) {
		ClientConfig clientConfig = new DefaultClientConfig();

		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

		String uri = HOST_SERVICES + "/get/punctationsByLevel";
		uri += "?id=" + id;

		WebResource webResource = Client.create(clientConfig).resource(uri);
		ClientResponse response = webResource.accept("application/json").type("application/json")
				.get(ClientResponse.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);

		System.out.println(output);

		try {
			ObjectMapper mapper = new ObjectMapper();
			List<PunctuationRule> punctuations = mapper.readValue(output,
					mapper.getTypeFactory().constructCollectionType(List.class, PunctuationRule.class));
			System.out.println(punctuations);
			return punctuations;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
