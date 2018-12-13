package com.jsf2184.json.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.jsf2184.json.utility.jackson.JacksonUtil;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

// We can generate a JsonString directly without having to build a tree of objects using a
// JsonGenerator as is demonstrated in these tests.
//
public class JacksonJsonGeneratorTests {
    @Test
    public void testStateCreationWithJsonGenerator() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = JacksonUtil.createJsonStringGenerator(true, outputStream);
        createJsonStateString(jsonGenerator);
        jsonGenerator.close();
        String jsonStr = outputStream.toString();
        System.out.println(jsonStr);
    }

    public static void createJsonStateString(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartObject();
        addCitiesToJsonString(jsonGenerator);
        jsonGenerator.writeStringField("nickname", "Land of Lincoln");
        jsonGenerator.writeStringField("capital", "Springfield");
        jsonGenerator.writeEndObject();
    }


    public static void addCitiesToJsonString(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeArrayFieldStart("cities");
        addCityToJsonString("Naperville", 140000, jsonGenerator);
        addCityToJsonString("Chicago", 2400000, jsonGenerator);
        addCityToJsonString("Rockford", 95000, jsonGenerator);
        addCityToJsonString("Springfield", 115000, jsonGenerator);
        jsonGenerator.writeEndArray();
    }


    public static void addCityToJsonString(String name, int population, JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", name);
        jsonGenerator.writeNumberField("population", population);
        jsonGenerator.writeEndObject();
    }

}

