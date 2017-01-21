/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.source.google.utils;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class OAuth2Helper {

    private static final String TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
    private static List<String> SCOPES = Arrays.asList(
            "https://spreadsheets.google.com/feeds",
            "https://www.googleapis.com/auth/drive.readonly");

    private static final int PORT = 8080;
    private static final String DOMAIN = "127.0.0.1";

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static DataStoreFactory DATA_STORE_FACTORY;

    public static Credential getCredential(String credentialId) throws IOException {
        String apiKey = getApiKey();
        AuthorizationCodeFlow authorizationCodeFlow
                = new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
                        HTTP_TRANSPORT, JSON_FACTORY, new GenericUrl(TOKEN_URL),
                        new ClientParametersAuthentication(apiKey, getApiSecret()), apiKey, AUTH_URL)
                .setScopes(SCOPES)
                .setDataStoreFactory(dataStoreFactory()).build();

        LocalServerReceiver localServerReceiver = new LocalServerReceiver.Builder()
                .setHost(OAuth2Helper.DOMAIN)
                .setPort(OAuth2Helper.PORT).build();
        return new AuthorizationCodeInstalledApp(authorizationCodeFlow, localServerReceiver).authorize(credentialId);
    }

    public static void deleteCredential(String credentialId) throws IOException {
        DataStore<?> dataStore = StoredCredential.getDefaultDataStore(dataStoreFactory());
        if (dataStore != null)
            dataStore.delete(credentialId);
    }

    public static Credential credentialFromFile(File serviceAccountFile) throws IOException {
        return GoogleCredential.fromStream(new FileInputStream(serviceAccountFile), HTTP_TRANSPORT, JSON_FACTORY)
                .createScoped(SCOPES);
    }

    private static DataStoreFactory dataStoreFactory() throws IOException {
        if (DATA_STORE_FACTORY == null)
            DATA_STORE_FACTORY = new FileDataStoreFactory(
                    new File(System.getProperty("user.home"), ".store/LocoLaser"));
        return DATA_STORE_FACTORY;
    }

    private static String getApiKey() throws IOException {
        return getProperty("google_oauth_api_key");
    }

    private static String getApiSecret() throws IOException {
        return getProperty("google_oauth_api_secret");
    }

    private static String getProperty(String property) throws IOException {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("properties/app.properties");
        properties.load(stream);
        return properties.getProperty(property);
    }
}
