package org.train.entity;

import org.newdawn.slick.Image;
import org.train.entity.Level.Item;

public class Truck extends LevelItem {
    private Image original;
    private boolean flippedHorizontal;
    private boolean flippedVertical;

    public Truck(Image image, Item type) {
        super(image, type);
        this.original = image;
    }

    public boolean isFlippedHorizontal() {
        return flippedHorizontal;
    }

    public void setFlippedHorizontal(boolean flippedHorizontal) {
        this.flippedHorizontal = flippedHorizontal;
        this.updateImage();
    }

    public boolean isFlippedVertical() {
        return flippedVertical;
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
