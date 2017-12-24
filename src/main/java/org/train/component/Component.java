package org.train.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import org.train.entity.Entity;

public abstract class Component {

    protected Entity owner;

    public void setOwnerEntity(Entity owner) {
	this.owner = owner;
    }

    public abstract void update(GameContainer container, StateBasedGame game, int delta);
}
