package com.kkwieer.erpwebapp.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class LinkRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "portal_links_prefs"
        private const val KEY_LINKS = "portal_links"
        private const val KEY_INITIALIZED = "initialized"

        @Volatile
        private var INSTANCE: LinkRepository? = null

        fun getInstance(context: Context): LinkRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LinkRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    init {
        if (!prefs.getBoolean(KEY_INITIALIZED, false)) {
            saveLinks(PortalLink.DEFAULT_LINKS)
            prefs.edit().putBoolean(KEY_INITIALIZED, true).apply()
        }
    }

    fun getLinks(): List<PortalLink> {
        val jsonString = prefs.getString(KEY_LINKS, null) ?: return PortalLink.DEFAULT_LINKS
        return try {
            val jsonArray = JSONArray(jsonString)
            (0 until jsonArray.length()).map { i ->
                val obj = jsonArray.getJSONObject(i)
                PortalLink(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    url = obj.getString("url"),
                    isDefault = obj.optBoolean("isDefault", false)
                )
            }
        } catch (e: Exception) {
            PortalLink.DEFAULT_LINKS
        }
    }

    fun saveLinks(links: List<PortalLink>) {
        val jsonArray = JSONArray()
        links.forEach { link ->
            jsonArray.put(JSONObject().apply {
                put("id", link.id)
                put("name", link.name)
                put("url", link.url)
                put("isDefault", link.isDefault)
            })
        }
        prefs.edit().putString(KEY_LINKS, jsonArray.toString()).apply()
    }

    fun addLink(link: PortalLink) {
        val links = getLinks().toMutableList()
        links.add(link)
        saveLinks(links)
    }

    fun updateLink(link: PortalLink) {
        val links = getLinks().toMutableList()
        val index = links.indexOfFirst { it.id == link.id }
        if (index != -1) {
            links[index] = link
            saveLinks(links)
        }
    }

    fun deleteLink(linkId: String) {
        val links = getLinks().toMutableList()
        links.removeAll { it.id == linkId }
        saveLinks(links)
    }

    fun resetToDefaults() {
        saveLinks(PortalLink.DEFAULT_LINKS)
    }

    fun moveLink(fromIndex: Int, toIndex: Int) {
        val links = getLinks().toMutableList()
        if (fromIndex in links.indices && toIndex in links.indices) {
            val item = links.removeAt(fromIndex)
            links.add(toIndex, item)
            saveLinks(links)
        }
    }
}
