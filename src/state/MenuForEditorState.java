package state;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.InteractiveLabel;
import other.LevelController;
import other.LevelPackage;
import other.Translator;
import app.Configuration;
import app.Game;
import factory.EffectFactory;
import factory.FontFactory;

public class MenuForEditorState extends BasicGameState {

    private enum Action {
        None, CreatingPackage, CreatingLevel, RenamingPackage, RenamingLevel, DeletingPackage, DeletingLevel, ResizingLevel
    }

    private Action action;
    private boolean isMouseOverPackageArrowUp, isMouseOverPackageArrowDown,
            isMouseOverLevelArrowUp, isMouseOverLevelArrowDown, isLevelArrowDownDisabled,
            isMouseOverPackageNames[], isMouseOverLevelNames[];
    private int stateId, width, height, packageIndex, levelIndex, packageBaseIndex, levelBaseIndex,
            inputState;
    private Font ubuntuSmall, ubuntuMedium;

    private Dimension optimalSize;
    private Rectangle packageArrowUpRectangle, packageArrowDownRectangle, levelArrowUpRectangle,
            levelArrowDownRectangle, packageNameRectangles[], levelNameRectangles[];
    private ArrayList<LevelPackage> levelPackages;
    private Image arrowUp, arrowDown, arrowMouseOverUp, arrowMouseOverDown, arrowDisabledUp,
            arrowDisabledDown;
    private String newLevelName, infoText;
    private InteractiveLabel packageActions[], levelActions[], back;
    private TextField textField;
    private Translator translator;
    private Dimension levelSize;
    private LevelController levelController;

    public MenuForEditorState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        FontFactory fonts = FontFactory.getInstance();
        EffectFactory effects = EffectFactory.getInstance();
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        Configuration configuration = Configuration.getInstance();
        translator = Translator.getInstance();
        width = container.getWidth();
        height = container.getHeight();

        action = Action.None;

        ubuntuSmall = fonts.getFont("ubuntu", width / 36, whiteEffect);
        ubuntuMedium = fonts.getFont("ubuntu", width / 20, whiteEffect);

        levelController = LevelController.getInstance();
        levelPackages = levelController.getLevels();
        float scale = Float.parseFloat(configuration.get("scale"));
        optimalSize = levelController.getOptimalLevelDimension(width, height, scale);

        arrowUp = new Image(configuration.get("contentPath") + "graphics/arrow.png")
                .getScaledCopy(width / 2000f);
        arrowDown = arrowUp.getFlippedCopy(false, true);
        arrowDisabledUp = new Image(configuration.get("contentPath") + "graphics/arrowDisabled.png")
                .getScaledCopy(width / 2000f);
        arrowDisabledDown = arrowDisabledUp.getFlippedCopy(false, true);
        arrowMouseOverUp = new Image(configuration.get("contentPath")
                + "graphics/arrowMouseOver.png").getScaledCopy(width / 2000f);
        arrowMouseOverDown = arrowMouseOverUp.getFlippedCopy(false, true);

        packageArrowUpRectangle = new Rectangle();
        packageArrowUpRectangle.width = arrowUp.getWidth();
        packageArrowUpRectangle.height = arrowUp.getHeight();
        packageArrowUpRectangle.x = width / 12;
        packageArrowUpRectangle.y = height * 3 / 12;

        packageArrowDownRectangle = new Rectangle();
        packageArrowDownRectangle.width = arrowDown.getWidth();
        packageArrowDownRectangle.height = arrowDown.getHeight();
        packageArrowDownRectangle.x = width / 12;
        packageArrowDownRectangle.y = height * 9 / 12;

        levelArrowUpRectangle = new Rectangle();
        levelArrowUpRectangle.width = arrowUp.getWidth();
        levelArrowUpRectangle.height = arrowUp.getHeight();
        levelArrowUpRectangle.x = width * 7 / 12;
        levelArrowUpRectangle.y = height * 3 / 12;

        levelArrowDownRectangle = new Rectangle();
        levelArrowDownRectangle.width = arrowDown.getWidth();
        levelArrowDownRectangle.height = arrowDown.getHeight();
        levelArrowDownRectangle.x = width * 7 / 12;
        levelArrowDownRectangle.y = height * 9 / 12;

