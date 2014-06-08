package org.train.entity;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;
import org.train.model.Margin;

public class CenteredLayout extends BaseLayout {

    private GameContainer gameContainer;

    public CenteredLayout(GameContainer gameContainer, Container container) {
        this.container = container;
        this.gameContainer = gameContainer;
        this.recalculateRectangles();
    }

    private void calculateRectangles() {
        this.rectangles = new ArrayList<Rectangle>(this.container.getChildren().size());

        int menuHeight = this.getContainerHeight();
        int maxWidth = this.getContainerWidth();

        int lastOffsetY = 0;
        for (ChildInterface item : this.container.getChildren()) {
            int width = item.getWidth();
            int height = item.getHeight();
            int x = this.gameContainer.getWidth() / 2 - width / 2;
            int y = this.gameContainer.getHeight() / 2 - menuHeight / 2 + lastOffsetY;
            this.rectangles
                    .add(new Rectangle(x - this.container.getMarginRight(), y, width, height));
            lastOffsetY += height;
        }

        this.container.setPosition(new Point(this.gameContainer.getWidth() / 2 - maxWidth / 2,
                this.gameContainer.getHeight() / 2 - menuHeight / 2));
        this.container.setWidth(maxWidth);
        this.container.setHeight(menuHeight);
    }

    private void applyItemMargin() {
        int marginHeight = 0;
        int marginWidth = 0;
        int counter = 0;
        int offsetY = 0;
        int lastMarginBottom = 0;
        for (ChildInterface item : this.container.getChildren()) {
            Margin itemMargin = item.getMargin();
            Rectangle addition = new Rectangle(itemMargin.getLeft(), itemMargin.getTop()
                    + lastMarginBottom, 0, 0);
            Rectangle r = this.rectangles.get(counter);
            r.setX(r.getX() + addition.getX());
            r.setY(r.getY() + addition.getY() + offsetY);
            marginHeight += itemMargin.getTop();
            marginHeight += itemMargin.getBottom();
            int itemMarginWidth = itemMargin.getLeft() + itemMargin.getRight();
            marginWidth = (marginWidth < itemMarginWidth) ? itemMarginWidth : marginWidth;
            lastMarginBottom = itemMargin.getBottom();
            offsetY += addition.getY();
            counter++;
        }
        for (Rectangle r : this.rectangles) {
            r.setX(r.getX() - marginWidth / 2);
            r.setY(r.getY() - marginHeight / 2);
        }
        this.container.setWidth(this.container.getWidth() + marginWidth);
        this.container.setHeight(this.container.getHeight() + marginHeight);
        this.container.setPosition(new Point(this.container.getPosition().x - marginWidth / 2,
                this.container.getPosition().y - marginHeight / 2));
    }

    private void placeMenuItems() {
        this.calculateRectangles();
        this.applyItemMargin();
    }

    @Override
    public void recalculateRectangles() {
        this.placeMenuItems();
        for (int i = 0; i < this.container.getChildren().size(); i++) {
            this.container.getChildren().get(i).setRectangle(this.rectangles.get(i));
        }
    }

    @Override
    protected int getContainerHeight() {
        int containerHeight = 0;
        for (ChildInterface child : this.container.getChildren()) {
            containerHeight += child.getHeight();
        }

        return containerHeight;
    }

    @Override
    protected int getContainerWidth() {
        int maxWidth = 0;
        for (ChildInterface child : this.container.getChildren()) {
            maxWidth = Math.max(child.getWidth(), maxWidth);
        }

        return maxWidth;
    }
}
