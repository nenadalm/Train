package org.train.entity;

import org.train.model.Border;
import org.train.model.Margin;
import org.train.model.Padding;

public class BoxModel {

    private Margin margin = new Margin();
    private Padding padding = new Padding();
    private Border border = new Border();

    public void setMargin(int all) {
        this.margin.setAll(all);
    }

    public void setPadding(int all) {
        this.padding.setAll(all);
    }

    public Margin getMargin() {
        return this.margin;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
    }

    public Padding getPadding() {
        return this.padding;
    }

    public void setPadding(Padding padding) {
        this.padding = padding;
    }

    public Border getBorder() {
        return this.border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public int getMarginTop() {
        return this.margin.getTop();
    }

    public void getBorderWidth(int width) {
        this.border.setWidth(width);
    }

    public int setBorderWidth() {
        return this.border.getWidth();
    }

    public void setMarginTop(int marginTop) {
        this.margin.setTop(marginTop);
    }

    public int getMarginRight() {
        return this.margin.getRight();
    }

    public void setMarginRight(int marginRight) {
        this.margin.setRight(marginRight);
    }

    public int getMarginBottom() {
        return this.margin.getBottom();
    }

    public void setMarginBottom(int marginBottom) {
        this.margin.setBottom(marginBottom);
    }

    public int getMarginLeft() {
        return this.margin.getLeft();
    }

    public void setMarginLeft(int marginLeft) {
        this.margin.setLeft(marginLeft);
    }

    public int getPaddingTop() {
        return this.padding.getTop();
    }

    public void setPaddingTop(int paddingTop) {
        this.padding.setTop(paddingTop);
    }

    public int getPaddingRight() {
        return this.padding.getRight();
    }

    public void setPaddingRight(int paddingRight) {
        this.padding.setRight(paddingRight);
    }

    public int getPaddingBottom() {
        return this.padding.getBottom();
    }

    public void setPaddingBottom(int paddingBottom) {
        this.padding.setBottom(paddingBottom);
    }

    public int getPaddingLeft() {
        return this.padding.getLeft();
    }

    public void setPaddingLeft(int paddingLeft) {
        this.padding.setLeft(paddingLeft);
    }
}
