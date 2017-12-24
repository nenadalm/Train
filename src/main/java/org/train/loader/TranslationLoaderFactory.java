package org.train.loader;

import java.io.File;

public class TranslationLoaderFactory {

    private String translationsPath;

    public TranslationLoaderFactory(String translationsPath) {
	this.translationsPath = translationsPath;
    }

    public TranslationLoader getLoader(String translationScope, String languageCode) {
	File file = new File(this.translationsPath + languageCode + "/" + translationScope + ".xml");
	return new TranslationXmlLoader(file);
    }
}
