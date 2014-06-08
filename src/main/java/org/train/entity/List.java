package org.train.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class List extends Container implements ChildInterface {
    private java.util.List<? extends ListItem> children;

    public List(java.util.List<? extends ListItem> children, GameContainer container) {
        this.children = children;

        this.setLayout(new CenteredLayout(container, this));
        this.getLayout().setContainer(this);
    }

    @Override
    protected java.util.List<? extends ListItem> getChildren() {
        return this.children;
    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void setRectangle(Rectangle rectangle) {

    }
}
