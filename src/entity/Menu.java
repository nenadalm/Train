package entity;

import helper.MathHelper;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import other.ResourceManager;

import component.RectangleComponent;

public class Menu extends Entity {
    private ArrayList<Rectangle> rectangles;
    int active = -1;
    private Font font;
    private List<MenuItem> items;

    public Menu(List<MenuItem> items, GameContainer container) {
        this.addComponent(new RectangleComponent());
        ResourceManager resourceManager = ResourceManager.getInstance();
        this.items = items;

        int fontWidth = container.getWidth() / 20;
        try {
            this.font = resourceManager.getFont("ubuntu", fontWidth, new ColorEffect(
                    java.awt.Color.WHITE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.calculateRectangles(container);
    }

    private void calculateRectangles(GameContainer container) {
        this.rectangles = new ArrayList<Rectangle>(this.items.size());
        int menuHeight = 0;
        for (MenuItem item : this.items) {
            menuHeight += this.font.getHeight(item.getText()) + item.getPaddingTop()
                    + item.getPaddingBottom();
        }
        int maxWidth = 0;
        int lastOffsetY = 0;
        for (MenuItem item : this.items) {
            int width = this.font.getWidth(item.getText());
            int height = this.font.getHeight(item.getText()) + item.getPaddingBottom()
                    + item.getPaddingTop();
            if (width > maxWidth) {
                maxWidth = width;
            }
            int x = container.getWidth() / 2 - width / 2;
            int y = container.getHeight() / 2 - menuHeight / 2 + lastOffsetY;
            this.rectangles.add(new Rectangle(x, y, width, this.font.getHeight(item.getText())));
            lastOffsetY += height;
        }
        this.setPosition(new Point(container.getWidth() / 2 - maxWidth / 2, container.getHeight()
                / 2 - menuHeight / 2));
        this.setWidth(maxWidth);
        this.setHeight(menuHeight);
        this.applyItemMargin();
    }

    private void applyItemMargin() {
        int marginHeight = 0;
        int marginWidth = 0;
        int counter = 0;
        int offsetY = 0;
        for (MenuItem item : this.items) {
            Rectangle addition = new Rectangle(item.getMarginLeft(), item.getMarginTop(),
                    item.getMarginRight(), item.getMarginBottom());
            Rectangle r = this.rectangles.get(counter);
            r.setX(r.getX() + addition.getX());
            r.setY(r.getY() + addition.getY() + offsetY);
            marginHeight += item.getMarginTop();
            marginHeight += item.getMarginBottom();
            marginWidth += item.getMarginLeft();
            marginWidth += item.getMarginRight();
            offsetY += addition.getY();
            counter++;
        }
        for (Rectangle r : this.rectangles) {
            r.setX(r.getX() - marginWidth / 2);
            r.setY(r.getY() - marginHeight / 2);
        }
        this.setWidth(this.getWidth() + marginWidth);
        this.setHeight(this.getHeight() + marginHeight);
        this.setPosition(new Point(this.getPosition().x - marginWidth / 2, this.getPosition().y
                - marginHeight / 2));
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setColor(Color.lightGray);
        super.render(container, game, g);
        container.getWidth();
        g.setFont(this.font);
        g.setColor(Color.red);
        int counter = 0;
        Color color = Color.red;
        Color activeColor = Color.blue;
        for (MenuItem item : this.items) {
            if (this.active == counter) {
                g.setColor(activeColor);
            }
            g.drawString(item.getText(), this.rectangles.get(counter).getX(),
                    this.rectangles.get(counter).getY());
            counter++;
            g.setColor(color);
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        Input input = container.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        Point mouse = new Point(mouseX, mouseY);
        int counter = 0;
        boolean over = false;
        for (Rectangle r : this.rectangles) {
            if (MathHelper.rectangleContainsPoint(r, mouse)) {
                this.active = counter;
                over = true;
            }
            counter++;
        }
        if (!over) {
            this.active = -1;
        }
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && over) {
            ActionListener listener = this.items.get(this.active).getListener();
            listener.actionPerformed(null);
        }
    }
}
