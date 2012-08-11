package entity;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import component.Component;
import component.RenderComponent;

public class Entity {

    private Point position;
    private float scale;
    private float rotation;
    private Point direction;

    RenderComponent renderComponent;
    ArrayList<Component> components;

    public Entity() {
        this.components = new ArrayList<Component>();
        this.position = new Point();
        this.scale = 1;
        this.rotation = 0;
        this.direction = new Point(0, 0);
    }

    public void addComponent(Component component) {
        if (RenderComponent.class.isInstance(component)) {
            this.renderComponent = (RenderComponent) component;
        }
        component.setOwnerEntity(this);
        this.components.add(component);
    }

    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        for (Component component : this.components) {
            component.update(gc, sb, delta);
        }
    }

    public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
        if (this.renderComponent != null) {
            this.renderComponent.render(gc, sb, gr);
        }
    }

    public Point getDirection() {
        return this.direction;
    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
