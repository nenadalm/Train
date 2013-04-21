package org.train.state.listener.editor;

import java.awt.event.ActionEvent;

import org.train.state.EditorState;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TestSelectedListener extends ItemSelectedListener {

    public TestSelectedListener(EditorState editorState) {
        super(editorState);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        throw new NotImplementedException();
    }
}
