package other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import entity.Level;
import entity.Level.Item;

public class LevelController {

    // loaded level
    int currentLevel = -1;
    Level level;

    // list of levels
    private String[] levels;

    public LevelController() {
        this.loadLevels();
    }

    /**
     * Returns level
     * 
     * @param number
     *            Number of level
     * @return
     * @throws Exception
     */
    public Level getLevel(int number) throws Exception {
        number--;

        if (this.currentLevel == number) {
            return this.level;
        }

        if (number >= this.levels.length || number < 0) {
            throw new Exception("Level does not exist.");
        }

        // read lines from file
        File file = new File(Level.LEVELS_PATH + this.levels[number]);
        List<String> lines = null;
        try {
            lines = this.getLines(file);
        } catch (Exception e) {
            throw new Exception("Cannot read from file '"
                    + file.getAbsolutePath() + "'.");
        }

        if (this.levelIsValid(lines)) {
            throw new Exception("Format of level is invalid. File: '"
                    + file.getAbsolutePath() + "'");
        }

        this.currentLevel = number;
        this.level.setArray(this.getArrayFromLines(lines));

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

    private void loadLevels() {
        File dir = new File(Level.LEVELS_PATH);
        this.levels = dir.list();
    }

    private Item[][] getArrayFromLines(List<String> lines) {
        int width = lines.get(0).length();
        Item level[][] = new Item[lines.size()][width];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < width; j++) {
                level[i][j] = this.getItemFromLetter(lines.get(i).charAt(j));
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
    private List<String> getLines(File file) throws FileNotFoundException,
            IOException {
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
     * Returns number of available levels.
     * 
     * @return
     */
    public int getNumberOfLevels() {
        return this.levels.length;
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
}
