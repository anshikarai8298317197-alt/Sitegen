package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.generator.AiWebsiteEngine
import com.example.data.generator.WebsiteCompiler
import com.example.data.generator.WebsiteGenerationParams
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SITEGEN", appName)
    }

    @Test
    fun `generate website with AI engine compiles to valid HTML`() {
        val params = WebsiteGenerationParams(
            name = "Apex Delhi Marketing",
            category = "Agency",
            description = "High-scale growth marketing agency in Delhi",
            numPages = 1
        )
        val project = AiWebsiteEngine.generateWebsite(params)
        assertEquals("Apex Delhi Marketing", project.name)
        assertTrue(project.pages.isNotEmpty())

        val html = WebsiteCompiler.compileToHtml(project)
        assertTrue(html.contains("<!DOCTYPE html>"))
        assertTrue(html.contains("Apex Delhi Marketing"))
        assertTrue(html.contains("SITEGEN"))
    }
}
