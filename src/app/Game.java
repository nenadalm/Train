package app;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import other.Translator;
import state.MenuState;

public class Game extends StateBasedGame {

    public static final String CONTENT_PATH = "content/";

    public static final int MENU_STATE = 0;
    public static final int GAME_STATE = 1;
    public static final int EDITOR_STATE = 2;

    public Game(String title) {
        super(title);
        Configuration.getInstance();
        Translator.getInstance();
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new MenuState(Game.MENU_STATE));
        this.addState(new MenuState(Game.GAME_STATE));
        this.addState(new MenuState(Game.EDITOR_STATE));
    }

}
