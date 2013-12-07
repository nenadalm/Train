package org.train.factory;

import java.io.File;

import org.newdawn.slick.AppGameContainer;
import org.picocontainer.Characteristics;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.behaviors.Caching;
import org.train.app.Configuration;
import org.train.app.Game;
import org.train.entity.MessageBox;
import org.train.helper.LevelHelper;
import org.train.loader.ConfigurationXmlLoader;
import org.train.loader.TranslationLoaderFactory;
import org.train.menu.MenuBuilder;
import org.train.other.Display;
import org.train.other.LevelController;
import org.train.other.ResourceManager;
import org.train.other.Translator;
import org.train.storer.ConfigurationXmlStorer;

public class PicoContainerFactory {

    public PicoContainer create() {
        DefaultPicoContainer container = new DefaultPicoContainer(new Caching());

        String configFilePath = "config.xml";
        container.addComponent(new ConfigurationXmlLoader(new File(configFilePath)));
        container.addComponent(new ConfigurationXmlStorer(new File(configFilePath)));
        container.addComponent(Configuration.class);
        Configuration config = container.getComponent(Configuration.class);

        String translationsPath = config.get("contentPath") + "translations/";
        container.addComponent(new TranslationLoaderFactory(translationsPath));
        container.addComponent(new Translator(container
                .getComponent(TranslationLoaderFactory.class), config.get("language")));

        container.addComponent(new Game("Train", container));
        container.addComponent(AppGameContainer.class);
        container.addComponent(MessageBox.class);
        container.addComponent(LevelController.class);
        container.addComponent(ResourceManager.class);
        container.addComponent(FontFactory.class);
        container.addComponent(EffectFactory.class);
        container.addComponent(LevelHelper.class);
        container.addComponent(Display.class);
        container.as(Characteristics.NO_CACHE).addComponent(MenuBuilder.class);

        return container;
    }
}
