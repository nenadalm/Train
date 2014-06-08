package org.train.state;

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
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;
import org.train.app.Game;
import org.train.entity.FlowLayout;
import org.train.entity.ImageMenuItem;
import org.train.entity.Level;
import org.train.entity.Level.Item;
import org.train.entity.LevelItem;
import org.train.entity.Menu;
import org.train.entity.MessageBox;
import org.train.factory.EffectFactory;
import org.train.helper.LevelHelper;
import org.train.listener.AbstractKeyListener;
import org.train.model.Truck;
import org.train.other.LevelController;
import org.train.other.ResourceManager;
import org.train.other.Translator;
import org.train.state.listener.editor.GateSelectedListener;
import org.train.state.listener.editor.ItemSelectedListener;
import org.train.state.listener.editor.SaveSelectedListener;
import org.train.state.listener.editor.TestSelectedListener;
import org.train.state.listener.editor.TrainSelectedListener;
import org.train.state.listener.editor.TreeSelectedListener;
import org.train.state.listener.editor.WallSelectedListener;

public class EditorState extends BasicGameState {

    // menu images
    private Image itemMenu;
    private Menu topMenu;
    private List<ImageMenuItem> imageMenuItems;
    // editor images
    private Image active;
    private LevelItem activeItem;
    // helping fields
    private boolean showActive = true;
    private Point fieldPosition;
    private Level level = null;
    // states
    private ResourceManager resourceManager;

    private LevelController levelController;
    private MessageBox messageBox;
    private Translator translator;
    private boolean wasTopMenuShowed = false;

    private Point gatePosition = null;
    private Point trainPosition = null;

    public EditorState(int stateId) {
        super(stateId);
    }

    @Override
    public void init(GameContainer container, final StateBasedGame game) throws SlickException {
        this.resourceManager = this.container.getComponent(ResourceManager.class);
        this.translator = this.container.getComponent(Translator.class);
        this.messageBox = this.container.getComponent(MessageBox.class);
        this.messageBox.setBackgroundColor(Color.lightGray);
        this.fieldPosition = new Point(0, 0);

        this.itemMenu = this.resourceManager.getImage("itemMenu");
        this.active = this.resourceManager.getImage("active");
        this.activeItem = new LevelItem("wall", this.resourceManager.getImage("wall"), Item.WALL);

        this.levelController = this.container.getComponent(LevelController.class);
        this.loadLevel(container);

        this.imageMenuItems = new ArrayList<ImageMenuItem>();
        this.initTopMenuListeners(game, this.resourceManager, container.getInput());

        for (ImageMenuItem item : this.imageMenuItems) {
            item.setScale(this.level.getScale());
        }
        this.topMenu = new Menu(this.imageMenuItems, container, this.resourceManager,
                this.container.getComponent(EffectFactory.class));
        this.topMenu.setPaddingLeft((int) (this.active.getWidth() * this.level.getScale()));
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
            this.itemMenu.draw(0, 0, container.getWidth(),
                    this.itemMenu.getHeight() * this.level.getScale());
        } else {
            this.itemMenu.draw(0, -this.level.getImageSize(), container.getWidth(),
                    this.itemMenu.getHeight() * this.level.getScale());
        }
        this.topMenu.render(container, game, g);
        if (this.showActive) {
            this.active.draw(this.fieldPosition.getX(), this.fieldPosition.getY(),
                    this.active.getWidth() * this.level.getScale(), this.active.getHeight()
                            * this.level.getScale());
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
                this.wasTopMenuShowed = true;
            } else if (!this.wasTopMenuShowed || !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                this.wasTopMenuShowed = false;
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
    }

