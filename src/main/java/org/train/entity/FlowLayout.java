package org.train.entity;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;

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

    private void calculateRectangles() {
        this.rectangles = new ArrayList<Rectangle>(this.container.getChildren().size());

        int index = 0;
        int menuWidth = this.getContainerWidth();
        int menuHeight = this.getContainerHeight();
        for (ChildInterface item : this.container.getChildren()) {
            int width = (int) (item.getWidth() * item.getScale());
            int height = (int) (item.getHeight() * item.getScale());
            int x = width * index;
            int y = 0;
            this.rectangles.add(new Rectangle(x + this.container.getPadding().getLeft(), y, width,
                    height));
            index++;
        }

        this.container.setPosition(new Point(0, 0));
        this.container.setWidth(menuWidth);
        this.container.setHeight(menuHeight);
    }

    @Override
    public void recalculateRectangles() {
        this.placeMenuItems();
    }

    @Override
    protected int getContainerHeight() {
        int menuHeight = 0;
        for (ChildInterface child : this.container.getChildren()) {
            menuHeight = Math.max(menuHeight, (int) (child.getHeight() * child.getScale()));
        }

        return menuHeight;
    }

    @Override
    protected int getContainerWidth() {
        int containerWidth = this.container.getPadding().getLeft();
        for (ChildInterface child : this.container.getChildren()) {
            containerWidth += (int) (child.getWidth() * child.getScale());
        }

        return containerWidth;
    }
}
