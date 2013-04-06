package org.train.entity;

public class BoxModel {

    private int marginTop = 0, marginRight = 0, marginBottom = 0, marginLeft = 0;
    private int paddingTop = 0, paddingRight = 0, paddingBottom = 0, paddingLeft = 0;
    private int borderWidth = 0;

    public void setMargin(int all) {
        this.marginTop = all;
        this.marginRight = all;
        this.marginBottom = all;
        this.marginLeft = all;
    }

    public void setPadding(int all) {
        this.paddingTop = all;
        this.paddingRight = all;
        this.paddingBottom = all;
        this.paddingLeft = all;
    }

    public int getMarginTop() {
        return this.marginTop;
    }

    public void getBorderWidth(int width) {
        this.borderWidth = width;
    }

    public int setBorderWidth() {
        return this.borderWidth;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getMarginRight() {
        return this.marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public int getMarginBottom() {
        return this.marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public int getMarginLeft() {
        return this.marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }
}
