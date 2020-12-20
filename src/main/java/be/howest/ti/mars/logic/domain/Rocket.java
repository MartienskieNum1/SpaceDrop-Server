package be.howest.ti.mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Rocket {
    private final int id;
    private final String name;
    private final String departLocation;
    private final String departure;
    private final String arrival;
    private final float pricePerKilo;
    private final float maxMass;
    private final float maxVolume;
    private float availableMass;
    private float availableVolume;

    @JsonCreator
    public Rocket(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("departLocation") String departLocation, @JsonProperty("departure") String departure,
                  @JsonProperty("arrival") String arrival, @JsonProperty("pricePerKilo") float pricePerKilo, @JsonProperty("maxMass") float maxMass,
                  @JsonProperty("maxVolume") float maxVolume, @JsonProperty("availableMass") float availableMass, @JsonProperty("availableVolume") float availableVolume) {
        this.id = id;
        this.name = name;
        this.departLocation = departLocation;
        this.departure = departure;
        this.arrival = arrival;
        this.pricePerKilo = pricePerKilo;
        this.maxMass = maxMass;
        this.maxVolume = maxVolume;
        this.availableMass = availableMass;
        this.availableVolume = availableVolume;
    }

    public int getId() {
        return id;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public float getMaxMass() {
        return maxMass;
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

    public String getName() {
        return name;
    }

    public String getDepartLocation() {
        return departLocation;
    }

    public float getPricePerKilo() {
        return pricePerKilo;
    }

    public float getMaxVolume() {
        return maxVolume;
    }

    @Override
    public String toString() {
        return "Rocket{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", departLocation='" + departLocation + '\'' +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
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
        return id == rocket.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
