package org.train.loader;

import java.util.Map;

import org.train.app.ConfigurationProperty;

public interface ConfigurationLoader {
    public Map<String, ConfigurationProperty> load();
}
