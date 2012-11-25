package entity;

import java.awt.event.ActionListener;

public class MenuItem extends Child {

    private ActionListener listener;

    public MenuItem(String text, ActionListener listener) {
        this.setText(text);
        this.listener = listener;
    }

    public ActionListener getListener() {
        return this.listener;
    }

    @Override
    public int getWidth() {
        return this.getFont().getWidth(this.getText());
    }

    @Override
    public int getHeight() {
        return this.getFont().getHeight(this.getText()) + this.getPaddingBottom()
                + this.getPaddingTop();
    }
}
