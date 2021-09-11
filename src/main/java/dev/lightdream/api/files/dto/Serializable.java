package dev.lightdream.api.files.dto;

import java.util.List;

public interface Serializable {

    String toString();

    Object deserialize(String serialized);

}
