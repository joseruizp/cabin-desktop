package com.cabin.rest;

import com.cabin.entity.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.representation.Form;

public class LoginRest {

	private static final String HOST_SERVICES = "http://localhost:8080/cabin-web/";

	public User login(String email, String password) {
		ClientConfig clientConfig = new DefaultClientConfig();

		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

		Client client = Client.create(clientConfig);

		String uri = HOST_SERVICES + "/post/login";
		Form form = new Form();
		form.add("email", email);
		form.add("password", password);

		WebResource webResource = client.resource(uri);
		ClientResponse response = webResource.accept("application/json").type("application/json")
				.put(ClientResponse.class, form);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);

		System.out.println("Server response .... \n");
		System.out.println(output);

		return null;
	}
}
