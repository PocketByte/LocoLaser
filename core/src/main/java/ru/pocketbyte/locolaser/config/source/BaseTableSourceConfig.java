/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source;

import ru.pocketbyte.locolaser.config.Config;

import java.util.Set;

/**
 * Base implementation of SourceConfig.
 *
 * @author Denis Shurygin
 */
public abstract class BaseTableSourceConfig extends Config.Child implements SourceConfig {

    private String mColumnKey;
    private String mColumnQuantity;
    private Set<String> mColumnLocales;
    private String mColumnComment;

    /**
     * Sets title of the column that contain resource Key's.
     * @param columnTitle Title of the column that contain resource Key's.
     */
    public void setKeyColumn(String columnTitle) {
        this.mColumnKey = columnTitle;
    }

    /**
     * Sets title of the column that contain resource quantity.
     * @param columnTitle Title of the column that contain resource quantity.
     */
    public void setQuantityColumn(String columnTitle) {
        this.mColumnQuantity = columnTitle;
    }

    /**
     * Sets titles set of the columns that contain resource values.
     * @param columnTitles Titles set of the columns that contain resource values.
     */
    public void setLocaleColumns(Set<String> columnTitles) {
        this.mColumnLocales = columnTitles;
    }

    /**
     * Sets title of the column that contain comments.
     * @param columnTitle Title of the column that contain comments.
     */
    public void setCommentColumn(String columnTitle) {
        this.mColumnComment = columnTitle;
    }

    /**
     * Gets title of the column that contain resource Key's.
     * @return Title of the column that contain resource Key's.
     */
    public String getKeyColumn() {
        return mColumnKey;
    }

    /**
     * Gets title of the column that contain resource quantity.
     * @return Title of the column that contain resource quantity.
     */
    public String getQuantityColumn() {
        return mColumnQuantity;
    }

    /**
     * Gets titles list of the columns that contain resource values.
     * @return Titles list of the columns that contain resource values.
     */
    public Set<String> getLocaleColumns() {
        return mColumnLocales;
    }

    /**
     * Gets title of the column that contain comments.
     * @return Title of the column that contain comments.
     */
    public String getCommentColumn() {
        return mColumnComment;
    }

    @Override
    public Set<String> getLocales() {
        return getLocaleColumns();
    }

    @Override
    public String toString() {
        return getType();
    }
}
