/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.source.google.sheet;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ResourceNotFoundException;
import com.google.gdata.util.ServiceException;
import ru.pocketbyte.locolaser.config.source.BaseTableSourceConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.source.google.utils.OAuth2Helper;
import ru.pocketbyte.locolaser.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author Denis Shurygin
 */
public class GoogleSheetConfig extends BaseTableSourceConfig {
    public static final String TYPE = "googlesheet";

    private String mId;
    private String mWorksheetTitle;
    private File mCredentialFile;

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public GoogleSheet open() {
        SpreadsheetService service = prepareService(getCredentialFile());
        if (service == null)
            return null;

        SpreadsheetEntry sheetEntry = prepareSheetEntry(service);
        if (sheetEntry == null)
            return null;

        LogUtils.info("Open sheet: " + sheetEntry.getTitle().getPlainText());


        WorksheetEntry worksheet = prepareWorksheet(sheetEntry, getWorksheetTitle());
        if (worksheet == null)
            return null;

        WorksheetFacade holder = new WorksheetFacade(service, sheetEntry, worksheet);

        return new GoogleSheet(this, holder);
    }

    public void setId(String id) {
        mId = id;
    }

    /**
     * Sets title of the Worksheet that should be used for localization.
     * @param title Title of the Worksheet that should be used for localization.
     */
    public void setWorksheetTitle(String title) {
        mWorksheetTitle = title;
    }

    public String getId() {
        return mId;
    }

    /**
     * Gets title of the Worksheet that should be used for localization.
     * @return Title of the Worksheet that should be used for localization.
     */
    public String getWorksheetTitle() {
        return mWorksheetTitle;
    }

    public void setCredentialFile(File file) throws InvalidConfigException {
        mCredentialFile = file;
    }

    public File getCredentialFile() {
        return mCredentialFile;
    }

    /**
     * Gets url for the sheet.
     * @return Url for the sheet.
     */
    public URL getUrl() throws MalformedURLException {
        return new URL("https://spreadsheets.google.com/feeds/spreadsheets/" + mId);
    }

    private SpreadsheetService prepareService(File credentialFile) {
        SpreadsheetService service = new SpreadsheetService("spreadsheetservice");

        if (credentialFile != null)
            try {
                service.setOAuth2Credentials(OAuth2Helper.credentialFromFile(credentialFile));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        else
            try {
                service.setOAuth2Credentials(OAuth2Helper.getCredential(getId()));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        return service;
    }

    private SpreadsheetEntry prepareSheetEntry(SpreadsheetService service) {
        SpreadsheetEntry entry;
        try {
            entry = service.getEntry(getUrl(), SpreadsheetEntry.class);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            try {
                OAuth2Helper.deleteCredential(getId());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        } catch (ServiceException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return entry;
    }

    private WorksheetEntry prepareWorksheet(SpreadsheetEntry sheetEntry, String title) {
        List<WorksheetEntry> worksheets ;
        try {
            worksheets = sheetEntry.getWorksheets();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ServiceException e) {
            e.printStackTrace();
            return null;
        }
        LogUtils.info(Integer.toString(worksheets.size()) + " worksheets found.");
        if (worksheets.size() == 0) {
            LogUtils.err("ERROR: Nothing to parse!");
        } else {
            WorksheetEntry worksheet = null;
            if (title != null) {
                LogUtils.info("Searching worksheet with title: " + title);
                for (WorksheetEntry entry : worksheets) {
                    if (title.equals(entry.getTitle().getPlainText())) {
                        worksheet = entry;
                        LogUtils.info("Worksheet found.");
                        break;
                    }
                }
                if (worksheet == null)
                    LogUtils.info("Nothing found!");
            }
            if (worksheet == null) {
                worksheet = worksheets.get(0);
                LogUtils.info("Use default worksheet with index 0. Worksheet title: " +
                        worksheet.getTitle().getPlainText());
            }
            return worksheet;
        }
        return null;
    }
}
