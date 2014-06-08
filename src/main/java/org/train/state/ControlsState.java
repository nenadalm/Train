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
import org.train.entity.ContainerImpl;
import org.train.entity.FlowLayout;
import org.train.entity.List;
import org.train.entity.ListItem;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.model.TextView;

public class ControlsState extends BasicGameState {

    private List gameList, editorList;
    private ContainerImpl childContainer;

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
        Font font = fonts.getFont("ubuntu", container.getWidth() / 20, whiteEffect);

        java.util.List<ListItem> gameListItems = new ArrayList<ListItem>();
        gameListItems.add(new ListItem(new TextView("Controls.Game", font, Color.red)));
        gameListItems.add(new ListItem(new TextView("Controls.Up", font, Color.red)));
        gameListItems.add(new ListItem(new TextView("Controls.Down", font, Color.red)));
        gameListItems.add(new ListItem(new TextView("Controls.Left", font, Color.red)));
        gameListItems.add(new ListItem(new TextView("Controls.Right", font, Color.red)));
        this.gameList = new List(gameListItems, container);

        java.util.List<ListItem> editorListItems = new ArrayList<ListItem>();
        editorListItems.add(new ListItem(new TextView("Controls.Editor", font, Color.red)));
        editorListItems
                .add(new ListItem(new TextView("Controls.ShowMenu", font, Color.red)));
        editorListItems
                .add(new ListItem(new TextView("Controls.HideMenu", font, Color.red)));
        editorListItems.add(new ListItem(new TextView("Controls.Train", font, Color.red)));
        editorListItems.add(new ListItem(new TextView("Controls.Gate", font, Color.red)));
        editorListItems.add(new ListItem(new TextView("Controls.Item", font, Color.red)));
        editorListItems.add(new ListItem(new TextView("Controls.Test", font, Color.red)));
        editorListItems.add(new ListItem(new TextView("Controls.Wall", font, Color.red)));
        this.editorList = new List(editorListItems, container);

        java.util.List<List> listItems = new ArrayList<List>();
        listItems.add(this.gameList);
        listItems.add(this.editorList);

        this.childContainer = new ContainerImpl();
        this.childContainer.setChildren(listItems);
        this.childContainer.setLayout(new FlowLayout(container, this.childContainer));
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        // this.gameList.render(container, game, g);
        // this.editorList.render(container, game, g);
        this.childContainer.render(container, game, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {

        if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }
    }

}