        String backText = translator.translate("back");
        Rectangle rectangle = new Rectangle();
        rectangle.width = ubuntuSmall.getWidth(backText);
        rectangle.height = ubuntuSmall.getHeight(backText);
        rectangle.x = width / 100;
        rectangle.y = (int) (height - rectangle.height * 1.1f);
        Point position = new Point(width / 100, (int) (height - rectangle.height * 1.1f));
        back = new InteractiveLabel(backText, position, rectangle);
        back.setColors(Color.white, Color.red, Color.white);

        initPackageActions();
        initLevelActions();

        packageIndex = -1;
        packageBaseIndex = 0;
        levelIndex = -1;
        levelBaseIndex = 0;

        setPackageNameRectangles();
        isMouseOverPackageNames = new boolean[5];
        isMouseOverLevelNames = new boolean[5];

        textField = new TextField(container, ubuntuSmall, width / 3, height - width / 26,
                width / 3, width / 30);
        textField.setBackgroundColor(Color.darkGray);
        textField.setMaxLength(15);
        textField.setTextColor(Color.white);
        textField.setText("");

        infoText = "";
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        String packagesText = translator.translate("Packages");
        String levelsText = translator.translate("Levels");
        g.setFont(ubuntuMedium);
        g.setColor(Color.gray);
        g.drawString(packagesText, width / 500, height / 6 - ubuntuMedium.getHeight(packagesText)
                / 2 + height / 750);
        g.drawString(levelsText, width / 2 + width / 500,
                height / 6 - ubuntuMedium.getHeight(packagesText) / 2 + height / 750);
        g.setColor(Color.white);
        g.drawString(packagesText, 0, height / 6 - ubuntuMedium.getHeight(packagesText) / 2);
        g.drawString(levelsText, width / 2, height / 6 - ubuntuMedium.getHeight(packagesText) / 2);
        g.setFont(ubuntuSmall);
        back.render(g);
        for (int i = 0; i < levelPackages.size() && i < 5; i++) {
            g.setColor((packageIndex == packageBaseIndex + i) ? Color.blue
                    : ((isMouseOverPackageNames[i]) ? Color.red : Color.white));
            g.drawString(levelPackages.get(packageBaseIndex + i).getName(), width / 100, height
                    * (4 + i) / 13);
        }
        if (packageIndex >= 0) {
            LevelPackage levelPackage = levelPackages.get(packageIndex);
            ArrayList<String> levelNames = levelPackage.getLevelNames();
            for (int i = 0; i < levelNames.size() && i < 5; i++) {
                g.setColor((levelIndex == levelBaseIndex + i) ? Color.blue
                        : ((isMouseOverLevelNames[i]) ? Color.red : Color.white));
                g.drawString(levelNames.get(levelBaseIndex + i), width / 2 + width / 100, height
                        * (4 + i) / 13);
            }
        }
        String text = "";
        String showing = translator.translate("showing"), of = translator.translate("of");
        g.setColor(Color.red);
        text = String.format("%4$s %1$d - %2$d %5$s %3$d", packageBaseIndex + 1,
                packageBaseIndex + 5, levelPackages.size(), showing, of);
        g.drawString(text, width / 200, height * 9 / 11);
        text = String.format("%4$s %1$d - %2$d %5$s %3$d", levelBaseIndex + 1, levelBaseIndex + 5,
                (packageIndex >= 0) ? levelPackages.get(packageIndex).getLevelNames().size() : 0,
                showing, of);
        g.drawString(text, width / 2 + width / 200, height * 9 / 11);

        g.drawString(infoText, width * 4 / 12, height * 13 / 15);

        if (action == Action.DeletingPackage || action == Action.DeletingLevel) {
            drawString(g, ubuntuSmall, String.format("%1$s %2$s? %3$s(Enter)/%4$s(Escape)",
                    translator.translate("Really"), translator.translate("delete"),
                    translator.translate("Yes"), translator.translate("No")), width / 2, height
                    - height / 24);
        }
        for (int i = 0; i < packageActions.length; i++) {
            packageActions[i].render(g);
        }
        for (int i = 0; i < levelActions.length; i++) {
            levelActions[i].render(g);
        }

