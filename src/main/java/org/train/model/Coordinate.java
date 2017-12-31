package org.train.model;

import org.newdawn.slick.geom.Point;

public class Coordinate {
    public final int x;
    public final int y;

    public static final Coordinate UP = new Coordinate(0, -1);
    public static final Coordinate DOWN = new Coordinate(0, 1);
    public static final Coordinate LEFT = new Coordinate(-1, 0);
    public static final Coordinate RIGHT = new Coordinate(1, 0);

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

    public Coordinate add(Coordinate c) {
        return new Coordinate(c.x + this.x, c.y + this.y);
    }

    public <T> boolean isValidFor(T[][] arr) {
        return this.x >= 0 && this.y >= 0 && this.x < arr.length && this.y < arr[0].length;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate)) {
            return false;
        }

        Coordinate c = (Coordinate) obj;

        return c.x == this.x && c.y == this.y;
    }
}
