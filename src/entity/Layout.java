package entity;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Layout {

    private List<Rectangle> rectangles = new ArrayList<Rectangle>();
    private Menu container;

    public Layout(GameContainer gameContainer, Menu container) {
        this.container = container;
        this.placeMenuItems(gameContainer);
    }

    public void setContainer(Menu container) {
        this.container = container;
    }

    public List<Rectangle> getRectangles() {
        return this.rectangles;
    }

    private void calculateRectangles(GameContainer container) {
        this.rectangles = new ArrayList<Rectangle>(this.container.getChildren().size());
        int menuHeight = this.getMenuHeight();
        int maxWidth = this.getMenuItemMaxWidth();

        int lastOffsetY = 0;
        for (MenuItem item : this.container.getChildren()) {
            int width = this.container.getFont().getWidth(item.getText());
            int height = this.container.getFont().getHeight(item.getText())
                    + item.getPaddingBottom() + item.getPaddingTop();
            int x = container.getWidth() / 2 - width / 2;
            int y = container.getHeight() / 2 - menuHeight / 2 + lastOffsetY;
            this.rectangles.add(new Rectangle(x, y, width, this.container.getFont().getHeight(
                    item.getText())));
            lastOffsetY += height;
        }

        this.container.setPosition(new Point(container.getWidth() / 2 - maxWidth / 2, container
                .getHeight() / 2 - menuHeight / 2));
        this.container.setWidth(maxWidth);
        this.container.setHeight(menuHeight);
    }

    private int getMenuHeight() {
        int menuHeight = 0;
        for (MenuItem item : this.container.getChildren()) {
            menuHeight += this.container.getFont().getHeight(item.getText()) + item.getPaddingTop()
                    + item.getPaddingBottom();
        }
        return menuHeight;
    }

    private int getMenuItemMaxWidth() {
        int maxWidth = 0;
        for (MenuItem item : this.container.getChildren()) {
            int width = this.container.getFont().getWidth(item.getText());
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    private void applyItemMargin() {
        int marginHeight = 0;
        int marginWidth = 0;
        int counter = 0;
        int offsetY = 0;
        int lastMarginBottom = 0;
        for (MenuItem item : this.container.getChildren()) {
            Rectangle addition = new Rectangle(item.getMarginLeft(), item.getMarginTop()
                    + lastMarginBottom, 0, 0);
            Rectangle r = this.rectangles.get(counter);
            r.setX(r.getX() + addition.getX());
            r.setY(r.getY() + addition.getY() + offsetY);
            marginHeight += item.getMarginTop();
            marginHeight += item.getMarginBottom();
            int itemMarginWidth = item.getMarginLeft() + item.getMarginRight();
            marginWidth = (marginWidth < itemMarginWidth) ? itemMarginWidth : marginWidth;
            lastMarginBottom = item.getMarginBottom();
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

    private void placeMenuItems(GameContainer container) {
        this.calculateRectangles(container);
        this.applyItemMargin();
    }

    public void render(Graphics g, int active) {
        int counter = 0;
        Color color = Color.red;
        Color activeColor = Color.blue;
        for (MenuItem item : this.container.getChildren()) {
            if (active == counter) {
                g.setColor(activeColor);
            }
            g.drawString(item.getText(), this.rectangles.get(counter).getX(),
                    this.rectangles.get(counter).getY());
            counter++;
            g.setColor(color);
        }
    }
}
