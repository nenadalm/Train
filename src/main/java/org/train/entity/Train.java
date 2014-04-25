package org.train.entity;

import java.awt.Point;

import org.newdawn.slick.Image;
import org.train.component.MoveComponent;

public class Train extends LevelItem {

    private Image original;
    boolean flippedHorizontal = false;
    boolean flippedVertical = false;

    public Train(Image image, Level.Item type) {
        super(image, type);
        this.original = image;
        this.addComponent(new MoveComponent());
    }

    @Override
    public void setDirection(Point direction) {
        super.setDirection(direction);

        float rotation = 0;
        switch (direction.x) {
            case -1:
                rotation = 180;
                this.setFlippedVertical(true);
                break;
            case 1:
                rotation = 0;
                this.setFlippedVertical(false);
                break;
        }
        switch (direction.y) {
            case -1:
                rotation = -90;
                break;
            case 1:
                rotation = 90;
                break;
        }
        this.setRotation(rotation);
    }

    public boolean isFlippedHorizontal() {
        return this.flippedHorizontal;
    }

    public void setFlippedHorizontal(boolean flippedHorizontal) {
        this.flippedHorizontal = flippedHorizontal;
        this.updateImage();
    }

    public boolean isFlippedVertical() {
        return this.flippedVertical;
    }

    public void setFlippedVertical(boolean flippedVertical) {
        this.flippedVertical = flippedVertical;
        this.updateImage();
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        this.updateImage();
    }

    private void updateImage() {
        this.image = this.original.getFlippedCopy(this.isFlippedHorizontal(),
                this.isFlippedVertical());
        int origin = (int) ((this.image.getWidth() * this.getScale()) / 2);
        this.image.setCenterOfRotation(origin, origin);
        this.image.setRotation(this.getRotation());
        this.setImage(this.image);
    }
}
