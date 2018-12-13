package com.jsf2184.json.gson;

import com.google.gson.*;
import com.jsf2184.json.play.jackson.JsonStrings;
import com.jsf2184.json.utility.jackson.JacksonUtil;
import com.jsf2184.model.City;
import com.jsf2184.model.State;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class GsonTests {


    @Test
    public void testCreateJsonStringForCity() {
        JsonObject jsonObject = createCity("Naperville", 140000);
        System.out.println( toString(false, jsonObject));
    }

    @Test
    public void testCreatePrettyJsonStringForCity() {
        JsonObject jsonObject = createCity("Naperville", 140000);
        System.out.println( toString(true, jsonObject));
    }

    @Test
    public void testJsonArrayOfCities() {
        JsonArray jsonArray = createCityList();
        String arrayStr = toString(true, jsonArray);
        System.out.println(arrayStr);
    }

    @Test
    public void testHolderOfCities() {
        JsonObject state = createState();
        System.out.println(toString(true, state));
    }

    @Test
    public void testFindCapital() {
        JsonObject state = createState();
        Integer capitalPopulation = findCapitalPopulation(state);
        Assert.assertEquals((Integer) 115000, capitalPopulation);
    }

    @Test
    public void testParse() {
        JsonObject state1 = createState();
        String state1Str =  toString(true, state1);
        JsonParser jsonParser = new JsonParser();
        JsonObject state2 = (JsonObject) jsonParser.parse(state1Str);
        Assert.assertEquals(state1, state2);
        String state2Str =  toString(true, state2);
        Assert.assertEquals(state1Str, state2Str);
        Integer capitalPopulation = findCapitalPopulation(state2);
        Assert.assertEquals((Integer) 115000, capitalPopulation);
    }

    @Test
    public void testObjConversion() {
        JsonObject jsonState = createState();
        String stateStr =  toString(true, jsonState);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        State state = gson.fromJson(stateStr, State.class);
        String stateStr2 = gson.toJson(state);
        Assert.assertEquals(stateStr, stateStr2);
    }

    @Test
    public void testParseWithObjectMapperOfCityWithExtraFields() throws IOException {
        String cityStrWithExtra = JsonStrings.cityStrWithExtra;
        System.out.println(cityStrWithExtra);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        City city = gson.fromJson(cityStrWithExtra, City.class);
        Assert.assertEquals(new City("Naperville", 140000), city);
    }


    public Integer findCapitalPopulation(JsonObject state) {
        String capital = getStringMember(state, "capital");
        JsonArray cities = (JsonArray) state.get("cities");
        for (Object cityObj : cities) {
            JsonObject city = (JsonObject) cityObj;
            String cityName = getStringMember(city, "name");
            if (cityName.equals(capital)) {
                return getIntMember(city, "population");
            }
        }
        return null;
    }

    public static String getStringMember(JsonObject jsonObject, String fieldName) {
        JsonElement jsonElement = jsonObject.get(fieldName);
        String res = jsonElement.getAsString();
        return res;
    }

    public static int getIntMember(JsonObject jsonObject, String fieldName) {
        JsonElement jsonElement = jsonObject.get(fieldName);
        int res = jsonElement.getAsInt();
        return res;
    }



    public static String toString(boolean pretty, JsonElement jsonObject) {
        if (pretty) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String res = gson.toJson(jsonObject);
            return res;
        }
        return jsonObject.toString();
    }

    public JsonObject createState() {
        JsonArray cityList = createCityList();
        JsonObject state = new JsonObject();
        state.add("cities", cityList);
        state.addProperty("nickname", "Land of Lincoln");
        state.addProperty("capital", "Springfield");
        return state;
    }

    public JsonArray createCityList() {
        JsonArray res = new JsonArray();
        addCityToArray("Naperville", 140000, res);
        addCityToArray("Chicago", 2400000, res);
        addCityToArray("Rockford", 95000, res);
        addCityToArray("Springfield", 115000, res);
        return res;
    }

    public static void addCityToArray(String name, int population, JsonArray array) {
        JsonObject city = createCity(name, population);
        array.add(city);
    }

    public static JsonObject createCity(String name, int population) {
        JsonObject res = new JsonObject();
        res.addProperty("name", name);
        res.addProperty("population", population);
        return res;
    }
}
