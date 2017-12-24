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
import org.train.other.Translator;

public class MenuBuilder {
    private EffectFactory effectFactory;
    private ResourceManager resourceManager;
    private List<MenuItem> menuItems = new ArrayList<MenuItem>();
    private List<org.train.model.MenuItem> menuItemModels = new ArrayList<org.train.model.MenuItem>();
    private GameContainer container;
    private int menuItemMargin = 30;
    private Translator translator;

    public MenuBuilder(EffectFactory effectFactory, ResourceManager resourceManager, GameContainer gameContainer,
            Translator translator) {
        this.effectFactory = effectFactory;
        this.resourceManager = resourceManager;
        this.container = gameContainer;
        this.translator = translator;
    }

    public MenuBuilder addMenuItem(String text, ActionListener listener) {
        this.menuItems.add(new MenuItem(text, listener));

        return this;
    }

    public MenuBuilder add(org.train.model.MenuItem menuItem) {
        this.menuItemModels.add(menuItem);

        return this;
    }

    public Menu getMenu() {
        if (this.menuItemModels.size() > 0) {
            this.menuItems = new ArrayList<MenuItem>();
            for (org.train.model.MenuItem menuItemModel : menuItemModels) {
                this.menuItems.add(
                        new MenuItem(this.translator.translate(menuItemModel.getText()), menuItemModel.getListener()));
                this.menuItems.get(this.menuItems.size() - 1).setMargin(menuItemModel.getMargin());
            }

        } else {
            for (MenuItem item : this.menuItems) {
                item.setMargin(this.menuItemMargin);
            }
        }
        Menu menu = new Menu(this.menuItems, this.container, this.resourceManager, this.effectFactory);

        if (this.menuItemModels.size() == 0) {
            menu.setBackgroundColor(Color.lightGray);
        }

        return menu;
    }
}