        Image arrowImageToBeDrawn = (packageBaseIndex == 0) ? arrowDisabledUp
                : ((isMouseOverPackageArrowUp) ? arrowMouseOverUp : arrowUp);
        drawImage(arrowImageToBeDrawn, width / 12, height * 3 / 12);
        arrowImageToBeDrawn = (packageBaseIndex + 5 >= levelPackages.size()) ? arrowDisabledDown
                : ((isMouseOverPackageArrowDown) ? arrowMouseOverDown : arrowDown);
        drawImage(arrowImageToBeDrawn, width / 12, height * 9 / 12);

        arrowImageToBeDrawn = (levelBaseIndex == 0) ? arrowDisabledUp
                : ((isMouseOverLevelArrowUp) ? arrowMouseOverUp : arrowUp);
        drawImage(arrowImageToBeDrawn, width * 7 / 12, height * 3 / 12);

        arrowImageToBeDrawn = (isLevelArrowDownDisabled) ? arrowDisabledDown
                : ((isMouseOverLevelArrowDown) ? arrowMouseOverDown : arrowDown);
        drawImage(arrowImageToBeDrawn, width * 7 / 12, height * 9 / 12);

        g.setColor(Color.white);
        if (action != Action.None && action != Action.DeletingPackage
                && action != Action.DeletingLevel) {
            textField.render(container, g);
            textField.setFocus(true);
            if (action == Action.CreatingPackage || action == Action.RenamingPackage
                    || (action == Action.CreatingLevel && inputState == 0)
                    || action == Action.RenamingLevel) {
                text = translator.translate("Name") + ":";
            } else if ((action == Action.CreatingLevel && inputState == 1)
                    || (action == Action.ResizingLevel && inputState == 0)) {
                text = translator.translate("Width") + ":";
            } else if ((action == Action.CreatingLevel && inputState == 2)
                    || (action == Action.ResizingLevel && inputState == 1)) {
                text = translator.translate("Height") + ":";
            }
            g.drawString(text, width / 3 - ubuntuSmall.getWidth(text) * 1.1f, height - width / 26);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();
        Point mouse = new Point(input.getMouseX(), input.getMouseY());

        packageActions[0].setEnabled(levelPackages.size() < 1000);
        packageActions[1].setEnabled(packageIndex >= 1);
        packageActions[2].setEnabled(packageIndex >= 0 && packageIndex < levelPackages.size() - 1);
        packageActions[3].setEnabled(packageIndex >= 0);
        packageActions[4].setEnabled(packageIndex >= 0);

        levelActions[0].setEnabled(packageIndex >= 0
                && levelPackages.get(packageIndex).getLevelNames().size() < 100);
        levelActions[1].setEnabled(packageIndex >= 0 && levelIndex >= 0);
        levelActions[2].setEnabled(packageIndex >= 0 && levelIndex >= 1);
        levelActions[3].setEnabled(packageIndex >= 0 && levelIndex >= 0
                && levelIndex < levelPackages.get(packageIndex).getLevelNames().size() - 1);
        levelActions[4].setEnabled(packageIndex >= 0 && levelIndex >= 0);
        levelActions[5].setEnabled(packageIndex >= 0 && levelIndex >= 0);
        levelActions[6].setEnabled(packageIndex >= 0 && levelIndex >= 0);

        isMouseOverPackageArrowUp = packageArrowUpRectangle.contains(mouse);
        isMouseOverPackageArrowDown = packageArrowDownRectangle.contains(mouse);
        isMouseOverLevelArrowUp = levelArrowUpRectangle.contains(mouse);
        isMouseOverLevelArrowDown = levelArrowDownRectangle.contains(mouse);

        back.setIsMouseOver(mouse);

        for (int i = 0; i < packageNameRectangles.length && i < levelPackages.size(); i++) {
            isMouseOverPackageNames[i] = packageNameRectangles[i].contains(mouse);
        }
        for (int i = 0; i < packageActions.length; i++) {
            packageActions[i].setIsMouseOver(mouse);
        }

        if (packageIndex >= 0) {
            for (int i = 0; i < levelNameRectangles.length
                    && i < levelPackages.get(packageIndex).getLevelNames().size(); i++) {
                isMouseOverLevelNames[i] = levelNameRectangles[i].contains(mouse);
            }
            for (int i = 0; i < levelActions.length; i++) {
                levelActions[i].setIsMouseOver(mouse);
            }
        }

        isLevelArrowDownDisabled = packageIndex < 0
                || (levelBaseIndex + 5 >= levelPackages.get(packageIndex).getLevelNames().size());

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            if (action != Action.None) {
                action = Action.None;
                textField.setText("");
                infoText = "";
            } else {
                game.enterState(Game.MENU_STATE);
            }
        }
        if (input.isKeyPressed(Input.KEY_ENTER) || input.isKeyPressed(Input.KEY_NUMPADENTER)) {
            infoText = "";
            if (action == Action.CreatingPackage) {
                String name = textField.getText();
                boolean isPackageAlreadyExist = false;
                for (LevelPackage levelPackage : levelPackages) {
                    if (name.equals(levelPackage.getName())) {
                        isPackageAlreadyExist = true;
                        break;
                    }
                }
                if (!isPackageAlreadyExist) {
                    action = Action.None;
                    LevelPackage levelPackage = new LevelPackage(name, new ArrayList<String>());
                    levelPackages.add(levelPackage);
                    levelController.createPackage(levelPackages.size() - 1, textField.getText());

                    textField.setText("");
                    setPackageNameRectangles();
                } else {
                    infoText = translator.translate("PackageAlreadyExist");
                }
            }
            if (action == Action.RenamingPackage) {
                action = Action.None;

                String oldName = levelPackages.get(packageIndex).getName();
                String newName = textField.getText();
                boolean isPackageAlreadyExist = false;
                for (LevelPackage levelPackage : levelPackages) {
                    if (newName.equals(levelPackage.getName())) {
                        isPackageAlreadyExist = true;
                        break;
                    }
                }
                if (!isPackageAlreadyExist) {
                    levelPackages.get(packageIndex).setName(newName);

                    levelController.renamePackage(packageIndex, oldName, packageIndex, newName);
                    textField.setText("");
                    setPackageNameRectangles();
                } else {
                    infoText = translator.translate("PackageAlreadyExist");
                }
            }
            if (action == Action.DeletingPackage) {
                action = Action.None;
                levelController.deletePackage(packageIndex, levelPackages.get(packageIndex)
                        .getName());
                levelPackages.remove(packageIndex);
                if (packageBaseIndex > 0) {
                    packageBaseIndex--;
                }
                packageIndex--;
                setPackageNameRectangles();

                levelBaseIndex = 0;
                levelIndex = -1;
                setLevelNameRectangles();
            }

            if (action == Action.CreatingLevel) {
                if (inputState == 2) {
                    try {
                        int levelHeight = Integer.parseInt(textField.getText());
                        if (levelHeight >= 7 && levelHeight <= 100) {
                            action = Action.None;
                            levelSize.height = levelHeight;

                            LevelPackage levelPackage = levelPackages.get(packageIndex);
                            levelPackage.getLevelNames().add(newLevelName);

                            levelController.createLevel(packageIndex, levelPackage.getName(),
                                    levelPackage.getLevelNames().size() - 1, newLevelName,
                                    levelSize.width, levelSize.height);
                            textField.setText("");
                            setLevelNameRectangles();

                            levelController.loadLevel(packageIndex, levelPackage.getLevelNames()
                                    .size() - 1);
                            game.enterState(Game.EDITOR_STATE);

                        } else {
                            infoText = translator.translate("WrongLevelSize") + " 7 - 100";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (inputState == 1) {
                    try {
                        int levelWidth = Integer.parseInt(textField.getText());
                        if (levelWidth >= 7 && levelWidth <= 100) {
                            inputState = 2;
                            levelSize = new Dimension(levelWidth, 0);
                            textField.setText(String.valueOf(optimalSize.height));
                        } else {
                            infoText = translator.translate("WrongLevelSize") + " 7 - 100";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (inputState == 0) {
                    String name = textField.getText();
                    boolean isLevelAlreadyExist = false;
                    LevelPackage levelPackage = levelPackages.get(packageIndex);
                    for (String string : levelPackage.getLevelNames()) {
                        if (name.equals(string)) {
                            isLevelAlreadyExist = true;
                            break;
                        }
                    }
                    if (!isLevelAlreadyExist) {
                        inputState = 1;
                        newLevelName = name;
                        textField.setText(String.valueOf(optimalSize.width));
                    } else {
                        infoText = translator.translate("LevelAlreadyExist");
                    }
                }
            }
            if (action == Action.RenamingLevel) {
                action = Action.None;
                LevelPackage levelPackage = levelPackages.get(packageIndex);
                String oldName = levelPackage.getLevelNames().get(levelIndex);
                String newName = textField.getText();
                boolean isLevelAlreadyExist = false;
                for (String string : levelPackage.getLevelNames()) {
                    if (newName.equals(string)) {
                        isLevelAlreadyExist = true;
                        break;
                    }
                }
                if (!isLevelAlreadyExist) {
                    levelPackage.getLevelNames().set(levelIndex, newName);

                    levelController.renameLevel(packageIndex, levelPackages.get(packageIndex)
                            .getName(), levelIndex, oldName, levelIndex, newName);
                    textField.setText("");
                    setLevelNameRectangles();
                } else {
                    infoText = translator.translate("LevelAlreadyExist");
                }
            }
            if (action == Action.ResizingLevel) {
                try {
                    if (inputState == 1) {
                        int levelHeight = Integer.parseInt(textField.getText());
                        if (levelHeight >= 7 && levelHeight <= 100) {
                            action = Action.None;
                            levelSize.height = levelHeight;
                            levelController.resizeLevel(packageIndex, levelIndex, levelSize.width,
                                    levelSize.height);
                            textField.setText("");
                        } else {
                            infoText = translator.translate("WrongLevelSize") + " 7 - 100";
                        }
                    }
                    if (inputState == 0) {
                        int levelWidth = Integer.parseInt(textField.getText());
                        if (levelWidth >= 7 && levelWidth <= 100) {
                            inputState = 1;
                            levelSize.width = levelWidth;
                            textField.setText(String.valueOf(levelSize.height));
                        } else {
                            infoText = translator.translate("WrongLevelSize") + " 7 - 100";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (action == Action.DeletingLevel) {
                action = Action.None;
                LevelPackage levelPackage = levelPackages.get(packageIndex);
                ArrayList<String> names = levelPackage.getLevelNames();
                levelController.deleteLevel(packageIndex, levelPackage.getName(), levelIndex,
                        names.get(levelIndex));
                if (levelBaseIndex > 0) {
                    levelBaseIndex--;
                }
                levelPackage.getLevelNames().remove(levelIndex);
                levelIndex--;
                setLevelNameRectangles();
            }
        }

        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            infoText = "";
            if (isMouseOverPackageArrowUp && packageBaseIndex > 0) {
                packageBaseIndex--;
                setPackageNameRectangles();
            }
            if (isMouseOverPackageArrowDown && packageBaseIndex + 5 < levelPackages.size()) {
                packageBaseIndex++;
                setPackageNameRectangles();
            }

            if (isMouseOverLevelArrowUp && levelBaseIndex > 0) {
                levelBaseIndex--;
                setLevelNameRectangles();
            }
            if (isMouseOverLevelArrowDown && !isLevelArrowDownDisabled) {
                levelBaseIndex++;
                setLevelNameRectangles();
            }
            if (back.isMouseOver()) {
                game.enterState(Game.MENU_STATE);
            }
            if (action == Action.None) {
                for (int i = 0; i < packageNameRectangles.length && i < levelPackages.size(); i++) {
                    if (isMouseOverPackageNames[i]) {
                        packageIndex = packageBaseIndex + i;
                        levelBaseIndex = 0;
                        levelIndex = -1;
                        setLevelNameRectangles();
                    }
                }

                if (packageActions[0].isMouseOver() && packageActions[0].isEnabled()) { // CREATE
                    action = Action.CreatingPackage;
                }
                if (packageActions[1].isMouseOver() && packageActions[1].isEnabled()) { // MOVEUP
                    String firstName = levelPackages.get(packageIndex).getName();
                    String secondName = levelPackages.get(packageIndex - 1).getName();
                    LevelPackage levelPackage = levelPackages.remove(packageIndex - 1);
                    levelPackages.add(packageIndex, levelPackage);

                    levelController.renamePackage(packageIndex, firstName, packageIndex - 1,
                            firstName);
                    levelController.renamePackage(packageIndex - 1, secondName, packageIndex,
                            secondName);
                    packageIndex--;
                }
                if (packageActions[2].isMouseOver() && packageActions[2].isEnabled()) { // MOVEDOWN
                    String firstName = levelPackages.get(packageIndex).getName();
                    String secondName = levelPackages.get(packageIndex + 1).getName();
                    LevelPackage levelPackage = levelPackages.remove(packageIndex);
                    levelPackages.add(packageIndex + 1, levelPackage);

                    levelController.renamePackage(packageIndex, firstName, packageIndex + 1,
                            firstName);
                    levelController.renamePackage(packageIndex + 1, secondName, packageIndex,
                            secondName);
                    packageIndex++;
                }
                if (packageActions[3].isMouseOver() && packageActions[3].isEnabled()) { // RENAME
                    action = Action.RenamingPackage;
                    String name = levelPackages.get(packageIndex).getName();
                    textField.setText(name);
                    textField.setCursorPos(name.length());
                }
                if (packageActions[4].isMouseOver() && packageActions[4].isEnabled()) { // DELETE
                    action = Action.DeletingPackage;
                }

                if (packageIndex >= 0) {
                    for (int i = 0; i < levelNameRectangles.length
                            && i < levelPackages.get(packageIndex).getLevelNames().size(); i++) {
                        if (isMouseOverLevelNames[i]) {
                            levelIndex = levelBaseIndex + i;
                        }
                    }
                    if (levelActions[0].isMouseOver() && levelActions[0].isEnabled()) { // CREATE
                        action = Action.CreatingLevel;
                        inputState = 0;
                    }
                    if (levelActions[1].isMouseOver() && levelActions[1].isEnabled()) { // EDIT
                        try {
                            levelController.loadLevel(packageIndex, levelIndex);
                            game.enterState(Game.EDITOR_STATE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (levelActions[2].isMouseOver() && levelActions[2].isEnabled()) { // MOVEUP
                        LevelPackage levelPackage = levelPackages.get(packageIndex);
                        String firstName = levelPackage.getLevelNames().get(levelIndex);
                        String secondName = levelPackage.getLevelNames().get(levelIndex - 1);
                        String level = levelPackage.getLevelNames().remove(levelIndex - 1);
                        levelPackage.getLevelNames().add(levelIndex, level);

                        levelController.renameLevel(packageIndex, levelPackages.get(packageIndex)
                                .getName(), levelIndex, firstName, levelIndex - 1, firstName);
                        levelController.renameLevel(packageIndex, levelPackages.get(packageIndex)
                                .getName(), levelIndex - 1, secondName, levelIndex, secondName);
                        levelIndex--;
                    }
                    if (levelActions[3].isMouseOver() && levelActions[3].isEnabled()) { // MOVEDOWN
                        LevelPackage levelPackage = levelPackages.get(packageIndex);
                        String firstName = levelPackage.getLevelNames().get(levelIndex);
                        String secondName = levelPackage.getLevelNames().get(levelIndex + 1);
                        String level = levelPackage.getLevelNames().remove(levelIndex);
                        levelPackage.getLevelNames().add(levelIndex + 1, level);

                        levelController.renameLevel(packageIndex, levelPackages.get(packageIndex)
                                .getName(), levelIndex, firstName, levelIndex + 1, firstName);
                        levelController.renameLevel(packageIndex, levelPackages.get(packageIndex)
                                .getName(), levelIndex + 1, secondName, levelIndex, secondName);
                        levelIndex++;
                    }
                    if (levelActions[4].isMouseOver() && levelActions[4].isEnabled()) { // RENAME
                        action = Action.RenamingLevel;
                        String name = levelPackages.get(packageIndex).getLevelNames()
                                .get(levelIndex);
                        textField.setText(name);
                        textField.setCursorPos(name.length());
                    }
                    if (levelActions[5].isMouseOver() && levelActions[5].isEnabled()) { // RESIZE
                        action = Action.ResizingLevel;
                        inputState = 0;
                        levelSize = levelController.getLevelSize(packageIndex, levelIndex);
                        textField.setText(String.valueOf(levelSize.width));
                    }
                    if (levelActions[6].isMouseOver() && levelActions[6].isEnabled()) { // DELETE
                        action = Action.DeletingLevel;
                    }
                }
            }
        }
    }

    @Override
    public int getID() {
        return this.stateId;
    }

    private void drawString(Graphics g, Font font, String text, float x, float y) {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        g.drawString(text, x - width / 2, y - height / 2);
    }

    private void drawImage(Image image, float x, float y) {
        image.draw(x, y);
    }

    private void setPackageNameRectangles() {
        packageNameRectangles = new Rectangle[5];
        for (int i = 0; i < packageNameRectangles.length && i < levelPackages.size(); i++) {
            packageNameRectangles[i] = new Rectangle();
            packageNameRectangles[i].x = width / 100;
            packageNameRectangles[i].y = height * (4 + i) / 13;
            packageNameRectangles[i].width = ubuntuSmall.getWidth(levelPackages.get(
                    i + packageBaseIndex).getName());
            packageNameRectangles[i].height = ubuntuSmall.getHeight(levelPackages.get(
                    i + packageBaseIndex).getName());
        }
    }

    private void setLevelNameRectangles() {
        levelNameRectangles = new Rectangle[5];
        LevelPackage levelPackage = levelPackages.get(packageIndex);
        ArrayList<String> levelNames = levelPackage.getLevelNames();
        for (int i = 0; i < levelNameRectangles.length && i < levelNames.size(); i++) {
            levelNameRectangles[i] = new Rectangle();
            levelNameRectangles[i].x = width / 2 + width / 100;
            levelNameRectangles[i].y = height * (4 + i) / 13;
            levelNameRectangles[i].width = ubuntuSmall.getWidth(levelNames.get(i + levelBaseIndex));
            levelNameRectangles[i].height = ubuntuSmall.getHeight(levelNames
                    .get(i + levelBaseIndex));
        }
    }

    private void initPackageActions() {
        String[] packageActionTexts = new String[5];
        packageActionTexts[0] = translator.translate("Create");
        packageActionTexts[1] = translator.translate("Move Up");
        packageActionTexts[2] = translator.translate("Move Down");
        packageActionTexts[3] = translator.translate("Rename");
        packageActionTexts[4] = translator.translate("Delete");

        packageActions = new InteractiveLabel[5];
        for (int i = 0; i < packageActions.length; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.width = ubuntuSmall.getWidth(packageActionTexts[i]);
            rectangle.height = ubuntuSmall.getHeight(packageActionTexts[i]);
            rectangle.x = width / 3 - rectangle.width / 2;
            rectangle.y = height * (4 + i) / 13;
            Point point = new Point(width / 3 - ubuntuSmall.getWidth(packageActionTexts[i]) / 2,
                    height * (4 + i) / 13);
            packageActions[i] = new InteractiveLabel(packageActionTexts[i], point, rectangle);
            packageActions[i].setColors(Color.red, Color.blue, Color.darkGray);
        }
    }

    private void initLevelActions() {
        String[] levelActionTexts = new String[7];
        levelActionTexts[0] = translator.translate("Create");
        levelActionTexts[1] = translator.translate("Edit");
        levelActionTexts[2] = translator.translate("Move Up");
        levelActionTexts[3] = translator.translate("Move Down");
        levelActionTexts[4] = translator.translate("Rename");
        levelActionTexts[5] = translator.translate("Resize");
        levelActionTexts[6] = translator.translate("Delete");

        levelActions = new InteractiveLabel[7];
        for (int i = 0; i < levelActions.length; i++) {
            Rectangle rectangle = new Rectangle();
            rectangle.width = ubuntuSmall.getWidth(levelActionTexts[i]);
            rectangle.height = ubuntuSmall.getHeight(levelActionTexts[i]);
            rectangle.x = width - width / 9 - rectangle.width / 2;
            rectangle.y = height * (3 + i) / 13;
            Point point = new Point(width - width / 9 - ubuntuSmall.getWidth(levelActionTexts[i])
                    / 2, height * (3 + i) / 13);
            levelActions[i] = new InteractiveLabel(levelActionTexts[i], point, rectangle);
            levelActions[i].setColors(Color.red, Color.blue, Color.darkGray);
        }
    }
}
