package org.train.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;

public class FlowLayout extends BaseLayout {

    public FlowLayout(GameContainer gameContainer, Container container) {
        this.container = container;
        this.recalculateRectangles();
    }

    private void placeMenuItems() {
        this.calculateRectangles();
        int counter = 0;
        for (ChildInterface c : this.container.getChildren()) {
            c.setRectangle(this.rectangles.get(counter));
            counter++;
        }
    }

    @Override
    protected void setContainerPosition() {
        this.container.setPosition(new Point(0, 0));
    }

    @Override
    public void recalculateRectangles() {
        this.placeMenuItems();
    }

    @Override
    protected org.newdawn.slick.geom.Point calculateChildPosition(ChildInterface child,
            int childIndex) {
        int x = (int) (child.getWidth() * child.getScale()) * childIndex;
        int y = 0;

        return new org.newdawn.slick.geom.Point(x, y);
    }

    @Override
    protected int calculateContainerHeight() {
        int menuHeight = 0;
        for (ChildInterface child : this.container.getChildren()) {
            menuHeight = Math.max(menuHeight, (int) (child.getHeight() * child.getScale()));
        }

        return menuHeight;
    }

    @Override
    protected int calculateContainerWidth() {
        int containerWidth = this.container.getPadding().getLeft();
        for (ChildInterface child : this.container.getChildren()) {
            containerWidth += (int) (child.getWidth() * child.getScale());
        }

        return containerWidth;
    }
}
