package org.train.factory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.train.entity.Button;
import org.train.model.TextView;
import org.train.model.ViewInterface;

public class ButtonFactory {
    private Font defaultFont;
    private Color defaultColor;

    private String defaultText;
    private Color normalColor, overColor, disabledColor;
    private ActionListener listener;

    public ButtonFactory() {
        this.defaultFont = new TrueTypeFont(new java.awt.Font("ubuntu", java.awt.Font.PLAIN, 40),
                true);
        this.defaultColor = Color.red;
        this.defaultText = "";
        this.listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        };
    }

    public Font getDefaultFont() {
        return this.defaultFont;
    }

    public ButtonFactory setDefaultText(String text) {
        this.defaultText = text;

        return this;
    }

    public ButtonFactory setDefaultFont(Font font) {
        this.defaultFont = font;

        return this;
    }

    public ButtonFactory setDefaultColor(Color color) {
        this.defaultColor = color;

        return this;
    }

    public ButtonFactory setNormalColor(Color color) {
        this.normalColor = color;

        return this;
    }

    public ButtonFactory setOverColor(Color color) {
        this.overColor = color;

        return this;
    }

    public ButtonFactory setDisabledColor(Color color) {
        this.disabledColor = color;

        return this;
    }

    public ButtonFactory setListener(ActionListener listener) {
        this.listener = listener;

        return this;
    }

    public Button createButton() {
        return new Button(this.createNormalView(), this.createOverView(),
                this.createDisabledView(), this.listener);
    }

    private ViewInterface createNormalView() {
        return this.createView(this.defaultText, this.defaultFont, this.normalColor);
    }

    private ViewInterface createOverView() {
        return this.createView(this.defaultText, this.defaultFont, this.overColor);
    }

    private ViewInterface createDisabledView() {
        return this.createView(this.defaultText, this.defaultFont, this.disabledColor);
    }

    private ViewInterface createView(String text, Font font, Color color) {
        Color usedColor = color != null ? color : this.defaultColor;
        Font usedFont = font != null ? font : this.defaultFont;
        String usedText = text != null ? text : this.defaultText;

        return new TextView(usedText, usedFont, usedColor);
    }
}
