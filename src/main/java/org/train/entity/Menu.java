package org.train.entity;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.train.component.RectangleComponent;
import org.train.factory.EffectFactory;
import org.train.other.ResourceManager;

public class Menu extends Container {
    int active = 0, lastActiveItem = -1;
    protected List<? extends MenuItem> items;
    private Point lastMousePosition;
    private boolean show = true, selectable = false;
    private boolean keyboardEnabled = true;
    protected MenuItem selected;
    protected Sound overSound;

    @Override
    protected List<? extends MenuItem> getChildren() {
        return this.items;
    }

    public Menu(List<? extends MenuItem> items, GameContainer container, ResourceManager resourceManager,
            EffectFactory effectFactory) {
        if (items.size() == 0) {
            this.close();
            return;
        }

        this.addComponent(new RectangleComponent());

        try {
            for (MenuItem item : items) {
                if (item.getFont() == null) {
                    item.setFont(resourceManager.getFont("ubuntu", container.getWidth() / 20,
                            effectFactory.getColorEffect(java.awt.Color.white)));
                }
            }
            this.overSound = resourceManager.getSound("menu");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        items.get(this.active).setColor(items.get(this.active).getActiveColor());
        this.items = items;
        this.setLayout(new CenteredLayout(container, this));
        this.getLayout().setContainer(this);
        this.lastMousePosition = new Point();
    }

    @Override
    public void setMarginRight(int marginRight) {
        super.setMarginRight(marginRight);
        this.getLayout().recalculateRectangles();
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

        if (this.keyboardEnabled) {
            if (input.isKeyPressed(Input.KEY_UP)) {
                this.markOver(this.active - 1);
            } else if (input.isKeyPressed(Input.KEY_DOWN)) {
                this.markOver(this.active + 1);
            }
        }

        int counter = 0;
        boolean over = false;
        for (Rectangle r : this.getLayout().getRectangles()) {
            if (r.contains(mouseX, mouseY) && this.items.get(counter).isEnabled()) {
                if (this.items.get(this.active) != this.selected) {
                    this.items.get(this.active).setColor(items.get(this.active).getNormalColor());
                }
                if (this.lastActiveItem != counter) {
                    this.overSound.play();
                }
                this.active = counter;

                if (this.items.get(this.active) != this.selected) {
                    this.items.get(counter).setColor(items.get(this.active).getActiveColor());
                }

                over = this.active == counter;
            }
            counter++;
        }

        this.lastActiveItem = this.active;
        if (!over && !this.keyboardEnabled && this.items.get(this.active).isEnabled()) {
            if (this.selected != this.items.get(this.active)) {
                this.items.get(this.active).setColor(items.get(this.active).getNormalColor());
                this.lastActiveItem = -1;
            }
        }

        if ((over && input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
                || (this.keyboardEnabled && input.isKeyPressed(Input.KEY_ENTER))) {
            if (this.selectable) {
                if (this.selected != null) {
                    this.selected.setColor(this.selected.getNormalColor());
                }
                this.selected = this.items.get(active);
                this.selected.setColor(this.selected.getSelectedColor());
            }
            ActionListener listener = this.items.get(this.active).getListener();
            listener.actionPerformed(null);
        }
        if (over && input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
            ActionListener listener = this.items.get(this.active).getListener2();
            if (listener != null) {
                listener.actionPerformed(null);
            }
        }
        this.lastMousePosition.setLocation(mouseX, mouseY);
    }

    private void markOver(int overMenuItemIndex) {
        this.overSound.play();
        this.items.get(this.active).setColor(items.get(this.active).getNormalColor());

        if (overMenuItemIndex < 0) {
            this.active = this.items.size() - 1;
        } else if (overMenuItemIndex >= this.items.size()) {
            this.active = 0;
        } else {
            this.active = overMenuItemIndex;
        }

        this.items.get(this.active).setColor(items.get(this.active).getActiveColor());
    }

    public void disableKeyboard() {
        this.keyboardEnabled = false;
    }

    public boolean isShowed() {
        return this.show;
    }

    public void show() {
        this.show = true;
    }

    public void setSelectable() {
        this.selectable = true;
    }

    public void close() {
        this.show = false;
    }

    public List<? extends MenuItem> getMenuItems() {
        return this.items;
    }
}
