package org.train.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.train.entity.Entity;

public class RectangleComponent extends RenderComponent {

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        Entity owner = this.owner;
        g.setColor(owner.getBackgroundColor());
        g.fillRect(owner.getPosition().getX() - owner.getPadding().getLeft() - owner.getMargin().getRight(),
                owner.getPosition().getY() - owner.getPadding().getTop(),
                owner.getWidth() + owner.getPadding().getRight() + owner.getPadding().getLeft(),
                owner.getHeight() + owner.getPadding().getBottom() + owner.getPadding().getTop());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {

    }

}
