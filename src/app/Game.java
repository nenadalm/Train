package app;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import other.Translator;
import state.EditorState;
import state.GameState;
import state.MenuForEditorState;
import state.MenuForGameState;
import state.MenuState;
import state.OptionsState;

public class Game extends StateBasedGame {

    public static final String CONTENT_PATH = "content/";

    public static final int MENU_STATE = 0;
    public static final int GAME_STATE = 1;
    public static final int EDITOR_STATE = 2;
    public static final int OPTIONS_STATE = 3;
    public static final int MENU_FOR_EDITOR_STATE = 4;
    public static final int MENU_FOR_GAME_STATE = 5;

    public static final String VERSION = "0.2";

    public static boolean isReinitializationRequried = false;

    public Game(String title) {
        super(title);
        Translator.getInstance();
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new MenuState(Game.MENU_STATE));
    }

    @Override
    public void enterState(int id) {
        try {
            if (this.getState(id) == null) {
                org.newdawn.slick.state.GameState state = this.createState(id);
                state.init(this.getContainer(), this);
                this.addState(state);
            } else {
                org.newdawn.slick.state.GameState state = this.getState(id);
                if ((Game.isReinitializationRequried || id == Game.GAME_STATE || id == Game.EDITOR_STATE)) {
                    state.init(this.getContainer(), this);
                }
            }
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(0);
        }
        super.enterState(id);
    }

    private org.newdawn.slick.state.GameState createState(int id) {
        switch (id) {
            case Game.MENU_STATE:
                return new MenuState(Game.MENU_STATE);
            case Game.GAME_STATE:
                return new GameState(Game.GAME_STATE);
            case Game.EDITOR_STATE:
                return new EditorState(Game.EDITOR_STATE);
            case Game.OPTIONS_STATE:
                return new OptionsState(Game.OPTIONS_STATE);
            case Game.MENU_FOR_EDITOR_STATE:
                return new MenuForEditorState(Game.MENU_FOR_EDITOR_STATE);
            case Game.MENU_FOR_GAME_STATE:
                return new MenuForGameState(Game.MENU_FOR_GAME_STATE);
        }
        return null;
    }
}