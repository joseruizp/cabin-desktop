package com.cabin.entity;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Level implements Serializable {

	private static final long serialVersionUID = 8629686850429438279L;

	private Long id;
	private Integer initialExperience;
	private Integer finalExperience;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getInitialExperience() {
		return initialExperience;
	}

	public void setInitialExperience(Integer initialExperience) {
		this.initialExperience = initialExperience;
	}

	public Integer getFinalExperience() {
		return finalExperience;
	}

	public void setFinalExperience(Integer finalExperience) {
		this.finalExperience = finalExperience;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Level [id=" + id + ", initialExperience=" + initialExperience + ", finalExperience=" + finalExperience
				+ "]";
	}

}
