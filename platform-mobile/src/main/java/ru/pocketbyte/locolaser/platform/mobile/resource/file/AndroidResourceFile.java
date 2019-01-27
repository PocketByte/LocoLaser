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
import ru.pocketbyte.locolaser.platform.mobile.utils.TemplateStr;
import ru.pocketbyte.locolaser.resource.entity.*;
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * ResourceFile implementation for Android platform.
 *
 * @author Denis Shurygin
 */
public class AndroidResourceFile extends ResourceStreamFile {

    private final String mLocale;

    public AndroidResourceFile(File file, String locale) {
        super(file);
        mLocale = locale;
    }

    @Override
    public ResMap read() {
        if (mFile.exists()) {
            AndroidXmlFileParser handler = new AndroidXmlFileParser();
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
        open();
        writeStringLn(TemplateStr.XML_DECLARATION);
        writeStringLn(TemplateStr.GENERATED_XML_COMMENT);
        writeln();
        writeStringLn("<resources>");

        ResLocale items = resMap.get(mLocale);

        for (String key: items.keySet()) {
            ResItem resItem = items.get(key);
            if (resItem != null) {

                if (!resItem.isHasQuantities()) {
                    String comment = resItem.values.get(0).comment;
                    String value = resItem.values.get(0).value;
                    if (comment != null && (
                            (writingConfig == null || writingConfig.isDuplicateComments()) || !comment.equals(value))) {
                        writeString("    /* ");
                        writeString(escapeComment(comment));
                        writeStringLn(" */");
                    }

                    writeString("    <string name=\"");
                    writeString(resItem.key.trim());
                    writeString("\">");
                    writeString(toPlatformValue(value));
                    writeStringLn("</string>");
                }
                else {
                    writeString("    <plurals name=\"");
                    writeString(resItem.key.trim());
                    writeStringLn("\">");

                    for (ResValue resValue: resItem.values) {

                        writeString("        <item quantity=\"");
                        writeString(resValue.quantity.toString());
                        writeString("\">");
                        writeString(toPlatformValue(resValue.value));
                        writeStringLn("</item>");
                    }
                    writeStringLn("    </plurals>");
                }
            }
        }

        writeString("</resources>");
        close();
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

    private static String escapeComment(String string) {
        return string
                .replace("<br>", "\n")
                .replace("<", "&lt;");
    }

    static String toPlatformValue(String string) {
        string = string
                .replace("'", "\\'")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("<", "&lt;");
        if (string.startsWith("?"))
            string = "\\" + string;
        return string;
    }

    static String fromPlatformValue(String string) {
        string = string
                .replace("\\'", "'")
                .replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("&lt;", "<");

        if (string.startsWith("\\?"))
            string = string.substring(1);

        return string;
    }

    private static class AndroidXmlFileParser  extends DefaultHandler {
        private ResLocale mMap;
        private ResItem mItem;
        private boolean isPlural;
        private Quantity mQuantity;
        private StringBuilder mValue;
        private String mComment; //TODO comments not work

        @Override
        public void startDocument() throws SAXException {
            mMap = new ResLocale();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("string".equals(qName)) {
                mItem = new ResItem(attributes.getValue("name"));
                isPlural = false;
                mQuantity = null;
            }
            else if ("plurals".equals(qName)) {
                mItem = new ResItem(attributes.getValue("name"));
                isPlural = true;
                mQuantity = null;
            }
            else if (mItem != null && isPlural && "item".equals(qName)) {
                mQuantity = Quantity.fromString(attributes.getValue("quantity"));
            }
            else {
                mItem = null;
                isPlural = false;
                mQuantity = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (mItem != null && (!isPlural || mQuantity != null)) {
                if (mValue == null)
                    mValue = new StringBuilder();
                mValue.append(AndroidResourceFile.fromPlatformValue(new String(ch, start, length)));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (mItem != null && mValue != null && "string".equals(qName)) {
                mItem.addValue(new ResValue(mValue.toString(), mComment)); // TODO read plurals http://developer.android.com/intl/ru/guide/topics/resources/string-resource.html#Plurals
                mMap.put(mItem);
                mItem = null;
            }
            else if (mItem != null && mValue != null && "item".equals(qName) && isPlural) {
                mItem.addValue(new ResValue(mValue.toString(), mComment, mQuantity));
            }
            else if (mItem != null && "plurals".equals(qName) && isPlural) {
                mMap.put(mItem);
                mItem = null;
            }

            mValue = null;
            mComment = null;
            mQuantity = null;
        }

        public ResLocale getMap() {
            return mMap;
        }
    }
}
