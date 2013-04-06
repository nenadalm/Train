package org.train.entity;

import java.awt.Point;

import org.train.component.MoveComponent;

public class Train extends Entity {

    boolean flippedHorizontal = false;
    boolean flippedVertical = false;

    public Train() {
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
    }

    public boolean isFlippedVertical() {
        return this.flippedVertical;
    }

    public void setFlippedVertical(boolean flippedVertical) {
        this.flippedVertical = flippedVertical;
    }
}
