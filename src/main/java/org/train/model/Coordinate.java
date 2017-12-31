package org.train.model;

import org.newdawn.slick.geom.Point;

public class Coordinate {
    public final int x;
    public final int y;

    public Coordinate(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinate fromPoint(Point point) {
        return new Coordinate((int) point.getX(), (int) point.getY());
    }

    public Point toPoint() {
        return new Point(this.x, this.y);
    }
}
