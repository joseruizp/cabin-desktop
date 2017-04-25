package com.cabin.entity;

import com.cabin.common.TimerUtil;

public class FormInformation {

    private Client client;
    private Computer computer;
    private Double tariff;
    private Long rentId;
    private NextBonus nextBonus;

    public FormInformation(Long rentId, Client client, Computer computer, Double tariff, NextBonus nextBonus) {
        super();
        this.rentId = rentId;
        this.client = client;
        this.computer = computer;
        this.tariff = tariff;
        this.nextBonus = nextBonus;
    }

    public Long getRentId() {
        return rentId;
    }

    public Client getClient() {
        return client;
    }

    public Computer getComputer() {
        return computer;
    }

    public Double getTariff() {
        return tariff;
    }
    
    public NextBonus getNextBonus() {
		return nextBonus;
	}

    public void updateBalance(String remainingTime) {
        this.client.setBalance(TimerUtil.getBalance(remainingTime, tariff));
    }

}
