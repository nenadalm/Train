package org.train.entity;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Container extends Entity {

    private LayoutInterface layout;

    protected abstract List<? extends ChildInterface> getChildren();

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
	super.render(container, game, g);
	this.layout.render(g);
    }

    public LayoutInterface getLayout() {
	return this.layout;
    }

    public void setLayout(LayoutInterface layout) {
	this.layout = layout;
    }
}
