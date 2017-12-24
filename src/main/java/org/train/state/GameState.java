package org.train.state;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.Level;
import org.train.entity.Menu;
import org.train.entity.MessageBox;
import org.train.helper.LevelHelper;
import org.train.menu.MenuBuilder;
import org.train.other.LevelController;
import org.train.other.Translator;

enum State {
    PLAYING, PAUSED, CRASHED, WON
}

public class GameState extends BasicGameState {

    private Level level = null;
    private Menu menu = null;
    private Menu gameOverMenu = null;
    private Translator translator;
    private LevelController levelController;
    private MessageBox messageBox;
    private State state = null;

    public GameState(int stateId) {
	super(stateId);
    }

    @Override
    public void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
	this.levelController = this.container.getComponent(LevelController.class);
	this.translator = this.container.getComponent(Translator.class);
	this.messageBox = this.container.getComponent(MessageBox.class);
	this.messageBox.setBackgroundColor(Color.lightGray);
	this.initMenuItems(container, game);
	this.initGameOverMenuItems(container, game);
	this.initLevel(container, game);
	this.menu.close();
	this.gameOverMenu.close();
	container.getInput().clearKeyPressedRecord();
    }

    private void initMenuItems(final GameContainer container, final StateBasedGame game) {
	MenuBuilder menuBuilder = this.container.getComponent(MenuBuilder.class);
	menuBuilder.addMenuItem(this.translator.translate("Game.Menu.Continue"), new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		GameState.this.state = State.PLAYING;
		GameState.this.menu.close();
		GameState.this.gameOverMenu.close();
	    }
	}).addMenuItem(this.translator.translate("Game.Menu.RepeatLevel"), new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		GameState.this.initLevel(container, game);
		GameState.this.menu.close();
		GameState.this.gameOverMenu.close();
	    }
	}).addMenuItem(this.translator.translate("Game.Menu.Menu"), new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		GameState.this.menu.close();
		GameState.this.gameOverMenu.close();
		game.enterState(Game.MENU_FOR_GAME_STATE);
	    }
	}).addMenuItem(this.translator.translate("Game.Menu.MainMenu"), new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		GameState.this.menu.close();
		GameState.this.gameOverMenu.close();
		game.enterState(Game.MENU_STATE);
	    }
	});
	this.menu = menuBuilder.getMenu();
    }

    private void initGameOverMenuItems(final GameContainer container, final StateBasedGame game) {
	MenuBuilder menuBuilder = this.container.getComponent(MenuBuilder.class);
	menuBuilder.addMenuItem(this.translator.translate("Game.Menu.RepeatLevel"), new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		GameState.this.initLevel(container, game);
		GameState.this.menu.close();
		GameState.this.gameOverMenu.close();
	    }
	}).addMenuItem(this.translator.translate("Game.Menu.Menu"), new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		GameState.this.menu.close();
		GameState.this.gameOverMenu.close();
		game.enterState(Game.MENU_FOR_GAME_STATE);
	    }
	}).addMenuItem(this.translator.translate("Game.Menu.MainMenu"), new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		GameState.this.menu.close();
		GameState.this.gameOverMenu.close();
		game.enterState(Game.MENU_STATE);
	    }
	});
	this.gameOverMenu = menuBuilder.getMenu();
    }

    private void initLevel(GameContainer container, final StateBasedGame game) {
	try {
	    this.level = this.levelController.getCurrentLevel();

	    LevelHelper levelHelper = this.container.getComponent(LevelHelper.class);
	    levelHelper.adjustLevelToContainer(container, level);
	    this.state = State.PLAYING;

	    if (!this.level.isValid()) {
		this.messageBox.showConfirm(this.translator.translate("Game.LevelIsInvalid"), new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent arg0) {
			game.enterState(Game.EDITOR_STATE);
		    }
		}, new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
			game.enterState(Game.MENU_FOR_GAME_STATE);
		    }
		});
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
	this.level.render(container, game, g);
	this.menu.render(container, game, g);
	this.gameOverMenu.render(container, game, g);
	this.messageBox.render(container, game, g);
    }

    @Override
    public void update(final GameContainer container, final StateBasedGame game, int delta) throws SlickException {
	Input input = container.getInput();

	switch (this.state) {
	case PLAYING:
	    if (this.level.isOver()) {
		this.state = State.CRASHED;
		this.gameOverMenu.show();
	    } else if (this.level.isFinished()) {
		this.state = State.WON;
		if (this.levelController.nextLevelExist()) {
		    this.levelController.updateProgress();
		    this.messageBox.showConfirm(this.translator.translate("Game.LevelFinished"), new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			    GameState.this.levelController.loadNextLevel();
			    GameState.this.initLevel(container, game);
			}
		    }, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			    game.enterState(Game.MENU_STATE);
			}
		    });
		} else {
		    this.messageBox.showConfirm(this.translator.translate("Game.Congratulation"), new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			    game.enterState(Game.MENU_FOR_GAME_STATE);
			}
		    }, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			    game.enterState(Game.MENU_STATE);
			}
		    });
		}
	    } else if (input.isKeyPressed(Input.KEY_ESCAPE)) {
		this.state = State.PAUSED;
		this.menu.show();
	    }
	    this.level.update(container, game, delta);
	    break;
	case PAUSED:
	    if (input.isKeyPressed(Input.KEY_ESCAPE)) {
		this.menu.close();
		this.state = State.PLAYING;
	    }
	    break;
	case CRASHED:
	    break;
	case WON:
	    break;
	}

	this.menu.update(container, game, delta);
	this.gameOverMenu.update(container, game, delta);
	this.messageBox.update(container, game, delta);
	input.clearKeyPressedRecord();
    }
}
