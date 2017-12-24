package org.train.app;

public class ConfigurationProperty {
    private String value;
    private boolean isDirty = false;

    public ConfigurationProperty(String value) {
	this.value = value;
	this.isDirty = true;
    }

    public String getValue() {
	return this.value;
    }

    public void setValue(String value) {
	this.value = value;
	this.isDirty = true;
    }

    public boolean isDirty() {
	return this.isDirty;
    }
}
