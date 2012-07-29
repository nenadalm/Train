package state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class EditorState extends BasicGameState {

    private int stateId;

    public EditorState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {

    }

    @Override
    public int getID() {
        return this.stateId;
    }

}
