package state;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.Translator;

import app.Configuration;
import app.Game;

public class MenuState extends BasicGameState {

	private boolean isMouseOverStartGame, isMouseOverLevelEditor,
			isMouseOverOptions, isMouseOverExit, isMouseOverReturn,
			isMouseOverSave, isMouseOverResolution, isMouseOverFullscreen,
			isMouseOverLanguage, isInOptions, isFullscreen;
	private int stateId, languageIndex, width, height,
			distanceBetweenMenuEntries, modeIndex, trainTextWidth,
			trainTextHeight;
	private UnicodeFont ubuntuMedium, ubuntuLarge;
	private Rectangle startGameRectangle, levelEditorRectangle,
			optionsRectangle, exitRectangle, saveRectangle, returnRectangle,
			resolutionRectangle, fullscreenRectangle, languageRectangle;
	private DisplayMode displayModes[];
	private Input input;
	private Point mouse;
	private String resolutionText, languages[];
	private Translator translator;

	public MenuState(int stateId) {
		this.stateId = stateId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		translator = Translator.getInstance();
		width = container.getWidth();
		height = container.getHeight();
		String fontPath = Game.CONTENT_PATH + "fonts/ubuntu.ttf";
		ubuntuMedium = new UnicodeFont(fontPath, width / 25, false, false);
		ubuntuMedium.addGlyphs(32, 800);
		ubuntuMedium.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		ubuntuMedium.loadGlyphs();

		ubuntuLarge = new UnicodeFont(fontPath, width / 16, false, false);
		ubuntuLarge.addGlyphs(32, 800);
		ubuntuLarge.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
		ubuntuLarge.loadGlyphs();

		distanceBetweenMenuEntries = height / 5;

		startGameRectangle = new Rectangle();
		startGameRectangle.width = ubuntuMedium.getWidth(translator
				.translate("new game"));
		startGameRectangle.height = ubuntuMedium.getHeight(translator
				.translate("new game"));
		startGameRectangle.x = width / 2 - startGameRectangle.width / 2;
		startGameRectangle.y = distanceBetweenMenuEntries
				- startGameRectangle.height / 2;

		levelEditorRectangle = new Rectangle();
		levelEditorRectangle.width = ubuntuMedium.getWidth(translator
				.translate("level editor"));
		levelEditorRectangle.height = ubuntuMedium.getHeight(translator
				.translate("level editor"));
		levelEditorRectangle.x = width / 2 - levelEditorRectangle.width / 2;
		levelEditorRectangle.y = distanceBetweenMenuEntries * 2
				- levelEditorRectangle.height / 2;

		optionsRectangle = new Rectangle();
		optionsRectangle.width = ubuntuMedium.getWidth(translator
				.translate("options"));
		optionsRectangle.height = ubuntuMedium.getHeight(translator
				.translate("options"));
		optionsRectangle.x = width / 2 - optionsRectangle.width / 2;
		optionsRectangle.y = distanceBetweenMenuEntries * 3
				- optionsRectangle.height / 2;

		exitRectangle = new Rectangle();
		exitRectangle.width = ubuntuMedium.getWidth(translator
				.translate("exit"));
		exitRectangle.height = ubuntuMedium.getHeight(translator
				.translate("exit"));
		exitRectangle.x = width / 2 - exitRectangle.width / 2;
		exitRectangle.y = distanceBetweenMenuEntries * 4 - exitRectangle.height
				/ 2;

		trainTextWidth = ubuntuLarge.getWidth("Train 1.0");
		trainTextHeight = ubuntuLarge.getHeight("Train 1.0");
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (!isInOptions) {
			drawMenu(container, game, g);
		} else {
			drawOptions(container, game, g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		input = container.getInput();
		mouse = new Point(input.getMouseX(), input.getMouseY());
		if (!isInOptions) {
			updateMenu(container, game, delta);
		} else {
			updateOptions(container, game, delta);
		}
	}

	@Override
	public int getID() {
		return this.stateId;
	}

	private void drawMenu(GameContainer container, StateBasedGame game,
			Graphics g) {
		g.setFont(ubuntuLarge);
		g.setColor(Color.gray);
		drawString(g, ubuntuLarge, "Train 1.0",
				(int) (trainTextWidth / 1.75) + 3,
				(int) (trainTextHeight / 1.5) + 2);
		g.setColor(Color.white);
		drawString(g, ubuntuLarge, "Train 1.0", (int) (trainTextWidth / 1.75),
				(int) (trainTextHeight / 1.5));
		g.setFont(ubuntuMedium);
		g.setColor((isMouseOverStartGame) ? Color.blue : Color.red);
		drawString(g, ubuntuMedium, translator.translate("start game"),
				width / 2, distanceBetweenMenuEntries);
		g.setColor((isMouseOverLevelEditor) ? Color.blue : Color.red);
		drawString(g, ubuntuMedium, translator.translate("level editor"),
				width / 2, distanceBetweenMenuEntries * 2);
		g.setColor((isMouseOverOptions) ? Color.blue : Color.red);
		drawString(g, ubuntuMedium, translator.translate("options"), width / 2,
				distanceBetweenMenuEntries * 3);
		g.setColor((isMouseOverExit) ? Color.blue : Color.red);
		drawString(g, ubuntuMedium, translator.translate("exit"), width / 2,
				distanceBetweenMenuEntries * 4);
	}

	private void drawOptions(GameContainer container, StateBasedGame game,
			Graphics g) {
		g.setFont(ubuntuLarge);
		g.setColor(Color.white);
		drawString(
				g,
				ubuntuLarge,
				translator.translate("Options"),
				width / 2,
				(int) (ubuntuLarge.getHeight(translator.translate("Options")) / 1.75));
		g.setFont(ubuntuMedium);
		drawString(g, ubuntuMedium, translator.translate("Resolution") + ":",
				width / 5 * 2, height / 8 * 2);
		drawString(g, ubuntuMedium, translator.translate("Fullscreen") + ":",
				width / 5 * 2, height / 8 * 3);
		drawString(g, ubuntuMedium, translator.translate("Language") + ":",
				width / 5 * 2, height / 8 * 4);
		g.setColor((isMouseOverResolution) ? Color.blue : Color.red);
		drawString(g, ubuntuMedium, resolutionText, width / 5 * 3,
				height / 8 * 2);
		g.setColor((isMouseOverFullscreen) ? Color.blue : Color.red);
		drawString(g, ubuntuMedium, isFullscreen ? translator.translate("yes")
				: translator.translate("no"), width / 5 * 3, height / 8 * 3);
		g.setColor((isMouseOverLanguage) ? Color.blue : Color.red);
		drawString(g, ubuntuMedium, languages[languageIndex], width / 5 * 3,
				height / 8 * 4);

		g.setColor((isMouseOverSave) ? Color.red : Color.white);
		drawString(g, ubuntuMedium, translator.translate("save"), width / 3,
				(int) (height - ubuntuMedium.getHeight(translator
						.translate("save"))));
		g.setColor((isMouseOverReturn) ? Color.red : Color.white);
		drawString(g, ubuntuMedium, translator.translate("return"),
				(int) (width / 1.5),
				(int) (height - ubuntuMedium.getHeight(translator
						.translate("return"))));
	}

	private void drawString(Graphics g, UnicodeFont font, String text, float x,
			float y) {
		int width = font.getWidth(text);
		int height = font.getHeight(text);
		g.drawString(text, x - width / 2, y - height / 2);
	}

	private void updateMenu(GameContainer container, StateBasedGame game,
			int delta) {
		isMouseOverStartGame = startGameRectangle.contains(mouse);
		isMouseOverLevelEditor = levelEditorRectangle.contains(mouse);
		isMouseOverOptions = optionsRectangle.contains(mouse);
		isMouseOverExit = exitRectangle.contains(mouse);

		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			container.exit();
		}

		if (input.isMousePressed(0)) {
			if (isMouseOverLevelEditor) {
				game.enterState(Game.EDITOR_STATE);
			}
			if (isMouseOverOptions) {
				isInOptions = true;
				initOptions(container);
			}
			if (isMouseOverExit) {
				container.exit();
			}
		}
	}

	private void updateOptions(GameContainer container, StateBasedGame game,
			int delta) {
		isMouseOverResolution = resolutionRectangle.contains(mouse);
		isMouseOverFullscreen = fullscreenRectangle.contains(mouse);
		isMouseOverLanguage = languageRectangle.contains(mouse);
		isMouseOverSave = saveRectangle.contains(mouse);
		isMouseOverReturn = returnRectangle.contains(mouse);

		if (input.isKeyDown(Input.KEY_ENTER)) {
			saveOptions(container, game);
		}
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			isInOptions = false;
		}
		if (input.isMousePressed(0)) {
			if (isMouseOverResolution) {
				modeIndex++;
				if (modeIndex == displayModes.length) {
					modeIndex = 0;
				}
				setResolutionRectangle();
			}
			if (isMouseOverFullscreen) {
				isFullscreen = !isFullscreen;
				setFullscreenRectangle();
			}
			if (isMouseOverLanguage) {
				languageIndex++;
				if (languageIndex == languages.length) {
					languageIndex = 0;
				}
				setLanguageRectangle();
			}
			if (isMouseOverLanguage) {
				setLanguageRectangle();
			}
			if (isMouseOverSave) {
				saveOptions(container, game);
			}
			if (isMouseOverReturn) {
				isInOptions = false;
			}
		}
		if (input.isMousePressed(1)) {
			if (isMouseOverResolution) {
				modeIndex--;
				if (modeIndex == -1) {
					modeIndex = displayModes.length - 1;
				}
				setResolutionRectangle();
			}
			if (isMouseOverLanguage) {
				languageIndex--;
				if (languageIndex == -1) {
					languageIndex = languages.length - 1;
				}
				setLanguageRectangle();
			}
		}
	}

