package com.jsf2184.json.utility.jackson;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.OutputStream;

public class JacksonUtil {
    private static ObjectMapper _objectMapper;
    private static JsonFactory _jsonFactory;
    private static ObjectWriter _prettyWriter;
    private static ObjectWriter _defaultWriter;

    static {
        _objectMapper = new ObjectMapper();
        _objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _prettyWriter = _objectMapper.writerWithDefaultPrettyPrinter();
        _defaultWriter = _objectMapper.writer();
        _jsonFactory = new JsonFactory();
    }

    public static JsonParser createParser(String jsonStr) throws IOException {
        JsonParser res = _jsonFactory.createParser(jsonStr);
        return res;
    }
    public static String parseString(JsonParser parser) throws IOException {
        JsonToken jsonToken = parser.nextToken();
        if (jsonToken == JsonToken.VALUE_NULL) {
            return null;
        }
        if (jsonToken.isScalarValue()) {
            String res = parser.getValueAsString();
            return res;
        }
        throw new RuntimeException(String.format("Expected field that could be converted to String, got: %s", jsonToken));
    }

    public static Integer parseInt(JsonParser parser) throws IOException {
        JsonToken jsonToken = parser.nextToken();
        if (jsonToken == JsonToken.VALUE_NULL) {
            return null;
        }
        if (jsonToken.isNumeric()) {
            int res = parser.getValueAsInt();
            return res;
        }
        if (jsonToken != JsonToken.VALUE_STRING) {
            throw new RuntimeException(String.format("Expected Int field, instead found: %s", jsonToken));
        }
        String strValue = parser.getValueAsString();
        Integer res;
        try {
            res = Integer.parseInt(strValue);
        } catch (Exception ignore) {
            throw new RuntimeException(String.format("Expected Int field, instead found: %s", strValue));
        }
        return res;
    }

    public static String toJsonString(boolean pretty, JsonNode jsonNode) {
        if (pretty) {
            String res = null;
            try {
                res = _prettyWriter.writeValueAsString(jsonNode);
            } catch (JsonProcessingException ignore) {
            }
            return res;
        }
        return jsonNode.toString();
    }

    public static String toJsonString(boolean pretty, Object obj) {
        String res = null;
        try {
            if (pretty) {
                res = _prettyWriter.writeValueAsString(obj);
            } else {
                res = _defaultWriter.writeValueAsString(obj);
            }
        } catch (JsonProcessingException ignore) {
        }
        return res;
    }

    public static ObjectNode createObjectNode() {
        ObjectNode res = _objectMapper.createObjectNode();
        return res;
    }

    public static ArrayNode createArrayNode() {
        ArrayNode res = _objectMapper.createArrayNode();
        return res;
    }

    public static JsonNode readTree(String jsonStr) throws IOException {
        JsonNode res = _objectMapper.readTree(jsonStr);
        return res;
    }

    public static <T> T readObject(String jsonStr, Class<T> valueType) throws IOException {
        T res = _objectMapper.readValue(jsonStr, valueType);
        return res;
    }

    public static String getStringMember(JsonNode jsonNode, String fieldName) {
        JsonNode element = jsonNode.get(fieldName);
        String res = element.asText();
        return res;
    }

    public static int getIntMember(JsonNode jsonNode, String fieldName) {
        JsonNode element = jsonNode.get(fieldName);
        int res = element.asInt();
        return res;
    }

    public static JsonGenerator createJsonStringGenerator(boolean pretty,
                                                          OutputStream outputStream) throws IOException
    {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator res = jsonFactory.createGenerator(outputStream);
        if (pretty) {
            res.useDefaultPrettyPrinter();
        }
        return res;
    }
}
