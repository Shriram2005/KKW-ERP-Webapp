package com.kkwieer.erpwebapp.update

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object UpdateChecker {

    private const val TAG = "UpdateChecker"
    private const val GITHUB_OWNER = "Shriram2005"
    private const val GITHUB_REPO = "KKW-ERP-Webapp"
    private const val GITHUB_API_URL =
        "https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/releases/latest"

    data class UpdateInfo(
        val latestVersion: String,
        val releaseUrl: String,
        val releaseNotes: String,
        val isUpdateAvailable: Boolean
    )

    suspend fun checkForUpdate(currentVersion: String): UpdateInfo? = withContext(Dispatchers.IO) {
        var connection: HttpURLConnection? = null
        try {
            connection = (URL(GITHUB_API_URL).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Accept", "application/vnd.github.v3+json")
                setRequestProperty("User-Agent", "KKW-ERP-App")
                connectTimeout = 15000
                readTimeout = 15000
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)

                val latestVersion = json.getString("tag_name").removePrefix("v").trim()
                val releaseUrl = json.getString("html_url")
                val releaseNotes = json.optString("body", "")
                val isUpdateAvailable = isNewerVersion(latestVersion, currentVersion)

                Log.d(
                    TAG,
                    "Current: $currentVersion, Latest: $latestVersion, Update: $isUpdateAvailable"
                )

                UpdateInfo(latestVersion, releaseUrl, releaseNotes, isUpdateAvailable)
            } else {
                Log.e(TAG, "GitHub API error: ${connection.responseCode}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Update check failed", e)
            null
        } finally {
            connection?.disconnect()
        }
    }

    private fun isNewerVersion(latestVersion: String, currentVersion: String): Boolean {
        return try {
            val latest = latestVersion.split("-")[0].split(".").map { it.toIntOrNull() ?: 0 }
            val current = currentVersion.split("-")[0].split(".").map { it.toIntOrNull() ?: 0 }
            val maxLength = maxOf(latest.size, current.size)

            for (i in 0 until maxLength) {
                val l = latest.getOrElse(i) { 0 }
                val c = current.getOrElse(i) { 0 }
                if (l > c) return true
                if (l < c) return false
            }
            false
        } catch (e: Exception) {
            Log.e(TAG, "Version comparison failed", e)
            false
        }
    }
}
