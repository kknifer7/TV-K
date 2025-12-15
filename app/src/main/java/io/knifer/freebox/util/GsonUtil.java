package io.knifer.freebox.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

/**
 * JSON工具类
 *
 * @author Knifer
 * @version 1.0.0
 */
public class GsonUtil {

    private final static Gson gson = new GsonBuilder().create();

    public static String toJson(Object object){
        return gson.toJson(object);
    }

    public static <T> T fromJson(String objectStr, Class<T> clazz){
        return gson.fromJson(objectStr, clazz);
    }

    public static <T> T fromJson(JsonElement jsonElement, Class<T> clazz) {
        return gson.fromJson(jsonElement, clazz);
    }

    public static <T> T fromJson(String objectStr, TypeToken<T> type) {
        return gson.fromJson(objectStr, type.getType());
    }

    public static <T> T fromJson(JsonElement jsonElement, TypeToken<T> type) {
        return gson.fromJson(jsonElement, type.getType());
    }

    public JsonElement toJsonTree(Object object) {
        return gson.toJsonTree(object);
    }

}
