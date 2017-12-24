package org.train.state.listener.editor;

import java.awt.event.ActionEvent;

import org.newdawn.slick.Image;
import org.train.entity.Level.Item;
import org.train.entity.LevelItem;
import org.train.state.EditorState;

public class TreeSelectedListener extends ItemSelectedListener {

    private String name;

    public TreeSelectedListener(EditorState editorState, String name, Image image) {
	super(editorState, image);
	this.name = name;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);
	this.editorState.setActiveItem(new LevelItem(this.name, this.image, Item.ITEM));
    }
}
