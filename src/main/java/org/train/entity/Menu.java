package org.train.entity;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.train.component.RectangleComponent;
import org.train.factory.EffectFactory;
import org.train.other.ResourceManager;

public class Menu extends Container {
    int active = 0;
    private List<? extends MenuItem> items;
    Point lastMousePosition;
    private boolean show = true;

    @Override
    protected List<? extends MenuItem> getChildren() {
        return this.items;
    }

    public Menu(List<? extends MenuItem> items, GameContainer container,
            ResourceManager resourceManager, EffectFactory effectFactory) {
        this.addComponent(new RectangleComponent());

        try {
            for (MenuItem item : items) {
                item.setFont(resourceManager.getFont("ubuntu", container.getWidth() / 20,
                        effectFactory.getColorEffect(java.awt.Color.white)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        items.get(this.active).setColor(Color.blue);
        this.items = items;
        this.setLayout(new CenteredLayout(container, this));
        this.getLayout().setContainer(this);
        this.lastMousePosition = new Point();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        if (!this.show) {
            return;
        }

        g.setColor(Color.lightGray);
        super.render(container, game, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        if (!this.show) {
            return;
        }

        Input input = container.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();

        List<Rectangle> rectangles = this.getLayout().getRectangles();
        if (input.isKeyPressed(Input.KEY_UP)) {
            this.items.get(this.active).setColor(Color.red);
            this.active = (this.active > 0) ? this.active - 1 : rectangles.size() - 1;
            this.items.get(this.active).setColor(Color.blue);
        } else if (input.isKeyPressed(Input.KEY_DOWN)) {
            this.items.get(this.active).setColor(Color.red);
            this.active = (this.active < rectangles.size() - 1) ? this.active + 1 : 0;
            this.items.get(this.active).setColor(Color.blue);
        }

        int counter = 0;
        boolean over = false;
        for (Rectangle r : this.getLayout().getRectangles()) {
            if (r.contains(mouseX, mouseY)) {
                if (this.lastMousePosition.x != mouseX || this.lastMousePosition.y != mouseY) {
                    this.items.get(this.active).setColor(Color.red);
                    this.active = counter;
                    this.items.get(counter).setColor(Color.blue);
                }
                over = this.active == counter;
            }
            counter++;
        }
        if ((input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && over)
                || input.isKeyPressed(Input.KEY_ENTER)) {
            ActionListener listener = this.items.get(this.active).getListener();
            listener.actionPerformed(null);
        }
        this.lastMousePosition.setLocation(mouseX, mouseY);
    }

    public boolean isShowed() {
        return this.show;
    }

    public void show() {
        this.show = true;
    }

    public void close() {
        this.show = false;
    }
}
