package dev.lightdream.plugin.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private final Gson gson;
    private final String data;

    public JsonUtils(String data) {
        this.gson = new Gson();
        this.data = data;
    }

    public String getString(String attribute) {
        return gson.fromJson(this.data, JsonObject.class).get(attribute).getAsString();
    }

    public double getDouble(String attribute) {
        return gson.fromJson(this.data, JsonObject.class).get(attribute).getAsDouble();
    }

    public List<String> getStringList(String attribute) {
        List<String> output = new ArrayList<>();
        for (JsonElement element : gson.fromJson(this.data, JsonObject.class).get(attribute).getAsJsonArray()) {
            output.add(element.getAsString());
        }
        return output;
    }

    public List<Integer> getIntList(String attribute) {
        List<Integer> output = new ArrayList<>();
        for (JsonElement element : gson.fromJson(this.data, JsonObject.class).get(attribute).getAsJsonArray()) {
            output.add(element.getAsInt());
        }
        return output;
    }


}
