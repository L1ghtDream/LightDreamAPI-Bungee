package dev.lightdream.api.managers;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.utils.MessageBuilder;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class LangManager {

    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<Class<?>, HashMap<String, Object>> instances = new HashMap<>();

    public LangManager(LightDreamPlugin plugin, HashMap<String, ?> langs) {
        register(plugin, langs);
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public void register(LightDreamPlugin plugin, HashMap<String, ?> langs) {
        this.instances.put(plugin.getClass(), (HashMap<String, Object>) langs);
    }

    @SneakyThrows
    @Deprecated
    public String getString(LightDreamPlugin plugin, String string, String lang) {
        Method getFieldValue = instances.get(plugin.getClass()).get(lang).getClass().getMethod("getFieldValue", Field.class);
        Method getFieldName = instances.get(plugin.getClass()).get(lang).getClass().getMethod("getFieldName", String.class);
        Field field = (Field) getFieldName.invoke(instances.get(plugin.getClass()).get(lang).getClass().newInstance(), string);
        return (String) getFieldValue.invoke(instances.get(plugin.getClass()).get(lang), field);
    }

    @SneakyThrows
    public String getString(LightDreamPlugin plugin, MessageBuilder builder, String lang) {
        Method getFieldValue = instances.get(plugin.getClass()).get(lang).getClass().getMethod("getFieldValue", Field.class);
        Method getFieldName = instances.get(plugin.getClass()).get(lang).getClass().getMethod("getFieldName", String.class);
        Field field = (Field) getFieldName.invoke(instances.get(plugin.getClass()).get(lang).getClass().newInstance(), builder.getBase());
        return (String) getFieldValue.invoke(instances.get(plugin.getClass()).get(lang), field);
    }

}
