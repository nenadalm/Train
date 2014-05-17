package org.train.level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Image;
import org.train.app.Configuration;
import org.train.entity.Level;
import org.train.entity.Level.Item;
import org.train.entity.LevelItem;
import org.train.model.Truck;
import org.train.other.LevelPackage;
import org.train.other.ResourceManager;

public class LevelManager {

    private Configuration config;
    private ResourceManager resourceManager;

    public LevelManager(Configuration config, ResourceManager resourceManager) {
        this.config = config;
        this.resourceManager = resourceManager;
    }

    public LevelPackage[] loadLevelPackages() {
        String packageFileNames[] = this.getPackageFilenames();
        Arrays.sort(packageFileNames);

        return this.getLevelPackages(packageFileNames);
    }

    private LevelPackage[] getLevelPackages(String[] packageFileNames) {
        LevelPackage levelPackages[] = new LevelPackage[packageFileNames.length];
        for (int i = 0; i < packageFileNames.length; i++) {
            String[] levelFileNames = this.getLevelFileNames(packageFileNames[i]);
            ArrayList<String> levelNames = this.getLevelNames(levelFileNames);

            String packageName = this.getEntityName(packageFileNames[i]);

            levelPackages[i] = new LevelPackage(packageName, levelNames);
        }

        return levelPackages;
    }

    private ArrayList<String> getLevelNames(String[] levelFileNames) {
        ArrayList<String> levelNames = new ArrayList<String>(levelFileNames.length);
        for (String levelFileName : levelFileNames) {
            String levelName = this.getEntityName(levelFileName);
            levelNames.add(levelName);
        }

        return levelNames;
    }

    private String[] getLevelFileNames(String packageFileName) {
        File levelPackage = new File(this.config.get("levelsPath") + packageFileName);
        String levelFileNames[] = levelPackage.list();
        Arrays.sort(levelFileNames);

        return levelFileNames;
    }

    private String[] getPackageFilenames() {
        return new File(this.config.get("levelsPath")).list();
    }

    public void saveLevel(Level level) {
        try {
            String levelFileName = this.getLevelFileName(level.getLevelIndex(),
                    level.getLevelName());
            String packageFileName = this.getPackageFileName(level.getPackageIndex(),
                    level.getPackageName());

            File file = new File(this.config.get("levelsPath") + packageFileName + '/'
                    + levelFileName);
            file.createNewFile();
            String levelString = this.levelToString(level);
            FileWriter fw = new FileWriter(file);
            fw.write(levelString);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String levelToString(Level level) {
        LevelItem[][] levelItems = level.getLevelItems();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < levelItems[0].length; i++) {
            for (int j = 0; j < levelItems.length; j++) {
                buffer.append(levelItems[j][i].getType().getChar());

                if (levelItems[j][i].getName().length() > 0) {
                    buffer.append(":" + levelItems[j][i].getName());
                }

                if (j < levelItems.length - 1) {
                    buffer.append(" ");
                }
            }
            buffer.append("\n");
        }

        return buffer.toString();
    }

    public Level loadLevel(int packageIndex, int levelIndex, String packageName, String levelName)
            throws Exception {
        // read lines from file
        String levelPath = this.getLevelPath(packageIndex, packageName, levelIndex, levelName);
        File file = new File(levelPath);
        List<String> lines = null;
        try {
            lines = this.getLines(file);
        } catch (Exception e) {
            throw new Exception("Cannot read from file '" + file.getAbsolutePath() + "'.");
        }

        Level level = new Level(0, 0, Integer.parseInt(this.config.get("refreshSpeed")),
                this.resourceManager);
        level.setLevelItems(this.getLevelItemsFromLines(lines));
        level.setLevelIndex(levelIndex);
        level.setPackageIndex(packageIndex);
        level.setLevelName(levelName);
        level.setPackageName(packageName);

        return level;
    }

    private List<String> getLines(File file) throws FileNotFoundException, IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        return lines;
    }

    private LevelItem[][] getLevelItemsFromLines(List<String> lines) {
        Map<Item, Image> images = new HashMap<Item, Image>(6);
        images.put(Item.WALL, this.resourceManager.getImage("wall"));
        images.put(Item.GATE, this.resourceManager.getImage("gate"));
        images.put(Item.TRAIN, this.resourceManager.getImage("train"));
        images.put(Item.EMPTY, this.resourceManager.getImage("empty"));

        int rows = lines.size();
        int cols = lines.get(0).split(" ").length;

        LevelItem[][] levelItems = new LevelItem[cols][rows];
        for (int i = 0; i < rows; i++) {
            String[] lineItems = lines.get(i).split(" ");
            for (int j = 0; j < cols; j++) {
                Item type = this.getItemFromLetter(lineItems[j].charAt(0));
                String[] typeName = lineItems[j].split(":");
                String name = "";
                if (typeName.length > 1) {
                    name = typeName[1];
                }

                Image img = null;
                if (type == Item.ITEM) {
                    Truck t = null;

                    t = this.resourceManager.getTruck(name);

                    if (t == null) {
                        name = "tree";
                        t = this.resourceManager.getTruck(name);
                    }
                    img = t.getItem();
                } else {
                    img = images.get(type);
                }

                levelItems[j][i] = new LevelItem(name, img, type);
            }
        }

        return levelItems;
    }

