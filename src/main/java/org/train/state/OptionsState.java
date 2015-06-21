package org.train.state;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Configuration;
import org.train.app.Game;
import org.train.entity.Button;
import org.train.entity.MenuItem;
import org.train.entity.ScrollableMenu;
import org.train.factory.ButtonFactory;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.other.LevelController;
import org.train.other.ResourceManager;
import org.train.other.Translator;

public class OptionsState extends BasicGameState {

    private boolean isMouseOverResolution, isMouseOverScale, isFullscreen, isAutoscale, isHolding;
    private int width, height, languageIndex, modeIndex, scale, holdCounter;
    private Font ubuntuSmall, ubuntuMedium, ubuntuLarge;
    private Rectangle scaleRectangle;
    private DisplayMode displayModes[];
    private Dimension size;
    private Image wall, wallPreview;

    private String languages[], yes, no, scaleText, sizeText;
    private Translator translator;
    private Configuration configuration;

    private Button backBtn, saveBtn;

    private ScrollableMenu resolutionMenu, languageMenu, fullscreenMenu, autoscaleMenu;

    public OptionsState(int stateId) {
        super(stateId);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        FontFactory fonts = this.container.getComponent(FontFactory.class);
        EffectFactory effects = this.container.getComponent(EffectFactory.class);
        translator = this.container.getComponent(Translator.class);
        configuration = this.container.getComponent(Configuration.class);
        width = container.getWidth();
        height = container.getHeight();
        scale = (int) (Float.parseFloat(configuration.get("scale")) * 100);

        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        ubuntuSmall = fonts.getFont("ubuntu", width / 40, whiteEffect);
        ubuntuMedium = fonts.getFont("ubuntu", width / 20, whiteEffect);
        ubuntuLarge = fonts.getFont("ubuntu", width / 16, whiteEffect);

        this.createBackButton(game);
        this.createSaveButton(game);

        translate();
        wall = new Image(configuration.get("contentPath") + "graphics/wall.png");
        wallPreview = wall.getScaledCopy(scale / 512f);

        ArrayList<DisplayMode> modes = new ArrayList<DisplayMode>();
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = environment.getDefaultScreenDevice();
        for (DisplayMode mode : graphicsDevice.getDisplayModes()) {
            if (mode.getRefreshRate() == 60
                    && (mode.getBitDepth() == 32 || mode.getBitDepth() == -1)) {
                modes.add(mode);
            }
        }
        Collections.sort(modes, new Comparator<DisplayMode>() {
            @Override
            public int compare(DisplayMode o1, DisplayMode o2) {
                if (o1.getWidth() - o2.getWidth() != 0) {
                    return o1.getWidth() - o2.getWidth();
                }
                return o1.getHeight() - o2.getHeight();
            }
        });
        int index = 0;
        displayModes = new DisplayMode[modes.size()];
        modes.toArray(displayModes);
        for (DisplayMode mode : displayModes) {
            if (mode.getWidth() == width && mode.getHeight() == height) {
                modeIndex = index;
                break;
            }
            index++;
        }

        File translations = new File(configuration.get("contentPath") + "translations/");
        translations.listFiles();
        ArrayList<String> names = new ArrayList<String>();
        index = 0;
        for (File file : translations.listFiles()) {
            if (file.isDirectory()) {
                names.add(file.getName());
                if (translator.getLanguageCode().equals(file.getName())) {
                    languageIndex = index;
                }
            }
            index++;
        }
        languages = new String[names.size()];
        names.toArray(languages);

        isFullscreen = container.isFullscreen();
        isAutoscale = Boolean.parseBoolean(configuration.get("autoscale"));

        this.createResolutionMenu(container, effects);
        this.createLanguageMenu(container, effects);
        this.createFullscreenMenu(container, effects);
        this.createAutoscaleMenu(container, effects);

        scaleRectangle = new Rectangle();
        setScaleRectangle();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        g.setFont(ubuntuLarge);
        g.setColor(Color.white);

        String options = translator.translate("Options");
        drawString(g, ubuntuLarge, options, width / 2, (ubuntuLarge.getHeight(options) / 2.25f));
        g.setFont(ubuntuMedium);

        drawString(g, ubuntuMedium, translator.translate("Resolution") + ":", width / 6 * 2, height * 2 / 10);
        drawString(g, ubuntuMedium, translator.translate("Fullscreen") + ":", width / 6 * 2, height * 3 / 10);
        drawString(g, ubuntuMedium, translator.translate("Language") + ":", width / 6 * 2, height * 4 / 10);
        drawString(g, ubuntuMedium, translator.translate("Autoscale") + ":", width / 6 * 2, height * 5 / 10);
        drawString(g, ubuntuMedium, translator.translate("Scale") + ":", width / 6 * 2, height * 6 / 10);

        g.setColor((isMouseOverResolution) ? Color.blue : Color.red);
        this.resolutionMenu.render(container, game, g);
        this.languageMenu.render(container, game, g);
        this.fullscreenMenu.render(container, game, g);
        this.autoscaleMenu.render(container, game, g);
        g.setColor((isAutoscale) ? Color.darkGray : (isMouseOverScale) ? Color.blue : Color.red);
        drawString(g, ubuntuMedium, scaleText, width / 6 * 4, height * 6 / 10);

        this.saveBtn.render(g);
        this.backBtn.render(g);

        g.setFont(ubuntuSmall);
        g.setColor((size.width > 7 && size.height > 7 && size.width <= 100 && size.height <= 100) ? Color.white
                : Color.blue);
        drawString(g, ubuntuSmall, sizeText, width / 6 * 4, height * 8 / 12);
        g.drawImage(wallPreview, width * 5 / 6, height * 7 / 10);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();
        Point mouse = new Point(input.getMouseX(), input.getMouseY());

        this.resolutionMenu.update(container, game, delta);
        this.languageMenu.update(container, game, delta);
        this.fullscreenMenu.update(container, game, delta);
        this.autoscaleMenu.update(container, game, delta);
        isMouseOverScale = scaleRectangle.contains(mouse) && !isAutoscale;
        this.saveBtn.update(container, game, delta);
        this.backBtn.update(container, game, delta);
        isHolding = false;

        if (input.isKeyPressed(Input.KEY_ENTER)) {
            saveOptions(container, game);
            game.enterState(Game.MENU_STATE);
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }
        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            if (isMouseOverScale && scale < 512 && size.width > 7 && size.height > 7) {
                isHolding = true;
                if (holdCounter == 0) {
                    scale++;
                } else if (holdCounter <= 60) {
                    if (holdCounter % 6 == 0) {
                        scale++;
                    }
                } else if (holdCounter <= 120) {
                    if (holdCounter % 2 == 0) {
                        scale++;
                    }
                } else {
                    scale++;
                }
                holdCounter++;
                setScaleRectangle();
            }
        }
        if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
            if (isMouseOverScale && scale > 25 && size.width <= 100 && size.height <= 100) {
                isHolding = true;
                if (holdCounter == 0) {
                    scale--;
                } else if (holdCounter <= 60) {
                    if (holdCounter % 6 == 0) {
                        scale--;
                    }
                } else if (holdCounter <= 120) {
                    if (holdCounter % 2 == 0) {
                        scale--;
                    }
                } else {
                    scale--;
                }
                holdCounter++;
                setScaleRectangle();
            }
        }
        if (!isHolding) {
            holdCounter = 0;
        }
    }

    private void saveOptions(GameContainer container, StateBasedGame game) {
        try {
            configuration.set("language", languages[languageIndex]);
            configuration.set("fullscreen", String.valueOf(isFullscreen));
            configuration.set("autoscale", String.valueOf(isAutoscale));
            configuration.set("scale", String.valueOf(scale / 100f));

            if (displayModes.length != 0) {
                configuration.set("width", String.valueOf(displayModes[modeIndex].getWidth()));
                configuration.set("height", String.valueOf(displayModes[modeIndex].getHeight()));
                ((AppGameContainer) container)
                        .setDisplayMode(displayModes[modeIndex].getWidth(), displayModes[modeIndex]
                                .getHeight(), isFullscreen);
            }

            configuration.saveChanges();

            if (!translator.getLanguageCode().equals(languages[languageIndex])) {
                translate();
            }
            translator.setLanguage(languages[languageIndex]);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    private void drawString(Graphics g, Font font, String text, float x, float y) {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        g.drawString(text, x - width / 2, y - height / 2);
    }

    private void setScaleRectangle() {
        size = this.container.getComponent(LevelController.class)
                .getOptimalLevelDimension(width, height, scale / 100f);
        sizeText = String.format("[%1$02d , %2$02d]", size.width, size.height);
        scaleText = String.format("%1$d.%2$02dx", scale / 100, scale % 100);
        wallPreview = wall.getScaledCopy(scale / 512f);
        setRectangle(scaleRectangle, scaleText, width * 4 / 6, height * 6 / 10);
    }

    private void setRectangle(Rectangle rectangle, String text, int x, int y) {
        rectangle.width = ubuntuMedium.getWidth(text);
        rectangle.height = ubuntuMedium.getHeight(text);
        rectangle.x = x - rectangle.width / 2;
        rectangle.y = y - rectangle.height / 2;
    }

    private void createBackButton(final StateBasedGame game) {
        ButtonFactory buttonFactory = this.container.getComponent(ButtonFactory.class);
        this.backBtn = buttonFactory.setDefaultColor(Color.white).setDisabledColor(Color.darkGray)
                .setOverColor(Color.red).setDefaultText(translator.translate("back"))
                .setDefaultFont(ubuntuMedium).setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.enterState(Game.MENU_STATE);
                    }
                }).createButton();

        this.backBtn.setPosition(new org.newdawn.slick.geom.Point((int) (width / 1.5 - this.backBtn
                .getWidth() / 2), (int) (height - this.backBtn.getHeight() * 1.2f)));
    }

    private void createSaveButton(final StateBasedGame game) {
        ButtonFactory buttonFactory = this.container.getComponent(ButtonFactory.class);
        this.saveBtn = buttonFactory.setDefaultColor(Color.white).setDisabledColor(Color.darkGray)
                .setOverColor(Color.red).setDefaultText(translator.translate("save"))
                .setDefaultFont(ubuntuMedium).setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveOptions(game.getContainer(), game);
                        game.enterState(Game.MENU_STATE);
                    }
                }).createButton();

        this.saveBtn.setPosition(new org.newdawn.slick.geom.Point(width / 3
                - this.saveBtn.getWidth() / 2, (int) (height - this.backBtn.getHeight() * 1.2f)));
    }

    private void translate() {
        yes = translator.translate("yes");
        no = translator.translate("no");
    }

    private void createAutoscaleMenu(GameContainer container, EffectFactory effects) {
        String[] items = new String[2];
        if (isAutoscale) {
            items[0] = yes;
            items[1] = no;
        } else {
            items[0] = no;
            items[1] = yes;
        }

        List<MenuItem> autoscaleMenuItems = new ArrayList<>();
        for (String text : items) {
            MenuItem menuItem = new MenuItem(text, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    isAutoscale = !isAutoscale;
                    autoscaleMenu.scrollDown();
                }
            }, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    isAutoscale = !isAutoscale;
                    autoscaleMenu.scrollUp();
                }
            });

            autoscaleMenuItems.add(menuItem);
        }

        ResourceManager resourceManager = this.container.getComponent(ResourceManager.class);
        this.autoscaleMenu = new ScrollableMenu(autoscaleMenuItems, container, resourceManager, effects);
        this.configureOptionMenu(this.autoscaleMenu, width * 4 / 6, height * 5 / 10);
    }

    private void createFullscreenMenu(GameContainer container, EffectFactory effects) {
        String[] items = new String[2];
        if (isFullscreen) {
            items[0] = yes;
            items[1] = no;
        } else {
            items[0] = no;
            items[1] = yes;
        }

        List<MenuItem> fullscreenMenuItems = new ArrayList<>();
        for (String text : items) {
            MenuItem menuItem = new MenuItem(text, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    isFullscreen = !isFullscreen;
                    fullscreenMenu.scrollDown();
                }
            }, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    isFullscreen = !isFullscreen;
                    fullscreenMenu.scrollUp();
                }
            });

            fullscreenMenuItems.add(menuItem);
        }

        ResourceManager resourceManager = this.container.getComponent(ResourceManager.class);
        this.fullscreenMenu = new ScrollableMenu(fullscreenMenuItems, container, resourceManager, effects);
        this.configureOptionMenu(this.fullscreenMenu, width * 4 / 6, height * 3 / 10);
    }

    private void createLanguageMenu(GameContainer container, EffectFactory effects) {
        int[] languageIndexOrder = this.getOrderedMenuItemIndexes(languages, languageIndex);

        List<MenuItem> languageMenuItems = new ArrayList<>();
        for (int i = 0; i < languageIndexOrder.length; i++) {
            String language = languages[languageIndexOrder[i]];

            MenuItem menuItem = new MenuItem(language, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    languageIndex++;
                    if (languageIndex == languages.length) {
                        languageIndex = 0;
                    }
                    languageMenu.scrollDown();
                }
            }, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    languageIndex--;
                    if (languageIndex == -1) {
                        languageIndex = languages.length - 1;
                    }
                    languageMenu.scrollUp();
                }
            });
            languageMenuItems.add(menuItem);
        }

        ResourceManager resourceManager = this.container.getComponent(ResourceManager.class);
        this.languageMenu = new ScrollableMenu(languageMenuItems, container, resourceManager, effects);
        this.configureOptionMenu(this.languageMenu, width * 4 / 6, height * 4 / 10);
    }

    private void createResolutionMenu(GameContainer container, EffectFactory effects) {
        int [] modeIndexOrder = this.getOrderedMenuItemIndexes(displayModes, modeIndex);

        List<MenuItem> resolutionMenuItems = new ArrayList<>();
        for (int i = 0; i < modeIndexOrder.length; i++) {
            DisplayMode displayMode = displayModes[modeIndexOrder[i]];

            String text = String
                .format("%1$dx%2$d", displayMode.getWidth(), displayMode.getHeight());

            MenuItem menuItem = new MenuItem(text, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    modeIndex++;
                    if (modeIndex >= displayModes.length) {
                        modeIndex = 0;
                    }
                    resolutionMenu.scrollDown();
                }
            }, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    modeIndex--;
                    if (modeIndex == -1) {
                        modeIndex = displayModes.length - 1;
                    }
                    resolutionMenu.scrollUp();
                }
            });
            resolutionMenuItems.add(menuItem);
        }
        ResourceManager resourceManager = this.container.getComponent(ResourceManager.class);
        this.resolutionMenu = new ScrollableMenu(resolutionMenuItems, container, resourceManager, effects);
        this.configureOptionMenu(this.resolutionMenu, width * 4 / 6, height * 2 / 10);
    }

    private int[] getOrderedMenuItemIndexes(Object[] options, int selectedIndex) {
        int[] orderedIndexes = new int[options.length];
        for (int i = selectedIndex; i < options.length; i++) {
            orderedIndexes[i - selectedIndex] = i;
        }
        for (int i = 0; i < selectedIndex; i++) {
            orderedIndexes[options.length - selectedIndex + i] = i;
        }

        return orderedIndexes;
    }

    private void configureOptionMenu(ScrollableMenu menu, int resolutionMenuX, int resolutionMenuY) {
        menu.setMarginRight(width / 2 - resolutionMenuX);
        menu.setMarginTop(height / 2 - resolutionMenuY);
        menu.setMaxItems(1);
        menu.disableKeyboard();
        menu.enableRoundScroll();
    }
}
