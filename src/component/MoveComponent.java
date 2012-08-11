package component;

import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class MoveComponent extends Component {

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {

        Point position = this.owner.getPosition();
        Point direction = this.owner.getDirection();
        position.setLocation(position.x + direction.x, position.y + direction.y);

        this.owner.setPosition(position);
    }
}
