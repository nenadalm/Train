package component;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class MoveComponent extends Component {

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        Input input = gc.getInput();
        Vector2f position = this.owner.getPosition();

        if (input.isKeyDown(Keyboard.KEY_UP)) {
            position.y++;
        } else if (input.isKeyDown(Keyboard.KEY_DOWN)) {
            position.y--;
        } else if (input.isKeyDown(Keyboard.KEY_LEFT)) {
            position.x--;
        } else if (input.isKeyDown(Keyboard.KEY_RIGHT)) {
            position.x++;
        }
    }
}
