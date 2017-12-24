package org.train.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.train.app.Configuration;
import org.train.model.Progress;

public class ProgressController {
    private Progress progress;
    private Configuration config;
    private byte[] progresses;

    public ProgressController(Configuration config) {
	this.config = config;
    }

    public Progress getCurrentProgress() {
	if (this.progress == null) {
	    this.loadProgress();
	}

	return this.progress;
    }

    public void updateProgress(int packageIndex, int finishedLevelIndex) {
	if (this.progresses[packageIndex] < (byte) (finishedLevelIndex + 1)) {
	    this.progresses[packageIndex] = (byte) (finishedLevelIndex + 1);
	}
	try {
	    FileOutputStream fos = new FileOutputStream(this.getProgressFile());
	    fos.write(this.progresses);
	    fos.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void loadProgress() {
	File saveFile = this.getProgressFile();
	this.progresses = new byte[100];
	try {
	    if (!saveFile.exists()) {
		saveFile.createNewFile();
	    }
	    FileInputStream in = new FileInputStream(saveFile);
	    in.read(this.progresses);
	    in.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	this.progress = new Progress(this.progresses);
    }

    private File getProgressFile() {
	return new File(this.config.get("contentPath") + "save");
    }
}
