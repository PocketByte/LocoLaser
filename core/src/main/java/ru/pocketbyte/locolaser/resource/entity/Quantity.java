/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity;

import ru.pocketbyte.locolaser.utils.LogUtils;
import ru.pocketbyte.locolaser.utils.TextUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Denis Shurygin
 */
public enum Quantity {

    ZERO, ONE, TWO, FEW, MANY, OTHER;
    public static final Set<Quantity> QUANTITY_OTHER = new HashSet<>(1);

    static {
        QUANTITY_OTHER.add(OTHER);
    }

    @Override
    public String toString() {
        switch (this) {
            case ZERO: return "zero";
            case ONE: return "one";
            case TWO: return "two";
            case FEW: return "few";
            case MANY: return "many";
            default: return "other";
        }
    }

    public static Quantity fromString(String quantity, Quantity fallback) {
        if (!TextUtils.isEmpty(quantity)) {
            switch (quantity.trim()) {
                case "zero":
                    return ZERO;
                case "one":
                    return ONE;
                case "two":
                    return TWO;
                case "few":
                    return FEW;
                case "many":
                    return MANY;
                case "other":
                    return OTHER;
                default: {
                    LogUtils.err("Quantity.fromString: Unknown quantity '" + quantity + "'.");
                    return fallback;
                }
            }
        }
        else {
            return fallback;
        }
    }

    public static Quantity fromString(String quantity) {
        return fromString(quantity, OTHER);
    }
}
