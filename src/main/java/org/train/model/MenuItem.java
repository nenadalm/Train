package org.train.model;

import java.awt.event.ActionListener;

public class MenuItem {
    private String text;
    private ActionListener listener;
    private Margin margin = new Margin();

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public ActionListener getListener() {
	return listener;
    }

    public void setListener(ActionListener listener) {
	this.listener = listener;
    }

    public Margin getMargin() {
	return margin;
    }

    public void setMargin(Margin margin) {
	this.margin = margin;
    }
}
