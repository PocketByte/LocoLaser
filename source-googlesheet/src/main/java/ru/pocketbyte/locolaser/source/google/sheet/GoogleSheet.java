/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.source.google.sheet;

import com.google.gdata.data.Link;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.util.ServiceException;
import ru.pocketbyte.locolaser.config.source.BaseTableSource;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.utils.LogUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @author Denis Shurygin
 */
public class GoogleSheet extends BaseTableSource {

    public static final String IGNORE_INDEX = "-";

    private final GoogleSheetConfig mConfig;
    private final WorksheetFacade mWorksheetFacade;

    private List<Integer> mIgnoreRows;
    private ColumnIndexes mColumnIndexes;

    private int mTitleRow = -1;
    private int mRowsCount = 0;

    private CellFeed mQuery;

    public GoogleSheet(GoogleSheetConfig config, WorksheetFacade worksheetFacade) {
        super(config);
        mConfig = config;
        mWorksheetFacade = worksheetFacade;
    }

    @Override
    public long getModifiedDate() {
        return mWorksheetFacade.getSheetEntry().getUpdated().getValue();
    }

    @Override
    public void write(ResMap resMap) {
        if (fetchCellsIfNeeded()) {
            int totalRows = mRowsCount;

            HashMap<String, NewRowItem> newRowIds = new HashMap<>();

            CellFeed batchRequest = new CellFeed();

            for (Map.Entry<String, ResLocale> localeItemEntry : resMap.entrySet()) {
                int localeColumn = mColumnIndexes.locales.get(localeItemEntry.getKey());

                if (localeColumn >= 0) {

                    for (Iterator<Map.Entry<String, ResItem>> iterator
                         = localeItemEntry.getValue().entrySet().iterator(); iterator.hasNext(); ) {
                        Map.Entry<String, ResItem> entry = iterator.next();
                        ResItem resItem = entry.getValue();


                        for (ResValue resValue : resItem.values) {
                            // =====================================
                            // Prepare batch for found missed resMap
                            if (resValue.getLocation() instanceof CellLocation) {
                                int resRow = ((CellLocation) resValue.getLocation()).row;
                                CellEntry batchEntry = new CellEntry(getCell(localeColumn, resRow));
                                batchEntry.changeInputValueLocal(valueToSourceValue(resValue.value));
                                BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
                                batchRequest.getEntries().add(batchEntry);

                                if (resValue.comment != null && mColumnIndexes.comment >= 0) {
                                    batchEntry = new CellEntry(getCell(mColumnIndexes.comment, resRow));
                                    batchEntry.changeInputValueLocal(valueToSourceValue(resValue.comment));
                                    BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
                                    batchRequest.getEntries().add(batchEntry);
                                }

                                resItem.removeValue(resValue);
                                if (resItem.values.size() == 0)
                                    iterator.remove();
                            } else {
                                // =====================================
                                // Reserve rows for new keys
                                int row;
                                String mapKey = resItem.key + ":" + resValue.quantity.toString();
                                NewRowItem newRowItem = newRowIds.get(mapKey);
                                if (newRowItem != null)
                                    row = newRowItem.row;
                                else {
                                    row = ++totalRows;
                                    newRowIds.put(mapKey, new NewRowItem(resItem.key, row));
                                }
                                resValue.setLocation(new CellLocation(this, localeColumn, row));
                            }
                        }
                    }
                }
            }

            try {
                Link batchLink = mQuery.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
                mWorksheetFacade.batch(new URL(batchLink.getHref()), batchRequest);

                if (totalRows > mWorksheetFacade.getRowCount()) {
                    mWorksheetFacade.setRowCount(totalRows);
                }

            } catch (IOException | ServiceException e) {
                e.printStackTrace();
                throw new RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.getId());
            }

            if (totalRows > mRowsCount) {
                CellFeed cellFeed = null;
                try {
                    cellFeed = mWorksheetFacade
                            .queryRange(mColumnIndexes.min, mRowsCount + 1, mColumnIndexes.max, totalRows, true);
                } catch (IOException | ServiceException e) {
                    e.printStackTrace();
                    throw new RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.getId());
                }

                for (NewRowItem newRowItem : newRowIds.values()) {
                    int index = entryIndex(mColumnIndexes.key, newRowItem.row, mRowsCount);
                    CellEntry batchEntry = new CellEntry(cellFeed.getEntries().get(index));
                    batchEntry.changeInputValueLocal(newRowItem.key);
                    BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
                    batchRequest.getEntries().add(batchEntry);
                }

                // Map to remember which quantity already written.
                Map<String, ArrayList<Quantity>> editedQuantities = new HashMap<>(newRowIds.size());

                for (Map.Entry<String, ResLocale> localeItemEntry : resMap.entrySet()) {
                    int localeColumn =  mColumnIndexes.locales.get(localeItemEntry.getKey());

                    if (localeColumn >= 0) {
                        for (Map.Entry<String, ResItem> entry : localeItemEntry.getValue().entrySet()) {
                            ResItem resItem = entry.getValue();

                            ArrayList<Quantity> editedQuantitiesForKey = editedQuantities.get(resItem.key);
                            if (editedQuantitiesForKey == null) {
                                editedQuantitiesForKey = new ArrayList<>(resItem.values.size());
                                editedQuantities.put(resItem.key, editedQuantitiesForKey);
                            }

                            for (ResValue resValue : resItem.values) {
                                if (resValue.getLocation() instanceof CellLocation) {
                                    int resRow = ((CellLocation) resValue.getLocation()).row;
                                    int index = entryIndex(localeColumn, resRow, mRowsCount);
                                    CellEntry batchEntry = new CellEntry(cellFeed.getEntries().get(index));
                                    batchEntry.changeInputValueLocal(valueToSourceValue(resValue.value));
                                    BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
                                    batchRequest.getEntries().add(batchEntry);

                                    if (resValue.comment != null && mColumnIndexes.comment >= 0) {
                                        index = entryIndex(mColumnIndexes.comment, resRow, mRowsCount);
                                        batchEntry = new CellEntry(cellFeed.getEntries().get(index));
                                        batchEntry.changeInputValueLocal(valueToSourceValue(resValue.comment));
                                        BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
                                        batchRequest.getEntries().add(batchEntry);
                                    }

                                    // Build request only if quantity doesn't edited before
                                    if (mColumnIndexes.quantity >= 0 && !editedQuantitiesForKey
                                            .contains(resValue.quantity)) {
                                        index = entryIndex(mColumnIndexes.quantity, resRow, mRowsCount);
                                        batchEntry = new CellEntry(cellFeed.getEntries().get(index));
                                        if (resItem.isHasQuantities())
                                            batchEntry.changeInputValueLocal(resValue.quantity.toString());
                                        else
                                            batchEntry.changeInputValueLocal("");
                                        BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE);
                                        batchRequest.getEntries().add(batchEntry);

                                        editedQuantitiesForKey.add(resValue.quantity);
                                    }
                                }
                                else {
                                    //TODO warn
                                }
                            }
                        }
                    }
                }

