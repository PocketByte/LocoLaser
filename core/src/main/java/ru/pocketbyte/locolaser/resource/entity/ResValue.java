/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity;

import ru.pocketbyte.locolaser.config.source.Source;

/**
 * @author Denis Shurygin
 */
public class ResValue {

    public final String value;

    /**
     * Resource comment.
     */
    public final String comment;

    public final Quantity quantity;

    private Source.ValueLocation mLocation;

    public ResValue(String value, String comment) {
        this(value, comment, null);
    }

    public ResValue(String value, String comment, Quantity quantity) {
        this.value = value;
        this.comment = comment;
        this.quantity = quantity == null ? Quantity.OTHER : quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResValue) {
            ResValue resValue = (ResValue) obj;
            return isStringEquals(value, resValue.value) &&
                    isStringEquals(comment, resValue.comment) &&
                    quantity == resValue.quantity;
        }
        return super.equals(obj);
    }

    /**
     * Sets source location where placed this resource item.
     * @param location Source location where placed this resource item.
     */
    public void setLocation(Source.ValueLocation location) {
        mLocation = location;
    }

    /**
     * Gets source location where placed this resource item.
     * @return Source location where placed this resource item.
     */
    public Source.ValueLocation getLocation() {
        return mLocation;
    }

    private boolean isStringEquals(String string1, String string2) {
        return (string1 != null && string1.equals(string2)) || (string1 == null && string2 == null);
    }
}
