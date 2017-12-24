package org.train.app;

import java.util.HashMap;
import java.util.Map;

import org.train.helper.FileHelper;
import org.train.loader.ConfigurationLoader;
import org.train.storer.ConfigurationStorer;

public class Configuration {

    private ConfigurationStorer configurationStorer;
    private ConfigurationLoader configurationLoader;
    private Map<String, ConfigurationProperty> properties;

    public Configuration(ConfigurationLoader configurationLoader, ConfigurationStorer configurationStorer) {
        this.configurationLoader = configurationLoader;
        this.configurationStorer = configurationStorer;
        this.loadConfiguration();
    }

    private void loadConfiguration() {
        this.properties = this.configurationLoader.load();
        this.refillDefaultProperties();
    }

    public void saveChanges() {
        this.configurationStorer.store(this.properties);
    }

    public void set(String configName, String configValue) {
        if (!this.properties.containsKey(configName)) {
            this.properties.put(configName, new ConfigurationProperty(configValue));
            return;
        }

        this.properties.get(configName).setValue(configValue);
    }

    private void refillDefaultProperties() {
        HashMap<String, String> properties = new HashMap<String, String>(7);
        properties.put("language", "en");
        properties.put("width", "0");
        properties.put("height", "0");
        properties.put("fullscreen", "true");
        properties.put("autoscale", "false");
        properties.put("scale", "1");
        properties.put("refreshSpeed", "350");
        properties.put("contentPath", "content/");
        properties.put("levelsPath", "content/levels/");
        properties.put("graphicsPath", "content/graphics/");
        properties.put("nativesPath", "natives/");
        properties.put("soundEnabled", "true");

        for (String key : properties.keySet()) {
            if (!this.properties.containsKey(key)) {
                this.properties.put(key, new ConfigurationProperty(properties.get(key)));
            }
        }
    }

    public String get(String configName) {
        return this.properties.get(configName).getValue();
    }

    public String getPath(String configName) {
        return FileHelper.canonicalPath(this.properties.get(configName).getValue());
    }
}
