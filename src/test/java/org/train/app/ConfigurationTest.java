package org.train.app;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.train.loader.ConfigurationLoader;
import org.train.loader.ConfigurationXmlLoader;
import org.train.storer.ConfigurationStorer;
import org.train.storer.ConfigurationXmlStorer;

public class ConfigurationTest {

    @Test
    public void testGetDefaultConfigurationValues() {
        File configFile = new File("testingContent/newConfig.xml");
        ConfigurationLoader loader = new ConfigurationXmlLoader(configFile);
        ConfigurationStorer storer = new ConfigurationXmlStorer(configFile);
        Configuration config = new Configuration(loader, storer);

        Assert.assertEquals("en", config.get("language"));
        Assert.assertEquals("0", config.get("width"));
        Assert.assertEquals("0", config.get("height"));
        Assert.assertEquals("true", config.get("fullscreen"));
        Assert.assertEquals("false", config.get("autoscale"));
        Assert.assertEquals("1", config.get("scale"));
        Assert.assertEquals("350", config.get("refreshSpeed"));
        Assert.assertEquals("content/", config.get("contentPath"));
        Assert.assertEquals("content/levels/", config.get("levelsPath"));
        Assert.assertEquals("content/graphics/", config.get("graphicsPath"));
    }

    @Test(expected = NullPointerException.class)
    public void testGetNonExistingConfigurationValue() {
        File configFile = new File("testingContent/newConfig.xml");
        ConfigurationLoader loader = new ConfigurationXmlLoader(configFile);
        ConfigurationStorer storer = new ConfigurationXmlStorer(configFile);
        Configuration config = new Configuration(loader, storer);

        config.get("non-existing-property");
    }
}
