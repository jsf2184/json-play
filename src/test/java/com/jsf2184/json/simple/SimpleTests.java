package com.jsf2184.json.simple;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class SimpleTests {

    @Test
    public void testCreateJsonStringForCity() {
        JSONObject jsonObject = createCity("Naperville", 140000);
        System.out.println(jsonObject.toJSONString());
    }



    @Test
    public void testJsonArrayOfCities() {
        JSONArray jsonArray = createCityList();
        String arrayStr = jsonArray.toJSONString();
        System.out.println(arrayStr);
    }

    @Test
    public void testHolderOfCities() {
        JSONObject state = createState();
        System.out.println(state.toJSONString());
        System.out.println(state.toString());

    }

    @Test
    public void testFindCapital() {
        JSONObject state = createState();
        Integer capitalPopulation = findCapitalPopulation(state);
        Assert.assertEquals((Integer) 115000, capitalPopulation);
    }

    @Test
    public void testParse() throws ParseException {
        JSONObject state1 = createState();
        String state1Str = state1.toJSONString();
        JSONParser jsonParser = new JSONParser();
        JSONObject state2 = (JSONObject) jsonParser.parse(state1Str);

        // At first was surprised that state1.equals(state2) returned false.
        // Eventually realized it was because the parser turned the
        // Integers in state1Str to Longs instead of Integers.
        //
        String state2Str = state2.toJSONString();
        Assert.assertEquals(state1Str, state2Str);
        Integer capitalPopulation = findCapitalPopulation(state2);
        Assert.assertEquals((Integer) 115000, capitalPopulation);
    }

    public Integer findCapitalPopulation(JSONObject state) {
        String capital = (String) state.get("capital");
        JSONArray cities = (JSONArray) state.get("cities");
        for (Object cityObj : cities) {
            JSONObject city = (JSONObject) cityObj;
            String cityName = (String) city.get("name");
            if (cityName.equals(capital)) {
                Object population = city.get("population");
                if (population instanceof Integer) {
                    return (Integer) population;
                } else if (population instanceof Long) {
                    return ((Long) population).intValue();
                }
                return null;
            }
        }
        return null;
    }

    public JSONObject createState() {
        JSONArray cityList = createCityList();
        JSONObject state = new JSONObject();
        state.put("cities", cityList);
        state.put("nickname", "Land of Lincoln");
        state.put("capital", "Springfield");
        return state;
    }



    public JSONArray createCityList() {
        JSONArray res = new JSONArray();
        addCityToArray("Naperville", 140000, res);
        addCityToArray("Chicago", 2400000, res);
        addCityToArray("Rockford", 95000, res);
        addCityToArray("Springfield", 115000, res);
        return res;
    }


    public static void addCityToArray(String name, int population, JSONArray array) {
        JSONObject city = createCity(name, population);
        array.add(city);
    }

    public static JSONObject createCity(String name, int population) {
        JSONObject res = new JSONObject();
        res.put("name", name);
        res.put("population", population);
        return res;
    }

}
