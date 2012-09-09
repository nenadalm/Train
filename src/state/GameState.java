package state;

import java.awt.Point;

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
        LevelController levelController = LevelController.getInstance();
        try {
            int itemSize = 50;
            this.level = levelController.getCurrentLevel();
            float scale = 1;
            float scaleWidth = container.getWidth() / ((float) this.level.getWidth() * itemSize);
            float scaleHeight = container.getHeight() / ((float) itemSize * this.level.getHeight());
            if (scaleWidth < 1 && scaleHeight < 1) {
                scale = (scaleWidth < scaleHeight) ? scaleWidth : scaleHeight;
            } else if (scaleWidth < 1 || scaleHeight < 1) {
                scale = (scaleWidth < 1) ? scaleWidth : scaleHeight;
            }
            this.level.setScale(scale);
            int width = this.level.getWidth() * (int) (itemSize * scale);
            int height = this.level.getHeight() * (int) (itemSize * scale);
            Point margin = new Point((container.getWidth() - width) / 2,
                    (container.getHeight() - height) / 2);
            this.level.setMargin(margin);
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
