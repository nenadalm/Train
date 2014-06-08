package org.train.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.Menu;
import org.train.model.Margin;

public class MainMenuFactory extends AbstractMenuFactory {

    public MainMenuFactory(MenuBuilder menuBuilder) {
        super(menuBuilder);
    }

    @Override
    public Menu create(final StateBasedGame game, final GameContainer container) {
        Margin margin = new Margin(0, 0, container.getHeight() / 14, 0);

        org.train.model.MenuItem startGame = new org.train.model.MenuItem();
        startGame.setText("Menu.StartGame");
        startGame.setMargin(margin);
        startGame.setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.enterState(Game.MENU_FOR_GAME_STATE);
            }
        });
        this.menuBuilder.add(startGame);

        org.train.model.MenuItem editor = new org.train.model.MenuItem();
        editor.setText("Menu.LevelEditor");
        editor.setMargin(margin);
        editor.setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.enterState(Game.MENU_FOR_EDITOR_STATE);
            }
        });
        this.menuBuilder.add(editor);

        org.train.model.MenuItem options = new org.train.model.MenuItem();
        options.setText("Menu.Options");
        options.setMargin(margin);
        options.setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                game.enterState(Game.OPTIONS_STATE);
            }
        });
        this.menuBuilder.add(options);

        org.train.model.MenuItem exit = new org.train.model.MenuItem();
        exit.setText("Menu.Exit");
        exit.setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                container.exit();
            }
        });
        this.menuBuilder.add(exit);

        return this.menuBuilder.getMenu();
    }
}
