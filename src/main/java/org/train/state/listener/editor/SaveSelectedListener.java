package org.train.state.listener.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.Level;
import org.train.entity.MessageBox;
import org.train.other.LevelController;
import org.train.other.Translator;
import org.train.state.EditorState;

public class SaveSelectedListener extends ItemSelectedListener {

    private Level level;
    private LevelController levelController;
    private Translator translator;
    private MessageBox messageBox;
    private StateBasedGame game;

    public SaveSelectedListener(EditorState editorState, Level level, LevelController levelController,
            Translator translator, MessageBox messageBox, StateBasedGame game) {
        super(editorState);
        this.level = level;
        this.levelController = levelController;
        this.translator = translator;
        this.messageBox = messageBox;
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (!this.level.isValid()) {
            String message = "";
            if (this.level.findTrainPosition() == null && this.level.findGatePosition() == null) {
                message = this.translator.translate("Editor.Message.TrainAndGateMissing");
            } else if (this.level.findTrainPosition() == null) {
                message = this.translator.translate("Editor.Message.TrainMissing");
            } else if (this.level.findGatePosition() == null) {
                message = this.translator.translate("Editor.Message.GateMissing");
            }
            this.messageBox.showConfirm(message, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    SaveSelectedListener.this.levelController.saveCurrentLevel();
                    game.enterState(Game.MENU_FOR_EDITOR_STATE);
                }
            }, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    SaveSelectedListener.this.messageBox.close();
                }
            });
        } else {
            this.levelController.saveCurrentLevel();
            this.game.enterState(Game.MENU_FOR_EDITOR_STATE);
        }
    }
}
