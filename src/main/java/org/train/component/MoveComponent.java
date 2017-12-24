package org.train.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;

public class MoveComponent extends Component {

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {

	Point position = this.owner.getPosition();
	Point direction = this.owner.getDirection();
	position.setLocation(position.getX() + direction.getX(), position.getY() + direction.getY());

	this.owner.setPosition(position);
    }
}
