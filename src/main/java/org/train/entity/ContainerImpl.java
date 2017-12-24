package org.train.entity;

import java.util.List;

public class ContainerImpl extends Container {
    private List<? extends ChildInterface> children;

    public void setChildren(List<? extends ChildInterface> children) {
	this.children = children;
    }

    @Override
    protected List<? extends ChildInterface> getChildren() {
	return this.children;
    }
}
