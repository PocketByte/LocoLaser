/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config;

import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.config.source.SourceConfig;

import java.io.File;

/**
 * Configuration object that contain information about localization rules.
 *
 * @author Denis Shurygin
 */
public class Config {

    public static class Child {

        private Config mParent;

        public Config getParent() {
            return mParent;
        }

    }

    private File mFile;
    private SourceConfig mSourceConfig;
    private PlatformConfig mPlatform;
    private boolean isForceImport;
    private ConflictStrategy mConflictStrategy;
    private long mDelay;

    public final WritingConfig writingConfig = new WritingConfig();

    public enum ConflictStrategy {

        /** Remove local resources and replace it with resources from source. */
        REMOVE_LOCAL,

        /** Keep local resource if source doesn't contain this resource. */
        KEEP_NEW_LOCAL,

        /** Resource should be exported from local resources into source if source doesn't contain this resource. */
        EXPORT_NEW_LOCAL
    }

    // =================================================================================================================
    // ============ Constructors =======================================================================================
    // =================================================================================================================

    /**
     * Construct new configuration object.
     */
    public Config() {}

    // =================================================================================================================
    // ============ Public methods =====================================================================================
    // =================================================================================================================

    // =================================================================================================================
    // ============ Setters ============================================================================================
    // =================================================================================================================

    /**
     * Sets file from which this config was read.
     * @param file File from which this config was read.
     */
    public void setFile(File file) {
        this.mFile = file;
    }

    /**
     * Sets source that contain resources.
     * @param sourceConfig Source that contain resources.
     */
    public void setSourceConfig(SourceConfig sourceConfig) {
        if (mSourceConfig instanceof Child)
            ((Child) mSourceConfig).mParent = null;

        mSourceConfig = sourceConfig;

        if (mSourceConfig instanceof Child)
            ((Child) mSourceConfig).mParent = this;
    }

    /**
     * Sets platform that contain logic of resource creation.
     * @param platform Platform that contain logic of resource creation.
     */
    public void setPlatform(PlatformConfig platform) {
        if (mPlatform instanceof Child)
            ((Child) mPlatform).mParent = null;

        this.mPlatform = platform;

        if (mPlatform instanceof Child)
            ((Child) mPlatform).mParent = this;
    }

    /**
     * Sets if import should be forced even if this is not necessary. Default value: false.
     * @param isForceImport True if import should be forced, false otherwise.
     */
    public void setForceImport(boolean isForceImport) {
        this.isForceImport = isForceImport;
    }

    /**
     * Sets which strategy should be processed for conflicts. Default value: REMOVE_LOCAL.
     * @param strategy see {@link ru.pocketbyte.locolaser.config.Config.ConflictStrategy}
     */
    public void setConflictStrategy(ConflictStrategy strategy) {
        this.mConflictStrategy = strategy;
    }

    /**
     * Sets if comment should be written even if it equal resource value. Default value: false.
     * @param isDuplicateComments True if comment should be written even if it equal resource value, false otherwise.
     */
    public void setDuplicateComments(boolean isDuplicateComments) {
        writingConfig.setDuplicateComments(isDuplicateComments);
    }

    /**
     * Sets time in milliseconds that define delay for next localization. Localization will executed not more often the specified delay. If force import switch on delay will be ignored.
     * @param delay Time in milliseconds that define delay for next localization.
     */
    public void setDelay(long delay) {
        mDelay = delay;
    }

    // =================================================================================================================
    // ============ Getters ============================================================================================
    // =================================================================================================================

    /**
     * Gets file from which this config was read.
     * @return File from which this config was read.
     */
    public File getFile() {
        return mFile;
    }

    public File getTempDir() {
        return mPlatform.getTempDir();
    }

    /**
     * Gets source that contain resources.
     * @return Source that contain resources.
     */
    public SourceConfig getSourceConfig() {
        return mSourceConfig;
    }

    /**
     * Gets platform that contain logic of resource creation.
     * @return Platform that contain logic of resource creation.
     */
    public PlatformConfig getPlatform() {
        return mPlatform;
    }

    /**
     * Gets if import should be forced even if this is not necessary.
     * @return True if import should be forced, false otherwise.
     */
    public boolean isForceImport() {
        return isForceImport;
    }

    /**
     * Gets which strategy should be processed for conflicts.
     * @return Strategy which should be processed for conflicts.
     */
    public ConflictStrategy getConflictStrategy() {
        if (mConflictStrategy == null)
            return ConflictStrategy.REMOVE_LOCAL;
        return mConflictStrategy;
    }

    /**
     * Gets if comment should be written even if it equal resource value.
     * @return True if comment should be written even if it equal resource value, false otherwise.
     */
    public boolean isDuplicateComments() {
        return writingConfig.isDuplicateComments();
    }

    /**
     * Gets time in milliseconds that define delay for next localization. Localization will executed not more often the specified delay. If force import switch on delay will be ignored.
     * @return Time in milliseconds that define delay for next localization.
     */
    public long getDelay() {
        return mDelay;
    }
}