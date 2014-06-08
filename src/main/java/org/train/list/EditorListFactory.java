package org.train.list;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.train.entity.List;
import org.train.entity.ListItem;
import org.train.model.TextView;
import org.train.other.Translator;

public class EditorListFactory {
    private Translator translator;

    public EditorListFactory(Translator translator) {
        this.translator = translator;
    }

    public List create(GameContainer container, Font font) {
        java.util.List<ListItem> editorListItems = new ArrayList<ListItem>();
        editorListItems.add(new ListItem(new TextView(this.translator.translate("Controls.Editor"),
                font, Color.blue)));
        editorListItems.add(new ListItem(new TextView(this.getText("Controls.ShowMenu", "e"), font,
                Color.red)));
        editorListItems.add(new ListItem(new TextView(this.getText("Controls.Train", "t"), font,
                Color.red)));
        editorListItems.add(new ListItem(new TextView(this.getText("Controls.Gate", "g"), font,
                Color.red)));
        editorListItems.add(new ListItem(new TextView(this.getText("Controls.Item", "1-9"), font,
                Color.red)));
        editorListItems.add(new ListItem(new TextView(this.getText("Controls.Test", "r"), font,
                Color.red)));
        editorListItems.add(new ListItem(new TextView(this.getText("Controls.Wall", "w"), font,
                Color.red)));

        return new List(editorListItems, container);
    }

    private String getText(String option, String key) {
        return String.format("%s: %s", this.translator.translate(option), key);
    }
}
