package org.train.entity;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
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

    @Override
    public void render(Graphics g) {
        for (ChildInterface item : this.container.getChildren()) {
            item.render(g);
        }
    }

    protected void calculateRectangles() {
        this.rectangles = new ArrayList<Rectangle>(this.container.getChildren().size());

        int index = 0;
        for (ChildInterface child : this.container.getChildren()) {
            int width = (int) (child.getWidth() * child.getScale());
            int height = (int) (child.getHeight() * child.getScale());

            org.newdawn.slick.geom.Point childPosition = this.calculateChildPosition(child, index);

            this.rectangles.add(new Rectangle(
                    childPosition.getX() - this.container.getMarginRight() + this.container.getPadding().getLeft(),
                    childPosition.getY() - this.container.getMarginTop() + this.container.getPaddingTop(), width,
                    height));
            index++;
        }

        this.setContainerPosition();
        this.container.setWidth(this.calculateContainerWidth());
        this.container.setHeight(this.calculateContainerHeight());
    }

    protected abstract int calculateContainerHeight();

    protected abstract int calculateContainerWidth();

    protected abstract Point calculateChildPosition(ChildInterface child, int childIndex);

    protected abstract void setContainerPosition();
}
