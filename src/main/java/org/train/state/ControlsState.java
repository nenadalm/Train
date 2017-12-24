package org.train.state;

import java.util.ArrayList;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.CenteredLayout;
import org.train.entity.ContainerImpl;
import org.train.entity.List;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.list.EditorListFactory;
import org.train.list.GameListFactory;
import org.train.other.Translator;

public class ControlsState extends BasicGameState {

    private ContainerImpl childContainer;

    public ControlsState(int stateId) {
        super(stateId);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        FontFactory fonts = this.container.getComponent(FontFactory.class);
        EffectFactory effects = this.container.getComponent(EffectFactory.class);
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        Font font = fonts.getFont("ubuntu", container.getHeight() / 14, whiteEffect);

        Translator translator = this.container.getComponent(Translator.class);

        GameListFactory gameListFactory = new GameListFactory(translator);
        EditorListFactory editorListFactory = new EditorListFactory(translator);

        java.util.List<List> listItems = new ArrayList<List>();
        listItems.add(gameListFactory.create(container, font));
        listItems.add(editorListFactory.create(container, font));

        listItems.get(0).getMargin().setBottom(container.getHeight() / 10);

        this.childContainer = new ContainerImpl();
        this.childContainer.setChildren(listItems);
        this.childContainer.setLayout(new CenteredLayout(container, this.childContainer));
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        this.childContainer.render(container, game, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

        if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }
    }

}
