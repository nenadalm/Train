package org.train.state;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.Menu;
import org.train.entity.MenuItem;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.other.ResourceManager;
import org.train.other.Translator;

public class MenuState extends BasicGameState {

    private int stateId, width, trainTextWidth, trainTextHeight;
    private Font ubuntuLarge;
    private String trainText;
    private Menu menu;

    public MenuState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(final GameContainer container, final StateBasedGame game)
            throws SlickException {
        Translator translator = this.container.getComponent(Translator.class);
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem(translator.translate("Menu.StartGame"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.enterState(Game.MENU_FOR_GAME_STATE);
            }
        }));
        menuItems.add(new MenuItem(translator.translate("Menu.LevelEditor"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.enterState(Game.MENU_FOR_EDITOR_STATE);
            }
        }));
        menuItems.add(new MenuItem(translator.translate("Menu.Options"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.enterState(Game.OPTIONS_STATE);
            }
        }));
        menuItems.add(new MenuItem(translator.translate("Menu.Exit"), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                container.exit();
            }
        }));
        for (int i = 0; i < menuItems.size() - 1; i++) {
            menuItems.get(i).setMarginBottom(container.getHeight() / 14);
        }
        this.menu = new Menu(menuItems, container,
                this.container.getComponent(ResourceManager.class));

        FontFactory fonts = this.container.getComponent(FontFactory.class);
        EffectFactory effects = EffectFactory.getInstance();
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        width = container.getWidth();

        ubuntuLarge = fonts.getFont("ubuntu", width / 16, whiteEffect);

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
        this.menu.render(container, game, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            container.exit();
        }

        this.menu.update(container, game, delta);
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
}
