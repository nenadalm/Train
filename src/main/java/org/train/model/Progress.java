package org.train.model;

public class Progress {

    private byte progresses[];

    public Progress(byte progresses[]) {
        this.progresses = progresses;
    }

    public boolean isLevelAvailable(int packageIndex, int levelIndex) {
        return this.getLastAvailableLevelIndex(packageIndex) >= levelIndex;
    }

    public byte getLastAvailableLevelIndex(int packageIndex) {
        return this.progresses[packageIndex];
    }
}
