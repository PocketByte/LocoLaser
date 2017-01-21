/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config;

/**
 * @author Denis Shurygin
 */
public class WritingConfig {

    private boolean isDuplicateComments;

    /**
     * Sets if comment should be written even if it equal resource value. Default value: false.
     * @param isDuplicateComments True if comment should be written even if it equal resource value, false otherwise.
     */
    public void setDuplicateComments(boolean isDuplicateComments) {
        this.isDuplicateComments = isDuplicateComments;
    }

    /**
     * Gets if comment should be written even if it equal resource value.
     * @return True if comment should be written even if it equal resource value, false otherwise.
     */
    public boolean isDuplicateComments() {
        return isDuplicateComments;
    }
}
