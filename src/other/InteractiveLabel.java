package other;

import java.awt.Point;
import java.awt.Rectangle;

import org.newdawn.slick.Graphics;

public class InteractiveLabel {
    private Point position;
    private Rectangle rectangle;
    private String text;
    private boolean isEnabled, isMouseOver;

    public InteractiveLabel(Point position, Rectangle rectangle) {
        this.position = position;
        this.rectangle = rectangle;
        isEnabled = true;
    }

    public InteractiveLabel(String text, Point position, Rectangle rectangle) {
        this.text = text;
        this.position = position;
        this.rectangle = rectangle;
        isEnabled = true;
    }

    public void render(Graphics g) {
        g.drawString(text, position.x, position.y);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setEnabled(boolean value) {
        isEnabled = value;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setIsMouseOver(Point mouse) {
        isMouseOver = rectangle.contains(mouse);
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }
}
