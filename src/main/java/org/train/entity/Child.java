package org.train.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public abstract class Child extends BoxModel implements ChildInterface {

    private Color color = Color.red;
    private float scale = 1;
    protected Rectangle rectangle;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    @Override
    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void setPosition(Point position) {
        this.setRectangle(new Rectangle(position.getX(), position.getY(), this.getWidth(), this.getHeight()));
    }

    public Point getPosition() {
        return new Point(this.getRectangle().getX(), this.getRectangle().getY());
    }

    @Override
    public abstract void render(Graphics g);

    @Override
    public abstract int getWidth();

    @Override
    public abstract int getHeight();
}
