package config;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final String CONFIG_FILE = "config/config.properties";
    private Properties properties;

    public Config() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }
}
