package com.cabin.entity;

import com.cabin.common.TimerUtil;

public class FormInformation {

    private Client client;
    private Computer computer;
    private Double tariff;
    private Long rentId;

    public FormInformation(Long rentId, Client client, Computer computer, Double tariff) {
        super();
        this.rentId = rentId;
        this.client = client;
        this.computer = computer;
        this.tariff = tariff;
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

    public void updateBalance(String remainingTime) {
        this.client.setBalance(TimerUtil.getBalance(remainingTime, tariff));
    }

}
