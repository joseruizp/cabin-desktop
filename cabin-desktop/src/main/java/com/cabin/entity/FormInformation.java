package com.cabin.entity;

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
        double hour = getHours(remainingTime);
        this.client.setBalance(hour * tariff);
    }
    
    private double getHours(String remainingTime) {
        double totalHours = 0.0;
        int hour = Integer.parseInt(remainingTime.split(":")[0]);
        totalHours += hour;
        int min = Integer.parseInt(remainingTime.split(":")[1]);
        if (min > 0) {
            totalHours += (min / 60.0);
        }
        return round(totalHours);
    }
    
    private static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        double factorValue = value * factor;
        long tmp = Math.round(factorValue);
        return (double) tmp / factor;
    }

}
