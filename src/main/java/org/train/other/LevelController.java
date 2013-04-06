package org.train.other;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.train.app.Configuration;
import org.train.entity.Level;
import org.train.entity.Level.Item;

public class LevelController {

    private static LevelController levelController = null;

    // loaded level
    private String currentLevelFileName;
    private String currentLevelPackageName;
    private int currentLevelIndex;
    private int currentPackageIndex;
    private Level level;
    private Level levelClone;
    // list of levels
    private LevelPackage[] levels;
    private byte progresses[];
    private Configuration config;

    public static LevelController getInstance() {
        if (LevelController.levelController == null) {
            LevelController.levelController = new LevelController();
        }
        return LevelController.levelController;
    }

    private LevelController() {
        this.config = Configuration.getInstance();
        this.loadLevels();
        this.level = new Level(0, 0);
    }

    /**
     * Returns currently loaded level
     * 
     * @return
     */
    public Level getCurrentLevel() {
        this.levelClone = this.level.clone();
        return this.levelClone;
    }

    public Dimension getOptimalLevelDimension(int screenWidth, int screenHeight, float scale) {
        int imageSize = (int) (50 * scale);
        return new Dimension(screenWidth / imageSize, screenHeight / imageSize);
    }

    /**
     * Loads level into this.currentLevel
     * 
     * @param number
     *            Number of level
     * @return
     * @throws Exception
     */
    public void loadLevel(int packageIndex, int levelIndex) throws Exception {

        if (levelIndex >= this.levels[packageIndex].getLevelNames().size() || levelIndex < 0) {
            throw new Exception("Level does not exist.");
        }

        // read lines from file
        LevelPackage levelPackage = this.levels[packageIndex];
        String packageName = levelPackage.getName();
        String levelName = levelPackage.getLevelNames().get(levelIndex);
        String levelPath = this.getLevelPath(packageIndex, packageName, levelIndex, levelName);
        File file = new File(levelPath);
        List<String> lines = null;
        try {
            lines = this.getLines(file);
        } catch (Exception e) {
            throw new Exception("Cannot read from file '" + file.getAbsolutePath() + "'.");
        }

        if (!this.levelIsValid(lines)) {
            throw new Exception("Format of level is invalid. File: '" + file.getAbsolutePath()
                    + "'");
        }

        this.currentLevelIndex = levelIndex;
        this.currentPackageIndex = packageIndex;
        this.currentLevelFileName = this.getLevelFileName(levelIndex, levelName);
        this.currentLevelPackageName = this.getPackageFileName(packageIndex, packageName);
        this.level.setArray(this.getArrayFromLines(lines));
    }

    public byte[] getProgresses() {
        File saveFile = new File(this.config.get("contentPath") + "save");
        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            FileInputStream in = new FileInputStream(saveFile);
            this.progresses = new byte[this.getLevels().size()];
            in.read(this.progresses);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.progresses;
    }

    public boolean nextLevelExist() {
        return (this.levels[this.currentPackageIndex].getLevelNames().size() > this.currentLevelIndex + 1);
    }

