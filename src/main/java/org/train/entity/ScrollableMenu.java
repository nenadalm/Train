package org.train.entity;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.train.factory.EffectFactory;
import org.train.other.ResourceManager;

public class ScrollableMenu extends Menu {

    private List<? extends MenuItem> children;
    private int fromIndex = 0, toIndex = 0;

    public ScrollableMenu(List<? extends MenuItem> items, GameContainer container,
            ResourceManager resourceManager, EffectFactory effectFactory) {

        super(items, container, resourceManager, effectFactory);
        this.children = items;
        this.updateItems();
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
        this.fromIndex++;
        this.toIndex++;
        this.updateItems();
    }

    public void showPrev() {
        this.fromIndex--;
        this.toIndex--;
        this.updateItems();
    }

    private void updateItems() {
        this.items = this.children.subList(fromIndex, toIndex);
        this.getLayout().recalculateRectangles();
    }
}
