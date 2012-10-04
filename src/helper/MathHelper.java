package helper;

import java.awt.Point;

import org.newdawn.slick.geom.Rectangle;

public class MathHelper {

    public static boolean rectangleContainsPoint(Rectangle r, Point p) {
        if (r.getX() < p.getX() && r.getX() + r.getWidth() > p.getX() && r.getY() < p.getY()
                && r.getY() + r.getHeight() > p.getY()) {
            return true;
        }
        return false;
    }
}
