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

import other.InteractiveLabel;
import other.Translator;
import app.Game;
import factory.EffectFactory;
import factory.FontFactory;

public class MenuState extends BasicGameState {

    private int stateId, width, height, trainTextWidth, trainTextHeight;
    private Font ubuntuMedium, ubuntuLarge;
    private String trainText;
    private InteractiveLabel[] items;

    public MenuState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        FontFactory fonts = FontFactory.getInstance();
        EffectFactory effects = EffectFactory.getInstance();
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        Translator translator = Translator.getInstance();
        width = container.getWidth();
        height = container.getHeight();

        ubuntuMedium = fonts.getFont("ubuntu", width / 20, whiteEffect);
        ubuntuLarge = fonts.getFont("ubuntu", width / 16, whiteEffect);

        String[] labels = new String[4];
        labels[0] = translator.translate("start game");
        labels[1] = translator.translate("level editor");
        labels[2] = translator.translate("options");
        labels[3] = translator.translate("exit");

        int distanceBetweenMenuEntries = height / 7;

        items = new InteractiveLabel[4];
        for (int i = 0; i < items.length; i++) {
            Rectangle rectangle = new Rectangle();
            setRectangle(rectangle, labels[i], width / 2, distanceBetweenMenuEntries * (2 + i));
            Point position = new Point(width / 2 - rectangle.width / 2, distanceBetweenMenuEntries
                    * (2 + i) - rectangle.height / 2);
            items[i] = new InteractiveLabel(labels[i], position, rectangle);
        }

        trainText = "Train " + Game.VERSION;
        trainTextWidth = ubuntuLarge.getWidth(trainText);
        trainTextHeight = ubuntuLarge.getHeight(trainText);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        g.setFont(ubuntuLarge);
        g.setColor(Color.gray);
        drawString(g, ubuntuLarge, trainText, (int) (trainTextWidth / 1.75) + width / 500,
                (int) (trainTextHeight / 1.5) + width / 750);
        g.setColor(Color.white);
        drawString(g, ubuntuLarge, trainText, (int) (trainTextWidth / 1.75),
                (int) (trainTextHeight / 1.5));
        g.setFont(ubuntuMedium);
        for (int i = 0; i < items.length; i++) {
            g.setColor((items[i].isMouseOver()) ? Color.blue : Color.red);
            items[i].render(g);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();
        Point mouse = new Point(input.getMouseX(), input.getMouseY());
        for (int i = 0; i < items.length; i++) {
            items[i].setIsMouseOver(mouse);
        }

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            container.exit();
        }

        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (items[0].isMouseOver()) {
                game.enterState(Game.MENU_FOR_GAME_STATE);
            }
            if (items[1].isMouseOver()) {
                game.enterState(Game.MENU_FOR_EDITOR_STATE);
            }
            if (items[2].isMouseOver()) {
                game.enterState(Game.OPTIONS_STATE);
            }
            if (items[3].isMouseOver()) {
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