    private Item getItemFromLetter(char letter) {
        switch (letter) {
            case 'W':
                return Item.WALL;
            case 'G':
                return Item.GATE;
            case 'T':
                return Item.ITEM;
            case 'V':
                return Item.TRAIN;
            case 'E':
                return Item.EMPTY;
        }

        throw new RuntimeException(String.format("Invalid letter %1$", letter));
    }

    private String getLevelFileName(int levelIndex, String levelName) {
        return String.format("%1$02d_%2$s", levelIndex, levelName);
    }

    private String getPackageFileName(int packageIndex, String packageName) {
        return String.format("%1$03d_%2$s", packageIndex, packageName);
    }

    private int getEntityIndex(String fileName) {
        return Integer.valueOf(fileName.replaceFirst("_.*", ""));
    }

    private String getEntityName(String fileName) {
        return fileName.replaceFirst(".*?_", "");
    }

    private String getLevelPath(int packageIndex, String packageName, int levelIndex,
            String levelName) {
        String levelFileName = this.getLevelFileName(levelIndex, levelName);
        String packageFileName = this.getPackageFileName(packageIndex, packageName);
        return String.format("%1$s%2$s/%3$s", this.config.get("levelsPath"), packageFileName,
                levelFileName);
    }

    private String getPackagePath(int packageIndex, String packageName) {
        String packageFileName = this.getPackageFileName(packageIndex, packageName);
        return String.format("%1$s%2$s", this.config.get("levelsPath"), packageFileName);
    }

    public void createPackage(int packageIndex, String packageName) {
        String path = this.getPackagePath(packageIndex, packageName);
        File newPackage = new File(path);
        newPackage.mkdir();
    }

    public void renamePackage(int oldIndex, String oldName, int newIndex, String newName) {
        String path = this.getPackagePath(oldIndex, oldName);
        File beingRenamedPackage = new File(path);
        path = this.getPackagePath(newIndex, newName);
        File renamedPackage = new File(path);
        beingRenamedPackage.renameTo(renamedPackage);
    }

    public void renameLevel(int packageIndex, String packageName, int oldIndex, String oldName,
            int newIndex, String newName) {
        String path = this.getLevelPath(packageIndex, packageName, oldIndex, oldName);
        File level = new File(path);
        path = this.getLevelPath(packageIndex, packageName, newIndex, newName);
        File renamedLevel = new File(path);
        level.renameTo(renamedLevel);
    }

    public void deletePackage(int packageIndex, String packageName) {
        String path = this.getPackagePath(packageIndex, packageName);
        File beingDeletedPackage = new File(path);
        for (File file : beingDeletedPackage.listFiles()) {
            file.delete();
        }
        beingDeletedPackage.delete();

        this.repairPackagesNames();
    }

    public void deleteLevel(int packageIndex, String packageName, int levelIndex, String levelName) {
        String path = this.getLevelPath(packageIndex, packageName, levelIndex, levelName);
        File beingDeletedLevel = new File(path);
        beingDeletedLevel.delete();
    }

    public void repairLevelsNames() {
        File pkg = new File(this.config.get("levelsPath"));
        File packages[] = pkg.listFiles();
        Arrays.sort(packages);
        for (int i = 0; i < packages.length; i++) {
            File levels[] = packages[i].listFiles();
            Arrays.sort(levels);
            String packageName = this.getEntityName(packages[i].getName());
            for (int j = 0; j < levels.length; j++) {
                int currentIndex = this.getEntityIndex(levels[j].getName());
                String name = this.getEntityName(levels[j].getName());
                if (currentIndex != j) {
                    this.renameLevel(i, packageName, currentIndex, name, j, name);
                }
            }
        }
    }

    public void repairPackagesNames() {
        File pkg = new File(this.config.get("levelsPath"));
        File packages[] = pkg.listFiles();
        Arrays.sort(packages);
        for (int i = 0; i < packages.length; i++) {
            String currentIndex = packages[i].getName().replaceFirst("_.*", "");
            if (currentIndex.matches("^\\d+$")) {

            }
            String name = this.getEntityName(packages[i].getName());
            if (!currentIndex.equals(String.format("%1$02d", i))) {
                this.renamePackage(Integer.valueOf(currentIndex), name, i, name);
            }
        }
    }
}