                try {
                    Link batchLink = cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
                    mWorksheetFacade.batch(new URL(batchLink.getHref()), batchRequest);
                } catch (IOException | ServiceException e) {
                    e.printStackTrace();
                    throw new RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.getId());
                }
            }
        }
    }

    @Override
    public void close() {
        mQuery = null;
        mColumnIndexes = null;
    }

    @Override
    public int getFirstRow() {
        fetchCellsIfNeeded();
        return mTitleRow + 1;
    }

    @Override
    public ColumnIndexes getColumnIndexes() {
        fetchCellsIfNeeded();
        return mColumnIndexes;
    }

    @Override
    public String getValue(int col, int row) {
        fetchCellsIfNeeded();
        if (mIgnoreRows == null || mIgnoreRows.contains(row))
            return null;

        CellEntry cell = getCell(col, row);
        if (cell != null)
            return cell.getCell().getValue();
        return null;
    }

    @Override
    public int getRowsCount() {
        fetchCellsIfNeeded();
        return mRowsCount;
    }

    @Override
    public String valueToSourceValue(String value) {
        if (value == null)
            return null;

        // Add "'" if string beginning from "'", "+" or "="
        return value.replaceAll("^(['+=])", "'$1");
    }

    @Override
    public String sourceValueToValue(String sourceValue) {
        if (sourceValue == null)
            return null;

        // Remove "'" from the beginning
        return sourceValue.replaceAll("^(')", "");
    }

    private boolean fetchCellsIfNeeded() {
        if (mQuery == null) {
            // Ignore rows
            List<Integer> ignoreRows = new ArrayList<>();
            CellFeed indexColumnFeed;
            try {
                indexColumnFeed = mWorksheetFacade.queryRange(1, -1, 1, -1, false);
            } catch (IOException | ServiceException e) {
                e.printStackTrace();
                return false;
            }

            if (indexColumnFeed != null) {
                for (CellEntry cell : indexColumnFeed.getEntries()) {
                    if (IGNORE_INDEX.equals(cell.getCell().getValue()))
                        ignoreRows.add(cell.getCell().getRow());
                }
            }
            mIgnoreRows = ignoreRows;

            // Title row
            int titleRow = 1;
            while (mIgnoreRows.contains(titleRow))
                titleRow++;
            mTitleRow = titleRow;
            LogUtils.info("Title row: " + mTitleRow);

            int key = 0;
            int quantity = -1;
            int comment = -1;
            Map<String, Integer> locales = new HashMap<>();

            CellFeed titleRowFeed;
            try {
                titleRowFeed = mWorksheetFacade.queryRange(-1, mTitleRow, -1, mTitleRow, false);
            } catch (IOException | ServiceException e) {
                e.printStackTrace();
                return false;
            }

            CellEntry cellEntry = findCellByValue(titleRowFeed, getSourceConfig().getKeyColumn());
            if (cellEntry == null) {
                LogUtils.warn("Column " + getSourceConfig().getKeyColumn() + " not found.");
                return false;
            }
            key = cellEntry.getCell().getCol();

            cellEntry = findCellByValue(titleRowFeed, getSourceConfig().getQuantityColumn());
            if (cellEntry != null)
                quantity = cellEntry.getCell().getCol();

            cellEntry = findCellByValue(titleRowFeed, getSourceConfig().getCommentColumn());
            if (cellEntry != null)
                comment = cellEntry.getCell().getCol();

            for (String locale :getSourceConfig().getLocaleColumns()) {
                cellEntry = findCellByValue(titleRowFeed, locale);
                if (cellEntry == null) {
                    LogUtils.warn("Column " + locale +
                            " not found. Resource files will not be created/changed for this locale.");
                    locales.put(locale, -1);
                } else
                    locales.put(locale, cellEntry.getCell().getCol());
            }
            mColumnIndexes = new ColumnIndexes(key, quantity, comment, locales);

            try {
                List<CellEntry> list = mWorksheetFacade
                        .queryRange(mColumnIndexes.key, mTitleRow + 1, mColumnIndexes.key, -1, false).getEntries();
                if (list.size() > 0)
                    mRowsCount = list.get(list.size() - 1).getCell().getRow();
                else
                    mRowsCount = mTitleRow;
            } catch (IOException | ServiceException e) {
                e.printStackTrace();
                return false;
            }

            try {
                mQuery = mWorksheetFacade
                        .queryRange(mColumnIndexes.min, mTitleRow + 1, mColumnIndexes.max, mRowsCount, true);
            } catch (IOException | ServiceException e) {
                e.printStackTrace();
            }
        }
        return mQuery != null;
    }

    private CellEntry getCell(int col, int row) {
        if (fetchCellsIfNeeded()) {
            int index = entryIndex(col, row, mTitleRow);
            if (index < mQuery.getEntries().size())
                return mQuery.getEntries().get(index);
        }
        return null;
    }

    private static CellEntry findCellByValue(CellFeed cellFeed, String value) {
        if (value != null) for (CellEntry cell : cellFeed.getEntries()) {
            if (value.equals(cell.getCell().getValue()))
                return cell;
        }
        return null;
    }

    private int entryIndex(int col, int row, int top) {
        return (row - top - 1) * (mColumnIndexes.max - mColumnIndexes.min + 1) + col - mColumnIndexes.min;
    }

    private static class NewRowItem {

        final String key;
        final int row;

        private NewRowItem(String key, int row) {
            this.key = key;
            this.row = row;
        }
    }
}
