package org.train.other;

import java.util.HashMap;
import java.util.Map;

import org.train.loader.TranslationLoader;
import org.train.loader.TranslationLoaderFactory;

public class Translator {

    private String languageCode;
    private Map<String, Map<String, String>> translations;
    private TranslationLoaderFactory translationLoaderFactory;

    public Translator(TranslationLoaderFactory translationLoaderFactory, String languageCode) {
        this.translationLoaderFactory = translationLoaderFactory;
        this.languageCode = languageCode;
        this.init();
    };

    private void init() {
        this.translations = new HashMap<String, Map<String, String>>();
        this.loadTranslations("global");
    }

    private void loadTranslations(String scope) {
        TranslationLoader translationLoader = this.translationLoaderFactory.getLoader(scope,
                this.languageCode);
        this.translations.put(scope, translationLoader.load());
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public void setLanguage(String code) {
        this.languageCode = code;
        this.init();
    }

    public String translate(String text) {
        return this.translate(text, "global");
    }

    public String translate(String text, String scope) {
        if (!this.translations.containsKey(scope)) {
            this.loadTranslations(scope);
        }

        if (this.translations.get(scope).containsKey(text)) {
            return this.translations.get(scope).get(text);
        }

        return text;
    }
}
