package other;

import java.awt.Point;
import java.awt.Rectangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class InteractiveLabel {
    private Color normal, mouseOver, disabled;
    private Point position;
    private Rectangle rectangle;
    private String text;
    private boolean isEnabled, isMouseOver, isColorSet;

    public InteractiveLabel(String text, Point position, Rectangle rectangle) {
        this.text = text;
        this.position = position;
        this.rectangle = rectangle;
        isEnabled = true;
        isColorSet = false;
    }

    public void setColors(Color normal, Color mouseOver, Color disabled) {
        this.normal = normal;
        this.mouseOver = mouseOver;
        this.disabled = disabled;
        isColorSet = true;
    }

    public void render(Graphics g) {
        if (isColorSet) {
            g.setColor((!isEnabled) ? disabled : ((isMouseOver) ? mouseOver : normal));
        }
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
        isMouseOver = rectangle.contains(mouse) && isEnabled;
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }
}
