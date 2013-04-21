package org.train.state.listener.editor;

import java.awt.event.ActionEvent;

import org.train.entity.Level.Item;
import org.train.state.EditorState;

public class TrainSelectedListener extends ItemSelectedListener {

    public TrainSelectedListener(EditorState editorState) {
        super(editorState);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        this.editorState.setActiveItem(Item.TRAIN);
    }

}
