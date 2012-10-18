package state;

import java.awt.Point;
import java.awt.Rectangle;

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
import app.Game;
import factory.FontFactory;

public class MenuState extends BasicGameState {

    private boolean isMouseOverStartGame, isMouseOverLevelEditor, isMouseOverOptions,
            isMouseOverExit;
    private int stateId, width, height, distanceBetweenMenuEntries, trainTextWidth,
            trainTextHeight;
    private Font ubuntuMedium, ubuntuLarge;
    private Rectangle startGameRectangle, levelEditorRectangle, optionsRectangle, exitRectangle;
    private Input input;
    private Point mouse;
    private String version;
    private Translator translator;

    public MenuState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        FontFactory fonts = FontFactory.getInstance();
        translator = Translator.getInstance();
        width = container.getWidth();
        height = container.getHeight();
        version = Game.VERSION;

        ColorEffect whiteEffect = new ColorEffect(java.awt.Color.WHITE);
        ubuntuMedium = fonts.getFont("ubuntu", width / 20, whiteEffect);
        ubuntuLarge = fonts.getFont("ubuntu", width / 16, whiteEffect);

        distanceBetweenMenuEntries = height / 7;

        startGameRectangle = new Rectangle();
        setRectangle(startGameRectangle, translator.translate("new game"), width / 2,
                distanceBetweenMenuEntries * 2);

        levelEditorRectangle = new Rectangle();
        setRectangle(levelEditorRectangle, translator.translate("level editor"), width / 2,
                distanceBetweenMenuEntries * 3);

        optionsRectangle = new Rectangle();
        setRectangle(optionsRectangle, translator.translate("options"), width / 2,
                distanceBetweenMenuEntries * 4);

        exitRectangle = new Rectangle();
        setRectangle(exitRectangle, translator.translate("exit"), width / 2,
                distanceBetweenMenuEntries * 5);

        trainTextWidth = ubuntuLarge.getWidth("Train " + version);
        trainTextHeight = ubuntuLarge.getHeight("Train " + version);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        g.setFont(ubuntuLarge);
        g.setColor(Color.gray);
        drawString(g, ubuntuLarge, "Train " + version, (int) (trainTextWidth / 1.75) + width / 500,
                (int) (trainTextHeight / 1.5) + width / 750);
        g.setColor(Color.white);
        drawString(g, ubuntuLarge, "Train " + version, (int) (trainTextWidth / 1.75),
                (int) (trainTextHeight / 1.5));
        g.setFont(ubuntuMedium);
        g.setColor((isMouseOverStartGame) ? Color.blue : Color.red);
        drawString(g, ubuntuMedium, translator.translate("start game"), width / 2,
                distanceBetweenMenuEntries * 2);
        g.setColor((isMouseOverLevelEditor) ? Color.blue : Color.red);
        drawString(g, ubuntuMedium, translator.translate("level editor"), width / 2,
                distanceBetweenMenuEntries * 3);
        g.setColor((isMouseOverOptions) ? Color.blue : Color.red);
        drawString(g, ubuntuMedium, translator.translate("options"), width / 2,
                distanceBetweenMenuEntries * 4);
        g.setColor((isMouseOverExit) ? Color.blue : Color.red);
        drawString(g, ubuntuMedium, translator.translate("exit"), width / 2,
                distanceBetweenMenuEntries * 5);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        input = container.getInput();
        mouse = new Point(input.getMouseX(), input.getMouseY());
        isMouseOverStartGame = startGameRectangle.contains(mouse);
        isMouseOverLevelEditor = levelEditorRectangle.contains(mouse);
        isMouseOverOptions = optionsRectangle.contains(mouse);
        isMouseOverExit = exitRectangle.contains(mouse);

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            container.exit();
        }

        if (input.isMousePressed(0)) {
            if (isMouseOverStartGame) {
                game.enterState(Game.MENU_FOR_GAME_STATE);
            }
            if (isMouseOverLevelEditor) {
                game.enterState(Game.MENU_FOR_EDITOR_STATE);
            }
            if (isMouseOverOptions) {
                game.enterState(Game.OPTIONS_STATE);
            }
            if (isMouseOverExit) {
                container.exit();
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

    private void setRectangle(Rectangle rectangle, String text, int x, int y) {
        rectangle.width = ubuntuMedium.getWidth(text);
        rectangle.height = ubuntuMedium.getHeight(text);
        rectangle.x = x - rectangle.width / 2;
        rectangle.y = y - rectangle.height / 2;
    }
}
