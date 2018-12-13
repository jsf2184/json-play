package com.jsf2184.json.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jsf2184.json.utility.jackson.JacksonUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class JacksonTreeAndNodeTests {

    @Test
    public void testCreateJsonStringForCity() {
        JsonNode naperville = createCityNode("Naperville", 140000);
        System.out.println(JacksonUtil.toJsonString(false, naperville));
    }

    @Test
    public void testCreatePrettyJsonStringForCity() {
        JsonNode naperville = createCityNode("Naperville", 140000);
        System.out.println(JacksonUtil.toJsonString(true, naperville));
    }

    @Test
    public void testCreateArrayNodeOfCities() {
        ArrayNode cityList = JacksonUtil.createArrayNode();
        addCitiesToArrayNode(cityList);
        System.out.println(JacksonUtil.toJsonString(true, cityList));
    }

    @Test
    public void testFindCapitalPopulationInTreeModel() {
        JsonNode state = createState();
        Integer capitalPopulation = findCapitalPopulationInTreeModel(state);
        Assert.assertEquals((Integer) 115000, capitalPopulation);
    }

    @Test
    public void testParseToTreeRepresentation() throws IOException {
        JsonNode state1 = createState();
        String state1Str =  JacksonUtil.toJsonString(true, state1);
        JsonNode state2 = JacksonUtil.readTree(state1Str);
        Assert.assertEquals(state1, state2);
        String state2Str =  JacksonUtil.toJsonString(true, state2);
        Assert.assertEquals(state1Str, state2Str);
        Integer capitalPopulation = findCapitalPopulationInTreeModel(state2);
        Assert.assertEquals((Integer) 115000, capitalPopulation);
    }

    @Test
    public void testStateCreationWithTreeModel() {
        JsonNode state = createState();
        System.out.println(JacksonUtil.toJsonString(true, state));
    }


    public Integer findCapitalPopulationInTreeModel(JsonNode state) {
        String capital = JacksonUtil.getStringMember(state, "capital");
        ArrayNode cities = (ArrayNode) state.get("cities");
        for (JsonNode cityNode : cities) {
            String cityName = JacksonUtil.getStringMember(cityNode, "name");
            if (cityName.equals(capital)) {
                return JacksonUtil.getIntMember(cityNode, "population");
            }
        }
        return null;
    }


    public JsonNode createState() {
        ObjectNode state = JacksonUtil.createObjectNode();
        ArrayNode cities = state.putArray("cities");
        addCitiesToArrayNode(cities);
        state.put("nickname", "Land of Lincoln");
        state.put("capital", "Springfield");
        return state;
    }

    public ArrayNode addCitiesToArrayNode(ArrayNode arrayNode) {
        addCityNodeToArrayNode("Naperville", 140000, arrayNode);
        addCityNodeToArrayNode("Chicago", 2400000, arrayNode);
        addCityNodeToArrayNode("Rockford", 95000, arrayNode);
        addCityNodeToArrayNode("Springfield", 115000, arrayNode);
        return arrayNode;
    }

    public static void addCityNodeToArrayNode(String name, int population, ArrayNode array) {
        JsonNode city = createCityNode(name, population);
        array.add(city);
    }

    public static JsonNode createCityNode(String name, int population) {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        objectNode.put("name", name);
        objectNode.put("population", population);
        return objectNode;
    }

}
