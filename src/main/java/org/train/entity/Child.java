package org.train.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;

import org.train.other.ResourceManager;
import org.train.factory.EffectFactory;

public abstract class Child extends BoxModel {

    private Color color = Color.red;
    private Font font;
    private String text;

    public void setContainer(GameContainer container) {
        ResourceManager resourceManager = ResourceManager.getInstance();
        EffectFactory effectFactory = EffectFactory.getInstance();
        int fontWidth = container.getWidth() / 20;
        try {
            this.font = resourceManager.getFont("ubuntu", fontWidth,
                    effectFactory.getColorEffect(java.awt.Color.white));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract int getWidth();

    public abstract int getHeight();
}
