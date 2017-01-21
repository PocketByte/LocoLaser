/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.source.google.sheet;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.IFeed;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

/**
 * @author Denis Shurygin
 */
public class WorksheetFacade {

    private SpreadsheetService mService;
    private SpreadsheetEntry mSheetEntry;
    private WorksheetEntry mWorksheet;

    public WorksheetFacade(SpreadsheetService service, SpreadsheetEntry sheetEntry, WorksheetEntry worksheetEntry) {
        mService = service;
        mSheetEntry = sheetEntry;
        mWorksheet = worksheetEntry;
    }

    public SpreadsheetService getService() {
        return mService;
    }

    public SpreadsheetEntry getSheetEntry() {
        return mSheetEntry;
    }

    public WorksheetEntry getWorksheetEntry() {
        return mWorksheet;
    }

    public int getRowCount() {
        return getWorksheetEntry().getRowCount();
    }

    public void setRowCount(int count) throws IOException, ServiceException {
        getWorksheetEntry().setRowCount(count);
        getWorksheetEntry().update();
    }

    public <F extends IFeed> F batch(URL feedUrl, F inputFeed) throws IOException, ServiceException {
        return getService().batch(feedUrl, inputFeed);
    }

    public CellFeed queryRange(int left, int top, int right, int bottom,
                               boolean returnEmpty) throws IOException, ServiceException {
        CellQuery query = new CellQuery(mWorksheet.getCellFeedUrl());

        if (top >= 0)
            query.setMinimumRow(top);

        if (bottom >= 0)
            query.setMaximumRow(bottom);

        if (left >= 0)
            query.setMinimumCol(left);

        if (right >= 0)
            query.setMaximumCol(right);

        query.setReturnEmpty(returnEmpty);

        return mService.getFeed(query.getUrl(), CellFeed.class);

    }
}
