/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity;

import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Denis Shurygin
 */
@RunWith(JUnit4.class)
public class QuantityTest {

    private List<Pair<String, Quantity>> quantityPairs;

    @Before
    public void init() {
        quantityPairs = Arrays.asList(
                new Pair<>("zero", Quantity.ZERO),
                new Pair<>("one", Quantity.ONE),
                new Pair<>("two", Quantity.TWO),
                new Pair<>("few", Quantity.FEW),
                new Pair<>("many", Quantity.MANY),
                new Pair<>("other", Quantity.OTHER)
        );
    }

    @Test
    public void testToString() throws Exception {
        for (Pair<String, Quantity> pair: quantityPairs) {
            assertEquals(pair.getKey(), pair.getValue().toString());
        }
    }

    @Test
    public void testFromString() throws Exception {
        for (Pair<String, Quantity> pair: quantityPairs) {
            assertEquals(pair.getValue(), Quantity.fromString(pair.getKey()));
        }
    }

    @Test
    public void testFromStringFallback() throws Exception {
        String invalidQuantity = "invalid_quantity";
        assertEquals(Quantity.OTHER, Quantity.fromString(invalidQuantity));
        assertEquals(Quantity.OTHER, Quantity.fromString(null));
        assertEquals(Quantity.OTHER, Quantity.fromString(invalidQuantity, Quantity.OTHER));
        assertEquals(Quantity.MANY, Quantity.fromString(invalidQuantity, Quantity.MANY));
    }

    @Test
    public void testQuantityOther() throws Exception {
        assertEquals(1, Quantity.QUANTITY_OTHER.size());
        assert  Quantity.QUANTITY_OTHER.contains(Quantity.OTHER);
    }
}
