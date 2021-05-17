/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.google.utils

import com.google.api.client.auth.oauth2.*
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.DataStoreFactory
import com.google.api.client.util.store.FileDataStoreFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

object OAuth2Helper {

    private const val TOKEN_URL = "https://accounts.google.com/o/oauth2/token"
    private const val AUTH_URL = "https://accounts.google.com/o/oauth2/auth"
    private val SCOPES = listOf(
            "https://spreadsheets.google.com/feeds",
            "https://www.googleapis.com/auth/drive.readonly"
    )

    private const val DOMAIN = "127.0.0.1"

    private val HTTP_TRANSPORT = NetHttpTransport()
    private val JSON_FACTORY = JacksonFactory()
    private var DATA_STORE_FACTORY: DataStoreFactory? = null

    private val apiKey: String
        @Throws(IOException::class)
        get() = getProperty("google_oauth_api_key")

    private val apiSecret: String
        @Throws(IOException::class)
        get() = getProperty("google_oauth_api_secret")

    @Throws(IOException::class)
    fun getCredential(credentialId: String): Credential {
        val apiKey = apiKey
        val authorizationCodeFlow = AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT, JSON_FACTORY, GenericUrl(TOKEN_URL),
                ClientParametersAuthentication(apiKey, apiSecret), apiKey, AUTH_URL)
                .setScopes(SCOPES)
                .setDataStoreFactory(dataStoreFactory()).build()

        val localServerReceiver = LocalServerReceiver.Builder()
                .setHost(DOMAIN).build()
        return AuthorizationCodeInstalledApp(authorizationCodeFlow, localServerReceiver).authorize(credentialId)
    }

    @Throws(IOException::class)
    fun deleteCredential(credentialId: String) {
        val dataStore = StoredCredential.getDefaultDataStore(dataStoreFactory())
        dataStore?.delete(credentialId)
    }

    @Throws(IOException::class)
    fun credentialFromFile(serviceAccountFile: File): Credential {
        return GoogleCredential.fromStream(FileInputStream(serviceAccountFile), HTTP_TRANSPORT, JSON_FACTORY)
                .createScoped(SCOPES)
    }

    @Throws(IOException::class)
    private fun dataStoreFactory(): DataStoreFactory {
        if (DATA_STORE_FACTORY == null)
            DATA_STORE_FACTORY = FileDataStoreFactory(
                    File(System.getProperty("user.home"), ".store/LocoLaser"))
        return DATA_STORE_FACTORY!!
    }

    @Throws(IOException::class)
    private fun getProperty(property: String): String {
        val properties = Properties()
        val loader = Thread.currentThread().contextClassLoader
        val stream = loader.getResourceAsStream("properties/app.properties")
        properties.load(stream)
        return properties.getProperty(property)
    }
}
