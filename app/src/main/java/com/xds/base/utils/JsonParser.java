package com.xds.base.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonParser {

    private static Gson gson = new Gson();

    static{
    }


    public static <T> T parseJsonObject(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

    public static <T> T parseJsonObject(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static <T> T[] parseJsonArray(String json, Class<T> clz) {
    	T[] result = gson.fromJson(json, new TypeToken<T[]>(){}.getType());
        return result;
    }
    


    public static <K,V> Map<K,V> parseJsonMap(String json, Class<K> keyType, Class<V> valueType) {
    	Map<K,V> result = gson.fromJson(json,
                new TypeToken<Map<K,V>>(){}.getType());
        return result;
    }


    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        }catch(Exception e){
            return "{}";
        }
    }

    private static class NumberTypeAdapter implements JsonSerializer<Number> {
        @Override
        public JsonElement serialize(Number src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    public static <T> List<T> parseJsonList(String json, Class<T> clz) throws Exception {
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        JsonElement element = parser.parse(json);
        JsonArray array = element.getAsJsonArray();
        List<T> data = new ArrayList<T>();
        for (JsonElement jo : array) {
            data.add(gson.fromJson(jo, clz));
        }
        return data;
    }

}
