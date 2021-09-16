package dev.lightdream.api.files.dto;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Data
@SuperBuilder
public abstract class Serializable {

    private static HashMap<Class<?>, Method> baseParseMethods = null;

    public Serializable() {
        if (Serializable.baseParseMethods != null) {
            return;
        }

        HashMap<Class<?>, Method> baseParseMethods = new HashMap<>();

        for (Method method : Double.class.getMethods()) {
            if (method.getName().equals("parseDouble")) {
                baseParseMethods.put(Double.class, method);
                break;
            }
        }

        for (Method method : Integer.class.getMethods()) {
            if (method.getName().equals("parseInt")) {
                baseParseMethods.put(Integer.class, method);
                break;
            }
        }

        for (Method method : Long.class.getMethods()) {
            if (method.getName().equals("parseLong")) {
                baseParseMethods.put(Long.class, method);
                break;
            }
        }

        for (Method method : Boolean.class.getMethods()) {
            if (method.getName().equals("valueOf")) {
                baseParseMethods.put(Long.class, method);
                break;
            }
        }

        Serializable.baseParseMethods = baseParseMethods;
    }

    public abstract String toString();

    @SuppressWarnings("unused")
    @SneakyThrows
    public Object deserialize(String serialized) {


        serialized = serialized.replace(getClass().getSimpleName(), "");
        serialized = "." + serialized + ".";
        serialized = serialized.replace(".{", "");
        serialized = serialized.replace("}.", "");

        String[] params = serialized.split(", ");
        HashMap<String, String> parameters = new HashMap<>();

        for (String param : params) {
            List<String> split = Arrays.asList(param.split("="));
            if (split.size() != 2) {
                continue;
            }
            parameters.put(split.get(0), split.get(1));
        }

        Object obj = this.getClass().getConstructor().newInstance();

        for (Field field : obj.getClass().getFields()) {
            if (!parameters.containsKey(field.getName())) {
                continue;
            }
            String parameter = parameters.get(field.getName());
            Object parsed = null;
            if (Serializable.baseParseMethods.containsKey(field.getType())) {
                parsed = Serializable.baseParseMethods.get(field.getType()).invoke(field.getType().newInstance(), parameter);
            } else {
                for (Method method : field.getType().getMethods()) {
                    if (method.getName().equals("deserialize")) {
                        parsed = method.invoke(field.getType().newInstance(), parameter);
                    }
                }
            }
            field.set(obj, parsed);
        }

        return obj;
    }
}
