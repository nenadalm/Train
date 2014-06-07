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
import org.newdawn.slick.Sound;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.Menu;
import org.train.entity.MenuItem;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.model.TextView;
import org.train.other.ResourceManager;
import org.train.other.Translator;

public class MenuState extends BasicGameState {

    private Menu menu;
    private TextView trainVersion;
    private Sound intro;

    public MenuState(int stateId) {
        super(stateId);
    }

    @Override
    public void init(final GameContainer container, final StateBasedGame game)
            throws SlickException {
        this.intro = this.container.getComponent(ResourceManager.class).getSound("intro");
        this.intro.play();
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
                this.container.getComponent(ResourceManager.class),
                this.container.getComponent(EffectFactory.class));

        FontFactory fonts = this.container.getComponent(FontFactory.class);
        EffectFactory effects = this.container.getComponent(EffectFactory.class);
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        Font ubuntuLarge = fonts.getFont("ubuntu", container.getWidth() / 16, whiteEffect);

        this.trainVersion = new TextView("Train " + Game.VERSION, ubuntuLarge, Color.white);
        float trainVersionX = (int) (this.trainVersion.getWidth() / 1.75)
                - this.trainVersion.getWidth() / 2;
        float trainVersionY = (int) (this.trainVersion.getHeight() / 1.5)
                - this.trainVersion.getHeight() / 2;
        this.trainVersion.setPosition(new Point(trainVersionX, trainVersionY));
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) throws SlickException {
        this.intro.stop();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        this.trainVersion.render(g);
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
}
