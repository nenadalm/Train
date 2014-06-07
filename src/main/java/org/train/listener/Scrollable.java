package org.train.listener;

import org.newdawn.slick.geom.Rectangle;

public interface Scrollable {
    public Rectangle getOccupiedArea();

    public void scrollUp();

    public void scrollDown();
}
