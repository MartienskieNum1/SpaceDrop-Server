package be.howest.ti.mars.logic.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Rocket {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private final String name;
    private final Date departure;
    private final Date arrival;
    private final float pricePerKilo;
    private final float maxMass;
    private final float maxVolume;
    private float availableMass;
    private float availableVolume;

    public Rocket(String name, String departure, String arrival, float pricePerKilo, float maxMass,
                  float maxVolume) throws ParseException {
        this.name = name;
        this.departure = format.parse(departure);
        this.arrival = format.parse(arrival);
        this.pricePerKilo = pricePerKilo;
        this.maxMass = maxMass;
        this.maxVolume = maxVolume;
        this.availableMass = maxMass;
        this.availableVolume = maxVolume;
    }

    public String getName() {
        return name;
    }

    public Date getDeparture() {
        return departure;
    }

    public Date getArrival() {
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
