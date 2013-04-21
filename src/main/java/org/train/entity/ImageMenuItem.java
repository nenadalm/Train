package org.train.entity;

import java.awt.event.ActionListener;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class ImageMenuItem extends MenuItem {

    private Image image;

    public ImageMenuItem(Image image, ActionListener listener) {
        super("", listener);
        this.image = image;
    }

    public Image getImage() {
        return this.image;
    }

    @Override
    public void render(Graphics g) {
        this.image.draw(this.rectangle.getX(), this.rectangle.getY(), this.getScale());
    }

    @Override
    public int getWidth() {
        return this.image.getWidth();
    }

    @Override
    public int getHeight() {
        return this.image.getHeight();
    }

}
