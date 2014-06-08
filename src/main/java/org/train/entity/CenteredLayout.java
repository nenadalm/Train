package org.train.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.train.model.Margin;

public class CenteredLayout extends BaseLayout {

    private GameContainer gameContainer;
    private int lastOffsetY = 0;

    public CenteredLayout(GameContainer gameContainer, Container container) {
        this.container = container;
        this.gameContainer = gameContainer;
        this.recalculateRectangles();
    }

    private void applyItemMargin() {
        int marginHeight = 0;
        int marginWidth = 0;
        int index = 0;
        int offsetY = 0;
        int lastMarginBottom = 0;
        for (ChildInterface child : this.container.getChildren()) {
            Margin itemMargin = child.getMargin();
            Rectangle addition = new Rectangle(itemMargin.getLeft(), itemMargin.getTop()
                    + lastMarginBottom, 0, 0);
            Rectangle r = this.rectangles.get(index);
            r.setX(r.getX() + addition.getX());
            r.setY(r.getY() + addition.getY() + offsetY);
            marginHeight += itemMargin.getTop();
            marginHeight += itemMargin.getBottom();
            int itemMarginWidth = itemMargin.getLeft() + itemMargin.getRight();
            marginWidth = (marginWidth < itemMarginWidth) ? itemMarginWidth : marginWidth;
            lastMarginBottom = itemMargin.getBottom();
            offsetY += addition.getY();
            index++;
        }
        for (Rectangle r : this.rectangles) {
            r.setX(r.getX() - marginWidth / 2);
            r.setY(r.getY() - marginHeight / 2);
        }
        this.container.setWidth(this.container.getWidth() + marginWidth);
        this.container.setHeight(this.container.getHeight() + marginHeight);
        this.container.setPosition(new Point(this.container.getPosition().getX() - marginWidth / 2,
                this.container.getPosition().getY() - marginHeight / 2));
    }

    private void placeMenuItems() {
        this.calculateRectangles();
        this.applyItemMargin();
    }

    @Override
    protected void setContainerPosition() {
        this.container.setPosition(new Point(this.gameContainer.getWidth() / 2
                - this.calculateContainerWidth() / 2, this.gameContainer.getHeight() / 2
                - this.calculateContainerHeight() / 2));
    }

    @Override
    protected org.newdawn.slick.geom.Point calculateChildPosition(ChildInterface child,
            int childIndex) {
        if (childIndex == 0) {
            this.lastOffsetY = 0;
        }

        int x = this.gameContainer.getWidth() / 2 - (int) (child.getWidth() * child.getScale()) / 2;
        int y = this.gameContainer.getHeight() / 2 - this.calculateContainerHeight() / 2
                + this.lastOffsetY;
        this.lastOffsetY += (int) (child.getHeight() * child.getScale());

        return new org.newdawn.slick.geom.Point(x, y);
    }

    @Override
    public void recalculateRectangles() {
        this.placeMenuItems();
        for (int i = 0; i < this.container.getChildren().size(); i++) {
            this.container.getChildren().get(i).setRectangle(this.rectangles.get(i));
        }
    }

    @Override
    protected int calculateContainerHeight() {
        int containerHeight = 0;
        for (ChildInterface child : this.container.getChildren()) {
            containerHeight += child.getHeight();
        }

        return containerHeight;
    }

    @Override
    protected int calculateContainerWidth() {
        int maxWidth = 0;
        for (ChildInterface child : this.container.getChildren()) {
            maxWidth = Math.max(child.getWidth(), maxWidth);
        }

        return maxWidth;
    }
}
