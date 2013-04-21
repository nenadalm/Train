package org.train.state.listener.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.train.state.EditorState;

public class ItemSelectedListener implements ActionListener {

    protected EditorState editorState;

    public ItemSelectedListener(EditorState editorState) {
        this.editorState = editorState;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.editorState.getTopMenu().close();
    }
}
