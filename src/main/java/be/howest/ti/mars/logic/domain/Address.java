package be.howest.ti.mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Address {
    private String planet;
    private String countryOrColony;
    private String cityOrDistrict;
    private String street;
    private int number;

    @JsonCreator
    public Address(@JsonProperty("planet") String planet, @JsonProperty("countryOrColony") String countryOrColony,
                   @JsonProperty("cityOrDistrict") String cityOrDistrict, @JsonProperty("street") String street, @JsonProperty("number") int number) {
        this.planet = planet;
        this.countryOrColony = countryOrColony;
        this.cityOrDistrict = cityOrDistrict;
        this.street = street;
        this.number = number;
    }

    public String getPlanet() {
        return planet;
    }

    public String getCountryOrColony() {
        return countryOrColony;
    }

    public String getCityOrDistrict() {
        return cityOrDistrict;
    }

    public String getStreet() {
        return street;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Address{" +
                "planet='" + planet + '\'' +
                ", countryOrColony='" + countryOrColony + '\'' +
                ", cityOrDistrict='" + cityOrDistrict + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return number == address.number &&
                planet.equals(address.planet) &&
                countryOrColony.equals(address.countryOrColony) &&
                cityOrDistrict.equals(address.cityOrDistrict) &&
                street.equals(address.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planet, countryOrColony, cityOrDistrict, street, number);
    }
}
