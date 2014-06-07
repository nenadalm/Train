package org.train.model;

public class Progress {

    private byte progresses[];

    public Progress(byte progresses[]) {
        this.progresses = progresses;
    }

    public byte getLastCompletedLevelIndex(int packageIndex) {
        return this.progresses[packageIndex];
    }
}
