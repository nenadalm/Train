package entity;

import helper.MathHelper;

import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import other.ResourceManager;
import other.Translator;

public class MessageBox extends Entity {
    private String text;
    private Rectangle buttons[];
    private String buttonsText[];
    private Rectangle box;
    private boolean show = false;
    private Font font;
    private int active = -1;
    private ActionListener yesListener;
    private ActionListener noListener;

    public MessageBox(GameContainer container) {
        ResourceManager resourceManager = ResourceManager.getInstance();
        Translator translator = Translator.getInstance();
        this.buttons = new Rectangle[2];
        this.buttonsText = new String[2];
        this.buttonsText[0] = translator.translate("yes");
        this.buttonsText[1] = translator.translate("no");
        int fontWidth = container.getWidth() / 20;
        try {
            this.font = resourceManager.getFont("ubuntu", fontWidth, new ColorEffect(
                    java.awt.Color.WHITE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int width = 400;
        int height = 200;
        this.box = new Rectangle(container.getWidth() / 2 - width / 2, container.getHeight() / 2
                - height / 2, width, height);
        int buttonsWidth = this.font.getWidth(this.buttonsText[0] + this.buttonsText[1]);
        int buttonsHeight = this.font.getHeight(this.buttonsText[0] + this.buttonsText[1]);
        this.buttons[0] = new Rectangle(this.box.getCenterX() - buttonsWidth, this.box.getMaxY()
                - buttonsHeight, this.font.getWidth(this.buttonsText[0]),
                this.font.getHeight(this.buttonsText[0]));
        this.buttons[1] = new Rectangle(this.box.getCenterX() + buttonsWidth, this.box.getMaxY()
                - buttonsHeight, this.font.getWidth(this.buttonsText[1]),
                this.font.getHeight(this.buttonsText[1]));
    }

    public void showConfirm(String text, ActionListener yesListener, ActionListener noListener) {
        this.text = text;
        this.yesListener = yesListener;
        this.noListener = noListener;
        this.show = true;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        super.render(container, game, g);
        if (!this.show) {
            return;
        }
        g.setColor(Color.lightGray);
        g.fillRect(this.box.getX(), this.box.getY(), this.box.getWidth(), this.box.getHeight());
        container.getWidth();
        g.setFont(this.font);
        g.setColor(Color.red);
        String text[] = this.text.split(" ");
        ArrayList<String> lines = new ArrayList<String>(text.length);
        String line = "";
        int last = 0;
        for (int j = 0; j < text.length; j++) {
            if (this.font.getWidth(line + " " + text[j]) + 20 > this.box.getWidth()) {
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
            g.drawString(lines.get(j), this.box.getMinX() + 10, this.box.getMinY() + 10 + j
                    * lineHeight);
        }
        g.setColor(Color.blue);
        int i = 0;
        for (Rectangle button : this.buttons) {
            if (this.active == i) {
                g.setColor(Color.red);
            }
            g.drawString(this.buttonsText[i], button.getX(), button.getY());
            g.setColor(Color.blue);
            i++;
        }
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) {
        super.update(container, game, delta);
        if (!this.show) {
            return;
        }
        Input input = container.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        this.active = -1;
        for (int i = 0; i < this.buttons.length; i++) {
            if (MathHelper.rectangleContainsPoint(this.buttons[i], new Point(mouseX, mouseY))) {
                this.active = i;
            }
        }
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (this.active == 1) {
                this.show = false;
                this.noListener.actionPerformed(null);
            } else if (this.active == 0) {
                this.show = false;
                this.yesListener.actionPerformed(null);
            }
        }
    }
}
