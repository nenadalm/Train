package org.train.level;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.newdawn.slick.Image;
import org.train.entity.Level.Item;
import org.train.entity.LevelItem;

class ImageMock extends Image {
}

@RunWith(Parameterized.class)
public class LevelValidatorTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                // invalid levels
                { LevelValidatorTest.createLevelItems(new Item[][] { { Item.EMPTY, Item.EMPTY, Item.EMPTY },
                        { Item.EMPTY, Item.EMPTY, Item.EMPTY }, { Item.EMPTY, Item.EMPTY, Item.EMPTY } }),
                        LevelValidationError.MISSING_TRAIN },
                { LevelValidatorTest.createLevelItems(new Item[][] { { Item.EMPTY, Item.EMPTY, Item.EMPTY },
                        { Item.TRAIN, Item.EMPTY, Item.EMPTY }, { Item.EMPTY, Item.EMPTY, Item.EMPTY } }),
                        LevelValidationError.MISSING_GATE },
                { LevelValidatorTest.createLevelItems(new Item[][] { { Item.EMPTY, Item.WALL, Item.EMPTY },
                        { Item.TRAIN, Item.WALL, Item.EMPTY }, { Item.EMPTY, Item.WALL, Item.GATE } }),
                        LevelValidationError.IMPASSABLE },
                { LevelValidatorTest.createLevelItems(new Item[][] { { Item.TRAIN, Item.WALL, Item.ITEM },
                        { Item.EMPTY, Item.EMPTY, Item.WALL }, { Item.EMPTY, Item.EMPTY, Item.GATE } }),
                        LevelValidationError.UNREACHABLE_CONSUMABLE },
                { LevelValidatorTest.createLevelItems(new Item[][] { { Item.WALL, Item.WALL, Item.WALL },
                        { Item.TRAIN, Item.ITEM, Item.WALL }, { Item.GATE, Item.WALL, Item.WALL }, }),
                        LevelValidationError.DEAD_END_CONSUMABLE },
                /// valid levels
                { LevelValidatorTest.createLevelItems(new Item[][] { { Item.EMPTY, Item.EMPTY, Item.EMPTY },
                        { Item.TRAIN, Item.EMPTY, Item.EMPTY }, { Item.EMPTY, Item.EMPTY, Item.GATE } }), null },
                { LevelValidatorTest.createLevelItems(new Item[][] { { Item.TRAIN, Item.WALL, Item.EMPTY },
                        { Item.EMPTY, Item.EMPTY, Item.EMPTY }, { Item.EMPTY, Item.WALL, Item.GATE } }), null },
                { LevelValidatorTest.createLevelItems(new Item[][] { { Item.TRAIN, Item.WALL, Item.EMPTY },
                        { Item.EMPTY, Item.EMPTY, Item.ITEM }, { Item.EMPTY, Item.WALL, Item.GATE } }), null } });
    }

    @Parameter
    public LevelItem[][] levelItems;
    @Parameter(1)
    public LevelValidationError expectedError;

    @Test
    public void testValidate() {
        Assert.assertEquals(this.expectedError, LevelValidator.validateLevelItems(this.levelItems));
    }

    public static LevelItem[][] createLevelItems(Item[][] items) {
        LevelItem[][] levelItems = new LevelItem[items.length][items[0].length];

        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                levelItems[i][j] = new LevelItem("name", new ImageMock(), items[i][j]);
            }
        }

        return levelItems;
    }
}
