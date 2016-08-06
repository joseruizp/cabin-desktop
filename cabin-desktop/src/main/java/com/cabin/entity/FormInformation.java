package com.cabin.entity;

public class FormInformation {

    private Client client;
    private Computer computer;
    private Double tariff;

    public FormInformation(Client client, Computer computer, Double tariff) {
        super();
        this.client = client;
        this.computer = computer;
        this.tariff = tariff;
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

}
