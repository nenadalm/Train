package org.train.state;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.GradientEffect;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.Button;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.model.TextView;
import org.train.other.InteractiveLabel;
import org.train.other.LevelController;
import org.train.other.LevelPackage;
import org.train.other.Translator;

public class MenuForGameState extends BasicGameState {

    private int width, height, packageIndex, levelIndex;
    private Font ubuntuMedium, ubuntuLarge;
    private String progressText, showingText;

    private Translator translator;
    private byte[] progresses;
    private ArrayList<LevelPackage> levelPackages;
    private LevelController levelController;
    private InteractiveLabel play, back;
    private Button packageArrowLeft, packageArrowRight, levelArrowLeft, levelArrowRight;

    public MenuForGameState(int stateId) {
        super(stateId);
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        FontFactory fonts = this.container.getComponent(FontFactory.class);
        EffectFactory effects = this.container.getComponent(EffectFactory.class);
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        GradientEffect gradientEffect = effects.getGradientEffect(java.awt.Color.WHITE,
                java.awt.Color.GRAY, 0.5f);
        translator = this.container.getComponent(Translator.class);
        width = container.getWidth();
        height = container.getHeight();

        ubuntuMedium = fonts.getFont("ubuntu", width / 26, whiteEffect);
        ubuntuLarge = fonts.getFont("ubuntu", width / 16, gradientEffect);

        levelController = this.container.getComponent(LevelController.class);
        levelPackages = levelController.getLevels();
        progresses = levelController.getProgresses();

        this.packageArrowLeft = new Button(new TextView("<", this.ubuntuLarge, Color.white),
                new TextView("<", this.ubuntuLarge, Color.red), new TextView("<", this.ubuntuLarge,
                        Color.darkGray), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        packageIndex--;
                        int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
                        levelIndex = (progresses[packageIndex] == packageSize && packageSize > 0) ? progresses[packageIndex] - 1
                                : progresses[packageIndex];
                        setProgressText();
                        setShowingText();
                    }
                });
        this.packageArrowLeft.setPosition(new org.newdawn.slick.geom.Point(width * 1 / 4,
                height * 1 / 3));
        this.packageArrowRight = new Button(new TextView(">", this.ubuntuLarge, Color.white),
                new TextView(">", this.ubuntuLarge, Color.red), new TextView(">", this.ubuntuLarge,
                        Color.darkGray), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        packageIndex++;
                        int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
                        levelIndex = (progresses[packageIndex] == packageSize && packageSize > 0) ? progresses[packageIndex] - 1
                                : progresses[packageIndex];
                        setProgressText();
                        setShowingText();
                    }
                });

        this.packageArrowRight.setPosition(new org.newdawn.slick.geom.Point(width * 3 / 4
                - this.ubuntuLarge.getWidth(">"), height * 1 / 3));

        this.levelArrowLeft = new Button(new TextView("<", this.ubuntuLarge, Color.white),
                new TextView("<", this.ubuntuLarge, Color.red), new TextView("<", this.ubuntuLarge,
                        Color.darkGray), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        levelIndex--;
                        setShowingText();
                    }
                });
        this.levelArrowLeft.setPosition(new org.newdawn.slick.geom.Point(width * 1 / 4,
                height * 3 / 4));

        this.levelArrowRight = new Button(new TextView(">", this.ubuntuLarge, Color.white),
                new TextView(">", this.ubuntuLarge, Color.red), new TextView(">", this.ubuntuLarge,
                        Color.darkGray), new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        levelIndex++;
                        setShowingText();
                    }
                });
        this.levelArrowRight.setPosition(new org.newdawn.slick.geom.Point(width * 3 / 4
                - this.ubuntuLarge.getWidth(">"), height * 3 / 4));

        initBackLabel();
        initPlayLabel();

        packageIndex = 0;
        int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
        levelIndex = (progresses[packageIndex] == packageSize && packageSize > 0) ? progresses[packageIndex] - 1
                : progresses[packageIndex];
        setProgressText();
        setShowingText();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        g.setFont(ubuntuLarge);
        g.setColor(Color.white);
        g.drawString(translator.translate("Packages"), width / 30, height / 10);
        g.drawString(translator.translate("Levels"), width / 30, height / 2);

        this.packageArrowLeft.render(g);
        this.packageArrowRight.render(g);
        this.levelArrowLeft.render(g);
        this.levelArrowRight.render(g);

        String text = levelPackages.get(packageIndex).getName();
        g.setFont(ubuntuMedium);
        back.render(g);
        play.render(g);
        g.setColor(Color.white);
        g.drawString(text, width / 2 - ubuntuMedium.getWidth(text) / 2, height * 1 / 3 + height
                / 50);
        if (levelIndex < levelPackages.get(packageIndex).getLevelNames().size()) {
            g.drawString(showingText, width - ubuntuMedium.getWidth(showingText) * 1.1f, height
                    - ubuntuMedium.getHeight(showingText) * 1.1f);
            if (levelIndex <= progresses[packageIndex]) {
                text = levelPackages.get(packageIndex).getLevelNames().get(levelIndex);
            } else {
                g.setColor(Color.darkGray);
                text = String.format("» %1$s «", translator.translate("locked"));
            }

        } else {
            g.setColor(Color.darkGray);
            text = String.format("« %1$s »", translator.translate("none"));
        }
        g.drawString(text, width / 2 - ubuntuMedium.getWidth(text) / 2, height * 3 / 4 + height
                / 40);
        g.setColor(new Color(0, 0.5f, 0));
        g.drawString(progressText, width - ubuntuMedium.getWidth(progressText) * 1.1f,
                height * 16 / 30);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();
        Point mouse = new Point(input.getMouseX(), input.getMouseY());

        this.packageArrowLeft.setEnabled(packageIndex > 0);
        this.packageArrowRight.setEnabled(packageIndex < levelPackages.size() - 1);
        this.levelArrowLeft.setEnabled(levelIndex > 0);
        this.levelArrowRight.setEnabled(levelIndex < levelPackages.get(packageIndex)
                .getLevelNames().size() - 1);

        this.packageArrowLeft.update(container, game, delta);
        this.packageArrowRight.update(container, game, delta);
        this.levelArrowLeft.update(container, game, delta);
        this.levelArrowRight.update(container, game, delta);

        play.setEnabled(levelIndex < levelPackages.get(packageIndex).getLevelNames().size()
                && levelIndex <= progresses[packageIndex]);

        back.setIsMouseOver(mouse);
        play.setIsMouseOver(mouse);

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }

        if (input.isMousePressed(0)) {
            if (back.isMouseOver()) {
                game.enterState(Game.MENU_STATE);
            }
            if (play.isMouseOver()) {
                try {
                    levelController.loadLevel(packageIndex, levelIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                input.clearKeyPressedRecord();
                game.enterState(Game.GAME_STATE);
            }
        }
    }

    private void setProgressText() {
        int size = levelPackages.get(packageIndex).getLevelNames().size();
        progressText = String.format("%4$s %1$d %3$s %2$d", progresses[packageIndex], size,
                translator.translate((size > 1 && size < 5) ? "Alternative.Of" : "Of"),
                translator.translate("completed"));
    }

    private void setShowingText() {
        int size = levelPackages.get(packageIndex).getLevelNames().size();
        showingText = String.format("%4$s %1$d %3$s %2$d", levelIndex + 1, size,
                translator.translate((size > 1 && size < 5) ? "Alternative.of" : "Of"),
                translator.translate("showing"));
    }

    private void initBackLabel() {
        String backText = translator.translate("back");
        Rectangle rectangle = new Rectangle();
        rectangle.width = ubuntuMedium.getWidth(backText);
        rectangle.height = ubuntuMedium.getHeight(backText);
        rectangle.x = width / 100;
        rectangle.y = (int) (height - rectangle.height * 1.1f);
        Point position = new Point(rectangle.x, rectangle.y);
        back = new InteractiveLabel(backText, position, rectangle);
        back.setColors(Color.white, Color.red, Color.darkGray);
    }

    private void initPlayLabel() {
        String playText = translator.translate("play");
        Rectangle rectangle = new Rectangle();
        rectangle.width = ubuntuMedium.getWidth(playText);
        rectangle.height = ubuntuMedium.getHeight(playText);
        rectangle.x = width / 2 - rectangle.width / 2;
        rectangle.y = (int) (height - rectangle.height * 1.1f);
        Point position = new Point(rectangle.x, rectangle.y);
        play = new InteractiveLabel(playText, position, rectangle);
        play.setColors(Color.white, Color.red, Color.darkGray);
    }
}
