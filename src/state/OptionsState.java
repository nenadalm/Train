package state;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.Translator;
import app.Configuration;
import app.Game;
import factory.EffectFactory;
import factory.FontFactory;

public class OptionsState extends BasicGameState {

    private boolean isMouseOverReturn, isMouseOverSave, isMouseOverResolution,
            isMouseOverFullscreen, isMouseOverLanguage, isMouseOverAutoscale, isFullscreen,
            isAutoscale;
    private int stateId, width, height, languageIndex, modeIndex;
    private Font ubuntuMedium, ubuntuLarge;
    private Rectangle saveRectangle, returnRectangle, resolutionRectangle, fullscreenRectangle,
            languageRectangle, autoscaleRectangle;
    private DisplayMode displayModes[];
    private Input input;
    private Point mouse;
    private String resolutionText, languages[], yes, no, save;
    private Translator translator;
    private Configuration configuration;

    public OptionsState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        FontFactory fonts = FontFactory.getInstance();
        EffectFactory effects = EffectFactory.getInstance();
        translator = Translator.getInstance();
        configuration = Configuration.getInstance();
        width = container.getWidth();
        height = container.getHeight();

        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        ubuntuMedium = fonts.getFont("ubuntu", width / 20, whiteEffect);
        ubuntuLarge = fonts.getFont("ubuntu", width / 16, whiteEffect);

        translate();

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

        File translations = new File(Game.CONTENT_PATH + "translations/");
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

        resolutionRectangle = new Rectangle();
        setResolutionRectangle();

        fullscreenRectangle = new Rectangle();
        setFullscreenRectangle();

        languageRectangle = new Rectangle();
        setLanguageRectangle();

        autoscaleRectangle = new Rectangle();
        setAutoscaleRectangle();

        saveRectangle = new Rectangle();
        saveRectangle.width = ubuntuMedium.getWidth(save);
        saveRectangle.height = ubuntuMedium.getHeight(save);
        saveRectangle.x = (width / 3) - saveRectangle.width / 2;
        saveRectangle.y = (height - saveRectangle.height) - saveRectangle.height / 6;

        returnRectangle = new Rectangle();
        returnRectangle.width = ubuntuMedium.getWidth(translator.translate("return"));
        returnRectangle.height = ubuntuMedium.getHeight(save);
        returnRectangle.x = (int) (width / 1.5) - returnRectangle.width / 2;
        returnRectangle.y = (height - returnRectangle.height) - returnRectangle.height / 6;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        g.setFont(ubuntuLarge);
        g.setColor(Color.white);

        String options = translator.translate("Options");
        drawString(g, ubuntuLarge, options, width / 2, (ubuntuLarge.getHeight(options) / 2.25f));
        g.setFont(ubuntuMedium);

        drawString(g, ubuntuMedium, translator.translate("Resolution") + ":", width / 6 * 2,
                height * 2 / 10);
        drawString(g, ubuntuMedium, translator.translate("Fullscreen") + ":", width / 6 * 2,
                height * 3 / 10);
        drawString(g, ubuntuMedium, translator.translate("Language") + ":", width / 6 * 2,
                height * 4 / 10);
        drawString(g, ubuntuMedium, translator.translate("Autoscale") + ":", width / 6 * 2,
                height * 5 / 10);

        g.setColor((isMouseOverResolution) ? Color.blue : Color.red);
        drawString(g, ubuntuMedium, resolutionText, width / 6 * 4, height * 2 / 10);
        g.setColor((isMouseOverFullscreen) ? Color.blue : Color.red);
        drawString(g, ubuntuMedium, isFullscreen ? yes : no, width / 6 * 4, height * 3 / 10);
        g.setColor((isMouseOverLanguage) ? Color.blue : Color.red);
        drawString(g, ubuntuMedium, languages[languageIndex], width / 6 * 4, height * 4 / 10);
        g.setColor((isMouseOverAutoscale) ? Color.blue : Color.red);
        drawString(g, ubuntuMedium, isAutoscale ? yes : no, width / 6 * 4, height * 5 / 10);

        g.setColor((isMouseOverSave) ? Color.red : Color.white);
        drawString(g, ubuntuMedium, translator.translate("save"), width / 3,
                (height - saveRectangle.height * 3 / 4));
        g.setColor((isMouseOverReturn) ? Color.red : Color.white);
        drawString(g, ubuntuMedium, translator.translate("return"), (int) (width / 1.5),
                (height - returnRectangle.height * 3 / 4));
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        input = container.getInput();
        mouse = new Point(input.getMouseX(), input.getMouseY());

