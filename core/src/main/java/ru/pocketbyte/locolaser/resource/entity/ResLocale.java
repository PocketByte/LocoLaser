/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resources map for single locale.
 * Key is the key of resource, value is the resource item.
 *
 * @author Denis Shurygin
 */
public class ResLocale extends LinkedHashMap<String, ResItem> {

    public ResLocale() {
        super();
    }

    public ResLocale(Map<String, ResItem> map) {
        super();
        if (map != null)
            for (ResItem item: map.values()) {
                put(new ResItem(item));
            }
    }

    @Override
    public ResItem put(String key, ResItem value) {
        throw new UnsupportedOperationException("Please, use put(ResItem value).");
    }

    @Override
    public void putAll(Map<? extends String, ? extends ResItem> m) {
        throw new UnsupportedOperationException("Please, use put(ResItem value).");
    }

    @Override
    public ResItem putIfAbsent(String key, ResItem value) {
        throw new UnsupportedOperationException("Please, use put(ResItem value).");
    }

    public ResItem put(ResItem value) {
        return super.put(value.key, value);
    }

    public ResLocale merge(ResLocale resLocale) {
        if (resLocale != null) {
            for (Map.Entry<String, ResItem> itemsEntry : resLocale.entrySet()) {
                ResItem destinationItem = this.get(itemsEntry.getKey());
                if (destinationItem != null)
                    this.put(destinationItem.merge(itemsEntry.getValue()));
                else
                    this.put(itemsEntry.getValue());
            }
        }
        return this;
    }

    public ResLocale remove(ResLocale mapForRemove) {
        if (mapForRemove != null) {
            for(Map.Entry<String, ResItem> entry :mapForRemove.entrySet()) {
                ResItem resItem = this.get(entry.getKey());
                if (resItem != null) {
                    for (ResValue removeValue: entry.getValue().values) {
                        resItem.removeValueForQuantity(removeValue.quantity);
                    }
                    if (resItem.values.size() == 0)
                        this.remove(resItem.key);
                }
            }
        }
        return this;
    }
}