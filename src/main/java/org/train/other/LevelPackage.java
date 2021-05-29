package org.train.other;

import java.util.ArrayList;

public class LevelPackage {

    private String name;
    private ArrayList<String> levelNames;

    public LevelPackage(String name, ArrayList<String> levelNames) {
        this.name = name;
        this.levelNames = levelNames;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getLevelNames() {
        return this.levelNames;
    }

    public void setLevelNames(ArrayList<String> levelNames) {
        this.levelNames = levelNames;
    }

}
