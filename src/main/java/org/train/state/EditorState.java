package org.train.state;

import java.awt.Dimension;
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
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.FlowLayout;
import org.train.entity.ImageMenuItem;
import org.train.entity.Level;
import org.train.entity.Level.Item;
import org.train.entity.Menu;
import org.train.entity.MessageBox;
import org.train.helper.LevelHelper;
import org.train.other.LevelController;
import org.train.other.ResourceManager;
import org.train.other.Translator;

public class EditorState extends BasicGameState {

    private int stateId;
    // game images
    private Image train;
    private Image gate;
    private Image tree;
    private Image wall;
    private Image save;
    // menu images
    private Image itemMenu;
    private Menu topMenu;
    private List<ImageMenuItem> imageMenuItems;
    // editor images
    private Image active;
    private Item activeItem;
    // helping fields
    private int itemSize;
    private boolean showMenu = true;
    private boolean showActive = true;
    private Point fieldPosition;
    private Level level = null;
    // states
    private boolean wasLeftButtonDown = false;
    private ResourceManager resourceManager;

    private LevelController levelController;
    float scale = 1;
    private MessageBox messageBox;
    private Translator translator;

    private Point gatePosition = null;
    private Point trainPosition = null;

    public EditorState(int stateId) {
        this.stateId = stateId;
        this.resourceManager = ResourceManager.getInstance();
    }

    @Override
    public void init(GameContainer container, final StateBasedGame game) throws SlickException {
        this.translator = Translator.getInstance();
        this.messageBox = new MessageBox(container);
        this.messageBox.setBackgroundColor(Color.lightGray);
        this.fieldPosition = new Point();

        this.train = this.resourceManager.getImage("train");
        this.gate = this.resourceManager.getImage("gate");
        this.tree = this.resourceManager.getImage("tree");
        this.wall = this.resourceManager.getImage("wall");
        this.save = this.resourceManager.getImage("save");

        this.itemMenu = this.resourceManager.getImage("itemMenu");
        this.active = this.resourceManager.getImage("active");
        this.activeItem = Item.WALL;
        this.itemSize = this.active.getWidth();

        this.levelController = LevelController.getInstance();
        this.loadLevel(container);

        this.imageMenuItems = new ArrayList<ImageMenuItem>();
        this.imageMenuItems.add(new ImageMenuItem(this.train, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                EditorState.this.showMenu = false;
                EditorState.this.activeItem = Item.TRAIN;
            }
        }));
        this.imageMenuItems.add(new ImageMenuItem(this.gate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                EditorState.this.showMenu = false;
                EditorState.this.activeItem = Item.GATE;
            }
        }));
        this.imageMenuItems.add(new ImageMenuItem(this.tree, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                EditorState.this.showMenu = false;
                EditorState.this.activeItem = Item.TREE;
            }
        }));
        this.imageMenuItems.add(new ImageMenuItem(this.wall, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                EditorState.this.showMenu = false;
                EditorState.this.activeItem = Item.WALL;
            }
        }));
        this.imageMenuItems.add(new ImageMenuItem(this.save, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                EditorState.this.showMenu = false;
                if (!EditorState.this.level.isValid()) {
                    String message = "";
                    if (EditorState.this.level.findTrainPosition() == null
                            && EditorState.this.level.findGatePosition() == null) {
                        message = EditorState.this.translator
                                .translate("Editor.Message.TrainAndGateMissing");
                    } else if (EditorState.this.level.findTrainPosition() == null) {
                        message = EditorState.this.translator
                                .translate("Editor.Message.TrainMissing");
                    } else if (EditorState.this.level.findGatePosition() == null) {
                        message = EditorState.this.translator
                                .translate("Editor.Message.GateMissing");
                    }
                    EditorState.this.messageBox.showConfirm(message, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            EditorState.this.levelController.saveCurrentLevel();
                            game.enterState(Game.MENU_FOR_EDITOR_STATE);
                        }
                    }, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            EditorState.this.messageBox.close();
                        }
                    });
                } else {
                    EditorState.this.levelController.saveCurrentLevel();
                    game.enterState(Game.MENU_FOR_EDITOR_STATE);
                }
            }
        }));
        for (ImageMenuItem item : this.imageMenuItems) {
            item.setScale(this.scale);
        }
        this.topMenu = new Menu(this.imageMenuItems, container);
        this.topMenu.setPaddingLeft((int) (this.train.getWidth() * this.scale));
        this.topMenu.setBackgroundColor(new Color(0, 0, 0, 0));
        this.topMenu.setLayout(new FlowLayout(container, this.topMenu));
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {

        if (this.level == null) {
            this.loadLevel(container);
        }

        this.level.render(container, game, g);

        if (this.showMenu) {
            this.itemMenu.draw(0, 0, container.getWidth(), this.itemMenu.getHeight() * this.scale);
            this.topMenu.render(container, game, g);
        } else {
            this.itemMenu.draw(0, -this.itemSize, container.getWidth(), this.itemMenu.getHeight()
                    * this.scale);
        }
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
        if (mouseX > offsetX && mouseY > offsetY && mouseX < offsetX + this.itemSize * levelWidth
                && mouseY < offsetY + this.itemSize * levelHeight) {
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
            if (this.messageBox.isShowed()) {
                return;
            }
            if (this.showMenu) {
                indexX = mouseX / this.itemSize;
                indexY = mouseY / this.itemSize;
                offsetX = 0;
                offsetY = 0;
            } else {
                indexX = (mouseX - offsetX) / this.itemSize;
                indexY = (mouseY - offsetY) / this.itemSize;
            }
            int width = indexX * this.itemSize;
            int height = indexY * this.itemSize;
            this.fieldPosition.setLocation(offsetX + width, offsetY + height);
            Point gridPosition = new Point(indexX, indexY);

            if (this.showMenu) {
                this.updateMenu(game, input);
                this.topMenu.update(container, game, delta);
            } else {
                this.updateEditor(gridPosition, input);
            }
            this.wasLeftButtonDown = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
        }
        this.messageBox.update(container, game, delta);
    }

    private void loadLevel(GameContainer container) {
        this.level = this.levelController.getCurrentLevel();
        this.trainPosition = this.level.findTrainPosition();
        this.gatePosition = this.level.findGatePosition();
        float scale = LevelHelper.computeScale(container, this.level.getOriginalImageSize(),
                new Dimension(this.level.getWidth(), this.level.getHeight()));
        this.level.setScale(scale);
        this.itemSize *= scale;
        this.scale = scale;
        int width = this.level.getWidth() * (this.itemSize);
        int height = this.level.getHeight() * (this.itemSize);
        this.level.setMarginLeft((container.getWidth() - width) / 2);
        this.level.setMarginTop((container.getHeight() - height) / 2);
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
            this.showMenu = !this.showMenu;
        }
        if (input.isKeyPressed(Keyboard.KEY_E)) {
            this.showMenu = !this.showMenu;
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
        int index = this.fieldPosition.x / this.itemSize - 1;
        if (mouseY < this.itemSize && index >= 0 && index < this.imageMenuItems.size()) {
            this.showActive = true;
        } else {
            this.showActive = false;
        }
    }

    @Override
    public int getID() {
        return this.stateId;
    }

}
