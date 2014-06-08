package org.train.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.train.model.Border;
import org.train.model.Margin;
import org.train.model.Padding;

public interface ChildInterface {
    public Margin getMargin();

    public void setMargin(Margin margin);

    public Padding getPadding();

    public void setPadding(Padding padding);

    public Border getBorder();

    public void setBorder(Border border);

    public int getWidth();

    public int getHeight();

    public void render(Graphics g);

    public void setRectangle(Rectangle rectangle);

    public float getScale();

    public Point getPosition();

    public void setPosition(Point position);
}