	private void initOptions(GameContainer container) {
		ArrayList<DisplayMode> modes = new ArrayList<DisplayMode>();
		GraphicsEnvironment environment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice graphicsDevice = environment.getDefaultScreenDevice();
		int index = 0;
		for (DisplayMode mode : graphicsDevice.getDisplayModes()) {
			if (mode.getRefreshRate() == 60 && mode.getBitDepth() == 32) {
				modes.add(mode);
				if (mode.getWidth() == width && mode.getHeight() == height) {
					modeIndex = index;
				}
				index++;
			}
		}
		displayModes = new DisplayMode[modes.size()];
		modes.toArray(displayModes);

		File translations = new File(Game.CONTENT_PATH + "translations/");
		translations.listFiles();
		ArrayList<String> names = new ArrayList<String>();
		index = 0;
		for (File file : translations.listFiles()) {
			if (file.isDirectory()) {
				names.add(file.getName());
				if (translator.getLanguageCode().equals(file.getName())) {
					languageIndex = index;
				}
			}
			index++;
		}
		languages = new String[names.size()];
		names.toArray(languages);

		isFullscreen = container.isFullscreen();

		resolutionRectangle = new Rectangle();
		setResolutionRectangle();

		fullscreenRectangle = new Rectangle();
		setFullscreenRectangle();

		languageRectangle = new Rectangle();
		setLanguageRectangle();

		saveRectangle = new Rectangle();
		saveRectangle.width = ubuntuMedium.getWidth(translator
				.translate("save"));
		saveRectangle.height = ubuntuMedium.getHeight(translator
				.translate("save"));
		saveRectangle.x = (int) (width / 3) - saveRectangle.width / 2;
		saveRectangle.y = (int) (height - saveRectangle.height)
				- saveRectangle.height / 2;

		returnRectangle = new Rectangle();
		returnRectangle.width = ubuntuMedium.getWidth(translator
				.translate("return"));
		returnRectangle.height = ubuntuMedium.getHeight(translator
				.translate("return"));
		returnRectangle.x = (int) (width / 1.5) - returnRectangle.width / 2;
		returnRectangle.y = (int) (height - returnRectangle.height)
				- returnRectangle.height / 2;
	}

