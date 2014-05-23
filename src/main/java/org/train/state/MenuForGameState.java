package org.train.state;

import java.awt.Point;
import java.awt.Rectangle;
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
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.other.InteractiveLabel;
import org.train.other.LevelController;
import org.train.other.LevelPackage;
import org.train.other.Translator;

public class MenuForGameState extends BasicGameState {

    private boolean isMouseOverPackageArrowLeft, isMouseOverPackageArrowRight,
            isMouseOverLevelArrowLeft, isMouseOverLevelArrowRight, isPackageArrowLeftDisabled,
            isPackageArrowRightDisabled, isLevelArrowLeftDisabled, isLevelArrowRightDisabled;
    private int width, height, packageIndex, levelIndex;
    private Font ubuntuMedium, ubuntuLarge;
    private String progressText, showingText;

    private Translator translator;
    private byte[] progresses;
    private Rectangle packageArrowLeft, packageArrowRight, levelArrowLeft, levelArrowRight;
    private ArrayList<LevelPackage> levelPackages;
    private LevelController levelController;
    private InteractiveLabel play, back;

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

        packageArrowLeft = this.createArrowRectangle(width * 1 / 4, height * 1 / 3, 0);
        packageArrowRight = this.createArrowRectangle(width * 3 / 4, height * 1 / 3, 1);
        levelArrowLeft = this.createArrowRectangle(width * 1 / 4, height * 3 / 4, 0);
        levelArrowRight = this.createArrowRectangle(width * 3 / 4, height * 3 / 4, 1);

        initBackLabel();
        initPlayLabel();

        packageIndex = 0;
        int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
        levelIndex = (progresses[packageIndex] == packageSize && packageSize > 0) ? progresses[packageIndex] - 1
                : progresses[packageIndex];
        setProgressText();
        setShowingText();
    }

    private Rectangle createArrowRectangle(float dx, float dy, int c) {
        int charWidth = ubuntuLarge.getWidth(">");

        Rectangle arrow = new Rectangle();
        arrow.width = charWidth;
        arrow.height = charWidth;
        arrow.x = (int) (dx - charWidth * c);
        arrow.y = (int) (dy + arrow.height * 3 / 4);

        return arrow;
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        g.setFont(ubuntuLarge);
        g.setColor(Color.white);
        g.drawString(translator.translate("Packages"), width / 30, height / 10);
        g.drawString(translator.translate("Levels"), width / 30, height / 2);

        g.setColor((isPackageArrowLeftDisabled) ? Color.darkGray
                : ((isMouseOverPackageArrowLeft) ? Color.red : Color.white));
        g.drawString("<", width * 1 / 4, height * 1 / 3);
        g.setColor((isPackageArrowRightDisabled) ? Color.darkGray
                : ((isMouseOverPackageArrowRight) ? Color.red : Color.white));
        g.drawString(">", width * 3 / 4 - packageArrowRight.width, height * 1 / 3);
        g.setColor((isLevelArrowLeftDisabled) ? Color.darkGray
                : ((isMouseOverLevelArrowLeft) ? Color.red : Color.white));
        g.drawString("<", width * 1 / 4, height * 3 / 4);
        g.setColor((isLevelArrowRightDisabled) ? Color.darkGray
                : ((isMouseOverLevelArrowRight) ? Color.red : Color.white));
        g.drawString(">", width * 3 / 4 - levelArrowRight.width, height * 3 / 4);
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

        isPackageArrowLeftDisabled = packageIndex < 1;
        isPackageArrowRightDisabled = packageIndex >= levelPackages.size() - 1;
        isLevelArrowLeftDisabled = levelIndex < 1;
        isLevelArrowRightDisabled = levelIndex >= levelPackages.get(packageIndex).getLevelNames()
                .size() - 1;
        play.setEnabled(levelIndex < levelPackages.get(packageIndex).getLevelNames().size()
                && levelIndex <= progresses[packageIndex]);

        isMouseOverPackageArrowLeft = packageArrowLeft.contains(mouse);
        isMouseOverPackageArrowRight = packageArrowRight.contains(mouse);
        isMouseOverLevelArrowLeft = levelArrowLeft.contains(mouse);
        isMouseOverLevelArrowRight = levelArrowRight.contains(mouse);
        back.setIsMouseOver(mouse);
        play.setIsMouseOver(mouse);

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }

        if (input.isMousePressed(0)) {
            if (isMouseOverPackageArrowLeft && !isPackageArrowLeftDisabled) {
                packageIndex--;
                int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
                levelIndex = (progresses[packageIndex] == packageSize && packageSize > 0) ? progresses[packageIndex] - 1
                        : progresses[packageIndex];
                setProgressText();
                setShowingText();
            }
            if (isMouseOverPackageArrowRight && !isPackageArrowRightDisabled) {
                packageIndex++;
                int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
                levelIndex = (progresses[packageIndex] == packageSize && packageSize > 0) ? progresses[packageIndex] - 1
                        : progresses[packageIndex];
                setProgressText();
                setShowingText();
            }
            if (isMouseOverLevelArrowLeft && !isLevelArrowLeftDisabled) {
                levelIndex--;
                setShowingText();
            }
            if (isMouseOverLevelArrowRight && !isLevelArrowRightDisabled) {
                levelIndex++;
                setShowingText();
            }
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
