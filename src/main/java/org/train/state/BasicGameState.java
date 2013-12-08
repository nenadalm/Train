package org.train.state;

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
}
