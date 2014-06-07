package org.train.entity;

import java.awt.event.ActionListener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.train.model.ViewInterface;

public class Button extends Child {

    private ViewInterface normalView, overView, disabledView, currentView;
    private ActionListener listener;
    private boolean enabled = true;
    private boolean wasOver = false;
    private Sound overSound;

    public Button(ViewInterface normalView, ViewInterface overView, ViewInterface disabledView,
            ActionListener listener) {
        this.normalView = normalView;
        this.overView = overView;
        this.disabledView = disabledView;

        this.currentView = this.normalView;
        this.listener = listener;
    }

    public void update(GameContainer container, StateBasedGame game, int delta) {
        if (!this.enabled) {
            return;
        }

        Input input = container.getInput();

        if (this.getRectangle().contains(input.getMouseX(), input.getMouseY())) {
            this.currentView = this.overView;
            if (!this.wasOver) {
                this.onMouseOver();
            }
            this.wasOver = true;

            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                this.listener.actionPerformed(null);
            }
        } else {
            if (this.wasOver) {
                this.currentView = this.normalView;
            }
            this.wasOver = false;
        }
    }

    public void setOverSound(Sound sound) {
        this.overSound = sound;
    }

    private void onMouseOver() {
        if (this.overSound != null) {
            this.overSound.play();
        }
    }

    @Override
    public void render(Graphics g) {
        this.currentView.render(g);
    }

    @Override
    public int getWidth() {
        return this.currentView.getWidth();
    }

    @Override
    public int getHeight() {
        return this.currentView.getHeight();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.currentView = this.enabled ? this.normalView : this.disabledView;
    }

    @Override
    public void setRectangle(Rectangle rectangle) {
        super.setRectangle(rectangle);

        this.normalView.setPosition(this.getPosition());
        this.overView.setPosition(this.getPosition());
        this.disabledView.setPosition(this.getPosition());
    }
}
