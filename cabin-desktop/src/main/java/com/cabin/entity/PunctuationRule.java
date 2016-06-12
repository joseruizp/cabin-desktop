package com.cabin.entity;

public class PunctuationRule {
	private Long id;
	private String name;
	private Double rechargingFraction;
	private Integer points;
	private Status status;
	private Level level;
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRechargingFraction() {
		return rechargingFraction;
	}

	public void setRechargingFraction(Double rechargingFraction) {
		this.rechargingFraction = rechargingFraction;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	 public Status getStatus() {
	    return status;
	 }

	 public void setStatus(Status status) {
	    this.status = status;
	 }

}
