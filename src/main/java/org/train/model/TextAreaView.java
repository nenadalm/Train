package org.train.model;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.train.helper.StringHelper;

public class TextAreaView implements ViewInterface {

    private List<TextView> textViews = new ArrayList<TextView>();
    private Point position;
    private int width;
    private int height;
    private int optimalWidth;

    public TextAreaView(String text, Font font, Color color, int optimalWidth) {
        this.optimalWidth = optimalWidth;
        this.setPosition(new Point(0, 0));
        this.recreateTextViews(text, font, color);
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
        this.recomputeTextViewPositions();
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public int getOptimalWidth() {
        return this.optimalWidth;
    }

    @Override
    public void render(Graphics g) {
        for (TextView textView : this.textViews) {
            textView.render(g);
        }
    }

    private void recreateTextViews(String text, Font font, Color color) {
        this.width = 0;
        this.height = 0;
        this.textViews = new ArrayList<TextView>();

        List<String> lines = StringHelper.createLines(text, this.optimalWidth, font);
        for (String l : lines) {
            TextView textView = new TextView(l, font, color);
            this.textViews.add(textView);

            this.width = Math.max(this.width, textView.getWidth());
            this.height = Math.max(this.height, textView.getHeight());
        }

        this.recomputeTextViewPositions();
    }

    private void recomputeTextViewPositions() {
        Point nextPosition = this.getPosition();

        for (TextView textView : this.textViews) {
            textView.setPosition(nextPosition);
            nextPosition = new Point(nextPosition.getX(), nextPosition.getY()
                    + textView.getHeight() + 10);
        }
    }
}
