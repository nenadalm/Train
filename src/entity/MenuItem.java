package entity;

import java.awt.event.ActionListener;

import other.MarginPadding;

public class MenuItem extends MarginPadding {

    private String text;
    private ActionListener listener;

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
}
