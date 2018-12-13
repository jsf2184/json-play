package com.jsf2184.json.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsf2184.json.play.jackson.JsonStrings;
import com.jsf2184.json.utility.jackson.JacksonUtil;
import com.jsf2184.model.City;
import com.jsf2184.model.School;
import com.jsf2184.model.State;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JacksonConvertJsonTextToObjectTests {



    @Test
    public void testSchoolsToJsonString() {
        List<School> schools = Arrays.asList(new School("North", "Huskie"), new School("Central", "RedHawk"));
        System.out.println(JacksonUtil.toJsonString(true, schools));
    }


    // Like GSON, can turn a json string representation of a State into a State object all at once.
    @Test
    public void testStringToStateObjWithObjectMapperWithNoExtraFields() throws IOException {
        State state = JacksonUtil.readObject(JsonStrings.simpleStateStr, State.class);
        String stateStr = JacksonUtil.toJsonString(true, state);
        Assert.assertEquals(stateStr, stateStr);
    }

    @Test
    public void testStringToStateObjWithObjectMapperWithExtraFields() throws IOException {
        State state = JacksonUtil.readObject(JsonStrings.cityStrWithExtra, State.class);
        String stateStr = JacksonUtil.toJsonString(true, state);
        Assert.assertEquals(stateStr, stateStr);
    }


    @Test
    public void testFieldByFieldStreamingParseOfCityWithNoExtraFields() throws IOException {
        String simpleCityStr = JsonStrings.simpleCityStr;
        System.out.println(simpleCityStr);
        JsonParser parser = JacksonUtil.createParser(simpleCityStr);
        parser.nextToken(); // advance to the first token.
        City city = parseCityFieldByField(parser);
        Assert.assertEquals(new City("Naperville", 140000), city);
    }

    @Test
    public void testFieldByFieldStreamingParseOfCityWithExtraFields() throws IOException {
        String cityStrWithExtra = JsonStrings.cityStrWithExtra;
        System.out.println(cityStrWithExtra);
        JsonParser parser = JacksonUtil.createParser(cityStrWithExtra);
        parser.nextToken(); // advance to the first token.
        City city = parseCityFieldByField(parser);
        Assert.assertEquals(new City("Naperville", 140000), city);
    }

    // Instead of pulling data from a String, an ObjectMapper can utilize a Parser
    // which has the nice effect of later other code can pull additional data out
    // of the parser.
    //
    @Test
    public void testParseWithObjectMapperOfCityWithNoExtraFields() throws IOException {
        String simpleCityStr = JsonStrings.simpleCityStr;
        System.out.println(simpleCityStr);
        JsonParser parser = JacksonUtil.createParser(simpleCityStr);
        City city = parseCityWithObjectMapper(parser);
        Assert.assertEquals(new City("Naperville", 140000), city);
    }

    @Test
    public void testParseWithObjectMapperOfCityWithExtraFields() throws IOException {
        String cityStrWithExtra = JsonStrings.cityStrWithExtra;
        System.out.println(cityStrWithExtra);
        JsonParser parser = JacksonUtil.createParser(cityStrWithExtra);
        City city = parseCityWithObjectMapper(parser);
        Assert.assertEquals(new City("Naperville", 140000), city);
    }

    @Test
    public void testStreamingParseToStateObjUsingCityFieldByFieldParsing() throws IOException {
        JsonParser parser = JacksonUtil.createParser(JsonStrings.simpleStateStr);
        // find token that starts outer object
        State stateObj = parseStringIntoStateObj(parser, JacksonConvertJsonTextToObjectTests::parseCityFieldByField);
        String stateStr = JacksonUtil.toJsonString(true, stateObj);
        System.out.println(stateStr);
    }

    @Test
    public void testStreamingParseToStateObjUsingCityObjectMapperParsing() throws IOException {
        JsonParser parser = JacksonUtil.createParser(JsonStrings.simpleStateStr);
        // find token that starts outer object
        State stateObj = parseStringIntoStateObj(parser, JacksonConvertJsonTextToObjectTests::parseCityWithObjectMapper);
        String stateStr = JacksonUtil.toJsonString(true, stateObj);
        System.out.println(stateStr);
    }

    interface ThrowingFunction<T, R> {
        R apply(T t) throws IOException;
    }

    public static State parseStringIntoStateObj(JsonParser parser,
                                                ThrowingFunction<JsonParser, City> cityParser) throws IOException
    {
        JsonToken jsonToken = parser.nextToken();
        if (jsonToken != JsonToken.START_OBJECT) {
            return null;
        }
        String nickName = null;
        String capital = null;
        List<City> cities = null;

        while (true) {
            jsonToken = parser.nextToken();
            if (jsonToken == null || jsonToken == JsonToken.END_OBJECT) {
                break;
            }
            if (jsonToken == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                switch (fieldName) {
                    case "cities":
                        cities = parseCities(parser, cityParser);
                        break;
                    case "nickname":
                        nickName = JacksonUtil.parseString(parser);
                        break;
                    case "capital":
                        capital = JacksonUtil.parseString(parser);
                        break;
                    default:
                        consumeUnexpectedValue(parser);

                }
            }
            if (nickName != null && capital != null && cities != null) {
                return  new State(cities, nickName, capital);
            }

        }
        return null;
    }

    public static List<City> parseCities(JsonParser parser,
                                         ThrowingFunction<JsonParser, City> cityParser) throws IOException
    {
        JsonToken token = parser.nextToken();
        assertToken(JsonToken.START_ARRAY, token);

        List<City> res = new ArrayList<>();
        while(true) {
            token = parser.nextToken();
            if (token == JsonToken.END_ARRAY) {
                return res;
            }
            City city = cityParser.apply(parser);
            res.add(city);
        }
    }

    public static City parseCityFieldByField(JsonParser parser) throws IOException {

        JsonToken jsonToken = parser.currentToken();

        assertToken(JsonToken.START_OBJECT, jsonToken);
        City city = new City();

        while (true) {
            jsonToken = parser.nextToken();
            if (jsonToken == JsonToken.NOT_AVAILABLE || jsonToken == JsonToken.END_OBJECT) {
                return city;
            }
            assertToken(JsonToken.FIELD_NAME, jsonToken);
            getCityField(city, parser);
        }
    }

    public static City parseCityWithObjectMapper(JsonParser parser) throws IOException {
        // create an ObjectMapper that is tolerant of extra/unknown fields
        ObjectMapper objectMapper =
                new ObjectMapper().
                configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        City city = objectMapper.readValue(parser, City.class);
        return city;
    }

    public static void getCityField(City city, JsonParser parser) throws IOException {
        // when we get here, we just extracted a field name. See if it is one that we expect.
        String fieldName = parser.currentName();
        switch(fieldName) {
            case "name":
                String name = JacksonUtil.parseString(parser);
                city.name = name;
                break;
            case "population":
                Integer population = JacksonUtil.parseInt(parser);
                city.population = population;
                break;

            default:
                // we need to consume whatever was associated with this unexpected field.
                consumeUnexpectedValue(parser);
        }
    }


    public static void consumeUnexpectedValue(JsonParser parser) throws IOException {
        // we got here because we encountered an unexpected field name.
        // we need to consume the unexpected value so start by getting the
        // token for that value.

        JsonToken jsonToken = parser.nextToken();
        if (jsonToken.isScalarValue()) {
            return;
        }
        if (jsonToken.isStructStart()) {
            ObjectMapper mapper = new ObjectMapper();
            TreeNode treeNode = mapper.readTree(parser);
            return;
        }
        throw new RuntimeException(String.format("consumeUnexpectedValue(): Unexpected token: %s", jsonToken));
    }





    public static void assertToken(JsonToken expected, JsonToken actual) {
        if (expected != actual) {
            throw new RuntimeException(String.format("Expected Token: %s, received token %s", expected, actual));
        }
    }

}
