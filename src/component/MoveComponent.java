package component;

import java.awt.Dimension;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class MoveComponent extends Component {

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        Input input = gc.getInput();
        Dimension position = this.owner.getPosition();

        if (input.isKeyDown(Keyboard.KEY_UP)) {
            position.height++;
        } else if (input.isKeyDown(Keyboard.KEY_DOWN)) {
            position.height--;
        } else if (input.isKeyDown(Keyboard.KEY_LEFT)) {
            position.width--;
        } else if (input.isKeyDown(Keyboard.KEY_RIGHT)) {
            position.width++;
        }
    }
}
