package org.train.storer;

import java.util.Map;

import org.train.app.ConfigurationProperty;

public interface ConfigurationStorer {
    public void store(Map<String, ConfigurationProperty> properties);
}
