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
    private int currentLevel, currentPackage;
    private Level level;

    public LevelController() {

    }

    /**
     * Returns level
     * 
     * @return
     * @throws Exception
     */
    public Level getLevel() throws Exception {
        return this.level;
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

    public void loadLevel(int packageIndex, int levelIndex) {
        File base = new File(Level.LEVELS_PATH);
        File packageDirectory = base.listFiles()[packageIndex];
        File levelFile = packageDirectory.listFiles()[levelIndex];

        List<String> lines = null;
        try {
            lines = this.getLines(levelFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!this.levelIsValid(lines)) {
            System.out.println("Invalid level");
        }

        this.level.setArray(this.getArrayFromLines(lines));
        this.currentLevel = levelIndex;
        this.currentPackage = packageIndex;
    }

    public ArrayList<LevelPackage> getLevels() {
        ArrayList<LevelPackage> levelPackages = new ArrayList<LevelPackage>();
        File root = new File(Game.CONTENT_PATH + "levels/");
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                String name = file.getName().substring(4);
                ArrayList<String> levelNames = new ArrayList<String>();
                for (File levelFile : file.listFiles()) {
                    if (!levelFile.isDirectory()) {
                        levelNames.add(levelFile.getName().substring(3));
                    }
                }
                levelPackages.add(new LevelPackage(name, levelNames));
            }
        }
        return levelPackages;
    }

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
     * 
     * @param file
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
     * Saves level into content/levels/levelName
     * 
     * @param level
     *            Level to save
     * @param levelName
     *            Name of file
     */
    public void saveLevel(Item[][] level, String levelName) {

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
            File file = new File(Level.LEVELS_PATH + levelName);
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.write(buffer.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void createNewLevel(int packageIndex, int levelIndex, int width, int height) {
        // TODO fill newly created file with valid data. File already exist do
        // NOT create it again!
    }

    public void resizeLevel(int packageIndex, int levelIndex, int width, int height) {
        // TODO change level size
    }

    public Dimension getLevelSize(int packageIndex, int levelIndex) {
        // TODO load level then get its width and height
        return new Dimension(7, 7);
    }
}
