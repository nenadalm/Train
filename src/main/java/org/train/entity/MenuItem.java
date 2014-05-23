package org.train.entity;

import java.awt.event.ActionListener;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;

public class MenuItem extends Child {

    private boolean isEnabled = true;
    private ActionListener listener;
    private Color normal, active, disabled;
    private String text;
    private Font font;

    public MenuItem(String text, ActionListener listener) {
        this.setText(text);
        this.listener = listener;

        this.normal = Color.red;
        this.active = Color.blue;
        this.disabled = Color.darkGray;
    }

    public ActionListener getListener() {
        return this.listener;
    }

    public void setColors(Color normal, Color active, Color disabled) {
        super.setColor(normal);
        this.normal = normal;
        this.active = active;
        this.disabled = disabled;
    }

    public Color getNormalColor() {
        return this.normal;
    }

    public Color getActiveColor() {
        return this.active;
    }

    public Color getDisabledColor() {
        return this.disabled;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
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
        if (isEnabled != this.isEnabled) {
            if (isEnabled) {
                this.setColor(this.normal);
            } else {
                this.setColor(this.disabled);
            }
        }

        this.isEnabled = isEnabled;
    }

    @Override
    public void render(Graphics g) {
        g.setFont(this.getFont());
        g.setColor(this.getColor());
        g.drawString(this.getText(), this.rectangle.getX(), this.rectangle.getY());
    }
}
