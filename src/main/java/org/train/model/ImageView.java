package org.train.model;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;

public class ImageView implements ViewInterface {
    private Image image;
    private Point position;
    private float scale;

    public ImageView(Image image, float scale) {
        this.image = image;
        this.position = new Point(0, 0);
        this.scale = scale;
    }

    @Override
    public void render(Graphics g) {
        this.image.draw(this.getPosition().getX(), this.getPosition().getY(), this.scale);
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public int getWidth() {
        return this.image.getWidth();
    }

    @Override
    public int getHeight() {
        return this.image.getHeight();
    }
}
