package org.train.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.input.Input;
import org.newdawn.slick.input.sources.keymaps.USKeyboard;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.Level;
import org.train.helper.LevelHelper;
import org.train.other.LevelController;

public class TestGameState extends BasicGameState {

    private Level level;

    public TestGameState(int stateId) {
        super(stateId);
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        LevelController levelController = this.container.getComponent(LevelController.class);
        LevelHelper levelHelper = this.container.getComponent(LevelHelper.class);

        this.level = levelController.getCurrentLevelModified();
        levelHelper.adjustLevelToContainer(container, this.level);
        container.setMouseGrabbed(true);
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) {
        container.setMouseGrabbed(false);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        this.level.render(container, game, g);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        if (this.level.isFinished() || this.level.isOver()
                || container.getInput().isKeyPressed(USKeyboard.KEY_ESCAPE)) {
            game.enterState(Game.EDITOR_STATE);
        }

        this.level.update(container, game, delta);
    }
}
