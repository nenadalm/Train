package component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import other.Padding;
import entity.Entity;

public class RectangleComponent extends RenderComponent {

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        Entity owner = this.owner;
        g.fillRect(owner.getPosition().x - Padding.paddingLeft, owner.getPosition().y
                - Padding.paddingTop, owner.getWidth() + Padding.paddingRight, owner.getHeight()
                + Padding.paddingBottom);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {

    }

}
