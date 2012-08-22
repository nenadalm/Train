package state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.LevelController;
import entity.Level;

public class GameState extends BasicGameState {

    private int stateId;
    private Level level = null;

    public GameState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        LevelController levelController = new LevelController();
        try {
            this.level = levelController.getLevel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        this.level.render(container, game, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        this.level.update(container, game, delta);
    }

    @Override
    public int getID() {
        return this.stateId;
    }

}
