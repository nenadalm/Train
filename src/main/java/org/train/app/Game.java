package org.train.app;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.picocontainer.DefaultPicoContainer;
import org.train.other.LevelController;
import org.train.state.BasicGameState;
import org.train.state.ControlsState;
import org.train.state.EditorState;
import org.train.state.GameState;
import org.train.state.MenuForEditorState;
import org.train.state.MenuForGameState;
import org.train.state.MenuState;
import org.train.state.OptionsState;
import org.train.state.TestGameState;

public class Game extends StateBasedGame {

    public static final int MENU_STATE = 0;
    public static final int GAME_STATE = 1;
    public static final int EDITOR_STATE = 2;
    public static final int OPTIONS_STATE = 3;
    public static final int MENU_FOR_EDITOR_STATE = 4;
    public static final int MENU_FOR_GAME_STATE = 5;
    public static final int TEST_GAME_STATE = 6;
    public static final int CONTROLS_STATE = 7;

    public static final String VERSION = "0.8";

    private DefaultPicoContainer container;

    public Game(String title, DefaultPicoContainer container) {
        super(title);
        this.container = container;
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        LevelController levelController = this.container.getComponent(LevelController.class);
        levelController.repairPackagesNames();
        levelController.repairLevelsNames();

        this.addState(new MenuState(Game.MENU_STATE));
        this.addState(new ControlsState(Game.CONTROLS_STATE));
        this.addState(new GameState(Game.GAME_STATE));
        this.addState(new MenuForGameState(Game.MENU_FOR_GAME_STATE));
        this.addState(new OptionsState(Game.OPTIONS_STATE));
        this.addState(new TestGameState(Game.TEST_GAME_STATE));
        this.addState(new EditorState(Game.EDITOR_STATE));
        this.addState(new MenuForEditorState(Game.MENU_FOR_EDITOR_STATE));
    }

    @Override
    public void addState(org.newdawn.slick.state.GameState state) {
        if (state instanceof BasicGameState) {
            ((BasicGameState) state).setContainer(this.container);
        }

        super.addState(state);
    }
}