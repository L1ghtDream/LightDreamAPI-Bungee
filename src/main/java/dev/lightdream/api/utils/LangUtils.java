package dev.lightdream.api.utils;

import lombok.SneakyThrows;

public class LangUtils {

    @SneakyThrows
    public static Class<?> getLang(String lang) {
        return Class.forName(lang);
    }
    /*
    dev.lightdream.api.files.config.local.lang.en_us
    dev.lightdream.api.files.local.lang.en_us
     */

}
