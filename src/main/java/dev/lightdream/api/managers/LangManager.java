package dev.lightdream.api.managers;

import dev.lightdream.api.utils.MessageBuilder;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

@SuppressWarnings("CanBeFinal")
public class LangManager {

    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<Class<?>, HashMap<String, Object>> instances = new HashMap<>();

    public LangManager(Class<?> clazz, HashMap<String, ?> langs) {
        register(clazz, langs);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public void register(Class<?> clazz, HashMap<String, ?> langs) {
        this.instances.put(clazz, (HashMap<String, Object>) langs);
    }

    @SuppressWarnings({"JavaReflectionInvocation", "unused"})
    @SneakyThrows
    public Object getString(Class<?> clazz, MessageBuilder builder, String lang) {
        Method getFieldValue = instances.get(clazz).get(lang).getClass().getMethod("getFieldValue", Field.class);
        Method getFieldName = instances.get(clazz).get(lang).getClass().getMethod("getFieldName", String.class);
        Field field = (Field) getFieldName.invoke(instances.get(clazz).get(lang).getClass().newInstance(), builder.getBase());
        return getFieldValue.invoke(instances.get(clazz).get(lang), field);
    }

}
