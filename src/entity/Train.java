package entity;

import component.MoveComponent;

public class Train extends Entity {
    public Train() {
        this.addComponent(new MoveComponent());
    }
}
