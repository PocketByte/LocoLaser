/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Denis Shurygin
 */
public class ResMap extends HashMap<String, ResLocale> {

    public ResMap() {
        super();
    }

    public ResMap(Map<String, ResLocale> map) {
        super();
        if (map != null)
            for (Entry<String, ResLocale> entry: map.entrySet()) {
                put(entry.getKey(), new ResLocale(entry.getValue()));
            }
    }

    public ResMap merge(ResMap map) {
        if (map != null) {
            for (Map.Entry<String, ResLocale> itemsEntry : map.entrySet()) {
                String locale = itemsEntry.getKey();

                ResLocale destinationItems = this.get(locale);
                if (destinationItems != null)
                    destinationItems = destinationItems.merge(itemsEntry.getValue());
                else
                    destinationItems = itemsEntry.getValue();

                this.put(locale, destinationItems);
            }
        }
        return this;
    }

    public ResMap remove(ResMap map) {
        if (map != null) {
            for (Map.Entry<String, ResLocale> itemsEntry : map.entrySet()) {
                String locale = itemsEntry.getKey();
                ResLocale removeItem = itemsEntry.getValue();
                ResLocale destinationItems = this.get(locale);

                if (destinationItems != null) {
                    destinationItems.remove(removeItem);
                    if (destinationItems.size() == 0)
                        this.remove(locale);
                }
            }
        }
        return this;
    }
}
