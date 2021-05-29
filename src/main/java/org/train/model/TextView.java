package org.train.model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

public class TextView implements ViewInterface {

    private String text;
    private Point position;
    private Font font;
    private Color color;

    public TextView(String text, Font font, Color color) {
        this.font = font;
        this.text = text;
        this.color = color;
        this.position = new Point(0, 0);
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public int getWidth() {
        return this.font.getWidth(this.text);
    }

    @Override
    public int getHeight() {
        return this.font.getHeight(this.text);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(this.color);
        g.setFont(this.font);
        g.drawString(this.text, this.getPosition().getX(), this.position.getY());
    }

}
