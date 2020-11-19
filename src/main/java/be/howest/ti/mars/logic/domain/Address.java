package be.howest.ti.mars.logic.domain;

import java.util.Objects;

public class Address {
    private String planet;
    private String countryOrColony;
    private String cityOrDistrict;
    private String street;
    private int number;

    public Address(String planet, String countryOrColony, String cityOrDistrict, String street, int number) {
        this.planet = planet;
        this.countryOrColony = countryOrColony;
        this.cityOrDistrict = cityOrDistrict;
        this.street = street;
        this.number = number;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public String getCountryOrColony() {
        return countryOrColony;
    }

    public void setCountryOrColony(String countryOrColony) {
        this.countryOrColony = countryOrColony;
    }

    public String getCityOrDistrict() {
        return cityOrDistrict;
    }

    public void setCityOrDistrict(String cityOrDistrict) {
        this.cityOrDistrict = cityOrDistrict;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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
