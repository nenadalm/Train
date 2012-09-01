package other;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.Game;
import entity.Level;
import entity.Level.Item;

public class LevelController {

    // loaded level
    private int currentLevel = -1;
    private Level level;

    // list of levels
    private LevelPackage[] levels;

    public LevelController() {
        this.level = new Level(0, 0);
        this.loadLevels();
    }

    /**
     * Returns currently loaded level
     * 
     * @return
     */
    public Level getCurrentLevel() {
        return this.level;
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

        if (this.currentLevel == levelIndex) {
            return;
        }

        if (levelIndex >= this.levels[packageIndex].getLevelNames().size() || levelIndex < 0) {
            throw new Exception("Level does not exist.");
        }

        // read lines from file
        File file = new File(Level.LEVELS_PATH + this.levels[packageIndex].getName() + '/'
                + this.levels[packageIndex].getLevelNames().get(levelIndex));
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

        this.currentLevel = levelIndex;
        this.level.setArray(this.getArrayFromLines(lines));
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
        File dir = new File(Level.LEVELS_PATH);
        String packageNames[] = dir.list();
        ArrayList<LevelPackage> packages = new ArrayList<LevelPackage>(packageNames.length);
        LevelPackage levelPackages[] = new LevelPackage[packageNames.length];
        for (int i = 0; i < packageNames.length; i++) {
            File levelPackage = new File(Level.LEVELS_PATH + packageNames[i]);
            String levels[] = levelPackage.list();
            ArrayList<String> levelNames = new ArrayList<String>(levels.length);
            for (String levelName : levels) {
                levelNames.add(levelName);
            }
            packages.add(new LevelPackage(packageNames[i], levelNames));
            levelPackages[i] = new LevelPackage(packageNames[i], levelNames);
        }
        this.levels = levelPackages;
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
        File file = new File(Level.LEVELS_PATH);
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
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[0].length; j++) {
                buffer.append(level[i][j].getChar());
            }
            buffer.append("\n");
        }

        try {
            // save level into file
            File file = new File(Level.LEVELS_PATH + packageName + '/' + levelName);
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write(buffer.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.loadLevels();
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
        String path = String.format("%1$slevels/%2$03d_%3$s/%4$02d_%5$s", Game.CONTENT_PATH,
                packageIndex, packageName, levelIndex, levelName);
        File newLevel = new File(path);
        try {
            newLevel.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        levelName = String.format("%1$2td_level", levelIndex);
        Level level = new Level(width, height);
        this.saveLevel(level.toArray(), levelName, this.levels[levelIndex].getName());
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
                if (levelArray.length >= i && levelArray[0].length >= j) {
                    newArray[i][j] = levelArray[i][j];
                } else {
                    newArray[i][j] = Item.EMPTY;
                }
            }
        }
        LevelPackage levelPackage = this.levels[packageIndex];
        String packageName = levelPackage.getName();
        String levelName = levelPackage.getLevelNames().get(levelIndex);
        this.saveLevel(newArray, levelName, packageName);
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
        File level = new File(Level.LEVELS_PATH + packageName + '/' + levelName);
        try {
            List<String> lines = this.getLines(level);
            return new Dimension(lines.get(0).length(), lines.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createPackage(int packageIndex, String packageName) {
        String path = String.format("%1$slevels/%2$03d_%3$s", Game.CONTENT_PATH, packageIndex,
                packageName);
        File newPackage = new File(path);
        newPackage.mkdir();
    }

    public void renamePackage(int oldNumber, String oldName, int newNumber, String newName) {
        String path = String
                .format("%1$slevels/%2$03d_%3$s", Game.CONTENT_PATH, oldNumber, oldName);
        File beingRenamedPackage = new File(path);
        path = String.format("%1$slevels/%2$03d_%3$s", Game.CONTENT_PATH, newNumber, newName);
        File renamedPackage = new File(path);
        beingRenamedPackage.renameTo(renamedPackage);
    }

    public void renameLevel(int packageIndex, String packageName, int oldNumber, String oldName,
            int newNumber, String newName) {
        String base = String.format("%1$slevels/%2$03d_%3$s/", Game.CONTENT_PATH, packageIndex,
                packageName);
        String path = String.format("%1$02d_%2$s", oldNumber, oldName);
        File level = new File(base + path);
        path = String.format("%1$02d_%2$s", newNumber, newName);
        File renamedLevel = new File(base + path);
        level.renameTo(renamedLevel);
    }

    public void deletePackage(int packageIndex, String packageName) {
        String path = String.format("%1$slevels/%2$03d_%3$s", Game.CONTENT_PATH, packageIndex,
                packageName);
        File beingDeletedPackage = new File(path);
        for (File file : beingDeletedPackage.listFiles()) {
            file.delete();
        }
        beingDeletedPackage.delete();
        for (int i = packageIndex; i < levels.length; i++) {
            renamePackage(i + 1, levels[i].getName(), i, levels[i].getName());
        }
    }

    public void deleteLevel(int packageIndex, String packageName, int levelIndex, String levelName) {
        String path = String.format("%1$slevels/%2$03d_%3$s/%4$02d_%5$s", Game.CONTENT_PATH,
                packageIndex, packageName, levelIndex, levelName);
        File beingDeletedLevel = new File(path);
        beingDeletedLevel.delete();
        for (int i = levelIndex; i < levels[packageIndex].getLevelNames().size(); i++) {
            renameLevel(packageIndex, levels[packageIndex].getName(), i + 1, levels[packageIndex]
                    .getLevelNames().get(i), i, levels[packageIndex].getLevelNames().get(i));
        }
    }
}
