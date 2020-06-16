package com.es.demo.constants;

public class ElasticSearchConstants {

    public static final String INDEX_NAME = "members";
    public static final String OP_TYPE = "create";
    public static final String AMENITIES = "amenities";
    public static final String AMENITY_ID ="amenities.id";
    public static final String MAPPING = "{\n" +
            "\t\"properties\": {\n" +
            "\t\t\"id\": {\n" +
            "\t\t\t\"type\": \"keyword\"\n" +
            "\t\t},\n" +
            "\t\t\"firstName\": {\n" +
            "\t\t\t\"type\": \"text\"\n" +
            "\t\t},\n" +
            "\t\t\"lastName\": {\n" +
            "\t\t\t\"type\": \"text\"\n" +
            "\t\t},\n" +
            "\t\t\"membershipType\": {\n" +
            "\t\t\t\"type\": \"text\"\n" +
            "\t\t},\n" +
            "\t\t\"amenities\": {\n" +
            "\t\t\t\"type\": \"nested\",\n" +
            "\t\t\t\"properties\": {\n" +
            "\t\t\t\t\"id\": {\n" +
            "\t\t\t\t\t\"type\": \"keyword\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"name\": {\n" +
            "\t\t\t\t\t\"type\": \"text\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\n" +
            "\t}\n" +
            "}";

}