    private void setItemPosition(Point gridPosition) {
        // remove position
        Item item = this.level.getLevelItems()[(int) gridPosition.getX()][(int) gridPosition.getY()]
                .getType();
        switch (item) {
            case TRAIN:
                this.trainPosition = null;
                break;
            case GATE:
                this.gatePosition = null;
            default:
        }
        // load position
        switch (this.activeItem.getType()) {
            case TRAIN:
                if (this.trainPosition != null) {
                    this.level.setLevelItem(this.activeItem, this.trainPosition);
                }
                this.trainPosition = gridPosition;
                break;
            case GATE:
                if (this.gatePosition != null) {
                    this.level.setLevelItem(this.activeItem, this.gatePosition);
                }
                this.gatePosition = gridPosition;
                break;
            default:
                break;
        }
        this.level.setLevelItem(this.activeItem, gridPosition);
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
                this.level.setLevelItem(
                        new LevelItem("empty", this.resourceManager.getImage("empty"), Item.EMPTY),
                        gridPosition);
            }
        }
    }

    private void updateMenu(final StateBasedGame game, Input input) {
        int mouseY = input.getMouseY();
        int index = (int) this.fieldPosition.getX() / this.level.getImageSize() - 1;
        if (mouseY < this.level.getImageSize() && index >= 0 && index < this.imageMenuItems.size()) {
            this.showActive = true;
        } else {
            this.showActive = false;
        }
    }

    public Menu getTopMenu() {
        return this.topMenu;
    }

    public void setActiveItem(LevelItem item) {
        this.activeItem = item;
    }

    private void initTopMenuListeners(final StateBasedGame game, ResourceManager resourceManager,
            Input input) {
        Image train = this.resourceManager.getImage("train");
        Image gate = this.resourceManager.getImage("gate");
        Image wall = this.resourceManager.getImage("wall");
        Image save = this.resourceManager.getImage("save");
        Image test = this.resourceManager.getImage("try");

        final ItemSelectedListener trainSelectedListener = new TrainSelectedListener(this, train);
        this.imageMenuItems.add(new ImageMenuItem(train, trainSelectedListener));

        final ItemSelectedListener gateSelectedListener = new GateSelectedListener(this, gate);
        this.imageMenuItems.add(new ImageMenuItem(gate, gateSelectedListener));

        final List<ItemSelectedListener> itemSelectedListeners = new ArrayList<ItemSelectedListener>();
        for (String name : this.resourceManager.getTrucks().keySet()) {
            Truck t = this.resourceManager.getTrucks().get(name);
            itemSelectedListeners.add(new TreeSelectedListener(this, name, t.getItem()));
            this.imageMenuItems.add(new ImageMenuItem(t.getItem(), itemSelectedListeners
                    .get(itemSelectedListeners.size() - 1)));
        }

        final ItemSelectedListener wallSelectedListener = new WallSelectedListener(this, wall);
        this.imageMenuItems.add(new ImageMenuItem(wall, wallSelectedListener));
        final ItemSelectedListener saveSelectedListener = new SaveSelectedListener(this, level,
                this.levelController, this.translator, messageBox, game);
        this.imageMenuItems.add(new ImageMenuItem(save, saveSelectedListener));
        final ItemSelectedListener testSelectedListener = new TestSelectedListener(this, game);
        this.imageMenuItems.add(new ImageMenuItem(test, testSelectedListener));
        input.addKeyListener(new AbstractKeyListener() {
            @Override
            public boolean isAcceptingInput() {
                return true;
            }

            @Override
            public void keyPressed(int key, char c) {
                switch (c) {
                    case 't':
                        trainSelectedListener.actionPerformed(null);
                        break;
                    case 'g':
                        gateSelectedListener.actionPerformed(null);
                        break;
                    case 'w':
                        wallSelectedListener.actionPerformed(null);
                        break;
                    case 's':
                        saveSelectedListener.actionPerformed(null);
                        break;
                    case 'r':
                        testSelectedListener.actionPerformed(null);
                        break;
                    default:
                        if (c >= '0' && c <= '9') {
                            int index = Integer.valueOf(String.valueOf(c)) - 1;
                            if (index < itemSelectedListeners.size()) {
                                itemSelectedListeners.get(index).actionPerformed(null);
                            }
                        }
                }
            }
        });
    }
}
