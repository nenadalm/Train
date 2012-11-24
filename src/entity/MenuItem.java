package entity;

import java.awt.event.ActionListener;

import org.newdawn.slick.Color;

import other.BoxModel;

public class MenuItem extends BoxModel {

    private String text;
    private ActionListener listener;
    private Color color = Color.red;

    public MenuItem(String text) {
        this.text = text;
    }

    public MenuItem(String text, ActionListener listener) {
        this(text);
        this.listener = listener;
    }

    public String getText() {
        return this.text;
    }

    public ActionListener getListener() {
        return this.listener;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