    public void updateProgress() {
        if (this.progresses[this.currentPackageIndex] < (byte) (this.currentLevelIndex + 1)) {
            this.progresses[this.currentPackageIndex] = (byte) (this.currentLevelIndex + 1);
        }
        try {
            FileOutputStream fos = new FileOutputStream(this.config.get("contentPath") + "save");
            fos.write(this.progresses);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadNextLevel() {
        int packageIndex = this.currentPackageIndex;
        int levelIndex = this.currentLevelIndex + 1;
        try {
            this.loadLevel(packageIndex, levelIndex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Check, if all lines have same length.
     * 
     * @param lines
     * @return
     */
    private boolean levelIsValid(List<String> lines) {
        int width = lines.get(0).length();
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).length() != width) {
                return false;
            }
        }
        return true;
    }

    /**
     * Load all levels with packages into this.levels
     */
    private void loadLevels() {
        File dir = new File(this.config.get("levelsPath"));
        String packageFileNames[] = dir.list();
        this.sortArrayByStartingNumbers(packageFileNames);
        ArrayList<LevelPackage> packages = new ArrayList<LevelPackage>(packageFileNames.length);
        LevelPackage levelPackages[] = new LevelPackage[packageFileNames.length];
        for (int i = 0; i < packageFileNames.length; i++) {
            File levelPackage = new File(this.config.get("levelsPath") + packageFileNames[i]);
            String levelFileNames[] = levelPackage.list();
            this.sortArrayByStartingNumbers(levelFileNames);
            ArrayList<String> levelNames = new ArrayList<String>(levelFileNames.length);
            for (String levelFileName : levelFileNames) {
                String levelName = this.getEntityName(levelFileName);
                levelNames.add(levelName);
            }
            String packageName = this.getEntityName(packageFileNames[i]);
            packages.add(new LevelPackage(packageName, levelNames));
            levelPackages[i] = new LevelPackage(packageName, levelNames);
        }
        this.levels = levelPackages;
    }

    private void sortArrayByStartingNumbers(String list[]) {
        int index1;
        int index2;
        String tmp;
        for (int i = 0; i < list.length - 1; i++) {
            for (int j = 0; j < list.length - i - 1; j++) {
                index1 = this.getEntityIndex(list[j]);
                index2 = this.getEntityIndex(list[j + 1]);
                if (index1 > index2) {
                    tmp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = tmp;
                }
            }
        }
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

    /**
     * Returns array of items from lines of file of level.
     * 
     * @param lines
     * @return
     */
    private Item[][] getArrayFromLines(List<String> lines) {
        int width = lines.get(0).length();
        Item level[][] = new Item[width][lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < width; j++) {
                level[j][i] = this.getItemFromLetter(lines.get(i).charAt(j));
            }
        }

        return level;
    }

    /**
     * Returns lines of file with level
     * 
     * @param file
     *            File with level
     * @return Returns lines from file.
     * @throws FileNotFoundException
     * @throws IOException
     */
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

    /**
     * Remove file with level
     * 
     * @param levelName
     *            Name of level
     */
    public void removeLevel(String levelName) {
        File file = new File(this.config.get("levelsPath"));
        file.delete();
        this.loadLevels();
    }

    /**
     * Saves level into content/levels/levelName
     * 
     * @param level
     *            Level to save
     * @param levelName
     *            Name of file
     */
    public void saveLevel(Item[][] level, String levelName, String packageName) {

        // load level into string buffer
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < level[0].length; i++) {
            for (int j = 0; j < level.length; j++) {
                buffer.append(level[j][i].getChar());
            }
            buffer.append("\n");
        }

        try {
            // save level into file
            File file = new File(this.config.get("levelsPath") + packageName + '/' + levelName);
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write(buffer.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.loadLevels();
    }

    public void saveCurrentLevel() {
        Item level[][] = this.levelClone.toArray();
        this.saveLevel(level, this.currentLevelFileName, this.currentLevelPackageName);
    }

    /**
     * Converts letter to enum Item
     * 
     * @param letter
     *            Letter to convert
     * @return enum Item
     */
    private Item getItemFromLetter(char letter) {
        switch (letter) {
            case 'W':
                return Item.WALL;
            case 'G':
                return Item.GATE;
            case 'T':
                return Item.TREE;
            case 'V':
                return Item.TRAIN;
            case 'E':
                return Item.EMPTY;
        }
        return Item.EMPTY;
    }

    /**
     * Returns packages of levels
     * 
     * @return
     */
    public ArrayList<LevelPackage> getLevels() {
        ArrayList<LevelPackage> levels = new ArrayList<LevelPackage>(this.levels.length);
        for (int i = 0; i < this.levels.length; i++) {
            levels.add(this.levels[i]);
        }
        return levels;
    }

    /**
     * Creates new level
     * 
     * @param packageIndex
     *            Index of folder
     * @param levelIndex
     *            Index of file in folder
     * @param width
     *            Width of new level
     * @param height
     *            Height of new level
     */
    public void createLevel(int packageIndex, String packageName, int levelIndex, String levelName,
            int width, int height) {
        String levelFileName = this.getLevelFileName(levelIndex, levelName);
        String packageFileName = this.getPackageFileName(packageIndex, packageName);
        Level level = new Level(width, height);
        this.saveLevel(level.toArray(), levelFileName, packageFileName);
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

    /**
     * Resize level - if current level is bigger, it removes items from right
     * and bottom - fi current level is smaller, it adds empty items to right
     * and bottom
     * 
     * @param packageIndex
     *            Index of foler
     * @param levelIndex
     *            Index of file in folder
     * @param width
     * @param height
     */
    public void resizeLevel(int packageIndex, int levelIndex, int width, int height) {
        try {
            this.loadLevel(packageIndex, levelIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Item levelArray[][] = this.level.toArray();
        Item newArray[][] = new Item[width][height];
        for (int i = 0; i < newArray.length; i++) {
            for (int j = 0; j < newArray[0].length; j++) {
                if (levelArray.length > i && levelArray[0].length > j) {
                    newArray[i][j] = levelArray[i][j];
                } else {
                    newArray[i][j] = Item.EMPTY;
                }
            }
        }

        this.getCurrentLevel().setArray(newArray);
        this.saveCurrentLevel();
    }

    /**
     * Returns dimension of level
     * 
     * @param packageIndex
     * @param levelIndex
     * @return
     */
    public Dimension getLevelSize(int packageIndex, int levelIndex) {
        LevelPackage levelPackage = this.levels[packageIndex];
        String packageName = levelPackage.getName();
        String levelName = levelPackage.getLevelNames().get(levelIndex);
        File level = new File(this.getLevelPath(packageIndex, packageName, levelIndex, levelName));
        try {
            List<String> lines = this.getLines(level);
            return new Dimension(lines.get(0).length(), lines.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createPackage(int packageIndex, String packageName) {
        String path = this.getPackagePath(packageIndex, packageName);
        File newPackage = new File(path);
        newPackage.mkdir();
        this.loadLevels();
    }

    public void renamePackage(int oldIndex, String oldName, int newIndex, String newName) {
        String path = this.getPackagePath(oldIndex, oldName);
        File beingRenamedPackage = new File(path);
        path = this.getPackagePath(newIndex, newName);
        File renamedPackage = new File(path);
        beingRenamedPackage.renameTo(renamedPackage);
        this.loadLevels();
    }

    public void renameLevel(int packageIndex, String packageName, int oldIndex, String oldName,
            int newIndex, String newName) {
        String path = this.getLevelPath(packageIndex, packageName, oldIndex, oldName);
        File level = new File(path);
        path = this.getLevelPath(packageIndex, packageName, newIndex, newName);
        File renamedLevel = new File(path);
        level.renameTo(renamedLevel);
        this.loadLevels();
    }

    public void deletePackage(int packageIndex, String packageName) {
        String path = this.getPackagePath(packageIndex, packageName);
        File beingDeletedPackage = new File(path);
        for (File file : beingDeletedPackage.listFiles()) {
            file.delete();
        }
        beingDeletedPackage.delete();

        this.repairPackagesNames();
        this.loadLevels();
    }

    public void deleteLevel(int packageIndex, String packageName, int levelIndex, String levelName) {
        String path = this.getLevelPath(packageIndex, packageName, levelIndex, levelName);
        File beingDeletedLevel = new File(path);
        beingDeletedLevel.delete();
        this.levels[packageIndex].getLevelNames().remove(levelIndex);
        this.renumberLevels(packageIndex);
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

    private void renumberLevels(int packageIndex) {
        for (int i = 0; i < this.levels[packageIndex].getLevelNames().size(); i++) {
            this.renameLevel(packageIndex, this.levels[packageIndex].getName(), i + 1,
                    this.levels[packageIndex].getLevelNames().get(i), i, this.levels[packageIndex]
                            .getLevelNames().get(i));
        }
    }
}
