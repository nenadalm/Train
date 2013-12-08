package org.train.state;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.FlowLayout;
import org.train.entity.ImageMenuItem;
import org.train.entity.Level;
import org.train.entity.Level.Item;
import org.train.entity.Menu;
import org.train.entity.MessageBox;
import org.train.factory.EffectFactory;
import org.train.helper.LevelHelper;
import org.train.other.LevelController;
import org.train.other.ResourceManager;
import org.train.other.Translator;
import org.train.state.listener.editor.GateSelectedListener;
import org.train.state.listener.editor.SaveSelectedListener;
import org.train.state.listener.editor.TestSelectedListener;
import org.train.state.listener.editor.TrainSelectedListener;
import org.train.state.listener.editor.TreeSelectedListener;
import org.train.state.listener.editor.WallSelectedListener;

public class EditorState extends BasicGameState {

    private int stateId;
    // game images
    private Image train;
    private Image gate;
    private Image tree;
    private Image wall;
    private Image save;
    private Image test;
    // menu images
    private Image itemMenu;
    private Menu topMenu;
    private List<ImageMenuItem> imageMenuItems;
    // editor images
    private Image active;
    private Item activeItem;
    // helping fields
    private boolean showActive = true;
    private Point fieldPosition;
    private Level level = null;
    // states
    private ResourceManager resourceManager;

    private LevelController levelController;
    float scale = 1;
    private MessageBox messageBox;
    private Translator translator;

    private Point gatePosition = null;
    private Point trainPosition = null;

