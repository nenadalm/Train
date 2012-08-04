package app;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class App {

    /**
     * @param args
     * @throws SlickException
     */
    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Game("Train"));
            app.setDisplayMode(800, 500, false);
            app.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