        isMouseOverResolution = resolutionRectangle.contains(mouse);
        isMouseOverFullscreen = fullscreenRectangle.contains(mouse);
        isMouseOverLanguage = languageRectangle.contains(mouse);
        isMouseOverAutoscale = autoscaleRectangle.contains(mouse);
        isMouseOverSave = saveRectangle.contains(mouse);
        isMouseOverReturn = returnRectangle.contains(mouse);

        if (input.isKeyPressed(Input.KEY_ENTER)) {
            saveOptions(container, game);
            game.enterState(Game.MENU_STATE);
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }
        if (input.isMousePressed(0)) {
            if (isMouseOverResolution) {
                modeIndex++;
                if (modeIndex == displayModes.length) {
                    modeIndex = 0;
                }
                setResolutionRectangle();
            }
            if (isMouseOverFullscreen) {
                isFullscreen = !isFullscreen;
                setFullscreenRectangle();
            }
            if (isMouseOverLanguage) {
                languageIndex++;
                if (languageIndex == languages.length) {
                    languageIndex = 0;
                }
                setLanguageRectangle();
            }
            if (isMouseOverAutoscale) {
                isAutoscale = !isAutoscale;
                setAutoscaleRectangle();
            }
            if (isMouseOverSave) {
                saveOptions(container, game);
                game.enterState(Game.MENU_STATE);
            }
            if (isMouseOverReturn) {
                game.enterState(Game.MENU_STATE);
            }
        }
        if (input.isMousePressed(1)) {
            if (isMouseOverResolution) {
                modeIndex--;
                if (modeIndex == -1) {
                    modeIndex = displayModes.length - 1;
                }
                setResolutionRectangle();
            }
            if (isMouseOverLanguage) {
                languageIndex--;
                if (languageIndex == -1) {
                    languageIndex = languages.length - 1;
                }
                setLanguageRectangle();
            }
        }
    }

    @Override
    public int getID() {
        return stateId;
    }

    private void saveOptions(GameContainer container, StateBasedGame game) {
        try {
            configuration.set("width", String.valueOf(displayModes[modeIndex].getWidth()));
            configuration.set("height", String.valueOf(displayModes[modeIndex].getHeight()));
            configuration.set("language", languages[languageIndex]);
            configuration.set("fullscreen", String.valueOf(isFullscreen));
            configuration.set("autoscale", String.valueOf(isAutoscale));
            configuration.saveChanges();

            ((AppGameContainer) container).setDisplayMode(displayModes[modeIndex].getWidth(),
                    displayModes[modeIndex].getHeight(), isFullscreen);

            translator.setLanguage(languages[languageIndex]);

            if (width != displayModes[modeIndex].getWidth()
                    || height != displayModes[modeIndex].getHeight()) {
                Game.isReinitializationRequried = true;
            } else {
                translate();
            }
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    private void drawString(Graphics g, Font font, String text, float x, float y) {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        g.drawString(text, x - width / 2, y - height / 2);
    }

    private void setResolutionRectangle() {
        resolutionText = String.format("%1$dx%2$d", displayModes[modeIndex].getWidth(),
                displayModes[modeIndex].getHeight());
        setRectangle(resolutionRectangle, resolutionText, width * 4 / 6, height * 2 / 10);
    }

    private void setFullscreenRectangle() {
        String text = isFullscreen ? yes : no;
        setRectangle(fullscreenRectangle, text, width * 4 / 6, height * 3 / 10);
    }

    private void setLanguageRectangle() {
        String text = languages[languageIndex];
        setRectangle(languageRectangle, text, width * 4 / 6, height * 4 / 10);
    }

    private void setAutoscaleRectangle() {
        String text = isAutoscale ? yes : no;
        setRectangle(autoscaleRectangle, text, width * 4 / 6, height * 5 / 10);
    }

    private void setRectangle(Rectangle rectangle, String text, int x, int y) {
        rectangle.width = ubuntuMedium.getWidth(text);
        rectangle.height = ubuntuMedium.getHeight(text);
        rectangle.x = x - rectangle.width / 2;
        rectangle.y = y - rectangle.height / 2;
    }

    private void translate() {
        yes = translator.translate("yes");
        no = translator.translate("no");
        save = translator.translate("save");
    }
}
