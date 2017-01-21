/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity;

import ru.pocketbyte.locolaser.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Single resource item that contain key, value and comment.
 *
 * @author Denis Shurygin
 */
public class ResItem {

    /**
     * Resource key.
     */
    public final String key;

    
    private final List<ResValue> mutableValues = new ArrayList<>(1);

    /**
     * Resource values.
     */
    public final List<ResValue> values = Collections.unmodifiableList(mutableValues);


    public ResItem(ResItem item) {
        this(item.key);
        mutableValues.addAll(item.mutableValues);
    }

    public ResItem(String key) {
        if (key == null)
            throw new IllegalArgumentException("Key must be not null.");

        this.key = key;
    }

    //TODO docs
    public boolean addValue(ResValue value) {
        boolean isHasNoError = true;

        ResValue oldValue = valueForQuantity(value.quantity);
        if (oldValue != null) {
            isHasNoError = false;
            LogUtils.err("Duplicate quantity! Key= " + this.key + " Quantity=" + value.quantity.toString());
            mutableValues.remove(oldValue);
        }
        mutableValues.add(value);
        return isHasNoError;
    }

    //TODO docs
    public boolean removeValue(ResValue value) {
        return mutableValues.remove(value);
    }

    //TODO docs
    public ResValue removeValueForQuantity(Quantity quantity) {
        for (int i = 0; i < mutableValues.size(); i++) {
            ResValue resValue = mutableValues.get(i);
            if (resValue.quantity.equals(quantity)) {
                return mutableValues.remove(i);
            }
        }
        return null;
    }

    //TODO docs
    public ResValue valueForQuantity(Quantity quantity) {
        for (ResValue resValue: mutableValues) {
            if (resValue.quantity.equals(quantity)) {
                return resValue;
            }
        }
        return null;
    }

    //TODO docs
    public boolean isHasQuantities() {
        return mutableValues.size() > 1 || (mutableValues.size() == 1 && mutableValues.get(0).quantity != Quantity.OTHER);
    }

    //TODO docs
    public ResItem merge(ResItem item) {
        if (item != null) {
            for (ResValue value2 : item.mutableValues) {

                ResValue value1 = this.valueForQuantity(value2.quantity);
                if (value1 != null) {
                    this.removeValue(value1);
                }
                this.addValue(value2);
            }
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResItem) {
            ResItem item = (ResItem) obj;
            if (item.key.equals(key) && item.mutableValues.size() == mutableValues.size()) {
                for (ResValue resValue: item.values) {
                    if(!resValue.equals(valueForQuantity(resValue.quantity)))
                        return false;
                }
                return true;
            }
        }
        return super.equals(obj);
    }
}
