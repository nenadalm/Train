package app;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.newdawn.slick.AppGameContainer;

public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GraphicsEnvironment e = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice gd = e.getDefaultScreenDevice();
			DisplayMode actual = gd.getDisplayMode();
			
			AppGameContainer container = new AppGameContainer(new Game("Train"));
	    	container.setShowFPS(false);
			container.setTargetFrameRate(60);
			container.setDisplayMode(actual.getWidth(), actual.getHeight(), true);
			container.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
