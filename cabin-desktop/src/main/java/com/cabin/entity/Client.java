package com.cabin.entity;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Client implements Serializable {

	private static final long serialVersionUID = 3018012749173085005L;

	private Long id;
	private String email;
	private String name;
	private Double balance;
	private Integer points;
	private Integer experience;
	private Status status;
	private Level level;
	private String change_level;
	private Long id_bonification;
	private String bonus;
	
	public Long getId_bonification() {
		return id_bonification;
	}

	public void setId_bonification(Long id_bonification) {
		this.id_bonification = id_bonification;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getChange_level() {
		return change_level;
	}

	public void setChange_level(String change_level) {
		this.change_level = change_level;
	}

	public String getBonus() {
		return bonus;
	}

	public void setBonus(String bonus) {
		this.bonus = bonus;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", email=" + email + ", name=" + name + ", balance=" + balance + ", points="
				+ points + ", experience=" + experience + ", status=" + status + ", level=" + level + ", change_level=" + change_level + "], bonus=" + bonus + "]";
	}

}
