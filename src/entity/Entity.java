package entity;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import component.Component;
import component.RenderComponent;

public class Entity {

    private Vector2f position;
    private float scale;
    private float rotation;

    RenderComponent renderComponent;
    ArrayList<Component> components;

    public Entity() {
        this.components = new ArrayList<Component>();
        this.position = new Vector2f();
        this.scale = 1;
        this.rotation = 0;
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

    public Vector2f getPosition() {
        return this.position;
    }

    public void setPosition(Vector2f position) {
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
