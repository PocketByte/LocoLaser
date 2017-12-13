package locolaser.platform.mobile.resource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.platform.mobile.resource.GetTextResources;
import ru.pocketbyte.locolaser.resource.entity.*;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetTextResourcesTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Test
    public void testWriteAndRead() throws IOException {
        ResMap resMap1 = prepareResMap();

        GetTextResources resources = new GetTextResources(tempFolder.newFolder(), "test");
        resources.write(resMap1, null);

        ResMap resMap2 = resources.read(resMap1.keySet());

        assertNotNull(resMap2);
        assertEquals(resMap1, resMap2);
    }

    //FIXME Map without plurals. Add plural items when platform start support it
    private ResMap prepareResMap() {
        ResMap resMap = new ResMap();
        resMap.put("locale1", prepareResLocale1());
        resMap.put("locale2", prepareResLocale2());
        resMap.put("locale3", prepareResLocale3());
        return resMap;
    }

    private ResLocale prepareResLocale1() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER),
        }));
        resLocale.put(prepareResItem("key2", new ResValue[]{
                new ResValue("value2_1", null, Quantity.OTHER)
        }));
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value3_1", null, Quantity.OTHER),
        }));
        return resLocale;
    }

    private ResLocale prepareResLocale2() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER),
        }));
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value3_2", null, Quantity.OTHER)
        }));
        resLocale.put(prepareResItem("key4", new ResValue[]{
                new ResValue("value4_2", null, Quantity.OTHER),
        }));
        return resLocale;
    }

    private ResLocale prepareResLocale3() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_3", null, Quantity.OTHER),
        }));
        resLocale.put(prepareResItem("key4", new ResValue[]{
                new ResValue("value4_3", null, Quantity.OTHER),
        }));
        return resLocale;
    }

    private ResItem prepareResItem(String key, ResValue[] values) {
        ResItem resItem = new ResItem(key);
        for (ResValue value: values)
            resItem.addValue(value);
        return resItem;
    }
}
