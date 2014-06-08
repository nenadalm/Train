package org.train.state;

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
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.menu.MainMenuFactory;
import org.train.menu.MenuBuilder;
import org.train.menu.MenuFactoryInterface;
import org.train.model.TextView;
import org.train.other.ResourceManager;

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

        MenuBuilder menuBuilder = this.container.getComponent(MenuBuilder.class);
        MenuFactoryInterface mainMenuFactory = new MainMenuFactory(menuBuilder);
        this.menu = mainMenuFactory.create(game, container);

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
