package org.train.entity;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.train.component.RectangleComponent;
import org.train.factory.ButtonFactory;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.other.ResourceManager;
import org.train.other.Translator;

public class MessageBox extends Entity {
    private String text;
    private boolean show = false;
    private Font font;
    private ActionListener yesListener;
    private ActionListener noListener;

    private Color bakcgroundColor = Color.lightGray;
    private Color textColor = Color.red;

    private Button yesButton, noButton;

    public MessageBox(GameContainer container, Translator translator,
            ResourceManager resourceManager, FontFactory fontFactory, EffectFactory effectFactory,
            ButtonFactory buttonFactory) {
        this.addComponent(new RectangleComponent());

        try {
            int fontWidth = container.getWidth() / 20;
            this.font = fontFactory.getFont("ubuntu", fontWidth,
                    effectFactory.getColorEffect(java.awt.Color.WHITE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int width = (int) (container.getWidth() / 1.5);
        int height = container.getHeight() / 3;
        this.setPosition(new Point(container.getWidth() / 2 - width / 2, container.getHeight() / 2
                - height / 2));
        this.setWidth(width);
        this.setHeight(height);

        this.createButtons(buttonFactory, translator);
    }

    public void showConfirm(String text, ActionListener yesListener, ActionListener noListener) {
        this.text = text;
        this.yesListener = yesListener;
        this.noListener = noListener;
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
        this.renderMessage(g);

        this.yesButton.render(g);
        this.noButton.render(g);
    }

    private void renderMessage(Graphics g) {
        String text[] = this.text.split(" ");
        ArrayList<String> lines = new ArrayList<String>(text.length);
        String line = "";
        int last = 0;
        for (int j = 0; j < text.length; j++) {
            if (this.font.getWidth(line + " " + text[j]) + 20 > this.getWidth()) {
                if (last == j) {
                    line += text[j];
                }
                lines.add(line);
                line = (last == j) ? "" : text[j];
                last = j;
            } else {
                line += " " + text[j];
            }
        }
        lines.add(line);
        int lineHeight = this.font.getHeight("A");
        for (int j = 0; j < lines.size(); j++) {
            g.drawString(lines.get(j), this.getPosition().x + 10, this.getPosition().y + 10 + j
                    * lineHeight);
        }
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
    }

    private void createButtons(ButtonFactory buttonFactory, Translator translator) {
        buttonFactory.setDefaultFont(this.font).setNormalColor(Color.blue).setOverColor(Color.red);

        String yesButtonText = translator.translate("yes");
        String noButtonText = translator.translate("no");

        this.yesButton = buttonFactory.setDefaultText(yesButtonText)
                .setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        show = false;
                        yesListener.actionPerformed(null);
                    }
                }).createButton();

        this.noButton = buttonFactory.setDefaultText(noButtonText)
                .setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        show = false;
                        noListener.actionPerformed(null);
                    }
                }).createButton();

        String buttonsText = yesButtonText + noButtonText;
        int buttonsWidth = buttonFactory.getDefaultFont().getWidth(buttonsText);
        int buttonsHeight = buttonFactory.getDefaultFont().getHeight(buttonsText);

        this.yesButton.setPosition(new org.newdawn.slick.geom.Point(this.getCenterX()
                - buttonsWidth, this.getMaxY() - buttonsHeight));
        this.noButton.setPosition(new org.newdawn.slick.geom.Point(
                this.getCenterX() + buttonsWidth, this.getMaxY() - buttonsHeight));
    }
}
