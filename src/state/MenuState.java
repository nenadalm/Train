package state;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import app.Game;

public class MenuState extends BasicGameState {

	private boolean isMouseOverStartGame, isMouseOverLevelEditor,
			isMouseOverOptions, isMouseOverExit;
	private int stateId;
	private UnicodeFont ubuntu32, ubuntu48;
	private Rectangle startGameRectangle, levelEditorRectangle,
			optionsRectangle, exitRectangle;

	private void drawString(Graphics g, UnicodeFont font, String text, float x,
			float y) {
		int width = font.getWidth(text);
		int height = font.getHeight(text);
		g.drawString(text, x - width / 2, y - height / 2);
	}

	public MenuState(int stateId) {
		this.stateId = stateId;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		String fontPath = "content/fonts/ubuntu.ttf";
		ubuntu32 = new UnicodeFont(fontPath, 32, false, false);
		ubuntu32.addGlyphs(32, 256);
		ubuntu32.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		ubuntu32.loadGlyphs();

		ubuntu48 = new UnicodeFont(fontPath, 48, false, false);
		ubuntu48.addGlyphs(32, 256);
		ubuntu48.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		ubuntu48.loadGlyphs();

		startGameRectangle = new Rectangle();
		startGameRectangle.width = ubuntu32.getWidth("new game");
		startGameRectangle.height = ubuntu32.getHeight("new game");
		startGameRectangle.x = 400 - startGameRectangle.width / 2;
		startGameRectangle.y = 150 - startGameRectangle.height / 2;

		levelEditorRectangle = new Rectangle();
		levelEditorRectangle.width = ubuntu32.getWidth("level editor");
		levelEditorRectangle.height = ubuntu32.getHeight("level editor");
		levelEditorRectangle.x = 400 - levelEditorRectangle.width / 2;
		levelEditorRectangle.y = 233 - levelEditorRectangle.height / 2;

		optionsRectangle = new Rectangle();
		optionsRectangle.width = ubuntu32.getWidth("options");
		optionsRectangle.height = ubuntu32.getHeight("options");
		optionsRectangle.x = 400 - optionsRectangle.width / 2;
		optionsRectangle.y = 317 - optionsRectangle.height / 2;

		exitRectangle = new Rectangle();
		exitRectangle.width = ubuntu32.getWidth("exit");
		exitRectangle.height = ubuntu32.getHeight("exit");
		exitRectangle.x = 400 - exitRectangle.width / 2;
		exitRectangle.y = 400 - exitRectangle.height / 2;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setFont(ubuntu48);
		g.setColor(Color.gray);
		drawString(g, ubuntu48, "Train 1.0", 102, 51);
		g.setColor(Color.white);
		drawString(g, ubuntu48, "Train 1.0", 100, 50);
		g.setFont(ubuntu32);
		g.setColor((isMouseOverStartGame) ? Color.blue : Color.red);
		drawString(g, ubuntu32, "start game", 400, 150);
		g.setColor((isMouseOverLevelEditor) ? Color.blue : Color.red);
		drawString(g, ubuntu32, "level editor", 400, 233);
		g.setColor((isMouseOverOptions) ? Color.blue : Color.red);
		drawString(g, ubuntu32, "options", 400, 317);
		g.setColor((isMouseOverExit) ? Color.blue : Color.red);
		drawString(g, ubuntu32, "exit", 400, 400);
		Display.sync(60);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		Input input = container.getInput();
		Point mouse = new Point(input.getMouseX(), input.getMouseY());
		isMouseOverStartGame = startGameRectangle.contains(mouse);
		isMouseOverLevelEditor = levelEditorRectangle.contains(mouse);
		isMouseOverOptions = optionsRectangle.contains(mouse);
		isMouseOverExit = exitRectangle.contains(mouse);

		if (input.isMousePressed(0)) {
			if (isMouseOverLevelEditor) {
				game.enterState(Game.EDITOR_STATE);
			}
			if (isMouseOverExit) {
				container.exit();
			}
		}
	}

	@Override
	public int getID() {
		return this.stateId;
	}

}