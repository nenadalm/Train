package org.train.state;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Configuration;
import org.train.app.Game;
import org.train.entity.Button;
import org.train.entity.Menu;
import org.train.entity.MenuItem;
import org.train.entity.ScrollableMenu;
import org.train.factory.ButtonFactory;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.other.LevelController;
import org.train.other.LevelPackage;
import org.train.other.ResourceManager;
import org.train.other.Translator;

public class MenuForEditorState extends BasicGameState {

    private enum Action {
        None, CreatingPackage, CreatingLevel, RenamingPackage, RenamingLevel, DeletingPackage, DeletingLevel, ResizingLevel
    }

    private Action action;
    private int width, height, packageIndex, levelIndex, inputState;
    private Font ubuntuSmall, ubuntuMedium;

    private Dimension optimalSize;
    private ArrayList<LevelPackage> levelPackages;
    private String newLevelName, infoText;
    private TextField textField;
    private Translator translator;
    private Dimension levelSize;
    private LevelController levelController;
    private Button levelArrowUp, levelArrowDown, packageArrowUp, packageArrowDown;

    private Menu packageMenu, levelMenu;
    private ScrollableMenu levelsMenu, packagesMenu;
    private Button backBtn;

    public MenuForEditorState(int stateId) {
        super(stateId);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        FontFactory fonts = this.container.getComponent(FontFactory.class);
        EffectFactory effects = this.container.getComponent(EffectFactory.class);
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        Configuration configuration = this.container.getComponent(Configuration.class);

        float scale = Float.parseFloat(configuration.get("scale"));

        translator = this.container.getComponent(Translator.class);
        width = container.getWidth();
        height = container.getHeight();

        action = Action.None;

        ubuntuSmall = fonts.getFont("ubuntu", width / 36, whiteEffect);
        ubuntuMedium = fonts.getFont("ubuntu", width / 20, whiteEffect);

        levelController = this.container.getComponent(LevelController.class);
        levelPackages = levelController.getLevels();
        optimalSize = levelController.getOptimalLevelDimension(width, height, scale);

        this.createArrowButtons();

        String backText = translator.translate("back");
        Rectangle rectangle = new Rectangle();
        rectangle.width = ubuntuSmall.getWidth(backText);
        rectangle.height = ubuntuSmall.getHeight(backText);
        rectangle.x = width / 100;
        rectangle.y = (int) (height - rectangle.height * 1.1f);
        this.createBackButton(game);

        initPackageActions();
        initLevelActions(game);

        packageIndex = -1;
        levelIndex = -1;

        createPackagesMenu();

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
        g.drawString(levelsText, width / 2 + width / 500, height / 6
                - ubuntuMedium.getHeight(packagesText) / 2 + height / 750);
        g.setColor(Color.white);
        g.drawString(packagesText, 0, height / 6 - ubuntuMedium.getHeight(packagesText) / 2);
        g.drawString(levelsText, width / 2, height / 6 - ubuntuMedium.getHeight(packagesText) / 2);
        g.setFont(ubuntuSmall);
        this.backBtn.render(g);
        this.packagesMenu.render(container, game, g);
        if (packageIndex >= 0) {
            this.levelsMenu.render(container, game, g);
        }
        String text = "";
        String showing = translator.translate("showing"), of = translator.translate("Of");
        g.setColor(Color.red);
        text = String
                .format("%4$s %1$d - %2$d %5$s %3$d", this.packagesMenu.getFirstIndex() + 1, this.packagesMenu
                        .getLastIndex(), levelPackages.size(), showing, of);
        g.drawString(text, width / 200, height * 9 / 11);

        int from, to;
        from = this.levelsMenu == null ? 0 : this.levelsMenu.getFirstIndex() + 1;
        to = this.levelsMenu == null ? 0 : this.levelsMenu.getLastIndex();
        text = String
                .format("%4$s %1$d - %2$d %5$s %3$d", from, to, (packageIndex >= 0) ? levelPackages
                        .get(packageIndex).getLevelNames().size() : 0, showing, of);
        g.drawString(text, width / 2 + width / 200, height * 9 / 11);

        g.drawString(infoText, width * 4 / 12, height * 13 / 15);

        if (action == Action.DeletingPackage || action == Action.DeletingLevel) {
            drawString(g, ubuntuSmall, String.format("%1$s %2$s? %3$s(Enter)/%4$s(Escape)", translator
                    .translate("Really"), translator.translate("delete"), translator
                    .translate("Yes"), translator.translate("No")), width / 2, height - height / 24);
        }
        this.packageMenu.render(container, game, g);
        this.levelMenu.render(container, game, g);

        this.levelArrowUp.render(g);
        this.levelArrowDown.render(g);
        this.packageArrowUp.render(g);
        this.packageArrowDown.render(g);

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

        this.backBtn.update(container, game, delta);
        this.levelArrowUp.setEnabled(this.levelsMenu != null && this.levelsMenu.hasPrev());
        this.levelArrowDown.setEnabled(this.levelsMenu != null && this.levelsMenu.hasNext());

        this.packageArrowUp.setEnabled(this.packagesMenu.hasPrev());
        this.packageArrowDown.setEnabled(this.packagesMenu.hasNext());

        this.packageMenu.getMenuItems().get(0).setEnabled(levelPackages.size() < 1000);
        this.packageMenu.getMenuItems().get(1).setEnabled(packageIndex >= 1);
        this.packageMenu.getMenuItems().get(2)
                .setEnabled(packageIndex >= 0 && packageIndex < levelPackages.size() - 1);
        this.packageMenu.getMenuItems().get(3).setEnabled(packageIndex >= 0);
        this.packageMenu.getMenuItems().get(4).setEnabled(packageIndex >= 0);
        this.packageMenu.update(container, game, delta);

        this.levelMenu
                .getMenuItems()
                .get(0)
                .setEnabled(packageIndex >= 0
                        && levelPackages.get(packageIndex).getLevelNames().size() < 100);
        this.levelMenu.getMenuItems().get(1).setEnabled(packageIndex >= 0 && levelIndex >= 0);
        this.levelMenu.getMenuItems().get(2).setEnabled(packageIndex >= 0 && levelIndex >= 1);
        this.levelMenu
                .getMenuItems()
                .get(3)
                .setEnabled(packageIndex >= 0 && levelIndex >= 0
                        && levelIndex < levelPackages.get(packageIndex).getLevelNames().size() - 1);
        this.levelMenu.getMenuItems().get(4).setEnabled(packageIndex >= 0 && levelIndex >= 0);
        this.levelMenu.getMenuItems().get(5).setEnabled(packageIndex >= 0 && levelIndex >= 0);
        this.levelMenu.getMenuItems().get(6).setEnabled(packageIndex >= 0 && levelIndex >= 0);
        this.levelMenu.update(container, game, delta);

        if (this.levelsMenu != null) {
            this.levelsMenu.update(container, game, delta);
        }

        this.packageArrowDown.update(container, game, delta);
        this.levelArrowDown.update(container, game, delta);
        this.packageArrowUp.update(container, game, delta);
        this.levelArrowUp.update(container, game, delta);

        this.packagesMenu.update(container, game, delta);

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
                    createPackagesMenu();
                } else {
                    infoText = translator.translate("EditorMenu.PackageAlreadyExist");
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
                    createPackagesMenu();
                } else {
                    infoText = translator.translate("EditorMenu.PackageAlreadyExist");
                }
            }
            if (action == Action.DeletingPackage) {
                action = Action.None;
                levelController.deletePackage(packageIndex, levelPackages.get(packageIndex)
                        .getName());
                levelPackages.remove(packageIndex);
                packageIndex--;
                createPackagesMenu();

                levelIndex = -1;
                createLevelsMenu();
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

                            levelController
                                    .createLevel(packageIndex, levelPackage.getName(), levelPackage
                                            .getLevelNames().size() - 1, newLevelName, levelSize.width, levelSize.height);
                            textField.setText("");
                            createLevelsMenu();

                            levelController.loadLevel(packageIndex, levelPackage.getLevelNames()
                                    .size() - 1);
                            game.enterState(Game.EDITOR_STATE);

                        } else {
                            infoText = translator.translate("EditorMenu.WrongLevelSize")
                                    + " 7 - 100";
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
                            infoText = translator.translate("EditorMenu.WrongLevelSize")
                                    + " 7 - 100";
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
                        infoText = translator.translate("EditorMenu.LevelAlreadyExist");
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
                    createLevelsMenu();
                } else {
                    infoText = translator.translate("EditorMenu.LevelAlreadyExist");
                }
            }
            if (action == Action.ResizingLevel) {
                try {
                    if (inputState == 1) {
                        int levelHeight = Integer.parseInt(textField.getText());
                        if (levelHeight >= 7 && levelHeight <= 100) {
                            action = Action.None;
                            levelSize.height = levelHeight;
                            levelController
                                    .resizeLevel(packageIndex, levelIndex, levelSize.width, levelSize.height);
                            textField.setText("");
                        } else {
                            infoText = translator.translate("EditorMenu.WrongLevelSize")
                                    + " 7 - 100";
                        }
                    }
                    if (inputState == 0) {
                        int levelWidth = Integer.parseInt(textField.getText());
                        if (levelWidth >= 7 && levelWidth <= 100) {
                            inputState = 1;
                            levelSize.width = levelWidth;
                            textField.setText(String.valueOf(levelSize.height));
                        } else {
                            infoText = translator.translate("EditorMenu.WrongLevelSize")
                                    + " 7 - 100";
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
                levelController.deleteLevel(packageIndex, levelPackage.getName(), levelIndex, names
                        .get(levelIndex));
                levelPackage.getLevelNames().remove(levelIndex);
                levelIndex--;
                createLevelsMenu();
            }
        }

        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            infoText = "";
        }
    }

    private void drawString(Graphics g, Font font, String text, float x, float y) {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        g.drawString(text, x - width / 2, y - height / 2);
    }

    private void createPackagesMenu() {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        for (int i = 0; i < this.levelPackages.size(); i++) {
            final int index = i;
            menuItems.add(new MenuItem(this.levelPackages.get(index).getName(),
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            packageIndex = index;
                            levelIndex = -1;
                            createLevelsMenu();
                        }
                    }));
        }
        for (MenuItem item : menuItems) {
            item.setFont(this.ubuntuSmall);
            item.setMarginBottom(height / 25);
            item.setColors(Color.white, Color.red, Color.darkGray);
        }

        this.packagesMenu = new ScrollableMenu(menuItems,
                this.container.getComponent(GameContainer.class),
                this.container.getComponent(ResourceManager.class),
                this.container.getComponent(EffectFactory.class));
        this.packagesMenu.disableKeyboard();
        this.packagesMenu.setSelectable();
        this.packagesMenu.setMarginRight((int) (width / 2.35));
        this.packagesMenu.setMaxItems(5);

    }

    private void createLevelsMenu() {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        LevelPackage levelPackage = this.levelPackages.get(this.packageIndex);
        for (int i = 0; i < levelPackage.getLevelNames().size(); i++) {
            final int index = i;
            menuItems.add(new MenuItem(levelPackage.getLevelNames().get(index),
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            levelIndex = index;
                        }
                    }));
        }
        for (MenuItem item : menuItems) {
            item.setFont(this.ubuntuSmall);
            item.setMarginBottom(height / 25);
            item.setColors(Color.white, Color.red, Color.darkGray);
        }

        this.levelsMenu = new ScrollableMenu(menuItems,
                this.container.getComponent(GameContainer.class),
                this.container.getComponent(ResourceManager.class),
                this.container.getComponent(EffectFactory.class));
        this.levelsMenu.disableKeyboard();
        this.levelsMenu.setSelectable();
        if (menuItems.size() > 0) {
            this.levelsMenu.setMarginRight(-width / 20);
            this.levelsMenu.setMaxItems(5);
        }
    }

    private void initPackageActions() {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem(translator.translate("Create"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                action = Action.CreatingPackage;
                createPackagesMenu();
            }
        }));
        menuItems.add(new MenuItem(translator.translate("MoveUp"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = levelPackages.get(packageIndex).getName();
                String secondName = levelPackages.get(packageIndex - 1).getName();
                LevelPackage levelPackage = levelPackages.remove(packageIndex - 1);
                levelPackages.add(packageIndex, levelPackage);

                levelController.renamePackage(packageIndex, firstName, packageIndex - 1, firstName);
                levelController
                        .renamePackage(packageIndex - 1, secondName, packageIndex, secondName);
                packageIndex--;
                createPackagesMenu();
            }
        }));
        menuItems.add(new MenuItem(translator.translate("MoveDown"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = levelPackages.get(packageIndex).getName();
                String secondName = levelPackages.get(packageIndex + 1).getName();
                LevelPackage levelPackage = levelPackages.remove(packageIndex);
                levelPackages.add(packageIndex + 1, levelPackage);

                levelController.renamePackage(packageIndex, firstName, packageIndex + 1, firstName);
                levelController
                        .renamePackage(packageIndex + 1, secondName, packageIndex, secondName);
                packageIndex++;
                createPackagesMenu();
            }
        }));
        menuItems.add(new MenuItem(translator.translate("Rename"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = Action.RenamingPackage;
                String name = levelPackages.get(packageIndex).getName();
                textField.setText(name);
                textField.setCursorPos(name.length());
                createPackagesMenu();
            }
        }));
        menuItems.add(new MenuItem(translator.translate("Delete"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = Action.DeletingPackage;
                createPackagesMenu();
            }
        }));
        for (MenuItem item : menuItems) {
            item.setFont(this.ubuntuSmall);
            item.setMarginBottom(height / 25);
        }
        this.packageMenu = new Menu(menuItems, this.container.getComponent(GameContainer.class),
                this.container.getComponent(ResourceManager.class),
                this.container.getComponent(EffectFactory.class));
        this.packageMenu.setMarginRight(width / 6);
        this.packageMenu.disableKeyboard();
    }

    private void initLevelActions(final StateBasedGame game) {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem(translator.translate("Create"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                action = Action.CreatingLevel;
                inputState = 0;
            }
        }));
        menuItems.add(new MenuItem(translator.translate("Edit"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    levelController.loadLevel(packageIndex, levelIndex);
                    game.enterState(Game.EDITOR_STATE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        menuItems.add(new MenuItem(translator.translate("MoveUp"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                LevelPackage levelPackage = levelPackages.get(packageIndex);
                String firstName = levelPackage.getLevelNames().get(levelIndex);
                String secondName = levelPackage.getLevelNames().get(levelIndex - 1);
                String level = levelPackage.getLevelNames().remove(levelIndex - 1);
                levelPackage.getLevelNames().add(levelIndex, level);

                levelController
                        .renameLevel(packageIndex, levelPackages.get(packageIndex).getName(), levelIndex, firstName, levelIndex - 1, firstName);
                levelController
                        .renameLevel(packageIndex, levelPackages.get(packageIndex).getName(), levelIndex - 1, secondName, levelIndex, secondName);
                levelIndex--;
            }
        }));
        menuItems.add(new MenuItem(translator.translate("MoveDown"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                LevelPackage levelPackage = levelPackages.get(packageIndex);
                String firstName = levelPackage.getLevelNames().get(levelIndex);
                String secondName = levelPackage.getLevelNames().get(levelIndex + 1);
                String level = levelPackage.getLevelNames().remove(levelIndex);
                levelPackage.getLevelNames().add(levelIndex + 1, level);

                levelController
                        .renameLevel(packageIndex, levelPackages.get(packageIndex).getName(), levelIndex, firstName, levelIndex + 1, firstName);
                levelController
                        .renameLevel(packageIndex, levelPackages.get(packageIndex).getName(), levelIndex + 1, secondName, levelIndex, secondName);
                levelIndex++;
            }
        }));
        menuItems.add(new MenuItem(translator.translate("Rename"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                action = Action.RenamingLevel;
                String name = levelPackages.get(packageIndex).getLevelNames().get(levelIndex);
                textField.setText(name);
                textField.setCursorPos(name.length());
            }
        }));
        menuItems.add(new MenuItem(translator.translate("Resize"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                action = Action.ResizingLevel;
                inputState = 0;
                levelSize = levelController.getLevelSize(packageIndex, levelIndex);
                textField.setText(String.valueOf(levelSize.width));
            }
        }));
        menuItems.add(new MenuItem(translator.translate("Delete"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                action = Action.DeletingLevel;
            }
        }));
        for (MenuItem item : menuItems) {
            item.setFont(this.ubuntuSmall);
            item.setMarginBottom(height / 25);
        }
        this.levelMenu = new Menu(menuItems, this.container.getComponent(GameContainer.class),
                this.container.getComponent(ResourceManager.class),
                this.container.getComponent(EffectFactory.class));
        this.levelMenu.setMarginRight((int) (-width / 2.6));
        this.levelMenu.disableKeyboard();
    }

    private void createBackButton(final StateBasedGame game) {
        ButtonFactory buttonFactory = this.container.getComponent(ButtonFactory.class);
        this.backBtn = buttonFactory.setDefaultColor(Color.white).setDisabledColor(Color.darkGray)
                .setOverColor(Color.red).setDefaultText(translator.translate("back"))
                .setDefaultFont(this.ubuntuSmall).setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.enterState(Game.MENU_STATE);
                    }
                }).createButton();

        this.backBtn.setPosition(new org.newdawn.slick.geom.Point(width / 100,
                (int) (height - this.backBtn.getHeight() * 1.1f)));
    }

    private void createArrowButtons() {
        ResourceManager resourceManager = this.container.getComponent(ResourceManager.class);
        ButtonFactory buttonFactory = this.container.getComponent(ButtonFactory.class);

        Image arrowUpImage = resourceManager.getImage("arrow").getScaledCopy(width / 2000f);
        Image arrowUpMouseOverImage = resourceManager.getImage("arrowMouseOver")
                .getScaledCopy(width / 2000f);
        Image arrowUpDisabledImage = resourceManager.getImage("arrowDisabled")
                .getScaledCopy(width / 2000f);

        this.levelArrowUp = buttonFactory.setNormalImage(arrowUpImage)
                .setOverImage(arrowUpMouseOverImage).setDisabledImage(arrowUpDisabledImage)
                .setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        infoText = "";
                        levelsMenu.showPrev();
                    }
                }).createButton();
        this.levelArrowUp.setRectangle(new org.newdawn.slick.geom.Rectangle(width * 7 / 12,
                height * 3 / 12, arrowUpImage.getWidth(), arrowUpImage.getHeight()));

        this.packageArrowUp = buttonFactory.setNormalImage(arrowUpImage)
                .setOverImage(arrowUpMouseOverImage).setDisabledImage(arrowUpDisabledImage)
                .setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        infoText = "";
                        packagesMenu.showPrev();
                    }
                }).createButton();
        this.packageArrowUp.setRectangle(new org.newdawn.slick.geom.Rectangle(width / 12,
                height * 3 / 12, arrowUpImage.getWidth(), arrowUpImage.getHeight()));

        Image arrowDown = arrowUpImage.getFlippedCopy(false, true);
        Image arrowDisabledDown = arrowUpDisabledImage.getFlippedCopy(false, true);
        Image arrowMouseOverDown = arrowUpMouseOverImage.getFlippedCopy(false, true);

        this.packageArrowDown = buttonFactory.setNormalImage(arrowDown)
                .setOverImage(arrowMouseOverDown).setDisabledImage(arrowDisabledDown)
                .setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        infoText = "";
                        packagesMenu.showNext();
                    }
                }).createButton();
        this.packageArrowDown.setRectangle(new org.newdawn.slick.geom.Rectangle(width / 12,
                height * 9 / 12, arrowDown.getWidth(), arrowDown.getHeight()));

        this.levelArrowDown = buttonFactory.setNormalImage(arrowDown)
                .setOverImage(arrowMouseOverDown).setDisabledImage(arrowDisabledDown)
                .setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        infoText = "";
                        levelsMenu.showNext();
                    }
                }).createButton();
        this.levelArrowDown.setRectangle(new org.newdawn.slick.geom.Rectangle(width * 7 / 12,
                height * 9 / 12, arrowDown.getWidth(), arrowDown.getHeight()));
    }
}
