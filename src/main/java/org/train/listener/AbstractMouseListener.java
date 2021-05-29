package org.train.listener;

import org.newdawn.slick.input.Input;
import org.newdawn.slick.InputListener;

public abstract class AbstractMouseListener implements InputListener {

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
    public void mouseWheelMoved(int change) {
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
    }

    @Override
    public void mousePressed(int button, int x, int y) {
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
    }

    @Override
    public void keyPressed(int key, char c) {
    }

    @Override
    public void keyReleased(int key, char c) {
    }

    public void controllerLeftPressed(int controller) {
    }

    public void controllerLeftReleased(int controller) {
    }

    public void controllerRightPressed(int controller) {
    };

    public void controllerRightReleased(int controller) {
    };

    public void controllerUpPressed(int controller) {
    }

    public void controllerUpReleased(int controller) {
    };

    public void controllerDownPressed(int controller) {
    };

    public void controllerDownReleased(int controller) {
    };

    public void controllerButtonPressed(int controller, int button) {
    };

    public void controllerButtonReleased(int controller, int button) {
    };
}
