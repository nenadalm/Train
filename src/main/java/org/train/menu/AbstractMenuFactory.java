package org.train.menu;

public abstract class AbstractMenuFactory implements MenuFactoryInterface {
    protected MenuBuilder menuBuilder;

    public AbstractMenuFactory(MenuBuilder menuBuilder) {
        this.menuBuilder = menuBuilder;
    }
}
