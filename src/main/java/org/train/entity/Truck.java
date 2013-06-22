package org.train.entity;


public class Truck extends Entity {
    private boolean flippedHorizontal;
    private boolean flippedVertical;
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFlippedHorizontal() {
        return flippedHorizontal;
    }

    public void setFlippedHorizontal(boolean flippedHorizontal) {
        this.flippedHorizontal = flippedHorizontal;
    }

    public boolean isFlippedVertical() {
        return flippedVertical;
    }

    public void setFlippedVertical(boolean flippedVertical) {
        this.flippedVertical = flippedVertical;
    }
}
