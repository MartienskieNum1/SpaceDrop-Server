package be.howest.ti.mars.logic.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Role {
    private String name;
    private int rank;

    @JsonCreator
    public Role(@JsonProperty("name") String name, @JsonProperty("rank") int rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", rank=" + rank +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return rank == role.rank &&
                name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rank);
    }
}
