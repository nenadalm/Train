package org.train.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.Menu;
import org.train.model.Margin;
import org.train.model.MenuItem;

public class MainMenuFactory extends AbstractMenuFactory {

    public MainMenuFactory(MenuBuilder menuBuilder) {
	super(menuBuilder);
    }

    @Override
    public Menu create(final StateBasedGame game, final GameContainer container) {
	Margin margin = new Margin(0, 0, container.getHeight() / 14, 0);

	MenuItem startGame = new MenuItem();
	startGame.setText("Menu.StartGame");
	startGame.setMargin(margin);
	startGame.setListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		game.enterState(Game.MENU_FOR_GAME_STATE);
	    }
	});
	this.menuBuilder.add(startGame);

	MenuItem editor = new MenuItem();
	editor.setText("Menu.LevelEditor");
	editor.setMargin(margin);
	editor.setListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		game.enterState(Game.MENU_FOR_EDITOR_STATE);
	    }
	});
	this.menuBuilder.add(editor);

	MenuItem options = new MenuItem();
	options.setText("Menu.Options");
	options.setMargin(margin);
	options.setListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		game.enterState(Game.OPTIONS_STATE);
	    }
	});
	this.menuBuilder.add(options);

	MenuItem controls = new MenuItem();
	controls.setText("Menu.Controls");
	controls.setMargin(margin);
	controls.setListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		game.enterState(Game.CONTROLS_STATE);
	    }
	});
	this.menuBuilder.add(controls);

	MenuItem exit = new MenuItem();
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
