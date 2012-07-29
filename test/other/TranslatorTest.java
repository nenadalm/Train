package other;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TranslatorTest {

    @Before
    public void setUp() {
        Translator.getInstance().setLanguage("test");
    }

    @Test
    public void testTranslateString() {
        String translated = Translator.getInstance().translate("test");
        Assert.assertEquals("Testing translate", translated);
    }

    @Test
    public void testTranslateString_TwoParams() {
        String translated = Translator.getInstance()
                .translate("test", "global");
        Assert.assertEquals("Testing translate", translated);
    }

    @Test
    public void testTranslateString_notInGlobal() {
        String translated = Translator.getInstance().translate("not_there");
        Assert.assertEquals("not_there", translated);
    }

    @Test
    public void testGetLanguageCode() {
        String code = Translator.getInstance().getLanguageCode();
        Assert.assertEquals("test", code);
    }

}
