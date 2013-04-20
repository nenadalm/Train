package org.train.loader;

import java.io.File;

public class ConfigurationLoaderFactory {

    public ConfigurationLoader getLoader() {
        return new ConfigurationXmlLoader(new File("config.xml"));
    }
}
