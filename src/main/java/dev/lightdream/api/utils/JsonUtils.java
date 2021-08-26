package dev.lightdream.api.utils;

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
        JsonObject jsonObject = gson.fromJson(this.data, JsonObject.class);
        if (jsonObject == null) {
            return "";
        }
        JsonElement jsonElement = jsonObject.get(attribute);
        if (jsonElement == null) {
            return "";
        }
        return jsonElement.getAsString();
    }

    public double getDouble(String attribute) {
        JsonObject jsonObject = gson.fromJson(this.data, JsonObject.class);
        if (jsonObject == null) {
            return 0;
        }
        JsonElement jsonElement = jsonObject.get(attribute);
        if (jsonElement == null) {
            return 0;
        }
        return jsonElement.getAsDouble();
    }

    public List<String> getStringList(String attribute) {
        List<String> output = new ArrayList<>();
        JsonObject jsonObject = gson.fromJson(this.data, JsonObject.class);
        if (jsonObject == null) {
            return output;
        }
        JsonElement jsonElement = jsonObject.get(attribute);
        if (jsonElement == null) {
            return output;
        }
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            output.add(element.getAsString());
        }
        return output;
    }

    public List<Integer> getIntList(String attribute) {
        List<Integer> output = new ArrayList<>();
        JsonObject jsonObject = gson.fromJson(this.data, JsonObject.class);
        if (jsonObject == null) {
            return output;
        }
        JsonElement jsonElement = jsonObject.get(attribute);
        if (jsonElement == null) {
            return output;
        }
        for (JsonElement element : jsonElement.getAsJsonArray()) {
            output.add(element.getAsInt());
        }
        return output;
    }


}
