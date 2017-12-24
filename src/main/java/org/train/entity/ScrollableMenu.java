package org.train.entity;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Rectangle;
import org.train.factory.EffectFactory;
import org.train.listener.ScrollListener;
import org.train.listener.Scrollable;
import org.train.other.ResourceManager;

public class ScrollableMenu extends Menu implements Scrollable {

    private List<? extends MenuItem> children;
    private int fromIndex = 0, toIndex = 0;
    private MouseListener mouseListener;
    private boolean roundScroll = false;

    public ScrollableMenu(List<? extends MenuItem> items, GameContainer container, ResourceManager resourceManager,
            EffectFactory effectFactory) {

        super(items, container, resourceManager, effectFactory);
        this.children = items;
        this.updateItems();
        this.mouseListener = new ScrollListener(this);
        this.mouseListener.setInput(container.getInput());
        container.getInput().addMouseListener(this.mouseListener);
    }

    public void setMaxItems(int max) {
        this.fromIndex = 0;

        if (max > this.children.size()) {
            this.toIndex = this.children.size();
        } else {
            this.toIndex = max;
        }
        this.updateItems();
    }

    public void showNext() {
        if (this.items.get(active) != this.selected) {
            this.items.get(active).setColor(this.items.get(active).getNormalColor());
        }
        this.fromIndex++;
        this.toIndex++;
        this.updateItems();
    }

    public void showPrev() {
        if (this.items.get(active) != this.selected) {
            this.items.get(active).setColor(this.items.get(active).getNormalColor());
        }
        this.fromIndex--;
        this.toIndex--;
        this.updateItems();
    }

    public boolean hasNext() {
        return this.toIndex < this.children.size();
    }

    public int getFirstIndex() {
        return this.fromIndex;
    }

    public int getLastIndex() {
        return this.toIndex;
    }

    public boolean hasPrev() {
        return this.fromIndex > 0;
    }

    private void updateItems() {
        this.items = this.children.subList(fromIndex, toIndex);
        if (this.items.size() == 0) {
            return;
        }
        this.getLayout().recalculateRectangles();
    }

    @Override
    public Rectangle getOccupiedArea() {
        return new Rectangle(getPosition().getX() - getPaddingLeft() - getMarginRight(),
                getPosition().getY() - getPaddingTop(), getWidth() + getPaddingRight() + getPaddingLeft(),
                getHeight() + getPaddingBottom() + getPaddingTop());
    }

    public void enableRoundScroll() {
        this.roundScroll = true;
    }

    @Override
    public void scrollUp() {
        if (hasPrev()) {
            showPrev();
        } else if (this.roundScroll) {
            this.fromIndex = this.children.size() - this.toIndex;
            this.toIndex = this.children.size();
            this.updateItems();
        }
    }

    @Override
    public void scrollDown() {
        if (hasNext()) {
            showNext();
        } else if (this.roundScroll) {
            this.toIndex = this.toIndex - this.fromIndex;
            this.fromIndex = 0;
            this.updateItems();
        }
    }
}
