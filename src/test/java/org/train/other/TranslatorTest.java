package org.train.other;

import org.junit.Assert;
import org.junit.Test;
import org.train.loader.TranslationLoaderFactory;

public class TranslatorTest {

    private Translator translator;

    public TranslatorTest() {
        TranslationLoaderFactory factory = new TranslationLoaderFactory("testingContent/translations/");
        this.translator = new Translator(factory, "en");
    }

    @Test
    public void testTranslateStringNotExist() {
        Assert.assertEquals("Test.TranslationNotExist", translator.translate("Test.TranslationNotExist"));
    }

    @Test
    public void testTranslateStringExist() {
        Assert.assertEquals("Trans1", translator.translate("Test.Translation1"));
    }

    @Test
    public void testTranslateStringSpecificExist() {
        Assert.assertEquals("TransSpecific", translator.translate("Test.TranslationSpecific", "specific"));
    }

    @Test
    public void testTranslateSpecificStringNotExis() {
        Assert.assertEquals("Test.TranslationSpecificNotExist",
                translator.translate("Test.TranslationSpecificNotExist", "specific"));
    }
}
