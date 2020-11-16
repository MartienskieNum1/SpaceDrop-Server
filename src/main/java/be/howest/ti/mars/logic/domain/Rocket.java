package be.howest.ti.mars.logic.domain;

import java.time.format.DateTimeFormatter;

public class Rocket {
    private String name;
    private DateTimeFormatter departure;
    private DateTimeFormatter arrival;
    private float pricePerKilo;
    private float maxMass;
    private float maxVolume;
    private float availableMass;
    private float availableVolume;

    public Rocket(String name, DateTimeFormatter departure, DateTimeFormatter arrival, float pricePerKilo, float maxMass,
                  float maxVolume) {
        this.name = name;
        this.departure = departure;
        this.arrival = arrival;
        this.pricePerKilo = pricePerKilo;
        this.maxMass = maxMass;
        this.maxVolume = maxVolume;
        this.availableMass = maxMass;
        this.availableVolume = maxVolume;
    }

    public String getName() {
        return name;
    }

    public DateTimeFormatter getDeparture() {
        return departure;
    }

    public DateTimeFormatter getArrival() {
        return arrival;
    }

    public float getPricePerKilo() {
        return pricePerKilo;
    }

    public float getMaxMass() {
        return maxMass;
    }

    public float getMaxVolume() {
        return maxVolume;
    }

    public float getAvailableMass() {
        return availableMass;
    }

    public void setAvailableMass(float availableMass) {
        this.availableMass = availableMass;
    }

    public float getAvailableVolume() {
        return availableVolume;
    }

    public void setAvailableVolume(float availableVolume) {
        this.availableVolume = availableVolume;
    }
}
