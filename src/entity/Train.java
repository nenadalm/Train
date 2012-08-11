package entity;

import java.awt.Point;

import component.MoveComponent;

public class Train extends Entity {
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
                break;
            case 1:
                rotation = 0;
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
}
