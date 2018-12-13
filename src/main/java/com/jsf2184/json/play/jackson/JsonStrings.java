package com.jsf2184.json.play.jackson;

public class JsonStrings {
    public static final String simpleCityStr = "{\"name\":\"Naperville\",\"population\":140000}";
    public static final String cityStrWithExtra =
            "{" +
                    "\"name\":\"Naperville\"," +
                    "\"mayor\":\"Chirico\"," +
                    "\"schools\":" +
                    "[" +
                        " {\n" +
                        "  \"name\" : \"North\",\n" +
                        "  \"mascot\" : \"Huskie\"\n" +
                        "}," +
                        "{\n" +
                        "  \"name\" : \"Central\",\n" +
                        "  \"mascot\" : \"RedHawk\"\n" +
                        "}" +
                    " ],\n" +
                    "\"population\":140000" +
            "}";

    public static final String simpleStateStr = "{\n" +
            "  \"cities\" : [ {\n" +
            "    \"name\" : \"Naperville\",\n" +
            "    \"population\" : 140000\n" +
            "  }, {\n" +
            "    \"name\" : \"Chicago\",\n" +
            "    \"population\" : 2400000\n" +
            "  }, {\n" +
            "    \"name\" : \"Rockford\",\n" +
            "    \"population\" : 95000\n" +
            "  }, {\n" +
            "    \"name\" : \"Springfield\",\n" +
            "    \"population\" : 115000\n" +
            "  } ],\n" +
            "  \"nickname\" : \"Land of Lincoln\",\n" +
            "  \"capital\" : \"Springfield\"\n" +
            "}\n";
}
