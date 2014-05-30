package org.train.entity;

import java.awt.event.ActionListener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class ImageButton extends Child {

    private Image normalImage, overImage, disabledImage, currentImage;
    private ActionListener listener;
    private boolean enabled = true;

    public ImageButton(Image normalImage, Image overImage, Image disabledImage,
            ActionListener listener) {
        this.listener = listener;
        this.normalImage = normalImage;
        this.overImage = overImage;
        this.disabledImage = disabledImage;
        this.currentImage = normalImage;
    }

    public void update(GameContainer container, StateBasedGame game, int delta) {
        if (!this.enabled) {
            return;
        }

        Input input = container.getInput();

        if (this.getRectangle().contains(input.getMouseX(), input.getMouseY())) {
            this.currentImage = this.overImage;
            if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
                this.listener.actionPerformed(null);
            }
        } else {
            this.currentImage = this.normalImage;
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(this.currentImage, this.rectangle.getX(), this.rectangle.getY());
    }

    @Override
    public int getWidth() {
        return (int) (this.currentImage.getWidth() * this.getScale());
    }

    @Override
    public int getHeight() {
        return (int) (this.currentImage.getHeight() * this.getScale());
    }

    public void disable() {
        this.currentImage = this.disabledImage;
        this.enabled = false;
    }

    public void enable() {
        this.currentImage = this.normalImage;
        this.enabled = true;
    }
}
