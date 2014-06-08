package org.train.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.train.model.ViewInterface;

public class ListItem extends Child {

    private ViewInterface view;

    public ListItem(ViewInterface view) {
        this.view = view;
        this.rectangle = new Rectangle(this.view.getPosition().getX(), this.view.getPosition()
                .getY(), this.view.getWidth(), this.view.getHeight());
    }

    @Override
    public void render(Graphics g) {
        this.view.render(g);
    }

    @Override
    public int getWidth() {
        return this.view.getWidth();
    }

    @Override
    public int getHeight() {
        return this.view.getHeight();
    }

    @Override
    public void setRectangle(Rectangle rectangle) {
        super.setRectangle(rectangle);

        this.view.setPosition(new Point(rectangle.getX(), rectangle.getY()));
    }
}
