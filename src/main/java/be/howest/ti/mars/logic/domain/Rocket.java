package be.howest.ti.mars.logic.domain;

import java.util.Objects;

public class Rocket {
    private final String name;
    private final String departure;
    private final String arrival;
    private final float pricePerKilo;
    private final float maxMass;
    private final float maxVolume;
    private float availableMass;
    private float availableVolume;

    public Rocket(String name, String departure, String arrival, float pricePerKilo, float maxMass,
                  float maxVolume, float availableMass, float availableVolume) {
        this.name = name;
        this.departure = departure;
        this.arrival = arrival;
        this.pricePerKilo = pricePerKilo;
        this.maxMass = maxMass;
        this.maxVolume = maxVolume;
        this.availableMass = availableMass;
        this.availableVolume = availableVolume;
    }

    public String getName() {
        return name;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
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

    @Override
    public String toString() {
        return "Rocket{" +
                "name='" + name + '\'' +
                ", departure=" + departure +
                ", arrival=" + arrival +
                ", pricePerKilo=" + pricePerKilo +
                ", maxMass=" + maxMass +
                ", maxVolume=" + maxVolume +
                ", availableMass=" + availableMass +
                ", availableVolume=" + availableVolume +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rocket rocket = (Rocket) o;
        return name.equals(rocket.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
