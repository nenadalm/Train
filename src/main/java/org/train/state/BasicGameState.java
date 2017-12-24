package org.train.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.picocontainer.PicoContainer;

abstract public class BasicGameState extends org.newdawn.slick.state.BasicGameState {

    protected PicoContainer container;
    private int stateId;

    public BasicGameState(int stateId) {
	this.stateId = stateId;
    }

    public PicoContainer getContainer() {
	return container;
    }

    public void setContainer(PicoContainer container) {
	this.container = container;
    }

    @Override
    public int getID() {
	return this.stateId;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    }
}
