package org.train.state;

import org.picocontainer.PicoContainer;

abstract public class BasicGameState extends org.newdawn.slick.state.BasicGameState {

    protected PicoContainer container;

    public PicoContainer getContainer() {
        return container;
    }

    public void setContainer(PicoContainer container) {
        this.container = container;
    }
}
