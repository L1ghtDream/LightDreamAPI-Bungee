package dev.lightdream.api.files.dto;

import java.lang.reflect.Constructor;
import java.util.List;

public interface Serializable extends java.io.Serializable {

    String toString();

    Object deserialize(String serialized);

    Constructor<?> getMainConstructor();

    List<String> getParameterNames();

}
