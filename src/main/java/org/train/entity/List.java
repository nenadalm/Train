package org.train.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
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
        this.render(null, null, g);
    }

    @Override
    public void setRectangle(Rectangle rectangle) {
        Point originalPosition = this.getPosition();
        Point newPosition = new Point(rectangle.getX(), rectangle.getY());
        Point difference = new Point(newPosition.getX() - originalPosition.getX(),
                newPosition.getY() - originalPosition.getY());

        for (ChildInterface child : this.children) {
            Point childNewPosition = new Point(child.getPosition().getX() + difference.getX(),
                    child.getPosition().getY() + difference.getY());

            child.setPosition(childNewPosition);
        }

        this.setPosition(newPosition);
    }
}
