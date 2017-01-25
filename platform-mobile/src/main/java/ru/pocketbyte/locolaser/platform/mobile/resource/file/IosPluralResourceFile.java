/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import ru.pocketbyte.locolaser.config.WritingConfig;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * @author Denis Shurygin
 */
public class IosPluralResourceFile  extends ResourceStreamFile {

    private static final String[] FORMAT_VARIANTS = new String[]{
            "%d", "%D", "%u", "%U",
            "%x", "%X", "%o", "%O",
            "%f", "%F", "%e", "%E",
            "%g", "%G", "%a", "%A"
    };

    private final String mLocale;

    public IosPluralResourceFile(File file, String locale) {
        super(file);
        mLocale = locale;
    }

    @Override
    public ResMap read() {
        if (mFile.exists()) {
            IosXmlFileParser handler = new IosXmlFileParser();
            SAXParserFactory spf = SAXParserFactory.newInstance();
            try {
                SAXParser saxParser = spf.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();
                xmlReader.setContentHandler(handler);
                xmlReader.parse(convertToFileURL(mFile));

            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }

            ResMap resMap = new ResMap();
            resMap.put(mLocale, handler.getMap());
            return resMap;
        }
        return null;
    }

    @Override
    public void write(ResMap resMap, WritingConfig writingConfig) throws IOException {
        ResLocale items = resMap.get(mLocale);

        for (String key: items.keySet()) {
            ResItem resItem = items.get(key);
            if (resItem != null) {
                if (resItem.isHasQuantities()) {
                    if (!isOpen()) {
                        // Open and write header
                        open();
                        writeStringLn("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                        writeStringLn("<plist version=\"1.0\">");
                        writeStringLn("<dict>");
                    }

                    writeString("    <key>");
                    writeString(resItem.key.trim());
                    writeStringLn("</key>");

                    writeStringLn("    <dict>");
                    writeStringLn("        <key>NSStringLocalizedFormatKey</key>");
                    writeStringLn("        <string>%#@value@</string>");
                    writeStringLn("        <key>value</key>");
                    writeStringLn("        <dict>");
                    writeStringLn("            <key>NSStringFormatSpecTypeKey</key>");
                    writeStringLn("            <string>NSStringPluralRuleType</string>");
                    writeStringLn("            <key>NSStringFormatValueTypeKey</key>");

                    // Searching format
                    String format = "f";
                    for (String formatString: FORMAT_VARIANTS) {
                        if (resItem.valueForQuantity(Quantity.OTHER).value.contains(formatString)) {
                            format = formatString.substring(1);
                            break;
                        }
                    }
                    writeString("            <string>");
                    writeString(format);
                    writeStringLn("</string>");

                    for (ResValue resValue: resItem.values) {

                        writeString("            <key>");
                        writeString(resValue.quantity.toString());
                        writeStringLn("</key>");
                        writeString("            <string>");
                        writeString(toPlatformValue(resValue.value));
                        writeStringLn("</string>");
                    }
                    writeStringLn("        </dict>");
                    writeStringLn("    </dict>");
                }
            }
        }

        if (isOpen()) {
            // Close and write footer
            writeStringLn("</dict>");
            writeString("</plist>");
        }
    }

    static String toPlatformValue(String string) {
        string = string
                .replace("'", "\\'")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("<", "&lt;");
        return string;
    }

    static String fromPlatformValue(String string) {
        string = string
                .replace("\\'", "'")
                .replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("&lt;", "<");

        return string;
    }

    private static String convertToFileURL(File file) {
        String path = file.getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }

    private static class IosXmlFileParser extends DefaultHandler {

        private static final int LEVEL_NONE = 0;
        private static final int LEVEL_DOC = 1;
        private static final int LEVEL_ITEM_KEY = 2;
        private static final int LEVEL_ITEM_DICT = 3;
        private static final int LEVEL_ITEM_VALUE_DICT = 4;
        private static final int LEVEL_ITEM_VALUE_KEY = 5;
        private static final int LEVEL_ITEM_VALUE_STRING = 6;

        private ResLocale mMap;
        private ResItem mItem;

        private StringBuilder mValue;

        private int mDocLevel = LEVEL_NONE;

        private Quantity mQuantity;
        private String mComment; //TODO comments not work

        @Override
        public void startDocument() throws SAXException {
            mMap = new ResLocale();
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            switch (mDocLevel) {
                case LEVEL_NONE:
                    if ("dict".equals(qName)) {
                        mDocLevel = LEVEL_DOC;
                    }
                    break;
                case LEVEL_DOC:
                    if ("key".equals(qName)) {
                        mDocLevel = LEVEL_ITEM_KEY;
                        mValue = new StringBuilder();
                    }
                    else if ("dict".equals(qName)) {
                        mDocLevel = LEVEL_ITEM_DICT;
                    }
                    break;
                case LEVEL_ITEM_DICT:
                    if ("dict".equals(qName)) {
                        mDocLevel = LEVEL_ITEM_VALUE_DICT;
                    }
                    break;
                case LEVEL_ITEM_VALUE_DICT:
                    if ("key".equals(qName)) {
                        mDocLevel = LEVEL_ITEM_VALUE_KEY;
                        mQuantity = null;
                        mValue = new StringBuilder();
                    }
                    else if ("string".equals(qName)) {
                        mDocLevel = LEVEL_ITEM_VALUE_STRING;
                        if (mQuantity != null) {
                            mValue = new StringBuilder();
                        }
                    }
                    break;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            switch (mDocLevel) {
                case LEVEL_ITEM_KEY:
                    mValue.append(new String(ch, start, length));
                    break;
                case LEVEL_ITEM_VALUE_KEY:
                    mValue.append(new String(ch, start, length));
                    break;
                case LEVEL_ITEM_VALUE_STRING:
                    if (mQuantity != null) {
                        mValue.append(new String(ch, start, length));
                    }
                    break;

            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch (mDocLevel) {
                case LEVEL_ITEM_KEY:
                    mItem = new ResItem(mValue.toString());
                    mDocLevel = LEVEL_DOC;
                    break;
                case LEVEL_ITEM_VALUE_KEY:
                    String quantityString = mValue.toString();
                    if ("NSStringFormatSpecTypeKey".equals(quantityString)
                            || "NSStringFormatValueTypeKey".equals(quantityString)) {
                        mQuantity = null;
                    }
                    else {
                        mQuantity = Quantity.fromString(quantityString, null);
                    }
                    mDocLevel = LEVEL_ITEM_VALUE_DICT;
                    break;
                case LEVEL_ITEM_VALUE_STRING:
                    if (mQuantity != null && mItem != null) {
                        mItem.addValue(new ResValue(fromPlatformValue(mValue.toString()), mComment, mQuantity));
                    }
                    mQuantity = null;
                    mDocLevel = LEVEL_ITEM_VALUE_DICT;
                    break;
                case LEVEL_ITEM_VALUE_DICT:
                    mDocLevel = LEVEL_ITEM_DICT;
                    break;
                case LEVEL_ITEM_DICT:
                    if ("dict".equals(qName)) {
                        if (mItem != null) {
                            mMap.put(mItem);
                        }
                        mItem = null;
                        mDocLevel = LEVEL_DOC;
                    }
                    break;
            }
            mValue = null;
            mComment = null;
        }

        public ResLocale getMap() {
            return mMap;
        }
    }
}