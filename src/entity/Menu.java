package entity;

import helper.MathHelper;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import other.ResourceManager;

import component.RectangleComponent;

import factory.EffectFactory;

public class Menu extends Entity {
    int active = -1;
    private Font font;
    private List<MenuItem> items;
    private Layout layout = null;

    protected List<MenuItem> getChildren() {
        return this.items;
    }

    public Menu(List<MenuItem> items, GameContainer container) {
        this.addComponent(new RectangleComponent());
        ResourceManager resourceManager = ResourceManager.getInstance();
        EffectFactory effectFactory = EffectFactory.getInstance();
        this.items = items;

        int fontWidth = container.getWidth() / 20;
        try {
            this.font = resourceManager.getFont("ubuntu", fontWidth,
                    effectFactory.getColorEffect(java.awt.Color.white));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.layout = new Layout(container, this);
        layout.setContainer(this);
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    protected Font getFont() {
        return this.font;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        g.setColor(Color.lightGray);
        super.render(container, game, g);
        container.getWidth();
        g.setFont(this.font);
        g.setColor(Color.red);
        this.layout.render(g, active);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        Input input = container.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        Point mouse = new Point(mouseX, mouseY);
        int counter = 0;
        boolean over = false;
        for (Rectangle r : this.layout.getRectangles()) {
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
