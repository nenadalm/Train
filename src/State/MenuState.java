package State;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.Translator;

public class MenuState extends BasicGameState {

    private int stateId;

    public MenuState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game)
            throws SlickException {

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        Translator t = Translator.getInstance();
        g.drawString(t.translate("name"), 100, 100);
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
