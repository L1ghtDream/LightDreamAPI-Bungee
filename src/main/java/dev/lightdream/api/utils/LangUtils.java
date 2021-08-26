package dev.lightdream.api.utils;

import lombok.SneakyThrows;

public class LangUtils {

    @SneakyThrows
    public static Class<?> getLang(Class<?> main, String lang) {
        return Class.forName(main.getPackage().getName() + ".files.config.lang." + lang);
    }
}


