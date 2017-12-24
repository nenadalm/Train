package org.train.list;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.train.entity.List;
import org.train.entity.ListItem;
import org.train.model.TextView;
import org.train.other.Translator;

public class GameListFactory {
    private Translator translator;

    public GameListFactory(Translator translator) {
        this.translator = translator;
    }

    public List create(GameContainer container, Font font) {
        java.util.List<ListItem> gameListItems = new ArrayList<ListItem>();
        gameListItems.add(new ListItem(new TextView(this.translator.translate("Controls.Game"), font, Color.blue)));
        gameListItems.add(new ListItem(new TextView(this.getText("Controls.Up", "up"), font, Color.red)));
        gameListItems.add(new ListItem(new TextView(this.getText("Controls.Down", "down"), font, Color.red)));
        gameListItems.add(new ListItem(new TextView(this.getText("Controls.Left", "left"), font, Color.red)));
        gameListItems.add(new ListItem(new TextView(this.getText("Controls.Right", "right"), font, Color.red)));

        return new List(gameListItems, container);
    }

    private String getText(String option, String key) {
        return String.format("%s: %s", this.translator.translate(option), key);
    }
}