	private void saveOptions(GameContainer container, StateBasedGame game) {
		try {
			Configuration configuration = Configuration.getInstance();
			configuration.set("width",
					String.valueOf(displayModes[modeIndex].getWidth()));
			configuration.set("height",
					String.valueOf(displayModes[modeIndex].getHeight()));
			configuration.set("language", languages[languageIndex]);
			configuration.set("fullscreen", String.valueOf(isFullscreen));
			((AppGameContainer) container).setDisplayMode(
					displayModes[modeIndex].getWidth(),
					displayModes[modeIndex].getHeight(), isFullscreen);
			translator.setLanguage(languages[languageIndex]);
			init(container, game);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		isInOptions = false;
	}

	private void setResolutionRectangle() {
		resolutionText = String.format("%1$2sx%2$2s",
				displayModes[modeIndex].getWidth(),
				displayModes[modeIndex].getHeight());
		resolutionRectangle.width = ubuntuMedium.getWidth(resolutionText);
		resolutionRectangle.height = ubuntuMedium.getHeight(resolutionText);
		resolutionRectangle.x = width / 5 * 3 - resolutionRectangle.width / 2;
		resolutionRectangle.y = height / 8 * 2 - resolutionRectangle.height / 2;
	}

	private void setFullscreenRectangle() {
		fullscreenRectangle.width = ubuntuMedium
				.getWidth(isFullscreen ? translator.translate("yes")
						: translator.translate("no"));
		fullscreenRectangle.height = ubuntuMedium
				.getHeight(isFullscreen ? translator.translate("yes")
						: translator.translate("no"));
		fullscreenRectangle.x = width / 5 * 3 - fullscreenRectangle.width / 2;
		fullscreenRectangle.y = height / 8 * 3 - fullscreenRectangle.height / 2;
	}

	private void setLanguageRectangle() {
		languageRectangle.width = ubuntuMedium
				.getWidth(languages[languageIndex]);
		languageRectangle.height = ubuntuMedium
				.getHeight(languages[languageIndex]);
		languageRectangle.x = width / 5 * 3 - languageRectangle.width / 2;
		languageRectangle.y = height / 8 * 4 - languageRectangle.height / 2;
	}
}