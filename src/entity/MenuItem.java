package entity;

import java.awt.event.ActionListener;

import org.newdawn.slick.Color;

public class MenuItem extends Child {

    private boolean isEnabled;
    private ActionListener listener;
    private Color active, disabled;

    public MenuItem(String text, ActionListener listener) {
        this.setText(text);
        this.listener = listener;
    }

    public ActionListener getListener() {
        return this.listener;
    }

    public void setColors(Color normal, Color active, Color disabled) {
        super.setColor(normal);
        this.active = active;
        this.disabled = disabled;
    }

    public Color getActiveColor() {
        return this.active;
    }

    public Color getDisabledColor() {
        return this.disabled;
    }

    @Override
    public int getWidth() {
        return this.getFont().getWidth(this.getText());
    }

    @Override
    public int getHeight() {
        return this.getFont().getHeight(this.getText()) + this.getPaddingBottom()
                + this.getPaddingTop();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
