package org.train.entity;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * This interface must be implemented by all layouts
 */
public interface LayoutInterface {

    /**
     * Sets main entity on which other will be rendered
     */
    public abstract void setContainer(Container container);

    public abstract void recalculateRectangles();

    /**
     * Get childs of container as rectangles
     */
    public List<Rectangle> getRectangles();

    /**
     * Render will render all childs of container
     */
    public void render(Graphics g);
}
