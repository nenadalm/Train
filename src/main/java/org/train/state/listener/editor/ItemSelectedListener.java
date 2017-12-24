package org.train.state.listener.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.newdawn.slick.Image;
import org.train.state.EditorState;

public class ItemSelectedListener implements ActionListener {

    protected EditorState editorState;
    protected Image image;

    public ItemSelectedListener(EditorState editorState) {
	this(editorState, null);
    }

    public ItemSelectedListener(EditorState editorState, Image image) {
	this.editorState = editorState;
	this.image = image;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	this.editorState.getTopMenu().close();
    }
}
