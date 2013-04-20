package org.train.other;

import org.junit.Assert;
import org.junit.Test;
import org.train.app.Configuration;

public class TranslatorTest {

    private Translator translator;

    public TranslatorTest() {
        Configuration config = new Configuration();
        config.set("contentPath", "testingContent/");
        config.set("language", "en");
        this.translator = new Translator(config);
    }

    @Test
    public void testTranslateStringNotExist() {
        Assert.assertEquals("Test.TranslationNotExist",
                translator.translate("Test.TranslationNotExist"));
    }

    @Test
    public void testTranslateStringExist() {
        Assert.assertEquals("Trans1", translator.translate("Test.Translation1"));
    }

    @Test
    public void testTranslateStringSpecificExist() {
        Assert.assertEquals("TransSpecific",
                translator.translate("Test.TranslationSpecific", "specific"));
    }

    @Test
    public void testTranslateSpecificStringNotExis() {
        Assert.assertEquals("Test.TranslationSpecificNotExist",
                translator.translate("Test.TranslationSpecificNotExist", "specific"));
    }
}
