package app;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import State.MenuState;

public class Game extends StateBasedGame {

    public static final int MENU_STATE = 0;
    public static final int GAME_STATE = 1;

    public Game(String title) {
        super(title);
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new MenuState(Game.MENU_STATE));
        this.addState(new MenuState(Game.GAME_STATE));
    }

}
