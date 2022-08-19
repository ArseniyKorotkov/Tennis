package by.arsy.adapters;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesAdapter {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesAdapter() {
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        var resourceAsStream = PropertiesAdapter.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            PROPERTIES.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
