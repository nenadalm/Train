package org.train.state.listener.editor;

import java.awt.event.ActionEvent;

import org.newdawn.slick.Image;
import org.train.entity.Level.Item;
import org.train.entity.LevelItem;
import org.train.state.EditorState;

public class GateSelectedListener extends ItemSelectedListener {

    public GateSelectedListener(EditorState editorState, Image image) {
	super(editorState, image);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	super.actionPerformed(e);
	this.editorState.setActiveItem(new LevelItem("gate", image, Item.GATE));
    }
}
