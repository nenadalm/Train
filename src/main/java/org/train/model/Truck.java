package org.train.model;

import org.newdawn.slick.Image;

public class Truck {
    private Image truck;
    private Image item;

    public Truck(Image truck, Image item) {
        this.truck = truck;
        this.item = item;
    }

    public Image getTruck() {
        return truck;
    }

    public Image getItem() {
        return item;
    }
}
