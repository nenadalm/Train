package org.train.entity;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;

public abstract class BaseLayout implements LayoutInterface {

    protected List<Rectangle> rectangles = new ArrayList<Rectangle>();
    protected Container container;

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public List<Rectangle> getRectangles() {
        return this.rectangles;
    }

}
