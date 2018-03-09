package ru.pocketbyte.locolaser.resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.resource.file.ResourceFile;
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformResources;
import ru.pocketbyte.locolaser.testutils.mock.MockResourceFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PlatformSetResourcesTest {

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    private File resFolder;

    @Before
    public void init() throws IOException {
        resFolder = tempFolder.newFolder();
    }

    @Test
    public void testRead() {
        PlatformSetResources resources = prepareResources(
                new MockResourceFile(prepareResMap1()),
                new MockResourceFile(prepareResMap2())
        );

        ResMap expected = prepareResMap1().merge(prepareResMap2());

        assertEquals(expected, resources.read(getAllLocales()));
    }

    @Test
    public void testReadWrongDirection() {
        PlatformSetResources resources = prepareResources(
                new MockResourceFile(prepareResMap1()),
                new MockResourceFile(prepareResMap2())
        );

        ResMap expected = prepareResMap2().merge(prepareResMap1());

        assertNotEquals(expected, resources.read(getAllLocales()));
    }

    @Test
    public void testWrite() throws IOException {
        MockResourceFile file1 = new MockResourceFile(prepareResMap1());
        MockResourceFile file2 = new MockResourceFile(prepareResMap1());

        PlatformSetResources resources = prepareResources(file1, file2);

        ResMap expected = prepareResMap2();

        resources.write(expected, null);

        assertEquals(expected, file1.mMap);
        assertEquals(expected, file2.mMap);
    }

    private PlatformSetResources prepareResources(final ResourceFile res1, final ResourceFile res2) {
        Set<PlatformResources> set = new LinkedHashSet<>(2);

        set.add(new MockPlatformResources(resFolder, "resource1") {
            @Override
            protected ResourceFile[] getResourceFiles(Set<String> locales) {
                return new ResourceFile[] {res1};
            }
        });

        set.add(new MockPlatformResources(resFolder, "resource2") {
            @Override
            protected ResourceFile[] getResourceFiles(Set<String> locales) {
                return new ResourceFile[] {res2};
            }
        });

        return new PlatformSetResources(set);
    }

    private Set<String> getAllLocales() {
        return new HashSet<>(Arrays.asList("locale1", "locale2", "locale3", "locale4"));
    }

    private ResMap prepareResMap1() {
        ResMap resMap = new ResMap();
        resMap.put("locale1", prepareResLocale1());
        resMap.put("locale2", prepareResLocale2());
        resMap.put("locale3", prepareResLocale3());
        return resMap;
    }

    private ResMap prepareResMap2() {
        ResMap resMap = new ResMap();
        resMap.put("locale1", prepareResLocale2());
        resMap.put("locale2", prepareResLocale3());
        resMap.put("locale4", prepareResLocale1());
        return resMap;
    }

    private ResLocale prepareResLocale1() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER),
                new ResValue("value2_1", null, Quantity.MANY),
                new ResValue("value2_1", null, Quantity.FEW)
        }));
        resLocale.put(prepareResItem("key2", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER)
        }));
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER),
                new ResValue("value2_1", null, Quantity.MANY),
                new ResValue("value3_1", "Comment", Quantity.ZERO)
        }));
        return resLocale;
    }

    private ResLocale prepareResLocale2() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER),
                new ResValue("value2_2", null, Quantity.MANY),
                new ResValue("value3_2", "Comment", Quantity.TWO)
        }));
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER)
        }));
        resLocale.put(prepareResItem("key4", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER),
                new ResValue("value2_2", null, Quantity.MANY),
                new ResValue("value3_2", "Comment", Quantity.ZERO)
        }));
        return resLocale;
    }

    private ResLocale prepareResLocale3() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_3", null, Quantity.OTHER),
                new ResValue("value2_3", null, Quantity.FEW),
                new ResValue("value3_3", "Comment", Quantity.TWO),
                new ResValue("value4_3", "Comment", Quantity.ZERO)
        }));
        resLocale.put(prepareResItem("key4", new ResValue[]{
                new ResValue("value1_3", null, Quantity.OTHER),
                new ResValue("value3_3", "Comment", Quantity.ZERO)
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
