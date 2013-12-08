package org.train.state.listener.editor;

import java.awt.event.ActionEvent;

import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.state.EditorState;

public class TestSelectedListener extends ItemSelectedListener {

    private StateBasedGame game;

    public TestSelectedListener(EditorState editorState, StateBasedGame game) {
        super(editorState);
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.enterState(Game.TEST_GAME_STATE);
    }
}
