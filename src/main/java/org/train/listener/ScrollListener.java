package org.train.listener;

public class ScrollListener extends AbstractMouseListener {
    private Scrollable scrollable;

    public ScrollListener(Scrollable scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean isAcceptingInput() {
        return this.scrollable.getOccupiedArea().contains(this.input.getMouseX(),
                this.input.getMouseY());
    }

    @Override
    public void mouseWheelMoved(int change) {
        if (change > 0) {
            this.scrollable.scrollUp();
        } else {
            this.scrollable.scrollDown();
        }
    }
}
