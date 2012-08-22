package state;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.Translator;
import app.Game;

public class MenuForGameState extends BasicGameState {

    private int stateId, width, height;
    private UnicodeFont ubuntuMedium, ubuntuLarge;

    private Input input;
    private Point mouse;
    private Translator translator;
    private byte[] progresses;
    private Image arrowLeft, arrowRight, arrowMouseOverLeft, arrowMouseOverRight,
            arrowDisabledLeft, arrowDisabledRight;

    public MenuForGameState(int stateId) {
        this.stateId = stateId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        translator = Translator.getInstance();
        width = container.getWidth();
        height = container.getHeight();
        String fontPath = Game.CONTENT_PATH + "fonts/ubuntu.ttf";

        ubuntuMedium = new UnicodeFont(fontPath, width / 20, false, false);
        ubuntuMedium.addGlyphs(32, 800);
        ubuntuMedium.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        ubuntuMedium.loadGlyphs();

        ubuntuLarge = new UnicodeFont(fontPath, width / 16, false, false);
        ubuntuLarge.addGlyphs(32, 800);
        ubuntuLarge.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        ubuntuLarge.loadGlyphs();

        File saveFile = new File(Game.CONTENT_PATH + "save");
        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            FileInputStream in = new FileInputStream(saveFile);
            progresses = new byte[in.available()];
            in.read(progresses);
        } catch (IOException e) {
            e.printStackTrace();
        }

        arrowRight = new Image(Game.CONTENT_PATH + "graphics/arrow.png")
                .getScaledCopy(width / 2000f);
        arrowRight.rotate(90);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        g.setFont(ubuntuLarge);
        g.setColor(Color.white);
        g.drawString(translator.translate("Packages"), width / 30, height / 10);
        g.drawString(translator.translate("Levels"), width / 30, height / 2);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        input = container.getInput();
        mouse = new Point(input.getMouseX(), input.getMouseY());
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }
    }

    @Override
    public int getID() {
        return this.stateId;
    }

    private void drawString(Graphics g, UnicodeFont font, String text, float x, float y) {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        g.drawString(text, x - width / 2, y - height / 2);
    }
}
