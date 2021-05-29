package org.train.state;

import java.awt.event.ActionEvent;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.input.Input;
import org.newdawn.slick.input.sources.keymaps.USKeyboard;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.Level;
import org.train.entity.Menu;
import org.train.entity.MessageBox;
import org.train.helper.LevelHelper;
import org.train.listener.LevelStateChangeListener;
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
    public void enter(final GameContainer container, final StateBasedGame game) {
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

    @Override
    public void leave(GameContainer container, StateBasedGame game) {
        container.setMouseGrabbed(false);
    }

    private void initMenuItems(final GameContainer container, final StateBasedGame game) {
        this.menu = this.container.getComponent(MenuBuilder.class)
                .addMenuItem(this.translator.translate("Game.Menu.Continue"), (ActionEvent arg0) -> {
                    GameState.this.enterState(container, State.PLAYING);
                    GameState.this.menu.close();
                    GameState.this.gameOverMenu.close();
                }).addMenuItem(this.translator.translate("Game.Menu.RepeatLevel"), (ActionEvent e) -> {
                    GameState.this.initLevel(container, game);
                    GameState.this.menu.close();
                    GameState.this.gameOverMenu.close();
                }).addMenuItem(this.translator.translate("Game.Menu.Menu"), (ActionEvent e) -> {
                    GameState.this.menu.close();
                    GameState.this.gameOverMenu.close();
                    game.enterState(Game.MENU_FOR_GAME_STATE);
                }).addMenuItem(this.translator.translate("Game.Menu.MainMenu"), (ActionEvent e) -> {
                    GameState.this.menu.close();
                    GameState.this.gameOverMenu.close();
                    game.enterState(Game.MENU_STATE);
                }).getMenu();
    }

    private void initGameOverMenuItems(final GameContainer container, final StateBasedGame game) {
        this.gameOverMenu = this.container.getComponent(MenuBuilder.class)
                .addMenuItem(this.translator.translate("Game.Menu.RepeatLevel"), (ActionEvent e) -> {
                    GameState.this.initLevel(container, game);
                    GameState.this.menu.close();
                    GameState.this.gameOverMenu.close();
                }).addMenuItem(this.translator.translate("Game.Menu.Menu"), (ActionEvent e) -> {
                    GameState.this.menu.close();
                    GameState.this.gameOverMenu.close();
                    game.enterState(Game.MENU_FOR_GAME_STATE);
                }).addMenuItem(this.translator.translate("Game.Menu.MainMenu"), (ActionEvent e) -> {
                    GameState.this.menu.close();
                    GameState.this.gameOverMenu.close();
                    game.enterState(Game.MENU_STATE);
                }).getMenu();
    }

    private void initLevel(GameContainer container, final StateBasedGame game) {
        try {
            this.level = this.levelController.getCurrentLevel();
            this.level.addStateChangeListener(new LevelStateChangeListener() {
                @Override
                public void levelOver() {
                    GameState.this.enterState(container, State.CRASHED);
                    GameState.this.gameOverMenu.show();
                }

                @Override
                public void levelFinished() {
                    GameState.this.enterState(container, State.WON);
                    if (GameState.this.levelController.nextLevelExist()) {
                        GameState.this.levelController.updateProgress();
                        GameState.this.showLevelFinishedConfirmationBox(container, game);

                    } else {
                        GameState.this.showCongratulationConfirmationBox(game);
                    }
                }
            });

            LevelHelper levelHelper = this.container.getComponent(LevelHelper.class);
            levelHelper.adjustLevelToContainer(container, level);
            this.enterState(container, State.PLAYING);

            if (!this.level.isValid()) {
                this.messageBox.showConfirm(this.translator.translate("Game.LevelIsInvalid"), (ActionEvent arg0) -> {
                    game.enterState(Game.EDITOR_STATE);
                }, (ActionEvent e) -> {
                    game.enterState(Game.MENU_FOR_GAME_STATE);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        this.level.render(container, game, g);
        this.menu.render(container, game, g);
        this.gameOverMenu.render(container, game, g);
        this.messageBox.render(container, game, g);
    }

    @Override
    public void update(final GameContainer container, final StateBasedGame game, int delta) {
        Input input = container.getInput();

        switch (this.state) {
        case PLAYING:
            if (input.isKeyPressed(USKeyboard.KEY_ESCAPE)) {
                this.enterState(container, State.PAUSED);
                this.menu.show();
            }
            this.level.update(container, game, delta);
            break;
        case PAUSED:
            if (input.isKeyPressed(USKeyboard.KEY_ESCAPE)) {
                this.menu.close();
                this.enterState(container, State.PLAYING);
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

    private void enterState(final GameContainer container, State s) {
        container.setMouseGrabbed(s == State.PLAYING);
        this.state = s;
    }

    private void showCongratulationConfirmationBox(final StateBasedGame game) {
        this.messageBox.showConfirm(this.translator.translate("Game.Congratulation"), (ActionEvent e) -> {
            game.enterState(Game.MENU_FOR_GAME_STATE);
        }, (ActionEvent e) -> {
            game.enterState(Game.MENU_STATE);
        });
    }

    private void showLevelFinishedConfirmationBox(final GameContainer container, final StateBasedGame game) {
        this.messageBox.showConfirm(this.translator.translate("Game.LevelFinished"), (ActionEvent e) -> {
            GameState.this.levelController.loadNextLevel();
            GameState.this.initLevel(container, game);
        }, (ActionEvent e) -> {
            game.enterState(Game.MENU_STATE);
        });
    }
}
