package org.train.menu;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.train.entity.Menu;
import org.train.entity.MenuItem;
import org.train.factory.EffectFactory;
import org.train.other.ResourceManager;

public class MenuBuilder {
    private EffectFactory effectFactory;
    private ResourceManager resourceManager;
    private List<MenuItem> menuItems = new ArrayList<MenuItem>();
    private GameContainer container;
    private int menuItemMargin = 30;

    public MenuBuilder(EffectFactory effectFactory, ResourceManager resourceManager,
            GameContainer gameContainer) {
        this.effectFactory = effectFactory;
        this.resourceManager = resourceManager;
        this.container = gameContainer;
    }

    public MenuBuilder addMenuItem(String text, ActionListener listener) {
        this.menuItems.add(new MenuItem(text, listener));

        return this;
    }

    public Menu getMenu() {
        for (MenuItem item : this.menuItems) {
            item.setMargin(this.menuItemMargin);
        }
        Menu menu = new Menu(this.menuItems, this.container, this.resourceManager,
                this.effectFactory);
        menu.setBackgroundColor(Color.lightGray);

        return menu;
    }
}
