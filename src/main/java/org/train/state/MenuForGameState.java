package org.train.state;

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
import org.train.factory.ButtonFactory;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.model.Progress;
import org.train.other.LevelController;
import org.train.other.LevelPackage;
import org.train.other.Translator;

public class MenuForGameState extends BasicGameState {

    private int width, height, packageIndex, levelIndex;
    private Font ubuntuMedium, ubuntuLarge;
    private String progressText, showingText;

    private Translator translator;
    private Progress progress;
    private ArrayList<LevelPackage> levelPackages;
    private LevelController levelController;
    private Button playBtn, backBtn;
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
        this.progress = levelController.getProgress();

        this.createArrowButtons();

        this.createBackButton(game);
        this.createPlayButton(game);

        packageIndex = 0;
        int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
        byte lastCompletedLevel = this.progress.getLastCompletedLevelIndex(this.packageIndex);
        this.levelIndex = (lastCompletedLevel == packageSize && packageSize > 0) ? lastCompletedLevel - 1
                : lastCompletedLevel;
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
        this.backBtn.render(g);
        this.playBtn.render(g);
        g.setColor(Color.white);
        g.drawString(text, width / 2 - ubuntuMedium.getWidth(text) / 2, height * 1 / 3 + height
                / 50);
        if (levelIndex < levelPackages.get(packageIndex).getLevelNames().size()) {
            g.drawString(showingText, width - ubuntuMedium.getWidth(showingText) * 1.1f, height
                    - ubuntuMedium.getHeight(showingText) * 1.1f);
            if (levelIndex <= this.progress.getLastCompletedLevelIndex(this.packageIndex)) {
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

        this.packageArrowLeft.setEnabled(packageIndex > 0);
        this.packageArrowRight.setEnabled(packageIndex < levelPackages.size() - 1);
        this.levelArrowLeft.setEnabled(levelIndex > 0);
        this.levelArrowRight.setEnabled(levelIndex < levelPackages.get(packageIndex)
                .getLevelNames().size() - 1);

        this.packageArrowLeft.update(container, game, delta);
        this.packageArrowRight.update(container, game, delta);
        this.levelArrowLeft.update(container, game, delta);
        this.levelArrowRight.update(container, game, delta);

        playBtn.setEnabled(levelIndex < levelPackages.get(packageIndex).getLevelNames().size()
                && levelIndex <= this.progress.getLastCompletedLevelIndex(packageIndex));

        this.playBtn.update(container, game, delta);
        this.backBtn.update(container, game, delta);

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }
    }

    private void setProgressText() {
        int size = levelPackages.get(packageIndex).getLevelNames().size();
        progressText = String.format("%4$s %1$d %3$s %2$d",
                this.progress.getLastCompletedLevelIndex(packageIndex), size,
                translator.translate((size > 1 && size < 5) ? "Alternative.Of" : "Of"),
                translator.translate("completed"));
    }

    private void setShowingText() {
        int size = levelPackages.get(packageIndex).getLevelNames().size();
        showingText = String.format("%4$s %1$d %3$s %2$d", levelIndex + 1, size,
                translator.translate((size > 1 && size < 5) ? "Alternative.of" : "Of"),
                translator.translate("showing"));
    }

    private void createBackButton(final StateBasedGame game) {
        ButtonFactory buttonFactory = this.container.getComponent(ButtonFactory.class);
        this.backBtn = buttonFactory.setDefaultColor(Color.white).setDisabledColor(Color.darkGray)
                .setOverColor(Color.red).setDefaultText(translator.translate("back"))
                .setDefaultFont(ubuntuMedium).setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.enterState(Game.MENU_STATE);
                    }
                }).createButton();

        this.backBtn.setPosition(new org.newdawn.slick.geom.Point(width / 100,
                (int) (height - this.backBtn.getHeight() * 1.1f)));
    }

    private void createPlayButton(final StateBasedGame game) {
        ButtonFactory buttonFactory = this.container.getComponent(ButtonFactory.class);
        this.playBtn = buttonFactory.setDefaultColor(Color.white).setDisabledColor(Color.darkGray)
                .setOverColor(Color.red).setDefaultText(translator.translate("play"))
                .setDefaultFont(ubuntuMedium).setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            levelController.loadLevel(packageIndex, levelIndex);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        game.enterState(Game.GAME_STATE);
                    }
                }).createButton();

        this.playBtn.setPosition(new org.newdawn.slick.geom.Point(width / 2
                - this.playBtn.getWidth() / 2, (int) (height - this.playBtn.getHeight() * 1.1f)));
    }

    private void createArrowButtons() {
        ButtonFactory buttonFactory = this.container.getComponent(ButtonFactory.class);
        buttonFactory.setDefaultFont(this.ubuntuLarge).setNormalColor(Color.white)
                .setOverColor(Color.red).setDisabledColor(Color.darkGray);

        this.packageArrowLeft = buttonFactory.setDefaultText("<").setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                packageIndex--;
                int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
                byte lastCompletedLevel = progress.getLastCompletedLevelIndex(packageIndex);
                levelIndex = (lastCompletedLevel == packageSize && packageSize > 0) ? lastCompletedLevel - 1
                        : lastCompletedLevel;
                setProgressText();
                setShowingText();
            }
        }).createButton();
        this.packageArrowLeft.setPosition(new org.newdawn.slick.geom.Point(width * 1 / 4,
                height * 1 / 3));

        this.packageArrowRight = buttonFactory.setDefaultText(">")
                .setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        packageIndex++;
                        int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
                        byte lastCompletedLevel = progress.getLastCompletedLevelIndex(packageIndex);
                        levelIndex = (lastCompletedLevel == packageSize && packageSize > 0) ? lastCompletedLevel - 1
                                : lastCompletedLevel;
                        setProgressText();
                        setShowingText();
                    }
                }).createButton();
        this.packageArrowRight.setPosition(new org.newdawn.slick.geom.Point(width * 3 / 4
                - this.ubuntuLarge.getWidth(">"), height * 1 / 3));

        this.levelArrowLeft = buttonFactory.setDefaultText("<").setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                levelIndex--;
                setShowingText();
            }
        }).createButton();
        this.levelArrowLeft.setPosition(new org.newdawn.slick.geom.Point(width * 1 / 4,
                height * 3 / 4));

        this.levelArrowRight = buttonFactory.setDefaultText(">").setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                levelIndex++;
                setShowingText();
            }
        }).createButton();
        this.levelArrowRight.setPosition(new org.newdawn.slick.geom.Point(width * 3 / 4
                - this.ubuntuLarge.getWidth(">"), height * 3 / 4));
    }
}
