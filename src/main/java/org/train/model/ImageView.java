package org.train.model;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;

public class ImageView implements ViewInterface {
    private Image image;
    private Point position;

    public ImageView(Image image) {
        this.image = image;
        this.position = new Point(0, 0);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(this.image, this.getPosition().getX(), this.getPosition().getY());
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
