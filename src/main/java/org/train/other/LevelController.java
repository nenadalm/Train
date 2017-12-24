package org.train.other;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import org.train.app.Configuration;
import org.train.entity.Level;
import org.train.entity.Level.Item;
import org.train.entity.LevelItem;
import org.train.level.LevelManager;
import org.train.model.Progress;

public class LevelController {

    // loaded level
    private Level level;
    private Level levelClone;
    // list of levels
    private LevelPackage[] levels;
    private Configuration config;
    private ResourceManager resourceManager;
    private LevelManager levelManager;
    private ProgressController progressController;

    public LevelController(Configuration config, ResourceManager resourceManager, LevelManager levelManager,
            ProgressController progressController) {
        this.levelManager = levelManager;
        this.progressController = progressController;
        this.resourceManager = resourceManager;
        this.config = config;
        this.loadLevels();
        this.level = new Level(1, 1, Integer.parseInt(this.config.get("refreshSpeed")), resourceManager);
    }

    public Level getCurrentLevel() {
        this.levelClone = this.level.clone();
        return this.levelClone;
    }

    public Level getCurrentLevelModified() {
        return this.levelClone.clone();
    }

    public Dimension getOptimalLevelDimension(int screenWidth, int screenHeight, float scale) {
        int imageSize = (int) (50 * scale);
        return new Dimension(screenWidth / imageSize, screenHeight / imageSize);
    }

    public void loadLevel(int packageIndex, int levelIndex) throws Exception {

        if (levelIndex >= this.levels[packageIndex].getLevelNames().size() || levelIndex < 0) {
            throw new Exception("Level does not exist.");
        }

        LevelPackage levelPackage = this.levels[packageIndex];
        String packageName = levelPackage.getName();
        String levelName = levelPackage.getLevelNames().get(levelIndex);
        this.level = this.levelManager.loadLevel(packageIndex, levelIndex, packageName, levelName);
    }

    public Progress getProgress() {
        return this.progressController.getCurrentProgress();
    }

    public boolean nextLevelExist() {
        return (this.levels[this.level.getPackageIndex()].getLevelNames().size() > this.level.getLevelIndex() + 1);
    }

    public void updateProgress() {
        this.progressController.updateProgress(this.level.getPackageIndex(), this.level.getLevelIndex());
    }

    public void loadNextLevel() {
        int packageIndex = this.level.getPackageIndex();
        int levelIndex = this.level.getLevelIndex() + 1;
        try {
            this.loadLevel(packageIndex, levelIndex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadLevels() {
        this.levels = this.levelManager.loadLevelPackages();
    }

    public void removeLevel(String levelName) {
        File file = new File(this.config.get("levelsPath"));
        file.delete();
        this.loadLevels();
    }

    private void saveLevel(Level level) {
        this.levelManager.saveLevel(level);
        this.loadLevels();
    }

    public void saveCurrentLevel() {
        this.saveLevel(this.levelClone);
    }

    public ArrayList<LevelPackage> getLevels() {
        ArrayList<LevelPackage> levels = new ArrayList<LevelPackage>(this.levels.length);
        for (int i = 0; i < this.levels.length; i++) {
            levels.add(this.levels[i]);
        }
        return levels;
    }

    public void createLevel(int packageIndex, String packageName, int levelIndex, String levelName, int width,
            int height) {
        Level level = new Level(width, height, Integer.parseInt(this.config.get("refreshSpeed")), this.resourceManager);
        level.setPackageIndex(packageIndex);
        level.setPackageName(packageName);
        level.setLevelIndex(levelIndex);
        level.setLevelName(levelName);
        this.saveLevel(level);
    }

    public void resizeLevel(int packageIndex, int levelIndex, int width, int height) {
        try {
            this.loadLevel(packageIndex, levelIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LevelItem levelArray[][] = this.level.getLevelItems();
        LevelItem newLevelItems[][] = new LevelItem[width][height];
        for (int i = 0; i < newLevelItems.length; i++) {
            for (int j = 0; j < newLevelItems[0].length; j++) {
                if (levelArray.length > i && levelArray[0].length > j) {
                    newLevelItems[i][j] = levelArray[i][j];
                } else {
                    newLevelItems[i][j] = new LevelItem("empty", this.resourceManager.getImage("empty"), Item.EMPTY);
                }
            }
        }

        this.getCurrentLevel().setLevelItems(newLevelItems);
        this.saveCurrentLevel();
    }

    public Dimension getLevelSize(int packageIndex, int levelIndex) {
        LevelPackage levelPackage = this.levels[packageIndex];
        String packageName = levelPackage.getName();
        String levelName = levelPackage.getLevelNames().get(levelIndex);

        try {
            Level level = this.levelManager.loadLevel(packageIndex, levelIndex, packageName, levelName);

            return new Dimension(level.getWidth(), level.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void createPackage(int packageIndex, String packageName) {
        this.levelManager.createPackage(packageIndex, packageName);
        this.loadLevels();
    }

    public void renamePackage(int oldIndex, String oldName, int newIndex, String newName) {
        this.levelManager.renamePackage(oldIndex, oldName, newIndex, newName);
        this.loadLevels();
    }

    public void renameLevel(int packageIndex, String packageName, int oldIndex, String oldName, int newIndex,
            String newName) {
        this.levelManager.renameLevel(packageIndex, packageName, oldIndex, oldName, newIndex, newName);
        this.loadLevels();
    }

    public void deletePackage(int packageIndex, String packageName) {
        this.levelManager.deletePackage(packageIndex, packageName);
        this.loadLevels();
    }

    public void deleteLevel(int packageIndex, String packageName, int levelIndex, String levelName) {
        this.levelManager.deleteLevel(packageIndex, packageName, levelIndex, levelName);
        this.levels[packageIndex].getLevelNames().remove(levelIndex);
        this.renumberLevels(packageIndex);
    }

    public void repairLevelsNames() {
        this.levelManager.repairLevelsNames();
    }

    public void repairPackagesNames() {
        this.levelManager.repairPackagesNames();
    }

    private void renumberLevels(int packageIndex) {
        for (int i = 0; i < this.levels[packageIndex].getLevelNames().size(); i++) {
            this.renameLevel(packageIndex, this.levels[packageIndex].getName(), i + 1,
                    this.levels[packageIndex].getLevelNames().get(i), i,
                    this.levels[packageIndex].getLevelNames().get(i));
        }
    }
}
