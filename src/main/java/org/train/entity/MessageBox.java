package org.train.entity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.input.Input;
import org.newdawn.slick.input.sources.keymaps.USKeyboard;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;
import org.train.component.RectangleComponent;
import org.train.factory.ButtonFactory;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.model.TextAreaView;
import org.train.other.ResourceManager;
import org.train.other.Translator;

public class MessageBox extends Entity {
    private boolean show = false;
    private Font font;
    private ActionListener yesListener;
    private ActionListener noListener;

    private Color bakcgroundColor = Color.lightGray;
    private Color textColor = Color.red;

    private TextAreaView textMessage;
    private Button yesButton, noButton;

    private ButtonFactory buttonFactory;
    private Translator translator;
    private GameContainer container;

    public MessageBox(GameContainer container, Translator translator, ResourceManager resourceManager,
            FontFactory fontFactory, EffectFactory effectFactory, ButtonFactory buttonFactory) {
        this.buttonFactory = buttonFactory;
        this.translator = translator;
        this.container = container;

        this.addComponent(new RectangleComponent());

        try {
            int fontWidth = container.getWidth() / 20;
            this.font = fontFactory.getFont("ubuntu", fontWidth, effectFactory.getColorEffect(java.awt.Color.WHITE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setWidth((int) (this.container.getWidth() / 1.5));
    }

    public void showConfirm(String text, ActionListener yesListener, ActionListener noListener) {
        this.textMessage = new TextAreaView(text, this.font, this.textColor, this.getWidth());
        this.yesListener = yesListener;
        this.noListener = noListener;

        int height = (int) (this.textMessage.getHeight() + this.buttonFactory.getDefaultFont().getLineHeight() * 1.5);
        this.setPosition(new Point(this.container.getWidth() / 2 - this.getWidth() / 2,
                this.container.getHeight() / 2 - height / 2));
        this.setHeight(height);

        this.createButtons();
        this.textMessage
                .setPosition(new org.newdawn.slick.geom.Point(this.getPosition().getX(), this.getPosition().getY()));

        this.show = true;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        if (!this.show) {
            return;
        }

        g.setColor(this.bakcgroundColor);
        super.render(container, game, g);

        container.getWidth();
        g.setFont(this.font);

        g.setColor(this.textColor);
        this.textMessage.render(g);

        this.yesButton.render(g);
        this.noButton.render(g);
    }

    public void close() {
        this.show = false;
    }

    public boolean isShowed() {
        return this.show;
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        super.update(container, game, delta);
        if (!this.show) {
            return;
        }

        this.yesButton.update(container, game, delta);
        this.noButton.update(container, game, delta);

        Input input = container.getInput();
        if (input.isKeyPressed(USKeyboard.KEY_ENTER)) {
            this.close();
            this.yesListener.actionPerformed(null);
        } else if (input.isKeyPressed(USKeyboard.KEY_ESCAPE)) {
            this.close();
            this.noListener.actionPerformed(null);
        }
    }

    private void createButtons() {
        this.buttonFactory.setDefaultFont(this.font).setNormalColor(Color.blue).setOverColor(Color.red);

        String yesButtonText = this.translator.translate("yes");
        String noButtonText = this.translator.translate("no");

        this.yesButton = this.buttonFactory.setDefaultText(yesButtonText).setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                show = false;
                yesListener.actionPerformed(null);
            }
        }).createButton();

        this.noButton = this.buttonFactory.setDefaultText(noButtonText).setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                show = false;
                noListener.actionPerformed(null);
            }
        }).createButton();

        String buttonsText = yesButtonText + noButtonText;
        int buttonsWidth = this.buttonFactory.getDefaultFont().getWidth(buttonsText);
        int buttonsHeight = this.buttonFactory.getDefaultFont().getHeight(buttonsText);

        this.yesButton.setPosition(
                new org.newdawn.slick.geom.Point(this.getCenterX() - buttonsWidth, this.getMaxY() - buttonsHeight));
        this.noButton.setPosition(
                new org.newdawn.slick.geom.Point(this.getCenterX() + buttonsWidth, this.getMaxY() - buttonsHeight));
    }
}
