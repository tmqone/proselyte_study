package com.tmq.util;

import com.tmq.exception.GeneralException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream resourceAsStream = PropertiesUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")){
            PROPERTIES.load(resourceAsStream);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }
}
