package component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import entity.Entity;

public class RectangleComponent extends RenderComponent {

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        Entity owner = this.owner;
        g.setColor(owner.getBackgroundColor());
        g.fillRect(owner.getPosition().x - owner.getPaddingLeft(),
                owner.getPosition().y - owner.getPaddingTop(),
                owner.getWidth() + owner.getPaddingRight() + owner.getPaddingLeft(),
                owner.getHeight() + owner.getPaddingBottom() + owner.getPaddingTop());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {

    }

}