    public EditorState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(GameContainer container, final StateBasedGame game) throws SlickException {
        this.resourceManager = this.container.getComponent(ResourceManager.class);
        this.translator = this.container.getComponent(Translator.class);
        this.messageBox = this.container.getComponent(MessageBox.class);
        this.messageBox.setBackgroundColor(Color.lightGray);
        this.fieldPosition = new Point();

        this.train = this.resourceManager.getImage("train");
        this.gate = this.resourceManager.getImage("gate");
        this.tree = this.resourceManager.getImage("tree");
        this.wall = this.resourceManager.getImage("wall");
        this.save = this.resourceManager.getImage("save");
        this.test = this.resourceManager.getImage("try");

        this.itemMenu = this.resourceManager.getImage("itemMenu");
        this.active = this.resourceManager.getImage("active");
        this.activeItem = Item.WALL;

        this.levelController = this.container.getComponent(LevelController.class);
        this.loadLevel(container);

        this.imageMenuItems = new ArrayList<ImageMenuItem>();
        this.initTopMenuListeners(game);

        for (ImageMenuItem item : this.imageMenuItems) {
            item.setScale(this.scale);
        }
        this.topMenu = new Menu(this.imageMenuItems, container, this.resourceManager,
                this.container.getComponent(EffectFactory.class));
        this.topMenu.setPaddingLeft((int) (this.train.getWidth() * this.scale));
        this.topMenu.setBackgroundColor(new Color(0, 0, 0, 0));
        this.topMenu.setLayout(new FlowLayout(container, this.topMenu));
        this.topMenu.show();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {

        if (this.level == null) {
            this.loadLevel(container);
        }

        this.level.render(container, game, g);

        if (this.topMenu.isShowed()) {
            this.itemMenu.draw(0, 0, container.getWidth(), this.itemMenu.getHeight() * this.scale);
        } else {
            this.itemMenu.draw(0, -this.level.getImageSize(), container.getWidth(),
                    this.itemMenu.getHeight() * this.scale);
        }
        this.topMenu.render(container, game, g);
        if (this.showActive) {
            this.active.draw(this.fieldPosition.x, this.fieldPosition.y, this.active.getWidth()
                    * this.scale, this.active.getHeight() * this.scale);
        }
        this.messageBox.render(container, game, g);
    }

    public boolean isCursorInLevelArea(int mouseX, int mouseY) {
        int offsetX = this.level.getMarginLeft();
        int offsetY = this.level.getMarginTop();
        int levelWidth = this.level.getWidth();
        int levelHeight = this.level.getHeight();
        if (mouseX > offsetX && mouseY > offsetY
                && mouseX < offsetX + this.level.getImageSize() * levelWidth
                && mouseY < offsetY + this.level.getImageSize() * levelHeight) {
            return true;
        }
        return false;
    }

    @Override
    public void update(GameContainer container, final StateBasedGame game, int delta)
            throws SlickException {

        Input input = container.getInput();
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        int offsetX = this.level.getMarginLeft();
        int offsetY = this.level.getMarginTop();
        int indexX;
        int indexY;

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            this.messageBox.showConfirm(this.translator.translate("Editor.Menu.ExitWithoutSaving"),
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            EditorState.this.level = null;
                            game.enterState(Game.MENU_FOR_EDITOR_STATE);
                        }
                    }, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            EditorState.this.messageBox.close();
                        }
                    });
        }

        if (!this.messageBox.isShowed()) {
            if (this.topMenu.isShowed()) {
                indexX = mouseX / this.level.getImageSize();
                indexY = mouseY / this.level.getImageSize();
                offsetX = 0;
                offsetY = 0;
            } else {
                indexX = (mouseX - offsetX) / this.level.getImageSize();
                indexY = (mouseY - offsetY) / this.level.getImageSize();
            }
            int width = indexX * this.level.getImageSize();
            int height = indexY * this.level.getImageSize();
            this.fieldPosition.setLocation(offsetX + width, offsetY + height);
            Point gridPosition = new Point(indexX, indexY);

            if (this.topMenu.isShowed()) {
                this.updateMenu(game, input);
            } else {
                this.updateEditor(gridPosition, input);
            }
            this.topMenu.update(container, game, delta);
        }
        this.messageBox.update(container, game, delta);
    }

    private void loadLevel(GameContainer container) {
        this.level = this.levelController.getCurrentLevel();
        this.trainPosition = this.level.findTrainPosition();
        this.gatePosition = this.level.findGatePosition();

        LevelHelper levelHelper = this.container.getComponent(LevelHelper.class);
        levelHelper.adjustLevelToContainer(container, level);

        this.scale = this.level.getScale();
    }

    private void setItemPosition(Point gridPosition) {
        // remove position
        Item item = this.level.toArray()[gridPosition.x][gridPosition.y];
        switch (item) {
            case TRAIN:
                this.trainPosition = null;
                break;
            case GATE:
                this.gatePosition = null;
            default:
        }
        // load position
        switch (this.activeItem) {
            case TRAIN:
                if (this.trainPosition != null) {
                    this.level.setItem(this.trainPosition, Item.EMPTY);
                }
                this.trainPosition = gridPosition;
                break;
            case GATE:
                if (this.gatePosition != null) {
                    this.level.setItem(this.gatePosition, Item.EMPTY);
                }
                this.gatePosition = gridPosition;
                break;
            default:
                break;
        }
        this.level.setItem(gridPosition, this.activeItem);
    }

    private void updateEditor(Point gridPosition, Input input) {
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        if (mouseY < 10) {
            this.topMenu.show();
        }
        if (input.isKeyPressed(Keyboard.KEY_E)) {
            this.topMenu.show();
        }
        if (this.isCursorInLevelArea(mouseX, mouseY)) {
            this.showActive = true;
        } else {
            this.showActive = false;
        }
        if (this.isCursorInLevelArea(mouseX, mouseY)) {
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                this.setItemPosition(gridPosition);
            } else if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
                this.level.setItem(gridPosition, Item.EMPTY);
            }
        }
    }

    private void updateMenu(final StateBasedGame game, Input input) {
        int mouseY = input.getMouseY();
        int index = this.fieldPosition.x / this.level.getImageSize() - 1;
        if (mouseY < this.level.getImageSize() && index >= 0 && index < this.imageMenuItems.size()) {
            this.showActive = true;
        } else {
            this.showActive = false;
        }
    }

    @Override
    public int getID() {
        return this.stateId;
    }

    public Menu getTopMenu() {
        return this.topMenu;
    }

    public void setActiveItem(Item item) {
        this.activeItem = item;
    }

    private void initTopMenuListeners(final StateBasedGame game) {
        this.imageMenuItems.add(new ImageMenuItem(this.train, new TrainSelectedListener(this)));
        this.imageMenuItems.add(new ImageMenuItem(this.gate, new GateSelectedListener(this)));
        this.imageMenuItems.add(new ImageMenuItem(this.tree, new TreeSelectedListener(this)));
        this.imageMenuItems.add(new ImageMenuItem(this.wall, new WallSelectedListener(this)));
        this.imageMenuItems.add(new ImageMenuItem(this.save, new SaveSelectedListener(this, level,
                this.levelController, this.translator, this.messageBox, game)));
        this.imageMenuItems.add(new ImageMenuItem(this.test, new TestSelectedListener(this)));
    }
}
