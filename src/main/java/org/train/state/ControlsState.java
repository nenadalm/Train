package org.train.state;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.List;
import org.train.entity.ListItem;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.model.TextView;

public class ControlsState extends BasicGameState {

    private List gameList, editorList;

    public ControlsState(int stateId) {
        super(stateId);
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        FontFactory fonts = this.container.getComponent(FontFactory.class);
        EffectFactory effects = this.container.getComponent(EffectFactory.class);
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        Font ubuntuLarge = fonts.getFont("ubuntu", container.getWidth() / 16, whiteEffect);

        java.util.List<ListItem> gameListItems = new ArrayList<ListItem>();
        gameListItems.add(new ListItem(new TextView("Controls.Game", ubuntuLarge, Color.red)));
        gameListItems.add(new ListItem(new TextView("Controls.Up", ubuntuLarge, Color.red)));
        gameListItems.add(new ListItem(new TextView("Controls.Down", ubuntuLarge, Color.red)));
        gameListItems.add(new ListItem(new TextView("Controls.Left", ubuntuLarge, Color.red)));
        gameListItems.add(new ListItem(new TextView("Controls.Right", ubuntuLarge, Color.red)));
        this.gameList = new List(gameListItems, container);

        java.util.List<ListItem> editorListItems = new ArrayList<ListItem>();
        editorListItems.add(new ListItem(new TextView("Controls.Editor", ubuntuLarge, Color.red)));
        editorListItems
                .add(new ListItem(new TextView("Controls.ShowMenu", ubuntuLarge, Color.red)));
        editorListItems
                .add(new ListItem(new TextView("Controls.HideMenu", ubuntuLarge, Color.red)));
        editorListItems.add(new ListItem(new TextView("Controls.Train", ubuntuLarge, Color.red)));
        editorListItems.add(new ListItem(new TextView("Controls.Gate", ubuntuLarge, Color.red)));
        editorListItems.add(new ListItem(new TextView("Controls.Item", ubuntuLarge, Color.red)));
        editorListItems.add(new ListItem(new TextView("Controls.Test", ubuntuLarge, Color.red)));
        editorListItems.add(new ListItem(new TextView("Controls.Wall", ubuntuLarge, Color.red)));
        this.editorList = new List(editorListItems, container);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        this.gameList.render(container, game, g);
        this.editorList.render(container, game, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {

        if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }
    }

}
