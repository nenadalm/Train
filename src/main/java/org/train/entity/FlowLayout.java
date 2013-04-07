package org.train.entity;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class FlowLayout extends BaseLayout {

    public FlowLayout(GameContainer gameContainer, Container container) {
        this.container = container;
        this.placeMenuItems(gameContainer);
    }

    @Override
    public void render(Graphics g) {
        int counter = 0;
        for (Child child : this.container.getChildren()) {
            Rectangle r = this.rectangles.get(counter);
            ImageMenuItem item = ((ImageMenuItem) child);
            item.getImage().draw(r.getX(), r.getY(), item.getScale());
            counter++;
        }
    }

    private void placeMenuItems(GameContainer gameContainer) {
        this.calculateRectangles(gameContainer);
    }

    private void calculateRectangles(GameContainer container) {
        this.rectangles = new ArrayList<Rectangle>(this.container.getChildren().size());

        int index = 0;
        int menuWidth = this.container.getPaddingLeft();
        int menuHeight = 0;
        for (Child item : this.container.getChildren()) {
            int width = (int) (item.getWidth() * item.getScale());
            int height = (int) (item.getHeight() * item.getScale());
            int x = width * index;
            int y = 0;
            this.rectangles
                    .add(new Rectangle(x + this.container.getPaddingLeft(), y, width, height));
            index++;
            menuWidth += width;
            menuHeight = height > menuHeight ? height : menuHeight;
        }

        this.container.setPosition(new Point(0, 0));
        this.container.setWidth(menuWidth);
        this.container.setHeight(menuHeight);
    }
}
