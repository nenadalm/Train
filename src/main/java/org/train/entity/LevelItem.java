package org.train.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class LevelItem extends Entity implements Cloneable {
    protected Image image;
    private Level.Item type;
    private String name;

    public LevelItem(String name, Image image, Level.Item type) {
	this.image = image;
	this.type = type;
	this.name = name;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
	super.render(container, game, g);

	this.image.draw(this.getPosition().getX(), this.getPosition().getY(), this.getScale());
    }

    public void setImage(Image image) {
	this.image = image;
    }

    public Level.Item getType() {
	return type;
    }

    public String getName() {
	return name;
    }

    @Override
    public Object clone() {
	return new LevelItem(this.name, this.image, this.type);
    }
}
