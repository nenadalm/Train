package component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import entity.Entity;

public class RectangleComponent extends RenderComponent {

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        Entity owner = this.owner;
        g.fillRect(owner.getPosition().x, owner.getPosition().y, owner.getWidth(),
                owner.getHeight());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {

    }

}
