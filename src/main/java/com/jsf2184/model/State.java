package com.jsf2184.model;

import com.jsf2184.model.City;

import java.util.List;

public class State {
    public List<City> cities;
    public String nickname;
    public String capital;

    public State() {
    }

    public State(List<City> cities, String nickname, String capital) {
        this.cities = cities;
        this.nickname = nickname;
        this.capital = capital;
    }
}
