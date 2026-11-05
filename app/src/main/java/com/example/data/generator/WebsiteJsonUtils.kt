package com.example.data.generator

import com.example.data.model.SectionItem
import com.example.data.model.SectionType
import com.example.data.model.WebsitePage
import com.example.data.model.WebsiteSection
import org.json.JSONArray
import org.json.JSONObject

object WebsiteJsonUtils {

    fun serializePages(pages: List<WebsitePage>): String {
        val arr = JSONArray()
        for (page in pages) {
            val pageObj = JSONObject()
            pageObj.put("id", page.id)
            pageObj.put("title", page.title)
            pageObj.put("slug", page.slug)
            pageObj.put("isHome", page.isHome)

            val secArr = JSONArray()
            for (sec in page.sections) {
                val secObj = JSONObject()
                secObj.put("id", sec.id)
                secObj.put("type", sec.type.name)
                secObj.put("title", sec.title)
                secObj.put("subtitle", sec.subtitle)
                secObj.put("badgeText", sec.badgeText)
                secObj.put("primaryButtonText", sec.primaryButtonText)
                secObj.put("primaryButtonUrl", sec.primaryButtonUrl)
                secObj.put("secondaryButtonText", sec.secondaryButtonText)
                secObj.put("secondaryButtonUrl", sec.secondaryButtonUrl)
                secObj.put("imageUrl", sec.imageUrl)
                secObj.put("backgroundStyle", sec.backgroundStyle)
                secObj.put("paddingY", sec.paddingY)
                secObj.put("rawHtml", sec.rawHtml)

                val itemsArr = JSONArray()
                for (item in sec.items) {
                    val itObj = JSONObject()
                    itObj.put("id", item.id)
                    itObj.put("title", item.title)
                    itObj.put("description", item.description)
                    itObj.put("iconName", item.iconName)
                    itObj.put("price", item.price)
                    itObj.put("badge", item.badge)
                    itObj.put("imageUrl", item.imageUrl)
                    itObj.put("linkUrl", item.linkUrl)
                    itemsArr.put(itObj)
                }
                secObj.put("items", itemsArr)
                secArr.put(secObj)
            }
            pageObj.put("sections", secArr)
            arr.put(pageObj)
        }
        return arr.toString()
    }

    fun deserializePages(jsonStr: String): List<WebsitePage> {
        if (jsonStr.isBlank()) return emptyList()
        val pages = mutableListOf<WebsitePage>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                val pageObj = arr.getJSONObject(i)
                val sections = mutableListOf<WebsiteSection>()
                val secArr = pageObj.optJSONArray("sections")
                if (secArr != null) {
                    for (j in 0 until secArr.length()) {
                        val secObj = secArr.getJSONObject(j)
                        val typeStr = secObj.optString("type", SectionType.HERO.name)
                        val type = try {
                            SectionType.valueOf(typeStr)
                        } catch (e: Exception) {
                            SectionType.HERO
                        }

                        val items = mutableListOf<SectionItem>()
                        val itemsArr = secObj.optJSONArray("items")
                        if (itemsArr != null) {
                            for (k in 0 until itemsArr.length()) {
                                val itObj = itemsArr.getJSONObject(k)
                                items.add(
                                    SectionItem(
                                        id = itObj.optString("id", "it_$k"),
                                        title = itObj.optString("title", ""),
                                        description = itObj.optString("description", ""),
                                        iconName = itObj.optString("iconName", "Sparkles"),
                                        price = itObj.optString("price", ""),
                                        badge = itObj.optString("badge", ""),
                                        imageUrl = itObj.optString("imageUrl", ""),
                                        linkUrl = itObj.optString("linkUrl", "")
                                    )
                                )
                            }
                        }

                        sections.add(
                            WebsiteSection(
                                id = secObj.optString("id", "sec_$j"),
                                type = type,
                                title = secObj.optString("title", ""),
                                subtitle = secObj.optString("subtitle", ""),
                                badgeText = secObj.optString("badgeText", ""),
                                primaryButtonText = secObj.optString("primaryButtonText", ""),
                                primaryButtonUrl = secObj.optString("primaryButtonUrl", "#"),
                                secondaryButtonText = secObj.optString("secondaryButtonText", ""),
                                secondaryButtonUrl = secObj.optString("secondaryButtonUrl", "#"),
                                imageUrl = secObj.optString("imageUrl", ""),
                                backgroundStyle = secObj.optString("backgroundStyle", "surface"),
                                paddingY = secObj.optInt("paddingY", 48),
                                items = items,
                                rawHtml = secObj.optString("rawHtml", "")
                            )
                        )
                    }
                }

                pages.add(
                    WebsitePage(
                        id = pageObj.optString("id", "pg_$i"),
                        title = pageObj.optString("title", "Home"),
                        slug = pageObj.optString("slug", "home"),
                        isHome = pageObj.optBoolean("isHome", i == 0),
                        sections = sections
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pages
    }
}
