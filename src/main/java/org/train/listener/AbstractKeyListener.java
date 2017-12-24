package org.train.listener;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public abstract class AbstractKeyListener implements KeyListener {
    protected Input input;

    @Override
    public void setInput(Input input) {
	this.input = input;
    }

    @Override
    public void inputEnded() {
    }

    @Override
    public void inputStarted() {
    }

    @Override
    public void keyPressed(int key, char c) {
    }

    @Override
    public void keyReleased(int key, char c) {
    }
}
