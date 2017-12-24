package org.train.entity;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;
import org.train.component.Component;
import org.train.component.RenderComponent;

public class Entity extends BoxModel {

    private Point position;
    private float scale;
    private float rotation;
    private Point direction;
    private int width;
    private int height;
    private Color backgroundColor = Color.black;

    private RenderComponent renderComponent;
    private ArrayList<Component> components;

    public Entity() {
	this.components = new ArrayList<Component>();
	this.position = new Point(0, 0);
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

    public void update(GameContainer container, StateBasedGame game, int delta) {
	for (Component component : this.components) {
	    component.update(container, game, delta);
	}
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g) {
	if (this.renderComponent != null) {
	    this.renderComponent.render(container, game, g);
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

    public int getWidth() {
	return this.width;
    }

    public void setWidth(int width) {
	this.width = width;
    }

    public int getHeight() {
	return this.height;
    }

    public void setHeight(int height) {
	this.height = height;
    }

    public int getCenterX() {
	return (int) this.position.getX() + this.width / 2;
    }

    public int getCenterY() {
	return (int) this.position.getY() + this.height / 2;
    }

    public int getMaxX() {
	return (int) this.getPosition().getX() + this.width;
    }

    public int getMaxY() {
	return (int) this.getPosition().getY() + this.getHeight();
    }

    public void setBackgroundColor(Color c) {
	this.backgroundColor = c;
    }

    public Color getBackgroundColor() {
	return this.backgroundColor;
    }
}
