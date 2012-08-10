package state;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.Translator;
import app.Configuration;
import app.Game;

public class MenuState extends BasicGameState {

    private boolean isMouseOverStartGame, isMouseOverLevelEditor, isMouseOverOptions,
            isMouseOverExit, isMouseOverReturn, isMouseOverSave, isMouseOverResolution,
            isMouseOverFullscreen, isMouseOverLanguage, isInOptions, isFullscreen;
    private int stateId, languageIndex, width, height, distanceBetweenMenuEntries, modeIndex,
            trainTextWidth, trainTextHeight;
    private UnicodeFont ubuntuMedium, ubuntuLarge;
    private Rectangle startGameRectangle, levelEditorRectangle, optionsRectangle, exitRectangle,
            saveRectangle, returnRectangle, resolutionRectangle, fullscreenRectangle,
            languageRectangle;
    private DisplayMode displayModes[];
    private Input input;
    private Point mouse;
    private String resolutionText, languages[];
    private Translator translator;

    public MenuState(int stateId) {
        this.stateId = stateId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.translator = Translator.getInstance();
        this.width = container.getWidth();
        this.height = container.getHeight();
        String fontPath = Game.CONTENT_PATH + "fonts/ubuntu.ttf";
        this.ubuntuMedium = new UnicodeFont(fontPath, this.width / 25, false, false);
        this.ubuntuMedium.addGlyphs(32, 800);
        this.ubuntuMedium.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        this.ubuntuMedium.loadGlyphs();

        this.ubuntuLarge = new UnicodeFont(fontPath, this.width / 16, false, false);
        this.ubuntuLarge.addGlyphs(32, 800);
        this.ubuntuLarge.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        this.ubuntuLarge.loadGlyphs();

        this.distanceBetweenMenuEntries = this.height / 5;

        this.startGameRectangle = new Rectangle();
        this.startGameRectangle.width = this.ubuntuMedium.getWidth(this.translator
                .translate("new game"));
        this.startGameRectangle.height = this.ubuntuMedium.getHeight(this.translator
                .translate("new game"));
        this.startGameRectangle.x = this.width / 2 - this.startGameRectangle.width / 2;
        this.startGameRectangle.y = this.distanceBetweenMenuEntries
                - this.startGameRectangle.height / 2;

        this.levelEditorRectangle = new Rectangle();
        this.levelEditorRectangle.width = this.ubuntuMedium.getWidth(this.translator
                .translate("level editor"));
        this.levelEditorRectangle.height = this.ubuntuMedium.getHeight(this.translator
                .translate("level editor"));
        this.levelEditorRectangle.x = this.width / 2 - this.levelEditorRectangle.width / 2;
        this.levelEditorRectangle.y = this.distanceBetweenMenuEntries * 2
                - this.levelEditorRectangle.height / 2;

        this.optionsRectangle = new Rectangle();
        this.optionsRectangle.width = this.ubuntuMedium.getWidth(this.translator
                .translate("options"));
        this.optionsRectangle.height = this.ubuntuMedium.getHeight(this.translator
                .translate("options"));
        this.optionsRectangle.x = this.width / 2 - this.optionsRectangle.width / 2;
        this.optionsRectangle.y = this.distanceBetweenMenuEntries * 3
                - this.optionsRectangle.height / 2;

        this.exitRectangle = new Rectangle();
        this.exitRectangle.width = this.ubuntuMedium.getWidth(this.translator.translate("exit"));
        this.exitRectangle.height = this.ubuntuMedium.getHeight(this.translator.translate("exit"));
        this.exitRectangle.x = this.width / 2 - this.exitRectangle.width / 2;
        this.exitRectangle.y = this.distanceBetweenMenuEntries * 4 - this.exitRectangle.height / 2;

        this.trainTextWidth = this.ubuntuLarge.getWidth("Train 1.0");
        this.trainTextHeight = this.ubuntuLarge.getHeight("Train 1.0");
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        if (!this.isInOptions) {
            this.drawMenu(container, game, g);
        } else {
            this.drawOptions(container, game, g);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        this.input = container.getInput();
        this.mouse = new Point(this.input.getMouseX(), this.input.getMouseY());
        if (!this.isInOptions) {
            this.updateMenu(container, game, delta);
        } else {
            this.updateOptions(container, game, delta);
        }
    }

    @Override
    public int getID() {
        return this.stateId;
    }

    private void drawMenu(GameContainer container, StateBasedGame game, Graphics g) {
        g.setFont(this.ubuntuLarge);
        g.setColor(Color.gray);
        this.drawString(g, this.ubuntuLarge, "Train 1.0", (int) (this.trainTextWidth / 1.75) + 3,
                (int) (this.trainTextHeight / 1.5) + 2);
        g.setColor(Color.white);
        this.drawString(g, this.ubuntuLarge, "Train 1.0", (int) (this.trainTextWidth / 1.75),
                (int) (this.trainTextHeight / 1.5));
        g.setFont(this.ubuntuMedium);
        g.setColor((this.isMouseOverStartGame) ? Color.blue : Color.red);
        this.drawString(g, this.ubuntuMedium, this.translator.translate("start game"),
                this.width / 2, this.distanceBetweenMenuEntries);
        g.setColor((this.isMouseOverLevelEditor) ? Color.blue : Color.red);
        this.drawString(g, this.ubuntuMedium, this.translator.translate("level editor"),
                this.width / 2, this.distanceBetweenMenuEntries * 2);
        g.setColor((this.isMouseOverOptions) ? Color.blue : Color.red);
        this.drawString(g, this.ubuntuMedium, this.translator.translate("options"), this.width / 2,
                this.distanceBetweenMenuEntries * 3);
        g.setColor((this.isMouseOverExit) ? Color.blue : Color.red);
        this.drawString(g, this.ubuntuMedium, this.translator.translate("exit"), this.width / 2,
                this.distanceBetweenMenuEntries * 4);
    }

    private void drawOptions(GameContainer container, StateBasedGame game, Graphics g) {
        g.setFont(this.ubuntuLarge);
        g.setColor(Color.white);
        this.drawString(g, this.ubuntuLarge, this.translator.translate("Options"), this.width / 2,
                (int) (this.ubuntuLarge.getHeight(this.translator.translate("Options")) / 1.75));
        g.setFont(this.ubuntuMedium);
        this.drawString(g, this.ubuntuMedium, this.translator.translate("Resolution") + ":",
                this.width / 5 * 2, this.height / 8 * 2);
        this.drawString(g, this.ubuntuMedium, this.translator.translate("Fullscreen") + ":",
                this.width / 5 * 2, this.height / 8 * 3);
        this.drawString(g, this.ubuntuMedium, this.translator.translate("Language") + ":",
                this.width / 5 * 2, this.height / 8 * 4);
        g.setColor((this.isMouseOverResolution) ? Color.blue : Color.red);
        this.drawString(g, this.ubuntuMedium, this.resolutionText, this.width / 5 * 3,
                this.height / 8 * 2);
        g.setColor((this.isMouseOverFullscreen) ? Color.blue : Color.red);
        this.drawString(g, this.ubuntuMedium, this.isFullscreen ? this.translator.translate("yes")
                : this.translator.translate("no"), this.width / 5 * 3, this.height / 8 * 3);
        g.setColor((this.isMouseOverLanguage) ? Color.blue : Color.red);
        this.drawString(g, this.ubuntuMedium, this.languages[this.languageIndex],
                this.width / 5 * 3, this.height / 8 * 4);

        g.setColor((this.isMouseOverSave) ? Color.red : Color.white);
        this.drawString(g, this.ubuntuMedium, this.translator.translate("save"), this.width / 3,
                this.height - this.ubuntuMedium.getHeight(this.translator.translate("save")));
        g.setColor((this.isMouseOverReturn) ? Color.red : Color.white);
        this.drawString(g, this.ubuntuMedium, this.translator.translate("return"),
                (int) (this.width / 1.5),
                this.height - this.ubuntuMedium.getHeight(this.translator.translate("return")));
    }

    private void drawString(Graphics g, UnicodeFont font, String text, float x, float y) {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        g.drawString(text, x - width / 2, y - height / 2);
    }

    private void updateMenu(GameContainer container, StateBasedGame game, int delta) {
        this.isMouseOverStartGame = this.startGameRectangle.contains(this.mouse);
        this.isMouseOverLevelEditor = this.levelEditorRectangle.contains(this.mouse);
        this.isMouseOverOptions = this.optionsRectangle.contains(this.mouse);
        this.isMouseOverExit = this.exitRectangle.contains(this.mouse);

        if (this.input.isKeyDown(Input.KEY_ESCAPE)) {
            container.exit();
        }

        if (this.input.isMousePressed(0)) {
            if (this.isMouseOverLevelEditor) {
                game.enterState(Game.EDITOR_STATE);
            }
            if (this.isMouseOverOptions) {
                this.isInOptions = true;
                this.initOptions(container);
            }
            if (this.isMouseOverExit) {
                container.exit();
            }
        }
    }

    private void updateOptions(GameContainer container, StateBasedGame game, int delta) {
        this.isMouseOverResolution = this.resolutionRectangle.contains(this.mouse);
        this.isMouseOverFullscreen = this.fullscreenRectangle.contains(this.mouse);
        this.isMouseOverLanguage = this.languageRectangle.contains(this.mouse);
        this.isMouseOverSave = this.saveRectangle.contains(this.mouse);
        this.isMouseOverReturn = this.returnRectangle.contains(this.mouse);

        if (this.input.isKeyDown(Input.KEY_ENTER)) {
            this.saveOptions(container, game);
        }
        if (this.input.isKeyDown(Input.KEY_ESCAPE)) {
            this.isInOptions = false;
        }
        if (this.input.isMousePressed(0)) {
            if (this.isMouseOverResolution) {
                this.modeIndex++;
                if (this.modeIndex == this.displayModes.length) {
                    this.modeIndex = 0;
                }
                this.setResolutionRectangle();
            }
            if (this.isMouseOverFullscreen) {
                this.isFullscreen = !this.isFullscreen;
                this.setFullscreenRectangle();
            }
            if (this.isMouseOverLanguage) {
                this.languageIndex++;
                if (this.languageIndex == this.languages.length) {
                    this.languageIndex = 0;
                }
                this.setLanguageRectangle();
            }
            if (this.isMouseOverLanguage) {
                this.setLanguageRectangle();
            }
            if (this.isMouseOverSave) {
                this.saveOptions(container, game);
            }
            if (this.isMouseOverReturn) {
                this.isInOptions = false;
            }
        }
        if (this.input.isMousePressed(1)) {
            if (this.isMouseOverResolution) {
                this.modeIndex--;
                if (this.modeIndex == -1) {
                    this.modeIndex = this.displayModes.length - 1;
                }
                this.setResolutionRectangle();
            }
            if (this.isMouseOverLanguage) {
                this.languageIndex--;
                if (this.languageIndex == -1) {
                    this.languageIndex = this.languages.length - 1;
                }
                this.setLanguageRectangle();
            }
        }
    }

    private void initOptions(GameContainer container) {
        ArrayList<DisplayMode> modes = new ArrayList<DisplayMode>();
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = environment.getDefaultScreenDevice();
        int index = 0;
        for (DisplayMode mode : graphicsDevice.getDisplayModes()) {
            if (mode.getRefreshRate() == 60 && mode.getBitDepth() == 32) {
                modes.add(mode);
                if (mode.getWidth() == this.width && mode.getHeight() == this.height) {
                    this.modeIndex = index;
                }
                index++;
            }
        }
        this.displayModes = new DisplayMode[modes.size()];
        modes.toArray(this.displayModes);

        File translations = new File(Game.CONTENT_PATH + "translations/");
        translations.listFiles();
        ArrayList<String> names = new ArrayList<String>();
        index = 0;
        for (File file : translations.listFiles()) {
            if (file.isDirectory()) {
                names.add(file.getName());
                if (this.translator.getLanguageCode().equals(file.getName())) {
                    this.languageIndex = index;
                }
            }
            index++;
        }
        this.languages = new String[names.size()];
        names.toArray(this.languages);

        this.isFullscreen = container.isFullscreen();

        this.resolutionRectangle = new Rectangle();
        this.setResolutionRectangle();

        this.fullscreenRectangle = new Rectangle();
        this.setFullscreenRectangle();

        this.languageRectangle = new Rectangle();
        this.setLanguageRectangle();

        this.saveRectangle = new Rectangle();
        this.saveRectangle.width = this.ubuntuMedium.getWidth(this.translator.translate("save"));
        this.saveRectangle.height = this.ubuntuMedium.getHeight(this.translator.translate("save"));
        this.saveRectangle.x = this.width / 3 - this.saveRectangle.width / 2;
        this.saveRectangle.y = this.height - this.saveRectangle.height - this.saveRectangle.height
                / 2;

        this.returnRectangle = new Rectangle();
        this.returnRectangle.width = this.ubuntuMedium
                .getWidth(this.translator.translate("return"));
        this.returnRectangle.height = this.ubuntuMedium.getHeight(this.translator
                .translate("return"));
        this.returnRectangle.x = (int) (this.width / 1.5) - this.returnRectangle.width / 2;
        this.returnRectangle.y = this.height - this.returnRectangle.height
                - this.returnRectangle.height / 2;
    }

    private void saveOptions(GameContainer container, StateBasedGame game) {
        try {
            Configuration configuration = Configuration.getInstance();
            configuration
                    .set("width", String.valueOf(this.displayModes[this.modeIndex].getWidth()));
            configuration.set("height",
                    String.valueOf(this.displayModes[this.modeIndex].getHeight()));
            configuration.set("language", this.languages[this.languageIndex]);
            configuration.set("fullscreen", String.valueOf(this.isFullscreen));
            ((AppGameContainer) container).setDisplayMode(
                    this.displayModes[this.modeIndex].getWidth(),
                    this.displayModes[this.modeIndex].getHeight(), this.isFullscreen);
            this.translator.setLanguage(this.languages[this.languageIndex]);
            this.init(container, game);
        } catch (SlickException e) {
            e.printStackTrace();
        }
        this.isInOptions = false;
    }

    private void setResolutionRectangle() {
        this.resolutionText = String.format("%1$2sx%2$2s",
                this.displayModes[this.modeIndex].getWidth(),
                this.displayModes[this.modeIndex].getHeight());
        this.resolutionRectangle.width = this.ubuntuMedium.getWidth(this.resolutionText);
        this.resolutionRectangle.height = this.ubuntuMedium.getHeight(this.resolutionText);
        this.resolutionRectangle.x = this.width / 5 * 3 - this.resolutionRectangle.width / 2;
        this.resolutionRectangle.y = this.height / 8 * 2 - this.resolutionRectangle.height / 2;
    }

    private void setFullscreenRectangle() {
        this.fullscreenRectangle.width = this.ubuntuMedium
                .getWidth(this.isFullscreen ? this.translator.translate("yes") : this.translator
                        .translate("no"));
        this.fullscreenRectangle.height = this.ubuntuMedium
                .getHeight(this.isFullscreen ? this.translator.translate("yes") : this.translator
                        .translate("no"));
        this.fullscreenRectangle.x = this.width / 5 * 3 - this.fullscreenRectangle.width / 2;
        this.fullscreenRectangle.y = this.height / 8 * 3 - this.fullscreenRectangle.height / 2;
    }

    private void setLanguageRectangle() {
        this.languageRectangle.width = this.ubuntuMedium
                .getWidth(this.languages[this.languageIndex]);
        this.languageRectangle.height = this.ubuntuMedium
                .getHeight(this.languages[this.languageIndex]);
        this.languageRectangle.x = this.width / 5 * 3 - this.languageRectangle.width / 2;
        this.languageRectangle.y = this.height / 8 * 4 - this.languageRectangle.height / 2;
    }
}