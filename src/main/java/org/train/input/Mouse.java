package org.train.input;

import org.newdawn.slick.Input;

public class Mouse {
    private int mouseRepeat = 100;
    private int mouseRepeatInit = 1000;
    private int currentMouse = 0;
    private boolean mouseInitiated = false;

    public boolean isPressed(Input input, int button, int delta) {
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            return true;
        }

        if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            this.currentMouse += delta;

            if (this.mouseInitiated) {
                if (this.currentMouse / this.mouseRepeat >= 1) {
                    this.currentMouse = 0;
                    return true;
                }
            } else if (this.currentMouse >= this.mouseRepeatInit) {
                this.mouseInitiated = true;
                this.currentMouse = 0;
                return true;
            }

            return false;
        }

        this.currentMouse = 0;
        this.mouseInitiated = false;

        return false;
    }
}
