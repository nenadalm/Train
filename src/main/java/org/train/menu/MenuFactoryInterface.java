package org.train.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.train.entity.Menu;

public interface MenuFactoryInterface {
    public Menu create(final StateBasedGame game, final GameContainer container);
}
