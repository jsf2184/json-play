package com.jsf2184.model;

import java.util.Objects;

public class City {
    public String name;
    public int population;

    public City() {
    }

    public City(String name, int population) {
        this.name = name;
        this.population = population;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        City city = (City) o;
        return population == city.population &&
                Objects.equals(name, city.name);
    }

}
