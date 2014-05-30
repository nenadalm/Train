package org.train.model;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

public interface ViewInterface {
    public Point getPosition();

    public void setPosition(Point position);

    public int getWidth();

    public int getHeight();

    public void render(Graphics g);
}
