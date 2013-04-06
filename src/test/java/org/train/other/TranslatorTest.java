package org.train.other;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.train.app.Configuration;

public class TranslatorTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Configuration config = Configuration.getInstance();
        config.set("contentPath", "testingContent/");
        config.set("language", "en");
    }

    @Test
    public void testGetInstanceNotNull() {
        Translator instance1 = Translator.getInstance();
        Assert.assertNotNull(instance1);
    }

    @Test
    public void testGetInstanceSame() {
        Translator instance1 = Translator.getInstance();
        Translator instance2 = Translator.getInstance();
        Assert.assertSame(instance1, instance2);
    }

    @Test
    public void testTranslateStringNotExist() {
        Translator translator = Translator.getInstance();
        Assert.assertEquals("Test.TranslationNotExist",
                translator.translate("Test.TranslationNotExist"));
    }

    @Test
    public void testTranslateStringExist() {
        Translator translator = Translator.getInstance();
        Assert.assertEquals("Trans1", translator.translate("Test.Translation1"));
    }

    @Test
    public void testTranslateStringSpecificExist() {
        Translator translator = Translator.getInstance();
        Assert.assertEquals("TransSpecific",
                translator.translate("Test.TranslationSpecific", "specific"));
    }

    @Test
    public void testTranslateSpecificStringNotExis() {
        Translator translator = Translator.getInstance();
        Assert.assertEquals("Test.TranslationSpecificNotExist",
                translator.translate("Test.TranslationSpecificNotExist", "specific"));
    }
}
