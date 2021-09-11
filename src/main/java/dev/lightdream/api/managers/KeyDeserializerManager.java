package dev.lightdream.api.managers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class KeyDeserializerManager extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) {
        return key;
    }
}


