package org.train.state;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.GradientEffect;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.Button;
import org.train.factory.ButtonFactory;
import org.train.factory.EffectFactory;
import org.train.factory.FontFactory;
import org.train.listener.ScrollListener;
import org.train.listener.Scrollable;
import org.train.model.Progress;
import org.train.model.TextView;
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
    private TextView[] packageNameViews;
    private TextView[][] levelNameViews;
    private TextView lockedLevelNameView;
    private TextView noneLevelNameView;
    private TextView currentLevelView;
    private MouseListener packageNameMouseListener, levelNameMouseListener;

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

        this.packageNameViews = new TextView[this.levelPackages.size()];
        for (int i = 0; i < this.levelPackages.size(); i++) {
            this.packageNameViews[i] = new TextView(levelPackages.get(i).getName(),
                    this.ubuntuMedium, Color.white);
            this.packageNameViews[i].setPosition(new Point(width / 2
                    - this.packageNameViews[i].getWidth() / 2, height * 1 / 3 + height / 50));

        }
        this.levelNameViews = new TextView[this.levelPackages.size()][];
        for (int i = 0; i < this.levelNameViews.length; i++) {
            this.levelNameViews[i] = new TextView[this.levelPackages.get(i).getLevelNames().size()];

            for (int j = 0; j < this.levelNameViews[i].length; j++) {
                this.levelNameViews[i][j] = new TextView(this.levelPackages.get(i).getLevelNames()
                        .get(j), this.ubuntuMedium, Color.white);

                this.levelNameViews[i][j].setPosition(new Point(width / 2
                        - this.levelNameViews[i][j].getWidth() / 2, height * 3 / 4 + height / 40));
            }
        }
        this.lockedLevelNameView = new TextView(String.format("» %1$s «",
                translator.translate("locked")), this.ubuntuMedium, Color.darkGray);
        this.lockedLevelNameView.setPosition(new Point(width / 2
                - this.lockedLevelNameView.getWidth() / 2, height * 3 / 4 + height / 40));
        this.noneLevelNameView = new TextView(String.format("« %1$s »",
                translator.translate("none")), this.ubuntuMedium, Color.darkGray);
        this.noneLevelNameView.setPosition(new Point(width / 2 - this.noneLevelNameView.getWidth()
                / 2, height * 3 / 4 + height / 40));

        this.progress = levelController.getProgress();

        this.createArrowButtons();

        this.createBackButton(game);
        this.createPlayButton(game);

        packageIndex = 0;
        int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
        byte lastAvailableLevel = this.progress.getLastAvailableLevelIndex(this.packageIndex);
        this.levelIndex = (lastAvailableLevel == packageSize && packageSize > 0) ? lastAvailableLevel - 1
                : lastAvailableLevel;
        setProgressText();
        setShowingText();

        this.packageNameMouseListener = new ScrollListener(new Scrollable() {
            @Override
            public void scrollUp() {
                showPrevPackage();
            }

            @Override
            public void scrollDown() {
                showNextPackage();
            }

            @Override
            public Rectangle getOccupiedArea() {
                return new Rectangle(packageArrowLeft.getPosition().getX(), packageArrowLeft
                        .getPosition().getY(), packageArrowRight.getPosition().getX()
                        + packageArrowRight.getWidth() - packageArrowLeft.getPosition().getX(),
                        packageArrowRight.getPosition().getY() + packageArrowRight.getHeight()
                                - packageArrowLeft.getPosition().getY());
            }
        });
        this.packageNameMouseListener.setInput(container.getInput());
        container.getInput().addMouseListener(this.packageNameMouseListener);

        this.levelNameMouseListener = new ScrollListener(new Scrollable() {

            @Override
            public Rectangle getOccupiedArea() {
                return new Rectangle(levelArrowLeft.getPosition().getX(), levelArrowLeft
                        .getPosition().getY(), levelArrowRight.getPosition().getX()
                        + levelArrowRight.getWidth() - levelArrowLeft.getPosition().getX(),
                        levelArrowRight.getPosition().getY() + levelArrowRight.getHeight()
                                - levelArrowLeft.getPosition().getY());
            }

            @Override
            public void scrollUp() {
                showPrevLevel();
            }

            @Override
            public void scrollDown() {
                showNextLevel();
            }
        });
        this.levelNameMouseListener.setInput(container.getInput());
        container.getInput().addMouseListener(this.levelNameMouseListener);

        this.updateArrows();
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

        this.backBtn.render(g);
        this.playBtn.render(g);

        this.packageNameViews[this.packageIndex].render(g);
        this.currentLevelView.render(g);

        if (levelIndex < levelPackages.get(packageIndex).getLevelNames().size()) {
            g.drawString(showingText, width - ubuntuMedium.getWidth(showingText) * 1.1f, height
                    - ubuntuMedium.getHeight(showingText) * 1.1f);
        }

        g.setColor(new Color(0, 0.5f, 0));
        g.drawString(progressText, width - ubuntuMedium.getWidth(progressText) * 1.1f,
                height * 16 / 30);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();

        this.packageArrowLeft.update(container, game, delta);
        this.packageArrowRight.update(container, game, delta);
        this.levelArrowLeft.update(container, game, delta);
        this.levelArrowRight.update(container, game, delta);

        if (levelIndex < levelPackages.get(packageIndex).getLevelNames().size()) {
            if (this.progress.isLevelAvailable(this.packageIndex, this.levelIndex)) {
                this.currentLevelView = this.levelNameViews[this.packageIndex][this.levelIndex];
            } else {
                this.currentLevelView = this.lockedLevelNameView;
            }
        } else {
            this.currentLevelView = this.noneLevelNameView;
        }

        playBtn.setEnabled(levelIndex < levelPackages.get(packageIndex).getLevelNames().size()
                && this.progress.isLevelAvailable(packageIndex, this.levelIndex));

        this.playBtn.update(container, game, delta);
        this.backBtn.update(container, game, delta);

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }
    }

    private void setProgressText() {
        int size = levelPackages.get(packageIndex).getLevelNames().size();
        progressText = String.format("%4$s %1$d %3$s %2$d",
                this.progress.getLastAvailableLevelIndex(packageIndex), size,
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
                showPrevPackage();
            }
        }).createButton();
        this.packageArrowLeft.setPosition(new org.newdawn.slick.geom.Point(width * 1 / 4,
                height * 1 / 3));

        this.packageArrowRight = buttonFactory.setDefaultText(">")
                .setListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showNextPackage();
                    }
                }).createButton();
        this.packageArrowRight.setPosition(new org.newdawn.slick.geom.Point(width * 3 / 4
                - this.ubuntuLarge.getWidth(">"), height * 1 / 3));

        this.levelArrowLeft = buttonFactory.setDefaultText("<").setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPrevLevel();
            }
        }).createButton();
        this.levelArrowLeft.setPosition(new org.newdawn.slick.geom.Point(width * 1 / 4,
                height * 3 / 4));

        this.levelArrowRight = buttonFactory.setDefaultText(">").setListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextLevel();
            }
        }).createButton();
        this.levelArrowRight.setPosition(new org.newdawn.slick.geom.Point(width * 3 / 4
                - this.ubuntuLarge.getWidth(">"), height * 3 / 4));
    }

    private void showNextPackage() {
        if (!this.isNextPackageAvailable()) {
            return;
        }

        packageIndex++;
        int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
        byte lastAvailableLevel = progress.getLastAvailableLevelIndex(packageIndex);
        levelIndex = (lastAvailableLevel == packageSize && packageSize > 0) ? lastAvailableLevel - 1
                : lastAvailableLevel;
        setProgressText();
        setShowingText();
        this.updateArrows();
    }

    private void showPrevPackage() {
        if (!this.isPrevPackageAvailable()) {
            return;
        }

        packageIndex--;
        int packageSize = levelPackages.get(packageIndex).getLevelNames().size();
        byte lastAvailableLevel = progress.getLastAvailableLevelIndex(packageIndex);
        levelIndex = (lastAvailableLevel == packageSize && packageSize > 0) ? lastAvailableLevel - 1
                : lastAvailableLevel;
        setProgressText();
        setShowingText();
        this.updateArrows();
    }

    private void showNextLevel() {
        if (!this.isNextLevelAvailable()) {
            return;
        }

        levelIndex++;
        setShowingText();
        this.updateArrows();
    }

    private void showPrevLevel() {
        if (!this.isPrevLevelAvailable()) {
            return;
        }

        levelIndex--;
        setShowingText();
        this.updateArrows();
    }

    private boolean isNextPackageAvailable() {
        return packageIndex < levelPackages.size() - 1;
    }

    private boolean isPrevPackageAvailable() {
        return packageIndex > 0;
    }

    private boolean isNextLevelAvailable() {
        return levelIndex < levelPackages.get(packageIndex).getLevelNames().size() - 1;
    }

    private boolean isPrevLevelAvailable() {
        return levelIndex > 0;
    }

    private void updateArrows() {
        this.packageArrowRight.setEnabled(this.isNextPackageAvailable());
        this.packageArrowLeft.setEnabled(this.isPrevPackageAvailable());
        this.levelArrowRight.setEnabled(this.isNextLevelAvailable());
        this.levelArrowLeft.setEnabled(this.isPrevLevelAvailable());
    }
}
